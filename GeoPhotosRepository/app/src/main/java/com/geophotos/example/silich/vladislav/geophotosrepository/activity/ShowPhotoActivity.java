package com.geophotos.example.silich.vladislav.geophotosrepository.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.geophotos.example.silich.vladislav.geophotosrepository.R;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.ConstantManager;
import com.squareup.picasso.Picasso;

public class ShowPhotoActivity extends AppCompatActivity {
    ImageView imagePhoto;
    TextView txtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
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
    }

}
