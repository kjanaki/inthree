package com.src.inthree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.GrnDetailsActivity;
import com.src.inthree.R;
import com.src.inthree.model.GrnListResponse;

import java.util.ArrayList;
import java.util.List;

public class GRNListRecycleListAdapter extends RecyclerView.Adapter<GRNListRecycleListAdapter.ViewHolder> implements Filterable {
    Context context;
    List<GrnListResponse.GrnModel> grn_list_records;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        this.context = parent.getContext();
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_list_content_view, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sas, parent, false);

        GRNListRecycleListAdapter.ViewHolder viewHolder = new GRNListRecycleListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GrnListResponse.GrnModel grnModel = grn_list_records.get(position);
        holder.grn_number.setText((grnModel.getGrn_no() == null) ? "" : grnModel.getGrn_no());
        holder.supplier_name.setText((grnModel.getSupplier_name() == null) ? "" : grnModel.getSupplier_name());
        holder.grn_date.setText((grnModel.getGrn_date() == null) ? "" : ""+grnModel.getGrn_date());
        holder.product_count.setText((grnModel.getProduct_count()== null) ? "" : grnModel.getProduct_count());
        holder.po_order_date.setText((grnModel.getPo_created_at()== null) ? "" : grnModel.getPo_created_at());
        holder.po_order_number.setText((grnModel.getPo_order_number()== null) ? "" : grnModel.getPo_order_number());
    }

    @Override
    public int getItemCount() {

        return grn_list_records.size();
    }
    public GRNListRecycleListAdapter() {
        grn_list_records = new ArrayList<>();
    }

    public void setData(List<GrnListResponse.GrnModel> data) {
        this.grn_list_records = data;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView grn_number, supplier_name,  product_count,grn_date,po_order_date,po_order_number;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            grn_number = (TextView) view.findViewById(R.id.grn_no);
            supplier_name = (TextView) view.findViewById(R.id.supplier_name);
            grn_date = (TextView) view.findViewById(R.id.grn_date);
            product_count=(TextView)view.findViewById(R.id.product_count);
            po_order_number=(TextView)view.findViewById(R.id.po_order_number);
            po_order_date=(TextView)view.findViewById(R.id.po_order_date);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(view.getContext(), GrnDetailsActivity.class);
                    String grn_numbedr = grn_number.getText().toString();
                    i.putExtra("grn_number", grn_numbedr);
                    i.putExtra("supplier_name", supplier_name.getText().toString());
                    i.putExtra("grn_date", grn_date.getText().toString());
                    context.startActivity(i);
                }

            });


        }
    }
}
