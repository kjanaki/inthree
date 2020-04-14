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
import com.src.inthree.Adapter.WarehouseListRecycleAdapter;
import com.src.inthree.model.StockListResponse;
import com.src.inthree.model.UserRequestModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StockActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    TextView emptyView;
    WarehouseListRecycleAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Warehouse ");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });
        init_component();
    }



    private void init_component() {

        recyclerView = findViewById(R.id.recyclerView);
        emptyView = (TextView) findViewById(R.id.empty_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new WarehouseListRecycleAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        progressDialog = new ProgressDialog(StockActivity.this);
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
            progressDialog.dismiss();

            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
        UserRequestModel request = new UserRequestModel();
        Intent i = getIntent();
        String user_id = i.getSerializableExtra("login_user_id").toString();
        request.setUser_id(user_id);


        Call<StockListResponse> call = api.get_statistics_data(request);

        // Read the response from API and assign the details into PoOrder Model
        call.enqueue(new Callback<StockListResponse>() {
            @Override
            public void onResponse(Call<StockListResponse> call, Response<StockListResponse> response) {
                progressDialog.dismiss();
                String po_api_status = response.body().getStatus();
                String po_api_msg = response.body().getApi_message();
                if (po_api_status.equals("success")) {

                    List<StockListResponse.WarehouseList> checklistResponse = response.body().getList_details();

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
            public void onFailure(Call<StockListResponse> call, Throwable t) {
                progressDialog.dismiss();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}


