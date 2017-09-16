package com.geophotos.example.silich.vladislav.geophotosrepository.database.tables;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lenovo on 12.09.2017.
 */
@DatabaseTable(tableName = "photos")
public class Photos {
    public final static String PHOTOS_LOGIN_USER = "userLogin";
    public final static String PHOTOS_PHOTO_LATITUDE = "latitude";
    public final static String PHOTOS_PHOTO_LONGITUDE = "longitude";
    public final static String PHOTOS_PHOTO_URI = "uri";
    public final static String PHOTOS_PHOTO_ID = "photoId";
    public final static String PHOTOS_PHOTO_DATE = "date";

    public  String getPhotosPhotoDate() {
        return date;
    }
    public void setPhotosPhotoDate(String date){
        this.date = date;
    }

    public int getPhotoId(){
        return photoId;
    }
    public void setPhotoId (int photoId){
        this.photoId = photoId;
    }
    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = PHOTOS_LOGIN_USER)
    private String userLogin;
    @DatabaseField(columnName = PHOTOS_PHOTO_LATITUDE)
    private double latitude;
    @DatabaseField(columnName = PHOTOS_PHOTO_LONGITUDE)
    private double longitude;
    @DatabaseField(columnName = PHOTOS_PHOTO_URI)
    private String uri;
    @DatabaseField (columnName = PHOTOS_PHOTO_ID)
    private int photoId;
    @DatabaseField(columnName = PHOTOS_PHOTO_DATE)
    private String date;
}
