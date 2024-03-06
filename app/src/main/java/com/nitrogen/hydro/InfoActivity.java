package com.nitrogen.hydro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class InfoActivity extends AppCompatActivity {

    TextInputEditText name, family;
    AppCompatTextView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        name = findViewById(R.id.et_name);
        family = findViewById(R.id.et_family);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("account" , MODE_PRIVATE).edit().putString("name" , name.getText().toString()).putString("family" , family.getText().toString()).putBoolean("open" , true).apply();
                startActivity(new Intent(InfoActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}