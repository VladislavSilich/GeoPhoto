package com.geophotos.example.silich.vladislav.geophotosrepository.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geophotos.example.silich.vladislav.geophotosrepository.R;
import com.geophotos.example.silich.vladislav.geophotosrepository.RecyclerAdapter;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO.PhotosDAO;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.HelperFactory;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Photos;
import com.geophotos.example.silich.vladislav.geophotosrepository.manager.DataManager;
import com.geophotos.example.silich.vladislav.geophotosrepository.network.res.ModelGetPhotoRes;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.NetworkStatusChecker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lenovo on 13.09.2017.
 */
public class ListFragment extends Fragment {
    RecyclerView recyclerPhoto;
    TextView text;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Photos> listPhotos;
    Context context;
    DataManager manager;
    List<ModelGetPhotoRes> photoResponse;
    List<ModelGetPhotoRes.Datum> listDatum;
    List<Photos> listPhotoData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,container,false);
        manager = DataManager.getInstance();
        text = (TextView)v.findViewById(R.id.txtFragList);
        text.setText("Hallo: " +  manager.getPreferencesManager().getUserLogin() + "!");
        this.context = container.getContext();
        listPhotos = new ArrayList<>();
        photoResponse = new ArrayList<>();
        listDatum = new ArrayList<>();
        listPhotoData = new ArrayList<>();
        recyclerPhoto = (RecyclerView)v.findViewById(R.id.recycler_view);
        recyclerPhoto.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity().getBaseContext(),3);
        recyclerPhoto.setLayoutManager(mLayoutManager);
      //  mAdapter = new RecyclerAdapter(listPhotos,context);
        recyclerPhoto.setAdapter(mAdapter);
        if (NetworkStatusChecker.isNetworkAvailable(context) == true){
            getPhotoUser();
        }
        getPhotoUserData();
        return v;
    }
    private void getPhotoUser() {
        String token = manager.getPreferencesManager().getUserToken();
        int page = 0;
        Call<ModelGetPhotoRes> call = manager.getImageUser(page,token);
        call.enqueue(new Callback<ModelGetPhotoRes>() {
            @Override
            public void onResponse(Call<ModelGetPhotoRes> call, Response<ModelGetPhotoRes> response) {
                if (response.code() == 200) {
                    photoResponse.add(response.body());
                   listDatum.addAll( photoResponse.get(0).getData());
                    for (int i = 0; i < listDatum.size(); i++) {
                        if (checkDatabasePhoto(listDatum.get(i).getId()) == true) {
                            Photos photos = new Photos();
                            photos.setUri(listDatum.get(i).getUrl());
                            photos.setPhotosPhotoDate(listDatum.get(i).getDate().toString());
                            photos.setLatitude(listDatum.get(i).getLat());
                            photos.setLongitude(listDatum.get(i).getLng());
                            photos.setPhotoId(listDatum.get(i).getId());
                            photos.setUserLogin(manager.getPreferencesManager().getUserLogin());
                            try {
                                HelperFactory.getHelper().getPhotosDAO().create(photos);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ModelGetPhotoRes> call, Throwable t) {
            }
        });
    }
    private void getPhotoUserData() {
        String login = manager.getPreferencesManager().getUserLogin().toString();
        try {
            PhotosDAO photosDAO = HelperFactory.getHelper().getPhotosDAO();
            listPhotos.addAll(photosDAO.getUserPhotoByName(login));

            if (listPhotos.size() != 0) {
                text.setVisibility(View.INVISIBLE);
                mAdapter = new RecyclerAdapter(listPhotos,context);
                recyclerPhoto.setAdapter(mAdapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public boolean checkDatabasePhoto(int id){
        try {
            PhotosDAO photosDAO = HelperFactory.getHelper().getPhotosDAO();
            listPhotoData.addAll(photosDAO.getUserPhotoById(id));
            if (listPhotoData.size() != 0){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
