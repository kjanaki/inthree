package com.src.inthree.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GrnDetailRequest {
    public String getGrn_no() {
        return grn_no;
    }

    public void setGrn_no(String grn_no) {
        this.grn_no = grn_no;
    }

    @SerializedName("grn_id")
    @Expose
    String grn_no;



}
