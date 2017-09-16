package com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO;

import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Photos;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Lenovo on 12.09.2017.
 */

public class PhotosDAO extends BaseDaoImpl<Photos,Integer> {
    public PhotosDAO(ConnectionSource connectionSource, Class<Photos> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
    public List<Photos> getUserPhotoByName(String login)  throws SQLException{
        QueryBuilder<Photos, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Photos.PHOTOS_LOGIN_USER, login);
        PreparedQuery<Photos> preparedQuery = queryBuilder.prepare();
        List<Photos> userPhotoList =query(preparedQuery);
        return userPhotoList;
    }
    public List<Photos> getUserPhotoById(int id)throws SQLException{
        QueryBuilder<Photos, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq(Photos.PHOTOS_PHOTO_ID, id);
        PreparedQuery<Photos> preparedQuery = queryBuilder.prepare();
        List<Photos> userPhotoListById =query(preparedQuery);
        return userPhotoListById;
    }
    public void deleteImageById(int id) throws SQLException{
        DeleteBuilder<Photos, Integer> deleteBuilder = deleteBuilder();
        deleteBuilder.where().eq(Photos.PHOTOS_PHOTO_ID, id);
        deleteBuilder.delete();
    }


}
