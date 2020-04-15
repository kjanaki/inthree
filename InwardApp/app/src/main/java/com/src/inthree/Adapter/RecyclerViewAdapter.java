package com.src.inthree.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.GrnDetailsActivity;
import com.src.inthree.R;
import com.src.inthree.model.WarehouseProductListResponse;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    //public List<String> mItemList;

    public List<WarehouseProductListResponse.StockList> mItemList;
    Context context;


    public RecyclerViewAdapter(List<WarehouseProductListResponse.StockList> itemList) {

        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouse_details_view, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView product_name, item_code, good_qty,damaged_qty;
        ImageView product_image;
        public CardView cardView;

        public ItemViewHolder(@NonNull View view) {
            super(view);

            item_code = (TextView) view.findViewById(R.id.item_code);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            product_name = (TextView) view.findViewById(R.id.product_name);
            good_qty = (TextView) view.findViewById(R.id.good_qty);
            damaged_qty = (TextView) view.findViewById(R.id.damaged_qty);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder holder, int position) {

        WarehouseProductListResponse.StockList productModel = mItemList.get(position);

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


}