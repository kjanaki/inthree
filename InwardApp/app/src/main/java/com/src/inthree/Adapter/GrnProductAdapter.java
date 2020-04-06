package com.src.inthree.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.src.inthree.GrnDetailsActivity;
import com.src.inthree.GrnListActivity;
import com.src.inthree.R;
import com.src.inthree.model.GrnDetailResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import static androidx.constraintlayout.widget.Constraints.TAG;

public class GrnProductAdapter extends RecyclerView.Adapter<GrnProductAdapter.ViewHolder> {

    List<GrnDetailResponse.GrnDetailsView.ProductModel> product_list_records;
    Context context;
    private onScannerClick mScannerClick;


    public GrnProductAdapter(List<GrnDetailResponse.GrnDetailsView.ProductModel> product_list) {
        product_list_records = product_list;
    }

    public GrnProductAdapter() {
        product_list_records = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        this.context = parent.getContext();
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_product_list_view, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_product_content_view, parent, false);

        GrnProductAdapter.ViewHolder viewHolder = new GrnProductAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("sdfs"+position);
        GrnDetailResponse.GrnDetailsView.ProductModel pomdel = product_list_records.get(position);
        holder.product_name.setText((pomdel.getProduct_name() == null) ? "" : pomdel.getProduct_name());
        holder.item_code.setText((pomdel.getProduct_sku() == null) ? "" : pomdel.getProduct_sku());
        holder.product_model_value.setText((pomdel.getProduct_model_entered() == null) ? "" : pomdel.getProduct_model_entered());
        holder.item_code.setTag(position);
        //holder.getAdapterPosition()
        holder.received_qty.setText(String.valueOf(pomdel.getRecevied_qty()));
        holder.req_qty.setText(String.valueOf(pomdel.getReq_qty()));
        if(pomdel.isIs_product_model_match()){
            holder.product_model_value.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);


        }
        else{
            holder.product_model_value.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);


        }


        if(pomdel.getImage_url()!=null){
            Bitmap im= GrnDetailsActivity.getBitmapFromURL(pomdel.getImage_url().toString());
            if(im!=null){
                holder.product_image.setImageBitmap(im);
            }

        }
        holder.qrcode_recevied_cqty.setVisibility(View.GONE);
//        //holder.barcodevalue.setText((pomdel.getBarcode_value() == null) ? "" : pomdel.getBarcode_value());
//         holder.received_qty.setEnabled(false);
//        //holder.received_qty.isFocusable = true;
//        holder.received_qty.setFocusable(false);
//        if(!pomdel.isDisplay_scanner()){
//               holder.qrcode_recevied_cqty.setVisibility(View.GONE);
//            holder.received_qty.setEnabled(true);
//            //holder.received_qty.isFocusable = true;
//            holder.received_qty.setFocusable(true);
//
//           }

        if(pomdel.isDisplay_scanner())
        {
            holder.qrcode_recevied_cqty.setVisibility(View.VISIBLE);
            holder.received_qty.setEnabled(false);
            //holder.received_qty.isFocusable = true;
            holder.received_qty.setFocusable(false);
        }
        holder.received_qty.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){
                    int f= (int) holder.item_code.getTag();
                    if(f==holder.getAdapterPosition())
                   // GrnDetailResponse.GrnDetailsView.ProductModel pomdel_v = product_list_records.get(f);
                    {
                        int entered_qty = Integer.parseInt(holder.received_qty.getText().toString());
                        int required_qty = Integer.parseInt(holder.req_qty.getText().toString());
                        GrnDetailResponse.GrnDetailsView.ProductModel pomdel_v = product_list_records.get(f);

                        mScannerClick.Check_receviec_qty(pomdel_v, entered_qty, required_qty);

                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){

                }
            }
        });

        holder.product_model_value.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {

                    int f = (int) holder.item_code.getTag();
                    if (f == holder.getAdapterPosition()) {
                        //holder.product_model_value.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        GrnDetailResponse.GrnDetailsView.ProductModel pomdel_v = product_list_records.get(f);
                        String product_model = pomdel_v.getProduct_model().toString();
                        String product_model_value = s.toString();//holder.product_model_value.getText().toString();
                        if (product_model.equals(product_model_value)) {
                            holder.product_model_value.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0);
                            pomdel_v.setProduct_model_entered(product_model_value);
                            pomdel_v.setIs_product_model_match(true);

                        } else {
                            holder.product_model_value.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_notdone_black_24dp, 0);
                            pomdel_v.setIs_product_model_match(false);

                        }
                        mScannerClick.update_data(f, pomdel_v);
                        //mScannerClick.validate_product_model(pomdel_v,product_model,product_model_value);
                    }
                }

            }



            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {

                }
            }
        });



        holder.qrcode_recevied_cqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mScannerClick!=null) {
                    int f = (int) holder.item_code.getTag();
                    if (f == holder.getAdapterPosition()) {
                        GrnDetailResponse.GrnDetailsView.ProductModel pomdel_v = product_list_records.get(f);
                        boolean display_bbid = pomdel_v.getCapture_bbid();
                        boolean display_imei_no = pomdel_v.getCapture_imei();
                        boolean display_serial_no = pomdel_v.getCapture_serial_no();
                        if (pomdel_v.isIs_product_model_match()) {
                            if(pomdel_v.getRecevied_qty()< pomdel_v.getReq_qty()) {
                                mScannerClick.onClick(pomdel_v.getProduct_id(), display_bbid, display_imei_no, display_serial_no);
                            }else{
                                mScannerClick.Check_receviec_qty(pomdel_v, pomdel_v.getRecevied_qty(), pomdel_v.getReq_qty());
                            }
                        }
                        else {
                            holder.product_model_value.setError("Product model does't match");
                        }
                    }
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.product_list_records.size();
    }

    public void setData(List<GrnDetailResponse.GrnDetailsView.ProductModel> data) {
        this.product_list_records=data;
        notifyDataSetChanged();
    }

    public void UpdateData(Integer position,GrnDetailResponse.GrnDetailsView.ProductModel data) {
        this.product_list_records.add(position,data);
        this.product_list_records.get(position).setIs_product_model_match(true);
        this.product_list_records.get(position).setRecevied_qty(data.getRecevied_qty());
        this.product_list_records.get(position).setRecevied_qty(data.getRecevied_qty());

        notifyDataSetChanged();
        //notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView product_name, item_code, product_model_value,req_qty;
        EditText received_qty;
        public ImageView qrcode_recevied_cqty,product_image;

        public ViewHolder(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.product_name);
            item_code = (TextView) view.findViewById(R.id.item_code);
            product_model_value = (TextView) view.findViewById(R.id.product_model_value);
            received_qty = (EditText) view.findViewById(R.id.entered_qty);
            req_qty = (TextView) view.findViewById(R.id.req_qty);
            qrcode_recevied_cqty = (ImageView) view.findViewById(R.id.qrcode_recevied_cqty);
            product_image =(ImageView)view.findViewById(R.id.product_image);
            ///barcodevalue = (TextView)view.findViewById(R.id.barcodevalue);

        }
    }

    public void setOnScannerClick(onScannerClick scannerClick){
        mScannerClick = scannerClick;
    }



    public interface onScannerClick{
        void onClick(String product_id,boolean display_bbid,boolean display_imei_no,boolean display_serial_no);
        void Check_receviec_qty(GrnDetailResponse.GrnDetailsView.ProductModel pmodel,int entered_qty, int required_qty);
        void  update_data(int pos,GrnDetailResponse.GrnDetailsView.ProductModel pmodel);
       // void validate_product_model(GrnDetailResponse.GrnDetailsView.ProductModel pomdel, String product_model, String product_model_value);
    }


}
