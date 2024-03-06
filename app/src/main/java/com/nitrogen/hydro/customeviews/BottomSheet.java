package com.nitrogen.hydro.customeviews;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nitrogen.hydro.R;

public class BottomSheet extends BottomSheetDialog {
    String title, message, btnPositive, btnNegative;
    int icon, banner;
    public BottomSheet(@NonNull Context context) {
        super(context);
        init(context);
        setCanceledOnTouchOutside(false);
    }

    public BottomSheet(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    AppCompatTextView txtTitle, txtMessage, txtBtnPositive, txtBtnNegative, txtBtnNotural;
    AppCompatImageView imgIcon, imgBanner;
    public AppCompatTextView getTxtTitle() {
        return txtTitle;
    }
    public AppCompatTextView getTxtMessage() {
        return txtMessage;
    }
    public AppCompatTextView getTxtBtnPositive() {
        return txtBtnPositive;
    }
    public AppCompatTextView getTxtBtnNegative() {
        return txtBtnNegative;
    }
    public AppCompatImageView getImgIcon() {
        return imgIcon;
    }
    public AppCompatImageView getImgBanner() {
        return imgBanner;
    }
    FrameLayout childLayer;
    ProgressDialog dialog;
    private void init(Context ctx ){
        setContentView(R.layout.bottom_sheet_alert);
        SharedPreferences pref = ctx.getSharedPreferences("account", MODE_PRIVATE);
        dialog = new ProgressDialog(ctx);
        if (pref.getInt("setTheme", 0) == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
                                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                        );
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(/*ColorDatabase.TRANSPARENT*/ContextCompat.getColor(ctx, R.color.revers_500));
                getWindow().setNavigationBarColor(ContextCompat.getColor(ctx, R.color.revers_500));
            }
        } else if (pref.getInt("setTheme", 1) == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(/*ColorDatabase.TRANSPARENT*/ContextCompat.getColor(ctx, R.color.revers_500));
                getWindow().setNavigationBarColor(ContextCompat.getColor(ctx, R.color.revers_500));
            }
        }
//        Theme.setTheme(act , getWindow());
        imgIcon = findViewById(R.id.icon);
        imgIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ctx , R.color.gray_700)));
        imgBanner = findViewById(R.id.img_banner);
        txtTitle = findViewById(R.id.title);
        txtMessage = findViewById(R.id.description);
        txtBtnPositive = findViewById(R.id.ok); txtBtnNegative = findViewById(R.id.cancel);
//        txtBtnNotural = binding.notral;
        childLayer = findViewById(R.id.child_layout);
        imgIcon.setVisibility(View.GONE);
        txtTitle.setVisibility(View.GONE);
        txtMessage.setVisibility(View.GONE);
        txtBtnPositive.setVisibility(View.GONE);
        txtBtnNegative.setVisibility(View.GONE);
        imgBanner.setVisibility(View.GONE);
        txtBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPositiveButtonClickListener!= null) {
                    onPositiveButtonClickListener.onPositiveClickListener(v);
                    if (isShowingPositiveLoading) {
                        dialog.show();
                    }
                }
            }
        });
        txtBtnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNegativeButtonClickListener!=null){
                    onNegativeButtonClickListener.onNegativeClickListener(v);
                    if (isShowingNegativeLoading) {
                        dialog.show();
                    }
                }
            }
        });
    }

    public ProgressDialog getDialog() {
        return dialog;
    }
    boolean isShowingPositiveLoading = false;
    public void setLoadingPositive(boolean isShowing) {
        this.isShowingPositiveLoading = isShowing;
    }
    boolean isShowingNegativeLoading = false;
    public void setLoadingNegative(boolean isShowing) {
        this.isShowingNegativeLoading = isShowing;
    }

    public void setChildLayout(View child){
        childLayer.removeAllViews();
        childLayer.addView(child);
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(title);
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        txtMessage.setVisibility(View.VISIBLE);
        txtMessage.setText(message);
        this.message = message;
    }

    public String getBtnPositive() {
        return btnPositive;
    }

    public void setBtnPositive(String btnPositive) {
        txtBtnPositive.setVisibility(View.VISIBLE);
        txtBtnPositive.setText(btnPositive);
        this.btnPositive = btnPositive;
    }

    public String getBtnNegative() {
        return btnNegative;
    }

    public void setBtnNegative(String btnNegative) {
        txtBtnNegative.setVisibility(View.VISIBLE);
        txtBtnNegative.setText(btnNegative);
        this.btnNegative = btnNegative;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(icon);
        this.icon = icon;
    }

    public int getBanner() {
        return banner;
    }

    public void setBanner(int banner) {
        imgBanner.setVisibility(View.VISIBLE);
        imgBanner.setImageResource(banner);
        this.banner = banner;
    }

    OnPositiveButtonClickListener onPositiveButtonClickListener;
    OnNegativeButtonClickListener onNegativeButtonClickListener;

    public void setOnPositiveButtonClickListener(String title , OnPositiveButtonClickListener onPositiveButtonClickListener){
        setBtnPositive(title);
        this.onPositiveButtonClickListener = onPositiveButtonClickListener;
    }
    public void setOnNegativeButtonClickListener(String title , OnNegativeButtonClickListener onNegativeButtonClickListener){
        setBtnNegative(title);
        this.onNegativeButtonClickListener = onNegativeButtonClickListener;
    }
    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener onPositiveButtonClickListener){
        this.onPositiveButtonClickListener = onPositiveButtonClickListener;
    }
    public void setOnNegativeButtonClickListener(OnNegativeButtonClickListener onNegativeButtonClickListener){
        this.onNegativeButtonClickListener = onNegativeButtonClickListener;
    }
    public interface OnPositiveButtonClickListener{
        public boolean onPositiveClickListener(View v);
    }
    public interface OnNegativeButtonClickListener{
        public boolean onNegativeClickListener(View v);
    }
}
