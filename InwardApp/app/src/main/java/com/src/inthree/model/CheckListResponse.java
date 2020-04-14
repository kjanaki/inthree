package com.src.inthree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckListResponse {
    @SerializedName("message")
    @Expose
    String api_message;
    @SerializedName("status")
    @Expose
    private String status;


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

    public List<ChecklistModel> getList_details() {
        return list_details;
    }

    public void setList_details(List<ChecklistModel> list_details) {
        this.list_details = list_details;
    }

    @SerializedName("list_details")
    @Expose
    private List<ChecklistModel> list_details;

    public class ChecklistModel {
        @SerializedName("sku")
        @Expose
        String product_sku;
        @SerializedName("product")
        @Expose
        String product;
        @SerializedName("cnt")
        @Expose
        String product_count;
        @SerializedName("image_details")
        @Expose
        String product_image;

        public String getProduct_sku() {
            return product_sku;
        }

        public void setProduct_sku(String product_sku) {
            this.product_sku = product_sku;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getProduct_count() {
            return product_count;
        }

        public void setProduct_count(String product_count) {
            this.product_count = product_count;
        }

        public String getProduct_image() {
            return product_image;
        }

        public void setProduct_image(String product_image) {
            this.product_image = product_image;
        }
    }
}
