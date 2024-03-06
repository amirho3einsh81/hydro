package com.nitrogen.hydro;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nitrogen.hydro.fragements.AccountFragment;
import com.nitrogen.hydro.fragements.BestusersFragment;
import com.nitrogen.hydro.fragements.FragmentHome;
import com.nitrogen.hydro.fragements.WithdrawalFragment;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    AppCompatTextView name;
    BottomSheetDialog bottomSheetDialog;
    AppCompatImageView btnmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        name.setText(getSharedPreferences("account", MODE_PRIVATE).getString("name", "") + " " + getSharedPreferences("account", MODE_PRIVATE).getString("family", ""));
        btnmenu = findViewById(R.id.btn_menu);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = "09182145845";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }
                });
                bottomSheetDialog.show();
                bottomSheetDialog.findViewById(R.id.btn_about).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "نام تیم رویداد پرواز: نیتروژن", Toast.LENGTH_SHORT).show();
                    }
                });
                bottomSheetDialog.findViewById(R.id.btnSupport).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });


        bottomNavigationView
                = findViewById(R.id.bnv);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

    }


    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new FragmentHome())
                    .commit();
            return true;
        } else if (id == R.id.best) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new BestusersFragment())
                    .commit();
            return true;
        /*} else if (id == R.id.wallet) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new WithdrawalFragment())
                    .commit();
            return true;*/
        } else if (id == R.id.account) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new AccountFragment())
                    .commit();
        }
        /*switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new FragmentHome())
                        .commit();
                return true;

            case R.id.best:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new BestusersFragment())
                        .commit();
                return true;

            case R.id.wallet:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new WithdrawalFragment())
                        .commit();
                return true;
        }*/
        return true;
    }

    boolean isExit = false;

    @Override
    public void finish() {
        BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(this);
        bottomSheetDialog1.setContentView(R.layout.bottom_dialog);
        bottomSheetDialog1.findViewById(R.id.btn_clancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog1.dismiss();
                isExit = false;
            }
        });
        bottomSheetDialog1.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.finish();
            }
        });
        bottomSheetDialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isExit = false;
            }
        });
        if (isExit == true) {
            super.finishAffinity();
        } else {
            bottomSheetDialog1.show();
            isExit = true;
        }
    }
}
