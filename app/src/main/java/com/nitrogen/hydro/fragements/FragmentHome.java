package com.nitrogen.hydro.fragements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nitrogen.hydro.InsightsActivity;
import com.nitrogen.hydro.R;

import java.io.*;

public class FragmentHome extends Fragment {

    public FragmentHome(){
        // require a empty public constructor
    }

    View view;
    AppCompatTextView fab ;

    Dialog dDialog;
    AlertDialog.Builder alert;
    AlertDialog dialog;
    View vAlert;
    SwipeRefreshLayout refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        refresh = view.findViewById(R.id.refresh);
        refresh.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 1000);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        view.findViewById(R.id.price).setOnClickListener((v) -> {
            showDialog();
        });
        view.findViewById(R.id.refral).setOnClickListener((v) -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, "سلام چطوری؟ من هیدرو رو نصب کردم، خیلی عالییه، یه اپلیکیشن برای همشهریان همدانی" +
                            "\n" +
                            "هیدرو ");
            getActivity().startActivity(Intent.createChooser(sendIntent, "هیدرو صرفه جویی در مصرف آب"));
        });
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), InsightsActivity.class));
            }
        });
        return view;
    }
    SeekBar seekBar;
    AppCompatTextView kg , litr;
    public void showDialog() {
        dDialog = new Dialog(getContext());
        dDialog.setContentView(R.layout.dialog_litterprice);
        changeDialogSize(dDialog);
        litr = dDialog.findViewById(R.id.litr);
        kg = dDialog.findViewById(R.id.kg);
        seekBar = dDialog.findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int sum = (progress*150);
                kg.setText(sum+"تومان");
                litr.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dDialog.show();
        dDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private Point getScreenSize(Activity activity) {
        Point p = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(p);
        return p;
    }
    private void changeDialogSize(Dialog dialog) {
        Point scrSize = getScreenSize(getActivity());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout((int) (0.8 * scrSize.x), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}