package com.nitrogen.hydro.customeviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.nitrogen.hydro.R;

public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_loading);
        setCancelable(false);
    }

}
