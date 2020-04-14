package com.src.inthree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WarehouseProductListResponse {


    @SerializedName("message")
    @Expose
    String api_message;

    @SerializedName("list_details")
    @Expose
    List<StockList> stocklist;

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

    public List<StockList> getStocklist() {
        return stocklist;
    }

    public void setStocklist(List<StockList> stocklist) {
        this.stocklist = stocklist;
    }

    public class StockList {

        @SerializedName("sku")
        @Expose
        String sku;

        @SerializedName("name")
        @Expose
        String product_name;

        @SerializedName("image_details")
        @Expose
        String product_image;

        @SerializedName("good_stock")
        @Expose
        String good_stock;
        @SerializedName("damaged_stock")
        @Expose
        String damaged_stock;


        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_image() {
            return product_image;
        }

        public void setProduct_image(String product_image) {
            this.product_image = product_image;
        }

        public String getGood_stock() {
            return good_stock;
        }

        public void setGood_stock(String good_stock) {
            this.good_stock = good_stock;
        }

        public String getDamaged_stock() {
            return damaged_stock;
        }

        public void setDamaged_stock(String damaged_stock) {
            this.damaged_stock = damaged_stock;
        }
    }
}
