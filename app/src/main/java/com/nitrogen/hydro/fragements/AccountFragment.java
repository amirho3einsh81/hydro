package com.nitrogen.hydro.fragements;

import static android.content.Context.POWER_SERVICE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nitrogen.hydro.R;
import com.nitrogen.hydro.customeviews.ProgressDialog;
import com.nitrogen.hydro.utils.InternetCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private AppCompatTextView tvPremium, tvVersion, tvName, tvBiography, textView;
    private AppCompatImageView imgUserProf;
    private LinearLayoutCompat btnMessage,
            btnAddWidget,
            btnSource,
            btnFailure,
            btnBackground,
            btnSaved,
            btnSocial,
            btnGmail,
            btnTextSize,
            btnOptimize,
            btnNotifySetting,
            btnBuyPremium,
            btnAccount,
            btnSecurity;
    private AppCompatCheckBox btnTheme;
    private static final int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;
    private static final int IGNORE_APPEAR_ON_TOP_REQUEST = 1003;
    private RelativeLayout btnLikeWe;
    private Handler h = new Handler();
    private Runnable runnablePremium, runnableOnClick;
    View view;
    RequestQueue queue;
    InternetCheck internetCheck;
    SharedPreferences pref;
    LinearLayoutCompat information;
    ProgressDialog progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        pref = getContext().getSharedPreferences("account", getContext().MODE_PRIVATE);
        init();
        queue = Volley.newRequestQueue(getContext());
        internetCheck = new InternetCheck(getContext());
        initInformation();
//        check theme
        initThemeBtn();
//        String name = pref.getString("name", getResources().getString(R.string.gust_user));
//        String bio = pref.getString("biography", getResources().getString(R.string.gust_user));
        if (!internetCheck.isConnected()) {
            information.setVisibility(View.VISIBLE);
//            tvBiography.setText(bio);
//            tvName.setText(name);
            tvName.append(pref.getString("family",""));
        }
        return view;
    }

    private void init() {
        tvVersion = view.findViewById(R.id.tv_nitrogen_version);
        btnBackground = view.findViewById(R.id.btn_background);
//        tvVersion.setText("version " + BuildConfig.VERSION_NAME);
        btnAccount = view.findViewById(R.id.btn_edit_panel);
        btnSocial = view.findViewById(R.id.btn_social_media);
        btnGmail = view.findViewById(R.id.btn_gmail);
        btnLikeWe = view.findViewById(R.id.btn_like_we);
        btnFailure = view.findViewById(R.id.btn_failure);
        btnSource = view.findViewById(R.id.btn_source);
    }
    private void initInformation() {
        progress = new ProgressDialog(getContext());
        information = view.findViewById(R.id.information);
        information.setVisibility(View.GONE);
        tvName = view.findViewById(R.id.tv_name);
        tvBiography = view.findViewById(R.id.tv_biography);
    }
    private void initThemeBtn() {
        btnTheme = view.findViewById(R.id.btn_theme);
        imgUserProf = view.findViewById(R.id.img_user_prof);
        btnTheme.setChecked(pref.getInt("setTheme", 0) == 1);
        btnTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    pref.edit().putInt("setTheme", 1).apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    pref.edit().putInt("setTheme", 0).apply();
                }
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        progress.dismiss();
        h.removeCallbacks(getInform);
        h.removeCallbacks(runnablePremium);
        h.removeCallbacks(runnableOnClick);
    }
    @Override
    public void onPause() {
        super.onPause();
        progress.dismiss();
        h.removeCallbacks(getInform);
        h.removeCallbacks(runnablePremium);
        h.removeCallbacks(runnableOnClick);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        progress.dismiss();
        h.removeCallbacks(getInform);
        h.removeCallbacks(runnablePremium);
        h.removeCallbacks(runnableOnClick);
        h = null;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
     /*       case R.id.btn_edit_panel:
//                Navigation.findNavController(v).navigate(R.id.action_accountFragment_to_editAboutFragment);
                break;
            case R.id.btn_social_media:
//                Navigation.findNavController(v).navigate(R.id.action_accountFragment_to_socialMedialFragment);
                break;*/
//            case R.id.btn_gmail:
//                try {
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "nitrogenmsg@gmail.com"));
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
//                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), "متاسفانه جیمیل در دستگاه شما نصب نمیباشد", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.btn_like_we:
//                Intent sendIntent = new Intent(Intent.ACTION_SEND)
//                        .setType("text/plain")
//                        .putExtra(Intent.EXTRA_TEXT, "سلام چطوری؟ من نیتروژن رو نصب کردم، خیلی عالییه، یه اپلیکیشن برای شناخت بهتر خودته، اگه میخوای خودساخته بشی، خودت نصبش کن مطمئنم که پشیمون نمیشی دانلود از کافه بازار: " +
//                                "\n" +
//                                "https://www.cafebazaar.ir/app/com.danesh.mynitrogen" +
//                                "\n" +
//                                "نیتروژن ");
//                getActivity().startActivity(Intent.createChooser(sendIntent, "اپلیکیشن نیتروژن"));
//                break;
//            case R.id.btn_failure:
//                try {
//                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "nitrogenmsg@gmail.com"));
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
//                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), "متاسفانه جیمیل در دستگاه شما نصب نمیباشد", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//                break;
            default:
                break;
        }
    }
    Runnable getInform = new Runnable() {
        @Override
        public void run() {
            if (!internetCheck.isConnected()){
                progress.dismiss();
                return;
            }
            getInformation();
        }
    };
    private void getInformation() {
        String url = getResources().getString(R.string.http_url);
        JsonObjectRequest jos = new JsonObjectRequest(
                Request.Method.GET,
                url + "information.php?username="+pref.getString("username","-1")+"&password="+pref.getString("password","-1"),
                new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getProfile(response.getString("profile"));
                    tvName.setText(response.getJSONObject("information").getString("name"));
                    tvName.append(response.getJSONObject("information").getString("family"));
                    tvBiography.setText(response.getJSONObject("information").getString("biography"));
//                    profile = response.getJSONObject("information").getString("profile");
                    information.setVisibility(View.VISIBLE);
                    pref.edit()
                            .putString("username", response.getJSONObject("information").getString("username"))
                            .putString("name", response.getJSONObject("information").getString("name"))
                            .putString("family", response.getJSONObject("information").getString("family"))
                            .putString("gender", response.getJSONObject("information").getString("gender"))
                            .putString("profile", response.getString("profile"))
                            .putString("biography", response.getJSONObject("information").getString("biography"))
                            .apply();
                    Log.e("exception" , "res: "+ response.toString());
                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                    Log.e("exception","error response: "+e.getMessage());
                }
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String name = pref.getString("name", "");
                String bio = pref.getString("biography", "");
                tvBiography.setText(bio);
                tvName.setText(name);
                tvName.append(pref.getString("family",""));
                getProfile(pref.getString("profile",""));
                information.setVisibility(View.VISIBLE);
                progress.dismiss();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username" , pref.getString("username","-1"));
                map.put("password" , pref.getString("password","-1"));
                return map;
            }
        };
        queue.add(jos);
        progress.dismiss();
    }
    private void getProfile(String content){
        byte[] bytes = Base64.decode(content,Base64.DEFAULT);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imgUserProf.setImageBitmap(bitmap1);
    }
}