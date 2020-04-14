package com.src.inthree;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.API.Api;
import com.src.inthree.Adapter.WarehouseListRecycleAdapter;
import com.src.inthree.Adapter.WarehouseProductListAdapter;
import com.src.inthree.Adapter.WarehouseProductListAdapterScroll;
import com.src.inthree.model.GrnListResponse;
import com.src.inthree.model.StockListResponse;
import com.src.inthree.model.UserRequestModel;
import com.src.inthree.model.WarehouseProductListRequest;
import com.src.inthree.model.WarehouseProductListResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WarehouseProductDetailsActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    TextView emptyView;
    WarehouseProductListAdapterScroll recyclerViewAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.input_search)
    EditText inputSearch;

    @BindView(R.id.clear_search)
    ImageView clear_search;
    List<WarehouseProductListResponse.StockList> product_list_full = new ArrayList<>();
    List<WarehouseProductListResponse.StockList> product_list_filter = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_view);
        ButterKnife.bind(this);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//
//
        init_component();

    }



    private void init_component() {

        recyclerView = findViewById(R.id.recyclerView);
        emptyView = (TextView) findViewById(R.id.empty_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //recyclerViewAdapter = new WarehouseProductListAdapterScroll();

        clear_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputSearch.setText("");
                clear_search.setVisibility(View.GONE);
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                clear_search.setVisibility(View.VISIBLE);
                processQuery(inputSearch.getText().toString());

            }
        });

        Intent i = getIntent();
        String warehouse_name = i.getSerializableExtra("warehouse_name").toString();
        toolbar.setTitle(warehouse_name);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // back button pressed
//                finish();
//            }
//        });


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

    private void processQuery(String query) {
        // Filter the query on Adapter
        product_list_filter = new ArrayList<>();
        // case insensitive search
        for (WarehouseProductListResponse.StockList list : product_list_full) {
            if (((list.getSku().toLowerCase()).contains(query.toLowerCase())) ||((list.getProduct_name().toLowerCase()).contains(query.toLowerCase()))) {
                product_list_filter.add(list);
            }
        }

        recyclerViewAdapter.setData(product_list_filter);
    }

    private void load_checklist() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        WarehouseProductListRequest request = new WarehouseProductListRequest();
        Intent i = getIntent();
        String warehouse_id = i.getSerializableExtra("warehouse_id").toString();
        SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);

        String user_id = prefs.getString("login_user_id", "");
        request.setUser_id(user_id);
        request.setWarehouse_id(warehouse_id);


        Call<WarehouseProductListResponse> call = api.get_warehouse_product_list(request);

        // Read the response from API and assign the details into PoOrder Model
        call.enqueue(new Callback<WarehouseProductListResponse>() {
            @Override
            public void onResponse(Call<WarehouseProductListResponse> call, Response<WarehouseProductListResponse> response) {
                String po_api_status = response.body().getStatus();
                String po_api_msg = response.body().getApi_message();
                if (po_api_status.equals("success")) {

                    List<WarehouseProductListResponse.StockList> checklistResponse = response.body().getStocklist();
                    product_list_full = response.body().getStocklist();
                    recyclerViewAdapter = new WarehouseProductListAdapterScroll(recyclerView, product_list_full, WarehouseProductDetailsActivity.this);
                    recyclerView.setAdapter(recyclerViewAdapter);
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
            public void onFailure(Call<WarehouseProductListResponse> call, Throwable t) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

//    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // back button pressed
//        }
//    });
}


