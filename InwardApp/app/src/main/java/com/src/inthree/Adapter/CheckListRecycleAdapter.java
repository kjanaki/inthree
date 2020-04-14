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
import com.src.inthree.model.CheckListResponse;
import com.src.inthree.model.GrnListResponse;

import java.util.ArrayList;
import java.util.List;

public class CheckListRecycleAdapter extends RecyclerView.Adapter<CheckListRecycleAdapter.ViewHolder> implements Filterable {
    Context context;
    List<CheckListResponse.ChecklistModel> check_list_records;

    @Override
    public CheckListRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        this.context = parent.getContext();
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_list_content_view, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_content_view, parent, false);

        CheckListRecycleAdapter.ViewHolder viewHolder = new CheckListRecycleAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CheckListRecycleAdapter.ViewHolder holder, int position) {
        CheckListResponse.ChecklistModel ChecklistModel = check_list_records.get(position);
        holder.product_name.setText((ChecklistModel.getProduct() == null) ? "" : ChecklistModel.getProduct());
        holder.product_count.setText((ChecklistModel.getProduct_count()== null) ? "" : ChecklistModel.getProduct_count());
        if(ChecklistModel.getProduct_image()!=null){
            Bitmap im= GrnDetailsActivity.getBitmapFromURL(ChecklistModel.getProduct_image().toString());
            if(im!=null){
                holder.product_image.setImageBitmap(im);
            }

        }
    }

    @Override
    public int getItemCount() {

        return check_list_records.size();
    }
    public CheckListRecycleAdapter() {
        check_list_records = new ArrayList<>();
    }

    public void setData(List<CheckListResponse.ChecklistModel> data) {
        this.check_list_records = data;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  product_count,product_name;
        public CardView cardView;
        ImageView product_image;

        public ViewHolder(View view) {
            super(view);


            product_count=(TextView)view.findViewById(R.id.product_count);
            product_name=(TextView)view.findViewById(R.id.product_name);
            product_image=(ImageView) view.findViewById(R.id.product_img);
//            cardView = view.findViewById(R.id.cardView);
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // Launching new Activity on selecting single List Item
//                    Intent i = new Intent(view.getContext(), GrnDetailsActivity.class);
//                    String grn_numbedr = grn_number.getText().toString();
//                    i.putExtra("grn_number", grn_numbedr);
//                    i.putExtra("supplier_name", supplier_name.getText().toString());
//                    i.putExtra("grn_date", grn_date.getText().toString());
//                    context.startActivity(i);
//                }
//
//            });


        }
    }
}

