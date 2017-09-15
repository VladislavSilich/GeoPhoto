package com.geophotos.example.silich.vladislav.geophotosrepository;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ravi on 2/25/2017.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    //flag for GPS status
    boolean isGPSEnabled = false;

    //flag for network status
    boolean isNetworkEndabled = false;
    boolean canGetLocation = false;

    Location location; //location
    double latitude; //latitude
    double longitude; //longitude

    //The minimum distace to chage updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 meteres

    //The minimum time between updates in milliseconds
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 1;  //1 minutes

    //declaring a location manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting network status
            isNetworkEndabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEndabled) {
                //no Network provider is enabled
            } else {
                this.canGetLocation = true;
                //First get location from location provider
                if (isNetworkEndabled) {
                    //permission handling improvements required, but the code is working
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                    //if GPS is enabled, get latitudea nad longitude using the GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            //permission handling improvements required, but the code is working
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BETWEEN_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); //study about this
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //stop using GPS listener
    public void stopUsingGPS() {
        if (locationManager != null) {
            //permission handling improvements required, but the code is working
            locationManager.removeUpdates(GPSTracker.this);
            }

        }


    //functions to get the latitude and longitude
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        //return latitude
        return latitude;
    }

    public double getLongitude(){
        if(location!=null){
            longitude = location.getLongitude();
        }

        //return longitude
        return longitude;
    }


    //prompting users to turn on GPS
    public boolean canGetLocation(){
        return this.canGetLocation;
    }
    //function to show the alert dialog
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //setting dialog title
        alertDialog.setTitle("GPS settings");

        //setting dialog message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        //setting icon to dialog
        //alertDialog.setIcon(R,drwable.delete);

        //on pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        //on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //showing alert message
        alertDialog.show();



    }
}
