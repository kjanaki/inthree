package com.src.inthree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GrnListResponse {
    @SerializedName("message")
    @Expose
    String api_message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<GrnModel> getGrn_model() {
        return grn_model;
    }

    public void setGrn_model(List<GrnModel> grn_model) {
        this.grn_model = grn_model;
    }

    @SerializedName("po_details")
    @Expose
    private List<GrnModel> grn_model;

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

    public class GrnModel {
        @SerializedName("po_no")
        @Expose
        String po_order_number;
        @SerializedName("po_created_at")
        @Expose
        String po_created_at;

        public String getGrn_no() {
            return grn_no;
        }

        public void setGrn_no(String grn_no) {
            this.grn_no = grn_no;
        }

        @SerializedName("grn")
        @Expose
        String grn_no;
        @SerializedName("grn_date")
        @Expose
        String grn_date;

        @SerializedName("vendor_invoice")
        @Expose
        String invoice_number;
        @SerializedName("vendor_invoice_date")
        @Expose
        String invoice_date;
        @SerializedName("supplier_name")
        @Expose
        String supplier_name;
        @SerializedName("products")
        @Expose
        String product_count;

        public String getPo_order_number() {
            return po_order_number;
        }

        public void setPo_order_number(String po_order_number) {
            this.po_order_number = po_order_number;
        }

        public String getPo_created_at() {
            return po_created_at;
        }

        public void setPo_created_at(String po_created_at) {
            this.po_created_at = po_created_at;
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

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public String getProduct_count() {
            return product_count;
        }

        public void setProduct_count(String product_count) {
            this.product_count = product_count;
        }


    }


}
