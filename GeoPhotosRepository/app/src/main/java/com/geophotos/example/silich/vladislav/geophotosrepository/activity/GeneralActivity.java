package com.geophotos.example.silich.vladislav.geophotosrepository.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.geophotos.example.silich.vladislav.geophotosrepository.GPSTracker;
import com.geophotos.example.silich.vladislav.geophotosrepository.R;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.HelperFactory;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Photos;
import com.geophotos.example.silich.vladislav.geophotosrepository.fragment.ListFragment;
import com.geophotos.example.silich.vladislav.geophotosrepository.fragment.MapsFragment;
import com.geophotos.example.silich.vladislav.geophotosrepository.manager.DataManager;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.req.ModelImageReq;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelImageRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.ConstantManager;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.NetworkStatusChecker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView headerTxt;
    private DataManager manager;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        manager = DataManager.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkStatusChecker.isNetworkAvailable(GeneralActivity.this) == true) {
                    loadPhotoCamera();
                } else {
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        headerTxt = (TextView) hView.findViewById(R.id.nav_header_userName);
        headerTxt.setText(manager.getPreferencesManager().getUserLogin());
        gps = new GPSTracker(this);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new ListFragment()).commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();
        if (id == R.id.nav_gallery) {
            fm.beginTransaction().replace(R.id.content_frame, new ListFragment()).commit();
        } else if (id == R.id.nav_map) {
            fm.beginTransaction().replace(R.id.content_frame, new MapsFragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadPhotoCamera() {
        Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            mPhotoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mPhotoFile != null) {
            takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURES);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_CAMERA_PICTURES:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    convertImageToBase64(mSelectedImage);
                }
        }
    }

    private void convertImageToBase64(Uri mSelectedImage) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mSelectedImage);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] byteFormat = stream.toByteArray();
            String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
            sendImage(imgString);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendImage(String encImage) {
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String timeStamp = new SimpleDateFormat("ddMMyyyy").format(new Date());
            Call<ModelImageRes> call = manager.sendImages(manager.getPreferencesManager().getUserToken(), new ModelImageReq(encImage, timeStamp, latitude, longitude));
            call.enqueue(new Callback<ModelImageRes>() {
                @Override
                public void onResponse(Call<ModelImageRes> call, Response<ModelImageRes> response) {
                    if (response.code() == 200) {
                        Photos photos = new Photos();
                        photos.setUri(response.body().getData().getUrl());
                        photos.setPhotosPhotoDate(response.body().getData().getDate().toString());
                        photos.setLatitude(response.body().getData().getLat());
                        photos.setLongitude(response.body().getData().getLng());
                        photos.setPhotoId(response.body().getData().getId());
                        photos.setUserLogin(manager.getPreferencesManager().getUserLogin());
                        try {
                            HelperFactory.getHelper().getPhotosDAO().create(photos);
                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.content_frame, new ListFragment()).commit();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(GeneralActivity.this,"Bad photo",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelImageRes> call, Throwable t) {
                    Toast.makeText(GeneralActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            gps.showSettingsAlert();
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        return image;
    }

}
