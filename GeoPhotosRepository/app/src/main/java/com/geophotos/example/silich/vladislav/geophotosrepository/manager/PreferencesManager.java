package com.geophotos.example.silich.vladislav.geophotosrepository.manager;

import android.content.SharedPreferences;

import com.geophotos.example.silich.vladislav.geophotosrepository.utils.ConstantManager;
import com.geophotos.example.silich.vladislav.geophotosrepository.utils.NetworkActivityApplication;

/**
 * Created by Lenovo on 12.09.2017.
 */

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    public PreferencesManager(){
        mSharedPreferences = NetworkActivityApplication.getSharedPreferences();
    }
    public void saveUserLogin(String login){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_LOGIN_KEY,login);
        editor.apply();
    }

    public void saveUserToken(String token){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_LOGIN_TOKEN,token);
        editor.apply();
    }
    public String getUserLogin(){
        return mSharedPreferences.getString(ConstantManager.USER_LOGIN_KEY,"null");
    }

    public String getUserToken (){
        return mSharedPreferences.getString(ConstantManager.USER_LOGIN_TOKEN,"null");
    }
}
