package com.geophotos.example.silich.vladislav.geophotosrepository.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geophotos.example.silich.vladislav.geophotosrepository.R;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO.PhotosDAO;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.HelperFactory;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Photos;
import com.geophotos.example.silich.vladislav.geophotosrepository.manager.DataManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 13.09.2017.
 */

public class MapsFragment extends android.app.Fragment implements OnMapReadyCallback{
   private List<Photos> listPhoto;
   private DataManager manager;
   private  double latitude ;
   private  double longitude;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        listPhoto = new ArrayList<>();
        manager = DataManager.getInstance();
        try {
            PhotosDAO photosDAO = HelperFactory.getHelper().getPhotosDAO();
            listPhoto.addAll(photosDAO.getUserPhotoByName(manager.getPreferencesManager().getUserLogin()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_maps,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.mapsFragment);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for(int i = 0; i < listPhoto.size(); i++) {
             latitude = listPhoto.get(i).getLatitude();
             longitude = listPhoto.get(i).getLongitude();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        }
    }
}
