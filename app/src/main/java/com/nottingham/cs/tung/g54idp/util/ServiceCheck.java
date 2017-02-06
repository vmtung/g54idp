package com.nottingham.cs.tung.g54idp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.nottingham.cs.tung.g54idp.activity.MapsActivity;

/**
 * Created by Tung on 28/08/2016.
 */
//This class check if internet connection and GPS is on
//if any of them is not on, pop up will appear to notify the user
//only when both of them are on that the process starts
public class ServiceCheck {
    private Boolean locationServiceBoolean = false;
    private int providerType = 0;
    private static AlertDialog alert;
    private MapsActivity activity;
    private Boolean internetServiceBoolean = false;

    public ServiceCheck(MapsActivity activity) {
        this.activity = activity;
    }

    //check location first, if clear then check internet, if also clear then continue
    //else, show pop up
    public void doTotalCheck(){

        checkLocationOn();

        if (!locationServiceBoolean)
            createLocationServiceError();
        else {
            checkInternetOn();
            if (!internetServiceBoolean)
                createInternetServiceError();
            else activity.checkFinished();
        }
    }

    //check if location service is on
    public void checkLocationOn(){
        locationServiceBoolean = false;

        LocationManager locationManager = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);
        boolean gpsIsEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkIsEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (networkIsEnabled) {
            locationServiceBoolean = true;
            providerType = 1;

        } else if (gpsIsEnabled) {
            locationServiceBoolean = true;
            providerType = 2;
        }
    }

    ///check if internet is on
    private void checkInternetOn(){
        internetServiceBoolean = false;

        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        internetServiceBoolean = !(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable());
    }

    public Boolean isLocationServiceAvailable() {
        return locationServiceBoolean;
    }

    //show pop up notify about location service
    //if click recheck, will run the check again
    //else exit
    public void createLocationServiceError() {
        // show alert dialog if Location is not available
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(
                "You need to activate location service to use this feature. Please turn on network or GPS mode in location settings")
                .setTitle("GPS problem")
                .setCancelable(false)
                .setPositiveButton("Retry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alert.dismiss();
                                doTotalCheck();

                            }
                        })
                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alert.dismiss();
                                activity.finish();
                            }
                        });
        alert = builder.create();
        alert.show();
    }

    //show pop up notify about internet
    //if click recheck, will run the check again
    //else exit
    public void createInternetServiceError() {
        // show alert dialog if Internet is not connected
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(
                "You need an active internet connection to use this feature. Please connect to the internet before retry.")
                .setTitle("Internet problem")
                .setCancelable(false)
                .setPositiveButton("Retry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alert.dismiss();
                                doTotalCheck();
                            }
                        })
                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alert.dismiss();
                                activity.finish();
                            }
                        });
        alert = builder.create();
        alert.show();
    }


}
