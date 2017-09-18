package com.geophotos.example.silich.vladislav.geophotosrepository.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO.PhotosDAO;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.DAO.UsersDAO;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Photos;
import com.geophotos.example.silich.vladislav.geophotosrepository.database.tables.Users;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Lenovo on 11.09.2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "geophoto.db";
    private static final int DATABASE_VERSION = 32;

    UsersDAO usersDAO = null;
    PhotosDAO photosDAO = null;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource,Users.class);
            TableUtils.createTable(connectionSource, Photos.class);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            TableUtils.dropTable(connectionSource,Users.class,true);
            TableUtils.dropTable(connectionSource,Photos.class,true);
            onCreate(database);
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }
     public UsersDAO  getUsersDAO () throws SQLException{
         if (usersDAO == null){
             usersDAO = new UsersDAO(connectionSource,Users.class);
         }
         return usersDAO;
     }
     public PhotosDAO getPhotosDAO()throws SQLException{
        if (photosDAO == null){
            photosDAO = new PhotosDAO(connectionSource,Photos.class);
        }
        return photosDAO;
    }


    @Override
    public void close() {
        super.close();
        usersDAO = null;
        photosDAO = null;
    }
}
