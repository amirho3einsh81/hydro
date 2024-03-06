package com.nitrogen.hydro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    AppCompatTextView btnSubmit, btnLogin;
    TextInputEditText inUsername, inPhone, inPass;
    TextInputLayout layUsername, layPhone, layPass;
    String allowedChars;
    RequestQueue queue;
    Handler h = new Handler();
    ProgressDialog dialog;
    SharedPreferences pref;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        queue = Volley.newRequestQueue(this);
        url = getResources().getString(R.string.http_url);
        dialog = new ProgressDialog(this);
        pref = this.getSharedPreferences("account", Context.MODE_PRIVATE);
        initViews();
        if (!pref.getString("username", "").isEmpty() &&
                !pref.getString("phonenumber", "").isEmpty() &&
                !pref.getString("password", "").isEmpty()) {

            inUsername.setText(pref.getString("username", ""));
            inPhone.setText(pref.getString("phonenumber", ""));
            inPass.setText(pref.getString("password", ""));
        }
        inPass.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence src, int start,
                                       int end, Spanned dst, int dstart, int dend) {
                if (src.toString().matches("[a-zA-Z0-9!@#$%&*?._]")) {
                    return src;
                }

                return "";
            }
        }});
        inPhone.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence src, int start,
                                       int end, Spanned dst, int dstart, int dend) {
                if (src.toString().matches("[0-9]")) {
                    return src;
                }
                return "";
            }
        }});
        inUsername.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                if (src.toString().matches("[a-zA-Z0-9._]")) {
                    return src;
                }
                return "";
            }
        }});
        allowedChars = "[._a-z0-9]";
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
        btnSubmit.setOnClickListener(v -> {
            dialog.show();
            if (isOk()) {
                h.removeCallbacks(rIsExist);
//                dialog.show();
                dialog.show();
                h.removeCallbacks(runCreator);
                h.postDelayed(runCreator, 500);
//                h.postDelayed(rIsExist, 500);
            }
        });
    }

    Runnable rIsExist = new Runnable() {
        @Override
        public void run() {
            getExists();
        }
    };
    Runnable runCreator = new Runnable() {
        @Override
        public void run() {
            create();
        }
    };
    private void getExists() {
        JSONObject object = new JSONObject();
        try {
            object.put("username", inUsername.getText().toString().trim());
            object.put("phonenumber", inPhone.getText().toString().trim());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String body = object.toString();
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST, url + "is_exists.php", new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            action(layUsername, "", false);
                            action(layPhone, "", false);
                            if (response.getString("username").equals("true")) {
                                action(layUsername, "نام کاربری مطعلق به شخص دیگری است", true);
                            } else if (response.getString("phonenumber").equals("true")) {
                                action(layPhone, "این شماره متعلق به شخص دیگری است", true);
                            } else if (response.getString("state").equals("false")) {
                                action(layUsername, "", false);
                                action(layPhone, "", false);
                                Log.e("exception", "response : " + response);
                                pref.edit()
                                        .putString("username", inUsername.getText().toString())
                                        .putString("phonenumber", inPhone.getText().toString())
                                        .putString("password", inPass.getText().toString()).apply();
                                if (!pref.getString("username", "").isEmpty() &&
                                        !pref.getString("phonenumber", "").isEmpty() &&
                                        !pref.getString("password", "").isEmpty()) {
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("exception", "response err: " + error.getMessage());
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
        queue.add(objectRequest);
    }

    private boolean isOk() {
        if (inUsername.getText().toString().isEmpty()) {
            action(layUsername, "لطفا یک نام کاربری انتخاب کنید", true);
            dialog.dismiss();
            return false;
        } else {
            if (inUsername.getText().toString().length() < 4) {
                action(layUsername, "نام کاربری کمتر از4 کاراکتر است", true);
                dialog.dismiss();
                return false;
            } else {
                action(layUsername, "", false);
            }
        }
        if (inPhone.getText().toString().isEmpty()) {
            action(layPhone, "لطفا شماره موبایل خود را وارد کنید", true);
            dialog.dismiss();
            return false;
        } else {
            if (!inPhone.getText().toString().startsWith("9")) {
                action(layPhone, "شماره موبایل باید با 9 شروع شود", true);
                dialog.dismiss();
                return false;
            } else if (inPhone.getText().toString().length() > 10 || inPhone.getText().toString().length() < 10) {
                action(layPhone, "شماره موبایل باید ده رقمی باشد", true);
                dialog.dismiss();
                return false;
            } else {
                action(layPhone, "", false);
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

    private void initViews() {
        btnSubmit = findViewById(R.id.submit);
        btnLogin = findViewById(R.id.login);
        layPhone = findViewById(R.id.layer_phone);
        inPhone = findViewById(R.id.in_phone);
        /*TODO: initial (username)*/
        inUsername = findViewById(R.id.in_username);
        layUsername = findViewById(R.id.layer_username);
        /*TODO: initial (pass)*/
        layPass = findViewById(R.id.layer_pass);
        inPass = findViewById(R.id.in_pass);

        inUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                action(layUsername, "", false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                action(layPhone, "", false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                action(layPass, "", false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void action(TextInputLayout til, String msg, boolean state) {
        til.setErrorEnabled(state);
        til.setError(msg);
    }

    private void create() {
        String url = getResources().getString(R.string.http_url);
        JSONObject jsonBody = new JSONObject();
        try {
//            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//            String ipAddress = "null";
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.CUPCAKE) {
//                ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
//            }
            jsonBody.put("phonenumber", inPhone.getText().toString());
            jsonBody.put("username", inUsername.getText().toString());
            byte[] pass = Base64.encode(pref.getString("password", "").getBytes(), Base64.DEFAULT);
            jsonBody.put("password", inPass.getText().toString());
//            jsonBody.put("userIP", ipAddress.toString());
//            jsonBody.put("model", Build.MODEL.toString());
//            jsonBody.put("brand", Build.BRAND.toString());
//            jsonBody.put("device", Build.DEVICE.toString());
//            jsonBody.put("profile", pref.getString("profile", ""));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String requestBody = jsonBody.toString();
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,
                url + "register.php",
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        FirstPrifrence preference = new FirstPrifrence(RegisterActivity.this, "opened");
                        preference.setPrefvalue(false);
                        pref.edit()/*.putString("name", inName.getText().toString())*/
                                .putString("gender", "default")
                                .putBoolean("isLogin", true).apply();
                        dialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this, StartActivity.class));
                        finish();
                        Log.e("exception", "response : " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("exception", "error : " + error.getMessage());
                dialog.dismiss();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
        queue.add(jor);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(rIsExist);
    }

    @Override
    public void onStop() {
        super.onStop();
        h.removeCallbacks(rIsExist);
    }
}