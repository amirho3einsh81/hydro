package com.nitrogen.hydro.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FirstPrifrence {

    SharedPreferences pref ;
    Context context;
    String keyName;
    private static final String PREF_NAME = "open";
    private static final boolean PREF_VALUE = true;
    public FirstPrifrence(Context context , String keyName ){
        this.context = context;
        this.keyName = "opened";
        pref = context.getSharedPreferences(PREF_NAME , Context.MODE_PRIVATE);
    }
    public boolean pref(){
        return pref.getBoolean(keyName , PREF_VALUE);
    }

    public void setPrefvalue(boolean prefvalue){
        pref.edit().putBoolean(keyName , prefvalue).apply();
    }
}
