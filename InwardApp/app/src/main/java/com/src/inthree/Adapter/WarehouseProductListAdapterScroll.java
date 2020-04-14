package com.src.inthree.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.GrnDetailsActivity;
import com.src.inthree.OnLoadMoreListener;
import com.src.inthree.R;
import com.src.inthree.model.WarehouseProductListResponse;

import java.util.ArrayList;
import java.util.List;


public class WarehouseProductListAdapterScroll extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<WarehouseProductListResponse.StockList> warehouse_productlist_records;
    Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private Activity activity;
    private List<WarehouseProductListResponse.StockList> contacts;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public WarehouseProductListAdapterScroll(RecyclerView recyclerView, List<WarehouseProductListResponse.StockList> contacts, Activity activity) {
        this.contacts = contacts;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return contacts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    public WarehouseProductListAdapterScroll() {
        warehouse_productlist_records = new ArrayList<>();
    }



//    @Override
//    public WarehouseProductListAdapterScroll.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                                           int viewType) {
//        this.context = parent.getContext();
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warehouse_details_view, parent, false);
//
//        WarehouseProductListAdapterScroll.ViewHolder viewHolder = new WarehouseProductListAdapterScroll.ViewHolder(view);
//        return viewHolder;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.warehouse_details_view, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            WarehouseProductListResponse.StockList productModel = warehouse_productlist_records.get(position);
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.item_code.setText((productModel.getSku() == null) ? "" : productModel.getSku());
            userViewHolder.product_name.setText((productModel.getProduct_name() == null) ? "" : productModel.getProduct_name());
            userViewHolder.good_qty.setText((productModel.getGood_stock() == null) ? "" : productModel.getGood_stock());
            userViewHolder.damaged_qty.setText((productModel.getDamaged_stock() == null) ? "" : productModel.getDamaged_stock());
            if(productModel.getProduct_image()!=null){
                Bitmap im= GrnDetailsActivity.getBitmapFromURL(productModel.getProduct_image().toString());
                if(im!=null){
                    userViewHolder.product_image.setImageBitmap(im);
                }

            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
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


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, item_code, good_qty,damaged_qty;
        ImageView product_image;
        public CardView cardView;

        public UserViewHolder(View view) {
            super(view);


            item_code = (TextView) view.findViewById(R.id.item_code);
            product_image = (ImageView) view.findViewById(R.id.product_image);
            product_name = (TextView) view.findViewById(R.id.product_name);
            good_qty = (TextView) view.findViewById(R.id.good_qty);
            damaged_qty = (TextView) view.findViewById(R.id.damaged_qty);
//            cardView = view.findViewById(R.id.cardView);



        }


    }


//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView product_name, item_code, good_qty,damaged_qty;
//        ImageView product_image;
//        public CardView cardView;
//
//        public ViewHolder(View view) {
//            super(view);
//
//
//            item_code = (TextView) view.findViewById(R.id.item_code);
//            product_image = (ImageView) view.findViewById(R.id.product_image);
//            product_name = (TextView) view.findViewById(R.id.product_name);
//            good_qty = (TextView) view.findViewById(R.id.good_qty);
//            damaged_qty = (TextView) view.findViewById(R.id.damaged_qty);
////            cardView = view.findViewById(R.id.cardView);
//
//
//
//        }
//    }
}

