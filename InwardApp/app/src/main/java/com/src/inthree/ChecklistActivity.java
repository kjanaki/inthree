package com.src.inthree;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.API.Api;
import com.src.inthree.Adapter.CheckListRecycleAdapter;
import com.src.inthree.model.CheckListRequest;
import com.src.inthree.model.CheckListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChecklistActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    TextView emptyView;
    CheckListRecycleAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Checklist ");
        setSupportActionBar(toolbar);
        init_component();

    }

    private void init_component() {

        recyclerView = findViewById(R.id.recyclerView);
        emptyView = (TextView) findViewById(R.id.empty_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new CheckListRecycleAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        progressDialog = new ProgressDialog(ChecklistActivity.this);
        progressDialog.setTitle("");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.getProgress();
        progressDialog.setCancelable(false);
        progressDialog.show();
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        if(connected) {
            load_checklist();
        }
        else{

            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            progressDialog.dismiss();
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Message");
            builder.setMessage("No Internet Connection. Kindly check your connectivity");
            // add the buttons
            builder.setPositiveButton("Continue", null);
            builder.setNegativeButton("Cancel", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();


        }
    }

    private void load_checklist() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        CheckListRequest request = new CheckListRequest();
        Intent i = getIntent();
        String user_id = i.getSerializableExtra("login_user_id").toString();
        request.setUser_id(user_id);


        Call<CheckListResponse> call = api.get_checklist_data(request);

        // Read the response from API and assign the details into PoOrder Model
        call.enqueue(new Callback<CheckListResponse>() {
            @Override
            public void onResponse(Call<CheckListResponse> call, Response<CheckListResponse> response) {
                String po_api_status = response.body().getStatus();
                String po_api_msg = response.body().getApi_message();
                progressDialog.dismiss();
                if (po_api_status.equals("success")) {

                    List<CheckListResponse.ChecklistModel> checklistResponse = response.body().getList_details();
                   recyclerViewAdapter.setData(checklistResponse);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);

                } else {
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), po_api_msg, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<CheckListResponse> call, Throwable t) {
                progressDialog.dismiss();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}

