package com.src.inthree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WarehouseProductListRequest {
    @SerializedName("user_id")
    @Expose
    String user_id;
    @SerializedName("wh_id")
    @Expose
    String warehouse_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = warehouse_id;
    }
}
