package com.nitrogen.hydro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    CountDownTimer cdt;
    int cdtTime = 3;

    TextInputLayout number  , code , id , name , posti;
    TextInputEditText etnumber  , etcode , etid , etname , etposti;
    AppCompatTextView t1 , t2 , timer;
    AppCompatTextView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        number = findViewById(R.id.number);
        code = findViewById(R.id.code);
        posti = findViewById(R.id.posti);
        id = findViewById(R.id.id);
        name = findViewById(R.id.name);


        etnumber = findViewById(R.id.et_number);
        name = findViewById(R.id.name);

        etcode = findViewById(R.id.et_code);
        etposti = findViewById(R.id.et_posti);
        etid = findViewById(R.id.et_id);
        etname = findViewById(R.id.et_name);

        t1 = findViewById(R.id.text1);
        t2 = findViewById(R.id.text2);


        timer = findViewById(R.id.timer);


        submit = findViewById(R.id.submit);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etnumber.getText().toString().equals("") || etnumber.getText().toString().length()<11 || !etnumber.getText().toString().startsWith("0")) {
                   number.setError("شماره تلفن خود را به صورت صحیح وارد کنید!");

                }else {
                    code.setVisibility(View.VISIBLE);
                    number.setVisibility(View.GONE);
                    t1.setText("کد تایید");
                    t2.setText("لطفا کد احراز هویت را وارد کنید!");
                    submit.setText("بررسی کد");


                    time();
                /*startActivity(new Intent(LoginActivity.this , InfoActivity.class));*/

            }

            }
        });

    }
    public  void time(){

        cdt = new CountDownTimer(cdtTime * 40000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText((millisUntilFinished / 1000) + " " +" ثانیه مانده");
                timer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {

                timer.setText("ارسال مجدد کد فعالسازی");
            }

        }.start();


    }


}