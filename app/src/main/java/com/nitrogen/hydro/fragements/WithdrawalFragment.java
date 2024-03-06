package com.nitrogen.hydro.fragements;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nitrogen.hydro.R;

import java.io.*;

public class WithdrawalFragment extends Fragment {

    public WithdrawalFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_withdrawal, container, false);
    }
}