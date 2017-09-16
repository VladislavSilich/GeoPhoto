package com.geophotos.example.silich.vladislav.geophotosrepository.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geophotos.example.silich.vladislav.geophotosrepository.R;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO.PhotosDAO;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.HelperFactory;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Photos;
import com.geophotos.example.silich.vladislav.geophotosrepository.manager.DataManager;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelDeleteImage;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.ConstantManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPhotoActivity extends AppCompatActivity implements View.OnLongClickListener {
    private ImageView imagePhoto;
    private TextView txtDate;
    private int photoId;
    private String token;
    private DataManager manager;
    PhotosDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        manager = DataManager.getInstance();
        imagePhoto = (ImageView)findViewById(R.id.imgShow);
        txtDate = (TextView)findViewById(R.id.txtDateShow);
        Intent intent = getIntent();
        String photo = intent.getStringExtra(ConstantManager.PHOTO_INTENT);
        String Date = intent.getStringExtra(ConstantManager.PHOTO_INTENT_DATE);
        StringBuffer buffer = new StringBuffer(Date);
        String t = ".";
        buffer.insert(buffer.length() - 6,t);
        buffer.insert(buffer.length() - 4,t);
        txtDate.setText(buffer);
        Picasso.with(this).load(photo).into(imagePhoto);
        photoId = intent.getIntExtra(ConstantManager.PHOTO_INTENT_ID,0);
        token = manager.getPreferencesManager().getUserToken();
        imagePhoto.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.imgShow :{
                openDialog();
                break;
            }
        }
        return false;
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowPhotoActivity.this);
        builder.setTitle("Important message!")
                .setMessage("Do you really want to delete photos?")
                .setCancelable(false)
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteImage() {
        Call<ModelDeleteImage> call = manager.geleteImageById(photoId,token);
        call.enqueue(new Callback<ModelDeleteImage>() {
            @Override
            public void onResponse(Call<ModelDeleteImage> call, Response<ModelDeleteImage> response) {
                if(response.code() == 200) {
                    deleteImageDatabase(photoId);
                }
            }

            @Override
            public void onFailure(Call<ModelDeleteImage> call, Throwable t) {
            int a = 5;
            }
        });
    }

    private void deleteImageDatabase(int id) {
        try {
            PhotosDAO helper = HelperFactory.getHelper().getPhotosDAO();
            DeleteBuilder<Photos, Integer> deleteBuilder = helper.deleteBuilder();
            deleteBuilder.where().eq(Photos.PHOTOS_PHOTO_ID, id);
            deleteBuilder.delete();
            Intent intent = new Intent(this,GeneralActivity.class);
            startActivity(intent);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
