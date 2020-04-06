package com.src.inthree.API;


import com.src.inthree.model.CheckDuplicateRequest;
import com.src.inthree.model.CheckDuplicateResponse;
import com.src.inthree.model.GrnDetailRequest;
import com.src.inthree.model.GrnDetailResponse;
import com.src.inthree.model.GrnListRequest;
import com.src.inthree.model.GrnListResponse;
import com.src.inthree.model.LoginRequest;
import com.src.inthree.model.LoginResponse;
import com.src.inthree.model.ProductReceivedResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

   // public static String BASE_URL;
   public String BoonBox_URL ="http://dev.in3access.in";

    @POST("/bb_po_user.php/")
    Call <LoginResponse> get_authincation(@Body LoginRequest login) ;


    @POST("/bb_po_details.php")
    Call <GrnListResponse> get_po_grn_details(@Body GrnListRequest grnRequest) ;

    @POST("/bb_grn_details.php")
    Call<GrnDetailResponse> get_grn_detail_product_list(@Body GrnDetailRequest grnDetailRequest);

    @POST("/bb_product_duplication_check.php")
    Call <CheckDuplicateResponse> check_duplicate_ids(@Body CheckDuplicateRequest duplicateModel) ;

    @POST("/bbid/boonbox_api_create_grn.php")
    Call <ProductReceivedResponse> create_grn(@Body GrnDetailResponse.GrnDetailsView grnDetailResponse) ;









}
