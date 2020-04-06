package com.src.inthree;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstPage  extends AppCompatActivity {

    @BindView(R.id.inward_page)
    LinearLayout inward_page;

    @BindView(R.id.outward_page)
    LinearLayout outward_page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.inward_page})
    void inward_page(View v) {
        SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);

        String user_id = prefs.getString("login_user_id", "");
        Intent i = new Intent(FirstPage.this, GrnListActivity.class);
        i.putExtra("login_user_id", user_id);
        startActivity(i);

    }

    @OnClick({R.id.outward_page})
    void outward_page(View v) {
        Intent i = new Intent(FirstPage.this, OutWard.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder( this )
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setIcon( android.R.drawable.ic_dialog_alert )
                //.show()
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
