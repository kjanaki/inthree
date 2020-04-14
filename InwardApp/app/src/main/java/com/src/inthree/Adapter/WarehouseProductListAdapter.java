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
import com.src.inthree.model.StockListResponse;
import com.src.inthree.model.WarehouseProductListResponse;

import java.util.ArrayList;
import java.util.List;


public class WarehouseProductListAdapter extends RecyclerView.Adapter<WarehouseProductListAdapter.ViewHolder> implements Filterable {
    public List<WarehouseProductListResponse.StockList> warehouse_productlist_records;
    Context context;

    public WarehouseProductListAdapter() {
        warehouse_productlist_records = new ArrayList<>();
    }

    @Override
    public WarehouseProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouse_details_view, parent, false);

        WarehouseProductListAdapter.ViewHolder viewHolder = new WarehouseProductListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WarehouseProductListAdapter.ViewHolder holder, int position) {
        WarehouseProductListResponse.StockList productModel = warehouse_productlist_records.get(position);

        holder.item_code.setText((productModel.getSku() == null) ? "" : productModel.getSku());
        holder.product_name.setText((productModel.getProduct_name() == null) ? "" : productModel.getProduct_name());
        holder.good_qty.setText((productModel.getGood_stock() == null) ? "" : productModel.getGood_stock());
        holder.damaged_qty.setText((productModel.getDamaged_stock() == null) ? "" : productModel.getDamaged_stock());
        if(productModel.getProduct_image()!=null){
            Bitmap im= GrnDetailsActivity.getBitmapFromURL(productModel.getProduct_image().toString());
            if(im!=null){
                holder.product_image.setImageBitmap(im);
            }

        }

    }

    @Override
    public int getItemCount() {

        return warehouse_productlist_records.size();
    }

    public void setData(List<WarehouseProductListResponse.StockList> data) {
        this.warehouse_productlist_records = data;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView product_name, item_code, good_qty,damaged_qty;
        ImageView product_image;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);


            item_code = (TextView) view.findViewById(R.id.item_code);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            product_name = (TextView) view.findViewById(R.id.product_name);
            good_qty = (TextView) view.findViewById(R.id.good_qty);
            damaged_qty = (TextView) view.findViewById(R.id.damaged_qty);
//            cardView = view.findViewById(R.id.cardView);



        }
    }
}

