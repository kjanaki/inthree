package com.src.inthree.API;


import com.src.inthree.model.CheckDuplicateRequest;
import com.src.inthree.model.CheckDuplicateResponse;
import com.src.inthree.model.CheckListRequest;
import com.src.inthree.model.CheckListResponse;
import com.src.inthree.model.GrnDetailRequest;
import com.src.inthree.model.GrnDetailResponse;
import com.src.inthree.model.GrnListRequest;
import com.src.inthree.model.GrnListResponse;
import com.src.inthree.model.LoginRequest;
import com.src.inthree.model.LoginResponse;
import com.src.inthree.model.ProductReceivedResponse;
import com.src.inthree.model.StockListResponse;
import com.src.inthree.model.UserRequestModel;
import com.src.inthree.model.WarehouseProductListRequest;
import com.src.inthree.model.WarehouseProductListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

   // public static String BASE_URL;
 public String BoonBox_URL ="http://dev.in3access.in";
// public String BoonBox_URL ="http://testcloud.in3access.in";

    @POST("/bbid/bb_po_user.php/")
    Call <LoginResponse> get_authincation(@Body LoginRequest login) ;


    @POST("/bbid/bb_po_details.php")
    Call <GrnListResponse> get_po_grn_details(@Body GrnListRequest grnRequest) ;

    @POST("/bbid/bb_grn_details.php")
    Call<GrnDetailResponse> get_grn_detail_product_list(@Body GrnDetailRequest grnDetailRequest);

    @POST("/bbid/bb_product_duplication_check.php")
    Call <CheckDuplicateResponse> check_duplicate_ids(@Body CheckDuplicateRequest duplicateModel) ;

    @Headers("Content-Type: application/json")
    @POST("/bbid/boonbox_api_create_grn.php")
    Call <ProductReceivedResponse> create_grn(@Body GrnDetailResponse.GrnDetailsView grnDetailResponse) ;

    @POST("/bbid/checklist.php")
    Call <CheckListResponse> get_checklist_data(@Body CheckListRequest checkListRequest) ;

  @POST("/bbid/wh_stock.php")
  Call <StockListResponse> get_statistics_data(@Body UserRequestModel statisticsRequest) ;


  @POST("/bbid/wh_products.php")
  Call <WarehouseProductListResponse> get_warehouse_product_list(@Body WarehouseProductListRequest productListRequest) ;









}
