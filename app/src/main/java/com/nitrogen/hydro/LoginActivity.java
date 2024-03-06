package com.nitrogen.hydro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.IpSecManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.CellSignalStrength;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    CountDownTimer cdt;
    int cdtTime = 3;
    Handler mainHandler = new Handler();
    TextInputLayout layNumber, layPassword;
    TextInputEditText etNumber,  etPassword;
    AppCompatTextView btnRegister;
    AppCompatTextView submit;
    String http_url;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        http_url = getResources().getString(R.string.http_url);
        pref = getSharedPreferences("account" , MODE_PRIVATE);
        etNumber = findViewById(R.id.et_number);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFilds()){
                    mainHandler.postDelayed(run,300);
                }
            }
        });
    }
    private boolean checkFilds() {
        action(layNumber, "",false);
        action(layPassword, "",false);
        if (etNumber.getText().toString().isEmpty()){
            action(layNumber, "لطفا شماره خودرا وارد نمایید",true);
            return false;
        } else if (!etNumber.getText().toString().startsWith("9")) {
            action(layNumber, "شماره باید با 9 شروع شود",true);
            return false;
        } else if (etNumber.getText().toString().length()<10 || etNumber.getText().toString().length()>10) {
            action(layNumber, "فرمت شماره باید ده رقمی باشد",true);
            return false;
        }
        if (etPassword.getText().toString().isEmpty()){
            action(layPassword, "رمز عبور خودرا وارد کنید",true);
            return false;
        }
        return true;
    }
    public void time() {
        cdt = new CountDownTimer(cdtTime * 40000, 1000) {
            public void onTick(long millisUntilFinished) {
//                timer.setText((millisUntilFinished / 1000) + " " + " ثانیه مانده");
//                timer.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFinish() {
//                timer.setText("ارسال مجدد کد فعالسازی");
            }
        }.start();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            getdata();
        }
    };

    private void getdata() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("number",etNumber.getText().toString().trim());
            jsonObject.put("password" , etPassword.getText().toString().trim());
        }catch (JSONException ignored){
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                http_url + "login.php",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("state").equals("0") &&
                                    response.getString("object").equals("password")) {
                                action(layPassword, "رمزعبور یا اکانت وارد شده نادرست است", true);
                            } else if (response.getString("state").equals("0") &&
                                    response.getString("object").equals("username")) {
                                action(layNumber, "حسابی با این اطلاعات پیدا نشد", true);
                            } else {
                                action(layNumber, "", false);
                                action(layPassword, "", false);
                                pref.edit().putString("number",etNumber.getText().toString())
                                        .putString("password" , etPassword.getText().toString())
                                        .putBoolean("isLogin", true).apply();
                                startActivity(new Intent(LoginActivity.this, StartActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
//                    throw new RuntimeException(e);
                            Log.e("exception", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
    private void action(TextInputLayout til, String msg, boolean state) {
        til.setErrorEnabled(state);
        til.setError(msg);
    }

}