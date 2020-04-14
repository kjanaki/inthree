package com.src.inthree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockListResponse {

    @SerializedName("message")
    @Expose
    String api_message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("list_details")
    @Expose
    private List<WarehouseList> list_details;

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

    public List<WarehouseList> getList_details() {
        return list_details;
    }

    public void setList_details(List<WarehouseList> list_details) {
        this.list_details = list_details;
    }



    public class WarehouseList{
        @SerializedName("warehouse_id")
        @Expose
        String warehouse_id;

        @SerializedName("warehouse_name")
        @Expose
        String warehouse_name;

        public String getWarehouse_id() {
            return warehouse_id;
        }

        public void setWarehouse_id(String warehouse_id) {
            this.warehouse_id = warehouse_id;
        }

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }

        public String getProducts_count() {
            return products_count;
        }

        public void setProducts_count(String products_count) {
            this.products_count = products_count;
        }

        @SerializedName("products_count")
        @Expose
        String products_count;

    }
}
