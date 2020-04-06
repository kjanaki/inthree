//package com.src.inthree.Adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.src.inthree.GrnDetailsActivity;
//import com.src.inthree.R;
//import com.src.inthree.model.GrnDetailResponse;
//import com.src.inthree.model.GrnListResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GRNProductListRecyleAdapert extends RecyclerView.Adapter<GRNProductListRecyleAdapert.ViewHolder> {
//
//    List<GrnDetailResponse.ProductModel> product_list_records;
//    Context context;
//    private onScannerClick mScannerClick;
//
//
//    public GRNProductListRecyleAdapert(List<GrnDetailResponse.ProductModel> product_list) {
//        product_list_records = product_list;
//    }
//
//    public GRNProductListRecyleAdapert() {
//        product_list_records = new ArrayList<>();
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent,
//                                         int viewType) {
//        this.context = parent.getContext();
//        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_product_list_view, parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_product_content_view, parent, false);
//
//        GRNProductListRecyleAdapert.ViewHolder viewHolder = new GRNProductListRecyleAdapert.ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//
//        GrnDetailResponse.ProductModel pomdel = product_list_records.get(position);
//
//        holder.product_name.setText((pomdel.getProduct_name() == null) ? "" : pomdel.getProduct_name());
//        holder.item_code.setText((pomdel.getProduct_sku() == null) ? "" : pomdel.getProduct_sku());
//        holder.received_qty.setText(String.valueOf(pomdel.getRecevied_qty()));
//        holder.req_qty.setText(String.valueOf(pomdel.getReq_qty()));
//        holder.product_model_value.setText((pomdel.getProduct_model_entered() == null) ? "" : pomdel.getProduct_model_entered());
//        if (pomdel.getImage_url() != null) {
//            Bitmap im = GrnDetailsActivity.getBitmapFromURL(pomdel.getImage_url().toString());
//            if (im != null) {
//                holder.product_image.setImageBitmap(im);
//            }
//
//        }
//        holder.qrcode_recevied_cqty.setVisibility(View.GONE);
////        //holder.barcodevalue.setText((pomdel.getBarcode_value() == null) ? "" : pomdel.getBarcode_value());
////         holder.received_qty.setEnabled(false);
////        //holder.received_qty.isFocusable = true;
////        holder.received_qty.setFocusable(false);
////        if(!pomdel.isDisplay_scanner()){
////               holder.qrcode_recevied_cqty.setVisibility(View.GONE);
////            holder.received_qty.setEnabled(true);
////            //holder.received_qty.isFocusable = true;
////            holder.received_qty.setFocusable(true);
////
////           }
//        if (pomdel.isDisplay_scanner()) {
//            holder.qrcode_recevied_cqty.setVisibility(View.VISIBLE);
//           // holder.received_qty.setEnabled(false);
//           // holder.received_qty.setFocusable(false);
//        }
//        holder.received_qty.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() != 0) {
//                    int entered_qty = Integer.parseInt(holder.received_qty.getText().toString());
//                    int required_qty = Integer.parseInt(holder.req_qty.getText().toString());
//
//                    mScannerClick.Check_receviec_qty(pomdel, entered_qty, required_qty);
//                }
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                if (s.length() != 0) {
//
//                }
//            }
//        });
////        holder.product_model_value.addTextChangedListener(new TextWatcher() {
////
////            @Override
////            public void afterTextChanged(Editable s) {
////                if (s.length() != 0) {
////                    String product_model = pomdel.getProduct_model().toString();
////                    String product_model_value = holder.product_model_value.getText().toString();
////                   // mScannerClick.validate_product_model(pomdel,product_model,product_model_value);
////                }
////
////            }
////
////
////
////            @Override
////            public void beforeTextChanged(CharSequence s, int start,
////                                          int count, int after) {
////
////            }
////
////            @Override
////            public void onTextChanged(CharSequence s, int start,
////                                      int before, int count) {
////                if (s.length() != 0) {
////
////                }
////            }
////        });
//
//        holder.qrcode_recevied_cqty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mScannerClick != null) {
//                        String product_model = pomdel.getProduct_model().toString();
//                        String product_model_value = holder.product_model_value.getText().toString();
//                        boolean display_bbid = pomdel.getCapture_bbid();
//                        boolean display_imei_no = pomdel.getCapture_imei();
//                        boolean display_serial_no = pomdel.getCapture_serial_no();
//                        mScannerClick.onClick(pomdel,pomdel.getProduct_id(), display_bbid, display_imei_no, display_serial_no);
//
//
//
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return this.product_list_records.size();
//    }
//
//    public void setData(List<GrnDetailResponse.ProductModel> data) {
//
//        this.product_list_records = data;
//        System.out.println("data"+data);
//        notifyDataSetChanged();
//    }
//
//    public void setOnScannerClick(onScannerClick scannerClick) {
//        mScannerClick = scannerClick;
//    }
//
//    public interface onScannerClick {
//        void onClick(GrnDetailResponse.ProductModel pomdel,String product_id, boolean display_bbid, boolean display_imei_no, boolean display_serial_no);
//
//        void Check_receviec_qty(GrnDetailResponse.ProductModel pmodel, int entered_qty, int required_qty);
//
//        void validate_product_model(GrnDetailResponse.ProductModel pomdel, String product_model, String product_model_value);
//
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView product_name, item_code, product_model_value, req_qty;
//        public ImageView qrcode_recevied_cqty, product_image;
//        EditText received_qty;
//        private View bidView;
//
//        public ViewHolder(View view) {
//            super(view);
//            product_name = (TextView) view.findViewById(R.id.product_name);
//            item_code = (TextView) view.findViewById(R.id.item_code);
//            product_model_value = (TextView) view.findViewById(R.id.product_model_value);
//            received_qty = (EditText) view.findViewById(R.id.entered_qty);
//            req_qty = (TextView) view.findViewById(R.id.req_qty);
//            qrcode_recevied_cqty = (ImageView) view.findViewById(R.id.qrcode_recevied_cqty);
//            product_image = (ImageView) view.findViewById(R.id.product_image);
//            bidView = view;
//
//        }
//    }
//
//
//}
