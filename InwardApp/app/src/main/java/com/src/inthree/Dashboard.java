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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.src.inthree.Adapter.DashboardGridAdapter;

public class Dashboard extends AppCompatActivity {

    String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        String[] web = {
                "Purchase",
                "Sales",
                "Stock",
                "Check List",
                "Search",
                "Settings",


        };
        int[] imageId = {
                R.drawable.purchase,
                R.drawable.sales,
                R.drawable.stock,
                R.drawable.checklist_img,
                R.drawable.search,
                R.drawable.setting,

        };
        DashboardGridAdapter adapter = new DashboardGridAdapter(Dashboard.this, web, imageId);
        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);

        user_id = prefs.getString("login_user_id", "");


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(Dashboard.this, "" + position, Toast.LENGTH_SHORT).show();
                Intent i;

                switch (position) {
                    case 0:
                        i = new Intent(Dashboard.this, GrnListActivity.class);
                        i.putExtra("login_user_id", user_id);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(Dashboard.this, SalesActivity.class);
                        i.putExtra("login_user_id", user_id);
                        startActivity(i);
                        break;

                    case 2:
                        i = new Intent(Dashboard.this, ChecklistActivity.class);
                        i.putExtra("login_user_id", user_id);
                        startActivity(i);
                        break;

                    case 3:
                        i = new Intent(Dashboard.this, StockActivity.class);
                        i.putExtra("login_user_id", user_id);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(Dashboard.this, SerachActivity.class);
                        i.putExtra("login_user_id", user_id);
                        startActivity(i);
                        break;
                    case 5:
                        i = new Intent(Dashboard.this, SettingActivity.class);
                        i.putExtra("login_user_id", user_id);
                        startActivity(i);
                        break;


                    default:
                        break;
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dashboard.super.onBackPressed();
                        quit();
                    }
                }).create().show();
    }

    private void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(start);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
