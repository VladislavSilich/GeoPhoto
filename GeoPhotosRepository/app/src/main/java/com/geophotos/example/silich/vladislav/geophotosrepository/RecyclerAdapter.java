package com.geophotos.example.silich.vladislav.geophotosrepository;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geophotos.example.silich.vladislav.geophotosrepository.activity.ShowPhotoActivity;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Photos;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 13.09.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public ArrayList<Photos> mDataset;
    Context mContext;
    int positionPhoto = 0;
    StringBuffer buffer;
    // Конструктор
    public RecyclerAdapter(ArrayList<Photos> dataset, Context context) {
        mDataset = dataset;
        mContext = context;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imagePhoto;
        public TextView txtDate;
        public ViewHolder(View v) {
            super(v);
            txtDate = (TextView)v.findViewById(R.id.txt_adapter);
            imagePhoto = (ImageView)v.findViewById(R.id.imgPhoto);
            imagePhoto.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ShowPhotoActivity.class);
            intent.putExtra(ConstantManager.PHOTO_INTENT,mDataset.get(getPosition()).getUri());
            intent.putExtra(ConstantManager.PHOTO_INTENT_DATE,mDataset.get(getPosition()).getPhotosPhotoDate());
            intent.putExtra(ConstantManager.PHOTO_INTENT_ID,mDataset.get(getPosition()).getPhotoId());
            mContext.startActivity(intent);
        }
    }
    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_layout, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        positionPhoto = position;
        mDataset.size();
        Picasso.with(mContext).load(mDataset.get(position).getUri()).into(holder.imagePhoto);
        buffer = new StringBuffer(mDataset.get(position).getPhotosPhotoDate().toString());
        String t = ".";
        buffer.insert(buffer.length() - 6,t);
        buffer.insert(buffer.length() - 4,t);
        holder.txtDate.setText(buffer);
    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}