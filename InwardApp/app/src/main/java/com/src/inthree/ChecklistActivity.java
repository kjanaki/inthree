package com.src.inthree;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChecklistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inprogess_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Checklist ");
        setSupportActionBar(toolbar);
    }
}

