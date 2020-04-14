package com.src.inthree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.logout_layout)
    LinearLayout logout_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        ButterKnife.bind(this);

        logout_layout.callOnClick();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Settings ");
        setSupportActionBar(toolbar);




    }
    @OnClick({R.id.logout_layout})
    void login(View v) {
        log_out();
    }
    void log_out(){

        SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(SettingActivity.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
