package com.src.inthree;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.API.Api;
import com.src.inthree.Adapter.RecyclerViewAdapter;
import com.src.inthree.Adapter.WarehouseProductListAdapterScroll;
import com.src.inthree.model.WarehouseProductListRequest;
import com.src.inthree.model.WarehouseProductListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class smartview extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();

    boolean isLoading = false;
    List<WarehouseProductListResponse.StockList> product_list_full = new ArrayList<>();
    List<WarehouseProductListResponse.StockList> product_list_full_view = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dds);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        populateData();
        //initAdapter();
       // initScrollListener();


    }

    private void populateData() {
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
                    product_list_full_view = response.body().getStocklist();
                    if(product_list_full_view.size()>0)
                    {
                        for (int i = 0 ; i<10;i++){
                            product_list_full.add(product_list_full_view.get(i)) ;
                        }
                    }
                    initAdapter();
                    initScrollListener();


                } else {

                }

            }
            @Override
            public void onFailure(Call<WarehouseProductListResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void initAdapter() {

        recyclerViewAdapter = new RecyclerViewAdapter(product_list_full);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == product_list_full.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        product_list_full.add(null);
        recyclerViewAdapter.notifyItemInserted(product_list_full.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                product_list_full.remove(product_list_full.size() - 1);
                int scrollPosition = product_list_full.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;
                for (int i = currentSize - 1 ; i<nextLimit;i++){
                    product_list_full.add(product_list_full_view.get(i)) ;
                }

//                while (currentSize - 1 < nextLimit) {
//                    product_list_full.add("Item " + currentSize);
//                    currentSize++;
//                }

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 12);


    }
}