package com.geophotos.example.silich.vladislav.geophotosrepository.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geophotos.example.silich.vladislav.geophotosrepository.R;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO.CommentsDAO;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO.PhotosDAO;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.HelperFactory;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Comments;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Photos;
import com.geophotos.example.silich.vladislav.geophotosrepository.manager.DataManager;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.req.ModelCommentReq;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelDeleteImage;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelGetCommentRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelSendCommentRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.ConstantManager;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.NetworkStatusChecker;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPhotoActivity extends AppCompatActivity implements View.OnLongClickListener ,View.OnClickListener{
    private ImageView imagePhoto;
    private TextView txtDate;
    private ImageButton btnComment;
    private EditText edtComment;
    private int photoId;
    private String token;
    private DataManager manager;
    private ListView comments;
    private ArrayList<String> listAdapterComment;
    private ArrayList<Comments> listComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        manager = DataManager.getInstance();
        edtComment = (EditText)findViewById(R.id.edtComment);
        btnComment = (ImageButton)findViewById(R.id.btnComment);
        imagePhoto = (ImageView)findViewById(R.id.imgShow);
        txtDate = (TextView)findViewById(R.id.txtDateShow);
        comments = (ListView)findViewById(R.id.commentList);
        listAdapterComment = new ArrayList<>();
        listComment = new ArrayList<>();
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
        if (NetworkStatusChecker.isNetworkAvailable(this) == true){
            showPhotoComment();
        }else {
            showPhotoCommentLocal();
        }
        imagePhoto.setOnLongClickListener(this);
        btnComment.setOnClickListener(this);
    }

    private void showPhotoComment() {
        Call<ModelGetCommentRes> call = manager.getComments(photoId,0,token);
        call.enqueue(new Callback<ModelGetCommentRes>() {
            @Override
            public void onResponse(Call<ModelGetCommentRes> call, Response<ModelGetCommentRes> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<ModelGetCommentRes> call, Throwable t) {
        int a = 5;
            }
        });
    }

    private void showPhotoCommentLocal() {
        try {
            CommentsDAO commentsDAO = HelperFactory.getHelper().getCommentsDAO();
            listComment.addAll(commentsDAO.getCommentsByImageId(photoId));
            for (int i = 0; i < listComment.size(); i++){
                listAdapterComment.add(listComment.get(i).getCommentText());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, listAdapterComment);
            comments.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.imgShow :{
                if (NetworkStatusChecker.isNetworkAvailable(this)) {
                    openDialog();
                }else {
                    Toast.makeText(this,"No connection internet",Toast.LENGTH_LONG).show();
                }
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
        Call<ModelDeleteImage> call = manager.deleteImageById(photoId,token);
        call.enqueue(new Callback<ModelDeleteImage>() {
            @Override
            public void onResponse(Call<ModelDeleteImage> call, Response<ModelDeleteImage> response) {
                if(response.code() == 200) {
                    deleteImageDatabase(photoId);
                }
            }
            @Override
            public void onFailure(Call<ModelDeleteImage> call, Throwable t) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnComment:
              addPhotoComment();
                break;
        }
    }

    private void addPhotoComment() {
        final String comment = edtComment.getText().toString();
        edtComment.setText("");
        Call<ModelSendCommentRes> call = manager.sendComments(photoId,new ModelCommentReq(comment),token);
        call.enqueue(new Callback<ModelSendCommentRes>() {
            @Override
            public void onResponse(Call<ModelSendCommentRes> call, Response<ModelSendCommentRes> response) {
                if (response.code() == 200){
                    Comments commentsSend = new Comments();
                    commentsSend.setCommentId(response.body().getData().getId());
                    commentsSend.setCommentText(response.body().getData().getText());
                    commentsSend.setImageId(photoId);
                    try {
                        HelperFactory.getHelper().getCommentsDAO().create(commentsSend);
                        listAdapterComment.clear();
                        showPhotoCommentLocal();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ModelSendCommentRes> call, Throwable t) {
            }
        });
    }
}
