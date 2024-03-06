package com.nitrogen.hydro.fragements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitrogen.hydro.MainActivity;
import com.nitrogen.hydro.R;
import com.nitrogen.hydro.customeviews.BottomSheet;
import com.nitrogen.hydro.utils.FirstPrifrence;
import com.nitrogen.hydro.utils.InternetCheck;

public class SplashFragment extends Fragment {
    AppCompatTextView textView;
    SharedPreferences pref;
    ContentLoadingProgressBar progressBar;
    int versionCode;
    boolean isVerified;
    View view;
    AppCompatImageView esmalImage;
    FragmentNavigator nav;
    InternetCheck check;
    Handler h = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_splash, container, false);
        pref = getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        check = new InternetCheck(getContext());
        esmalImage = view.findViewById(R.id.img_nitrogen);
        versionCode = pref.getInt("versionCode", 0);
        isVerified = pref.getBoolean("isVerified", false);
        textView.setVisibility(View.GONE);
        h.postDelayed(runnable,3000);
//        nav = new FragmentNavigator(getContext(), getActivity().getSupportFragmentManager(), R.id.img_nitrogen);
        return view;
    }
    boolean preference;
    Runnable runnable = () -> {
        FirstPrifrence open = new FirstPrifrence(getContext(), "open");
        preference = open.pref();
        if (preference) {
            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment);
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }
    };
    Handler mainHandler = new Handler();
    private class RunnableChecker implements Runnable {
        @Override
        public void run() {
            mainHandler.postDelayed(runnable, 1500);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mainHandler.removeCallbacks(runnable);
    }
}