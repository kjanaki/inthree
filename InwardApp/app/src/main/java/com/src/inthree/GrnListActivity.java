package com.src.inthree;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.API.Api;
import com.src.inthree.Adapter.GRNListRecycleListAdapter;
import com.src.inthree.model.GrnListRequest;
import com.src.inthree.model.GrnListResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GrnListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<GrnListResponse.GrnModel> grn_list_full = new ArrayList<>();
    List<GrnListResponse.GrnModel> grn_list_filter = new ArrayList<>();
    

    @BindView(R.id.input_search)
    EditText inputSearch;

    @BindView(R.id.clear_search)
    ImageView clear_search;

    GRNListRecycleListAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grn_list_activity);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Purchase");
        setSupportActionBar(toolbar);
//        if(getSupportActionBar()!=null){
//            Drawable drawable= getResources().getDrawable(R.drawable.ic_launcher_background);
//            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(newdrawable);
//
//        }
        init_component();

    }

    private void init_component() {
        // Initalize the components in View
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new GRNListRecycleListAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
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
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        load_grn_list();

    }

    private void processQuery(String query) {
        // Filter the query on Adapter
        List<GrnListResponse.GrnModel> result = new ArrayList<>();
        grn_list_filter = new ArrayList<>();
        // case insensitive search
        for (GrnListResponse.GrnModel list : grn_list_full) {
            if (list.getGrn_no().contains(query.toLowerCase()) ||list.getPo_order_number().contains(query.toLowerCase())) {
                grn_list_filter.add(list);
            }
        }
        recyclerViewAdapter.setData(grn_list_filter);
    }


    private void load_grn_list() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        GrnListRequest request = new GrnListRequest();
        Intent i = getIntent();
        String user_id = i.getSerializableExtra("login_user_id").toString();
        request.setUser_id(user_id);


        Call<GrnListResponse> call = api.get_po_grn_details(request);

        // Read the response from API and assign the details into PoOrder Model
        call.enqueue(new Callback<GrnListResponse>() {
            @Override
            public void onResponse(Call<GrnListResponse> call, Response<GrnListResponse> response) {
                String po_api_status = response.body().getStatus();
                String po_api_msg = response.body().getApi_message();
                if (po_api_status.equals("success")) {
                    List<GrnListResponse.GrnModel> grnResponse = response.body().getGrn_model();
                    grn_list_full = grnResponse;
                    recyclerViewAdapter.setData(grnResponse);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                } else {
                    Toast.makeText(getApplicationContext(), po_api_msg, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<GrnListResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_logout:
//                SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.clear();
//                editor.apply();
//                Intent i = new Intent(GrnListActivity.this,MainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//
//                break;
//
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
