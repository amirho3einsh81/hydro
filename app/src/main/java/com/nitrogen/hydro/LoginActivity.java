package com.nitrogen.hydro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nitrogen.hydro.customeviews.ProgressDialog;
import com.nitrogen.hydro.utils.FirstPrifrence;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    AppCompatTextView btnSubmit, btnRegister;
    TextInputEditText inUsermethod, inPass;
    TextInputLayout layUsername, layPass;
    Handler h = new Handler();
    RequestQueue queue;
    ProgressBar loading;
    SharedPreferences pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        queue = Volley.newRequestQueue(this);
        pref = getSharedPreferences("account", Context.MODE_PRIVATE);
        dialog = new ProgressDialog(this);
        initViews();

    }

    private boolean notStartUsername(String s) {
        if (s.startsWith("_") || s.startsWith(".")) {
            return false;
        }
        return true;
    }

    private void initViews() {
        btnSubmit = findViewById(R.id.submit);
        btnRegister = findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loading = findViewById(R.id.process);
        inUsermethod = findViewById(R.id.in_username);
        inUsermethod.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.toString().matches("[a-zA-Z0-9_.]")) {
                            return src;
                        }
                        return "";
                    }
                }});
        layUsername = findViewById(R.id.layer_username);
        layPass = findViewById(R.id.layer_pass);
        inPass = findViewById(R.id.in_pass);
        inPass.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence src, int start,
                                       int end, Spanned dst, int dstart, int dend) {
                Log.e("tag", "start: " + start + " dstart: " + dstart + " end: " + end + " dend: " + dend);
                if (src.toString().matches("[a-zA-Z0-9!@#$%&*?._]")) {
                    return src;
                }
                return "";
            }
        }});
        btnSubmit.setOnClickListener(v -> {
            h.removeCallbacks(getUsername);
            h.postDelayed(getUsername, 500);
            dialog.show();
        });
    }

    private boolean checkPhoneSyntax(String phoneNum) {
        if (phoneNum.startsWith("9")) {
            return true;
        }
        return false;
    }

    private void action(TextInputLayout til, String msg, boolean state) {
        til.setErrorEnabled(state);
        til.setError(msg);
    }

    Runnable getUsername = new Runnable() {
        @Override
        public void run() {
            if (isOk()) {
                checkUser();
            }
        }
    };

    ProgressDialog dialog;

    private void checkUser() {
        String url = getResources().getString(R.string.http_url);
//      Request a string response from the provided URL.
        JSONObject object = new JSONObject();
        try {
            object.put("usermethod", inUsermethod.getText().toString());
            byte[] pass = Base64.encode(inPass.getText().toString().getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            object.put("password", inPass.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String body = object.toString();
        JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.POST
                , url + "login.php"
                , new JSONObject()
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("state").equals("0") &&
                            response.getString("object").equals("password")) {
                        action(layPass, "رمزعبور یا اکانت وارد شده نادرست است", true);
                    } else if (response.getString("state").equals("0") &&
                            response.getString("object").equals("username")) {
                        action(layUsername, "حسابی با این اطلاعات پیدا نشد", true);
                    } else {
                        action(layPass, "", false);
                        action(layUsername, "", false);
                        pref.edit()
                                .putString("username", response.getJSONObject("information").getString("username"))
                                .putString("name", response.getJSONObject("information").getString("name"))
                                .putString("gender", response.getJSONObject("information").getString("gender"))
                                .putString("biography", response.getJSONObject("information").getString("biography"))
                                .putString("password", inPass.getText().toString())
                                .putBoolean("isLogin", true).apply();
                        FirstPrifrence preference = new FirstPrifrence(LoginActivity.this, "");
                        preference.setPrefvalue(false);
                        startActivity(new Intent(LoginActivity.this, StartActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                    Log.e("exception", e.getMessage());
                }
                Log.e("exception", response.toString());
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("exception", "error: " + error.getMessage().toString());
                dialog.dismiss();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return body.getBytes(StandardCharsets.UTF_8);
            }
        };
        queue.add(arrReq);
    }

    private boolean isOk() {
        if (inUsermethod.getText().toString().isEmpty()) {
            action(layUsername, "لطفا یک نام کاربری انتخاب کنید", true);
            dialog.dismiss();
            return false;
        } else {
            if (inUsermethod.getText().toString().length() < 4) {
                action(layUsername, "نام کاربری کمتر از4 کاراکتر است", true);
                dialog.dismiss();
                return false;
            } else {
                action(layUsername, "", false);
            }
        }
        if (inPass.getText().toString().isEmpty()) {
            action(layPass, "لطفا یک رمز عبور انتخاب کنید", true);
            dialog.dismiss();
            return false;
        } else {
            if (inPass.getText().toString().length() < 6) {
                action(layPass, "رمز عبور نباید کمتر از6 کاراکتر باشد", true);
                dialog.dismiss();
                return false;
            } else {
                action(layPass, "", false);
            }
        }
        dialog.dismiss();
        return true;

    }

    @Override
    public void onStop() {
        super.onStop();
        h.removeCallbacks(getUsername);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(getUsername);
    }
}