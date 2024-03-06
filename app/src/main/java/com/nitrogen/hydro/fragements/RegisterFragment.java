package com.nitrogen.hydro.fragements;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nitrogen.hydro.R;
import com.nitrogen.hydro.customeviews.ProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class RegisterFragment extends Fragment {

    View view;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        queue = Volley.newRequestQueue(getContext());
        url = getResources().getString(R.string.http_url);
        dialog = new ProgressDialog(getContext());
        pref = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
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
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
        });
        btnSubmit.setOnClickListener(v -> {
            dialog.show();
            if (isOk()) {
                h.removeCallbacks(rIsExist);
                dialog.show();
                h.postDelayed(rIsExist, 500);
            }
        });

        return view;
    }

    Runnable rIsExist = new Runnable() {
        @Override
        public void run() {
            getExists();
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
                new Response.Listener<>() {
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
                                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_initInformationFragment);
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
            /*@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username" , inUsername.getText().toString().trim());
                params.put("phonenumber" , inPhone.getText().toString().trim());
                return params;
            }*/

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
        btnSubmit = view.findViewById(R.id.submit);
        btnLogin = view.findViewById(R.id.login);
        layPhone = view.findViewById(R.id.layer_phone);
        inPhone = view.findViewById(R.id.in_phone);
        /*TODO: initial (username)*/
        inUsername = view.findViewById(R.id.in_username);
        layUsername = view.findViewById(R.id.layer_username);
        /*TODO: initial (pass)*/
        layPass = view.findViewById(R.id.layer_pass);
        inPass = view.findViewById(R.id.in_pass);

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