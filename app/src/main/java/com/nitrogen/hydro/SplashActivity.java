package com.nitrogen.hydro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
    }
}