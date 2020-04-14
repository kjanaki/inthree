package com.src.inthree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.GrnDetailsActivity;
import com.src.inthree.R;
import com.src.inthree.WarehouseProductDetailsActivity;
import com.src.inthree.model.CheckListResponse;
import com.src.inthree.model.StockListResponse;

import java.util.ArrayList;
import java.util.List;

public class WarehouseListRecycleAdapter extends RecyclerView.Adapter<WarehouseListRecycleAdapter.ViewHolder> implements Filterable {
    public List<StockListResponse.WarehouseList> check_list_records;
    Context context;

    public WarehouseListRecycleAdapter() {
        check_list_records = new ArrayList<>();
    }

    @Override
    public WarehouseListRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_content_view, parent, false);

        WarehouseListRecycleAdapter.ViewHolder viewHolder = new WarehouseListRecycleAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WarehouseListRecycleAdapter.ViewHolder holder, int position) {
        StockListResponse.WarehouseList warehouseModel = check_list_records.get(position);

        holder.warehouse_name.setText((warehouseModel.getWarehouse_name() == null) ? "" : warehouseModel.getWarehouse_name());
        holder.product_count.setText((warehouseModel.getProducts_count() == null) ? "" : warehouseModel.getProducts_count());
        holder.warehouse_id.setText((warehouseModel.getWarehouse_id() == null) ? "" : warehouseModel.getWarehouse_id());

    }

    @Override
    public int getItemCount() {

        return check_list_records.size();
    }

    public void setData(List<StockListResponse.WarehouseList> data) {
        this.check_list_records = data;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView product_count, warehouse_name, warehouse_id;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);


            product_count = (TextView) view.findViewById(R.id.wproduct_count);
            warehouse_name = (TextView) view.findViewById(R.id.warehouse_name);
            warehouse_id = (TextView) view.findViewById(R.id.warehouse_id);
            //  product_image=(ImageView) view.findViewById(R.id.product_img);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(view.getContext(), WarehouseProductDetailsActivity.class);
                    i.putExtra("warehouse_id", warehouse_id.getText().toString());
                    i.putExtra("warehouse_name", warehouse_name.getText().toString());
                    context.startActivity(i);
                }

            });


        }
    }
}

