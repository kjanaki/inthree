package com.src.inthree.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class GrnDetailResponse {
    @SerializedName("message")
    @Expose
    String api_message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("grn_details")
    @Expose

    private GrnDetailsView grn_details;

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GrnDetailsView getGrn_details() {
        return grn_details;
    }

    public void setGrn_details(GrnDetailsView grn_details) {
        this.grn_details = grn_details;
    }

    public class GrnDetailsView {

        @SerializedName("grn_no")
        @Expose
        String grn_id;
        @SerializedName("po_no")
        @Expose
        String purchase_order_id;
        @SerializedName("po_date")
        @Expose
        String po_created_at;
        @SerializedName("grn_date")
        @Expose
        String grn_date;
        @SerializedName("vendor_invoice_number")
        @Expose
        String invoice_number;
        @SerializedName("vendor_invoice_date")
        @Expose
        String invoice_date;
        @SerializedName("user_id")
        @Expose
        String login_user_id;
        @SerializedName("products")
        @Expose
        private List<ProductModel> productmodel;

        public List<ProductModel> getProductmodel() {
            return productmodel;
        }

        public void setProductmodel(List<ProductModel> productmodel) {
            this.productmodel = productmodel;
        }

        public String getPo_created_at() {
            return po_created_at;
        }

        public void setPo_created_at(String po_created_at) {
            this.po_created_at =po_created_at;
        }

        public String getGrn_date() {
            return grn_date;
        }

        public void setGrn_date(String grn_date) {
            this.grn_date = grn_date;
        }

        public String getInvoice_number() {
            return invoice_number;
        }

        public void setInvoice_number(String invoice_number) {
            this.invoice_number = invoice_number;
        }

        public String getInvoice_date() {
            return invoice_date;
        }

        public void setInvoice_date(String invoice_date) {
            this.invoice_date = invoice_date;
        }

        public String getGrn_id() {
            return grn_id;
        }

        public void setGrn_id(String grn_id) {
            this.grn_id = grn_id;
        }

        public String getPurchase_order_id() {
            return purchase_order_id;
        }

        public void setPurchase_order_id(String purchase_order_id) {
            this.purchase_order_id = purchase_order_id;
        }

        public String getLogin_user_id() {
            return login_user_id;
        }

        public void setLogin_user_id(String login_user_id) {
            this.login_user_id = login_user_id;
        }

        public class ProductModel {
            @SerializedName("product_id")
            @Expose
            String product_id;

            @SerializedName("product_name")
            @Expose
            String product_name;

            @SerializedName("product_sku")
            @Expose
            String product_sku;

            @SerializedName("product_model")
            @Expose
            String product_model;
            @SerializedName("is_product_model_match")
            @Expose
            boolean is_product_model_match = false;
            @SerializedName("product_model_entered")
            @Expose
            String product_model_entered = "";
            @SerializedName("qty")
            @Expose
            String qty;
            @SerializedName("recevied_qty")
            @Expose
            int recevied_qty;
            @SerializedName("capture_bbid")
            @Expose
            boolean capture_bbid;
            @SerializedName("capture_serial_no")
            @Expose
            boolean capture_serial_no;
            @SerializedName("capture_imei")
            @Expose
            boolean capture_imei;
            @SerializedName("capture")
            @Expose
            boolean display_scanner;
            @SerializedName("image_details")
            @Expose
            String image_url;
            @SerializedName("req_qty")
            @Expose
            int req_qty;
            @SerializedName("qr_scanner_list")
            @Expose
            private List<Scanner_List> Scanner_list;

            public String getProduct_model_entered() {
                return product_model_entered;
            }

            public void setProduct_model_entered(String product_model_entered) {
                this.product_model_entered = product_model_entered;
            }

            public boolean isIs_product_model_match() {
                return is_product_model_match;
            }

            public void setIs_product_model_match(boolean is_product_model_match) {
                this.is_product_model_match = is_product_model_match;
            }

            public List<Scanner_List> getScanner_list() {
                return Scanner_list;
            }

            public void setScanner_list(List<Scanner_List> scanner_list) {
                Scanner_list = scanner_list;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getProduct_sku() {
                return product_sku;
            }

            public void setProduct_sku(String product_sku) {
                this.product_sku = product_sku;
            }

            public String getProduct_model() {
                return product_model;
            }

            public void setProduct_model(String product_model) {
                this.product_model = product_model;
            }

            public String getQty() {
                return qty;
            }

            public void setQty(String qty) {
                this.qty = qty;
            }

            public int getRecevied_qty() {
                return recevied_qty;
            }

            public void setRecevied_qty(int recevied_qty) {
                this.recevied_qty = recevied_qty;
            }

            public boolean getCapture_bbid() {
                return capture_bbid;
            }

            public void setCapture_bbid(boolean capture_bbid) {
                this.capture_bbid = capture_bbid;
            }

            public boolean getCapture_serial_no() {
                return capture_serial_no;
            }

            public void setCapture_serial_no(boolean capture_serial_no) {
                this.capture_serial_no = capture_serial_no;
            }

            public boolean getCapture_imei() {
                return capture_imei;
            }

            public void setCapture_imei(boolean capture_imei) {
                this.capture_imei = capture_imei;
            }

            public boolean isDisplay_scanner() {
                return display_scanner;
            }

            public void setDisplay_scanner(boolean display_scanner) {
                this.display_scanner = display_scanner;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public int getReq_qty() {
                return req_qty;
            }

            public void setReq_qty(int req_qty) {
                this.req_qty = req_qty;
            }


        }


    }


}

