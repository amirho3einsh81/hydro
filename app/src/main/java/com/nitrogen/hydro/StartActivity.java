package com.nitrogen.hydro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

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
import android.widget.Toast;

import com.nitrogen.hydro.utils.FirstPrifrence;
import com.nitrogen.hydro.utils.InternetCheck;
public class StartActivity extends AppCompatActivity {
    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_splash);
        h.postDelayed(runnable,3000);
    }
    boolean preference;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(StartActivity.this, "amir", Toast.LENGTH_SHORT).show();
            FirstPrifrence open = new FirstPrifrence(StartActivity.this, "open");
            preference = open.pref();
            if (preference) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                finish();
            } else {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        }
    };
}