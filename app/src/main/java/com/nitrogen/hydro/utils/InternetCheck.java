package com.nitrogen.hydro.utils;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetCheck {
    Context ctx;
    public InternetCheck(Context ctx){
        this.ctx = ctx;
    }
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifiConnection != null && wifiConnection.isConnected())
                || (mobileConnection != null && mobileConnection.isConnected())) {
            return true;
        }
        return false;
    }
}
