package com.nitrogen.hydro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Dialog dDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (isConnectedToInternet()) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    if (getSharedPreferences("account" , MODE_PRIVATE).getBoolean("open" , false) == true){
                        startActivity(new Intent(SplashActivity.this , MainActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this , LoginActivity.class));
                    }
                }
            } , 2000);
        }else {
            showDisConnectApp();
        }

    }

    public void showDisConnectApp() {
        AlertDialog.Builder aAlert = new AlertDialog.Builder(SplashActivity.this);
        View vAlert = LayoutInflater.from(SplashActivity.this).inflate(R.layout.errorwifi_dialog, null);
        aAlert.setView(vAlert);
        Button bYes = vAlert.findViewById(R.id.btnRetry);

        aAlert.setCancelable(false);

        bYes.setText("بستن");
        bYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        dDialog = aAlert.create();
        dDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dDialog.show();
    }
    boolean isConnectedToInternet() {

        ConnectivityManager cm =
                (ConnectivityManager)SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        return isConnected;

    }
}