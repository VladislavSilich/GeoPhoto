package com.geophotos.example.silich.vladislav.geophotosrepository.network.req;

/**
 * Created by Lenovo on 14.09.2017.
 */

public class ModelImageReq {
    String base64Image;
    String date;
    double lat;
    double lng;
    public ModelImageReq(String base64Image,String date,double lat,double lng){
        this.base64Image = base64Image;
        this.date = date;
        this.lat = lat;
        this.lng= lng;
    }
}


