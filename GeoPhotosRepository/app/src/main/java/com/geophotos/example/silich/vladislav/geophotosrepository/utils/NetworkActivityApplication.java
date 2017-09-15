package com.geophotos.example.silich.vladislav.geophotosrepository.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.geophotos.example.silich.vladislav.geophotosrepository.database.HelperFactory;

/**
 * Created by Lenovo on 12.09.2017.
 */

public class NetworkActivityApplication extends Application {
    public static SharedPreferences sSharedPreferences;
    @Override
    public void onCreate() {
        super.onCreate();

        HelperFactory.setHelper(getApplicationContext());
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
    public static SharedPreferences getSharedPreferences(){
        return sSharedPreferences;
    }
}

