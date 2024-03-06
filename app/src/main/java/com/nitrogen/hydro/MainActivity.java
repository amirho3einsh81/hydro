package com.nitrogen.hydro;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nitrogen.hydro.customeviews.ProgressDialog;
import com.nitrogen.hydro.fragements.AccountFragment;
import com.nitrogen.hydro.fragements.BestusersFragment;
import com.nitrogen.hydro.fragements.FragmentHome;
import com.nitrogen.hydro.fragements.WithdrawalFragment;
import com.nitrogen.hydro.utils.Utils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    AppCompatTextView name;
    BottomSheetDialog bottomSheetDialog;
    AppCompatImageView btnmenu;

    SharedPreferences shared;
    PublicFunctions pFunctions;
    Constants cons;
    String strUserName="" , strPassword="";

    TextView activity_mainTextView;

    ProgressDialog pb;

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
        init();

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
    public void btnClick(View view)
    {
        int id = view.getId();
        if(id == R.id.register_Button_delete)
        {
            if (!strUserName.equals("") && !strPassword.equals(""))
                new deleteUserRequest(strUserName,strPassword,getApplicationContext()).execute();
        }
    }

    private class deleteUserRequest extends AsyncTask<Void,Void,String>
    {
        String Username , Password;
        android.content.Context Context;

        public deleteUserRequest(String username , String password , Context context)
        {
            Username = username;
            Password = password;
            this.Password = password;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void[] p1)
        {
            HashMap hashmap = new HashMap();
            hashmap.put("username",Username);
            hashmap.put("password",Password);
            return Utils.sendData(cons.HOST_LogoutUser ,hashmap);
        }

        @Override
        protected void onPostExecute(String result)
        {
            pFunctions.showToast(result);
            if ( result.equals( "شما از حساب خود خارج شدید") )
            {
                if( shared.edit().putString("myKey","").commit() )
                {
                    Intent i=new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    private class checkUserRequest extends AsyncTask<Void,Void,String>
    {
        String Username , Password;
        Context Context;

        public checkUserRequest(String username , String password)
        {
            this.Username = username;
            this.Password = password;
        }

        @Override
        protected void onPreExecute()
        {
            pb = new ProgressDialog(MainActivity.this);
            pb.setTitle("در حال بررسی لایسنس");
            pb.setCancelable(false);
            pb.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void[] p1)
        {
            HashMap hashmap = new HashMap();
            hashmap.put("username",Username);
            hashmap.put("time",pFunctions.getCurrentTimeMillis());
            return Utils.sendData(cons.HOST_CheckUser ,hashmap);
        }

        @Override
        protected void onPostExecute(String result)
        {
            pb.dismiss();
            pFunctions.showToast(result);
            if ( result != null )
            {
                long l = Long.parseLong(result) ;

                if( l < pFunctions.getCurrentTimeMillis() )
                {
                    pFunctions.showToast("لتیسنس شما به پایان رسیده");
                    if( shared.edit().putString("myKey","").commit() )
                    {

                        Intent i=new Intent(MainActivity.this,RegisterActivity.class);
                        startActivity(i);
                        finish();
                    }
                }

                if ( l > pFunctions.getCurrentTimeMillis() )
                {
                    activity_mainTextView.setText("کد پستی شما : " + ( (Long.parseLong(result)-pFunctions.getCurrentTimeMillis() )/86400000));
                }
            }
        }
    }

    private void init()
    {
        shared = getSharedPreferences("shared",MODE_PRIVATE);
        pFunctions=new PublicFunctions(this);
        cons=new Constants();

        activity_mainTextView=(TextView) findViewById(R.id.activity_mainTextView);

        String strEnText = shared.getString("myKey","");
        String[]str=pFunctions.base64Decode(strEnText.substring(3,strEnText.length()-3)).split("-");

        if(str.length ==2 )
        {
            strUserName = str[0];
            strPassword = str[1];


            new checkUserRequest(strUserName,strPassword).execute();
        }else{
            pFunctions.showToast("خطا");
        }
    }
}

