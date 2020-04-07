package com.src.inthree;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.src.inthree.API.Api;
import com.src.inthree.Adapter.GrnProductAdapter;
import com.src.inthree.model.CheckDuplicateRequest;
import com.src.inthree.model.CheckDuplicateResponse;
import com.src.inthree.model.GrnDetailRequest;
import com.src.inthree.model.GrnDetailResponse;
import com.src.inthree.model.ProductReceivedResponse;
import com.src.inthree.model.Scanner_List;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SyncFailedException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.internal.util.BackpressureHelper;
import me.ydcool.lib.qrmodule.activity.QrScannerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GrnDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_QTY_BY_Serail = 2;
    private static final int REQUEST_QTY_BY_IMEI = 3;
    private static final int REQUEST_QTY_BY_BBID = 4;

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.grn_date)
    TextView grn_date;
    @BindView(R.id.grn_number)
    TextView grn_no;
    @BindView(R.id.supplier_name)
    TextView supplier_name;
    @BindView(R.id.order_date)
    TextView order_date;
    @BindView(R.id.order_number)
    TextView order_number;
    @BindView(R.id.invoice_number)
    TextView invoice_number;
    @BindView(R.id.invoice_date)
    TextView invoice_date;
    @BindView(R.id.back_btn)
            ImageView back_btn;

    RecyclerView grn_product_list_view;
    GrnProductAdapter recyclerViewAdapter;
    String grn_number,supplier_name_value;
    boolean add_min_qty = false;
    EditText serial_txt_no, bbid_no, imei_txt_no;
    Dialog dialog;
    View popUpView;
    Integer Selected_product_id;
    boolean is_duplicate = false;
    boolean is_bbid_duplicate, is_imei_no_duplicate, is_serial_no_duplicate = false;
    List<Scanner_List> list_ids;
    List<GrnDetailResponse.GrnDetailsView.ProductModel> Product_list;
    List<GrnDetailResponse.GrnDetailsView.ProductModel> Product_list_update;
    GrnDetailResponse.GrnDetailsView grnResponse;


    Map<Integer, List<String>> qr_code_list = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<Scanner_List>> qr_scanner_list = new HashMap<Integer, List<Scanner_List>>();

    public static Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(src);
            Bitmap bmp = BitmapFactory.decodeStream((InputStream) url.getContent());
            return bmp;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.resss);
        setContentView(R.layout.s);
        //setContentView(R.layout.grn_product_list);
        ButterKnife.bind(this);
        init_view();
        hideKeyboard();
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

    }

    private void init_view() {
        grn_product_list_view = findViewById(R.id.grn_list);
        recyclerViewAdapter = new GrnProductAdapter();
        grn_product_list_view.setLayoutManager(new LinearLayoutManager(this));
        grn_product_list_view.setAdapter(recyclerViewAdapter);
        Intent i = getIntent();
        grn_number = i.getSerializableExtra("grn_number").toString();
        supplier_name_value = i.getSerializableExtra("supplier_name").toString();

        recyclerViewAdapter.setOnScannerClick(new GrnProductAdapter.onScannerClick() {
            @Override
            public void onClick(String product_id, boolean display_bbid, boolean display_imei_no, boolean display_serial_no,int required_qty,int total_qty) {

                // Boolean is_valid = true;
                //Boolean is_valid = validateGSTNumber(GSTN_number.getText().toString());
                // if (is_valid) {
                display_scanner_view(product_id, display_bbid, display_imei_no, display_serial_no,required_qty,total_qty);
//                } else {
//                    Toast.makeText(GrnDetailsActivity.this, "Enter Valid GSTIN No", Toast.LENGTH_LONG).show();
//                    ViewDialog alert = new ViewDialog();
//                    String errormsg = "Enter Valid GSTIN No";
//                    alert.displayError(errormsg);
//
//                }
            }

            @Override
            public void Check_receviec_qty(GrnDetailResponse.GrnDetailsView.ProductModel pmodel, int entered_qty, int required_qty) {
                if (entered_qty > required_qty) {
                    Toast.makeText(getApplicationContext(), "You are not allowed to entered more than required qty", Toast.LENGTH_SHORT).show();
                    String error = "You are not allowed to entered more than required qty";
                    ViewDialog alert = new ViewDialog();
                    alert.displayError(error);
                } else {
                    pmodel.setRecevied_qty(entered_qty);
                    // Product_list.set(pos,pmodel);
                }
                if (entered_qty > 0) {
                    add_min_qty = true;
                }
            }

            @Override
            public void update_data(int pos, GrnDetailResponse.GrnDetailsView.ProductModel pmodel) {
                Product_list.set(pos, pmodel);
            }


        });
        load_product_list();
    }

    @OnClick({R.id.back_btn})
    void login(View v) {
        this.finish();

    }
    private void display_scanner_view(String product_id, boolean display_bbid, boolean display_imei_no, boolean display_serial_no,int received_qty,int total_qty) {
        dialog = new Dialog(GrnDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popUpView = getLayoutInflater().inflate(R.layout.scanlist_view,
                null);
        dialog.setContentView(popUpView);


        dialog.getWindow().setLayout(RecyclerView.LayoutParams.FILL_PARENT, 500);
        dialog.show();

        Selected_product_id = Integer.parseInt(product_id);
        ImageView scan_serial = (ImageView) popUpView.findViewById(R.id.serial_number_scan);
        ImageView scan_bbid = (ImageView) popUpView.findViewById(R.id.bbid_number_scan);
        ImageView scan_imei = (ImageView) popUpView.findViewById(R.id.imei_scan);

        serial_txt_no = (EditText) popUpView.findViewById(R.id.serial_number_scan_value);
        bbid_no = (EditText) popUpView.findViewById(R.id.bbid_number_scan_value);
        imei_txt_no = (EditText) popUpView.findViewById(R.id.imei_scan_value);
        TextView add_qty_txt = (TextView) popUpView.findViewById(R.id.added_qty);
        TextView total_qty_txt = (TextView) popUpView.findViewById(R.id.total_qty);
        add_qty_txt.setText(String.valueOf(received_qty));
        total_qty_txt.setText(String.valueOf(total_qty));


        if (!display_bbid) {
            popUpView.findViewById(R.id.scan_bbid_layout).setVisibility(View.GONE);
        }
        if (!display_imei_no) {
            popUpView.findViewById(R.id.scan_imei_layout).setVisibility(View.GONE);
        }
        if (!display_serial_no) {
            popUpView.findViewById(R.id.scan_serial_layout).setVisibility(View.GONE);
        }
        Button ok_btn = (Button) popUpView.findViewById(R.id.update);
        Button ok_cancel = (Button) popUpView.findViewById(R.id.cancel);
        list_ids = new ArrayList<>();


        scan_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerAction(product_id, REQUEST_QTY_BY_Serail, list_ids);
            }
        });
        scan_imei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerAction(product_id, REQUEST_QTY_BY_IMEI, list_ids);
            }
        });
        scan_bbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerAction(product_id, REQUEST_QTY_BY_BBID, list_ids);
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list_ids.add(new Scanner_List(bbid_no.getText().toString(),imei_no.getText().toString(),serial_no.getText().toString()));
                String bbid = (display_bbid) ? bbid_no.getText().toString() : "";
                String imei_no = (display_imei_no) ? imei_txt_no.getText().toString() : "";
                String serial_no = (display_serial_no) ? serial_txt_no.getText().toString() : "";
                boolean process_req = true;
                if (display_bbid) {
                    if (bbid.length() == 0 || bbid.equals("Scanned Nothing!")) {
                        bbid_no.setError("Please fill details");
                        process_req = false;
                    }
                }
                if (display_serial_no) {
                    if (serial_no.length() == 0 || serial_no.equals("Scanned Nothing!")) {
                        serial_txt_no.setError("Please fill details");
                        process_req = false;
                    }
                }
                if (display_imei_no) {
                    if (imei_no.length() == 0 || imei_no.equals("Scanned Nothing!")) {
                        imei_txt_no.setError("Please fill details");
                        process_req = false;
                    }
                }
                if (process_req) {
                    check_duplicate_serial_no(bbid, imei_no, serial_no, display_bbid, display_serial_no, display_imei_no, bbid_no, imei_txt_no, serial_txt_no, dialog);
                }

            }
        });

        ok_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_ids.clear();
                dialog.dismiss();
            }
        });
    }


    private synchronized void check_duplicate_serial_no(String bbid, String imei_no, String serial_no, boolean display_bbid, boolean display_serial_no, boolean display_imei_no, EditText edit_bbid, EditText edit_imei, EditText edit_serial, Dialog dialog) {
        String message = "";
        // Call the API and load the details view of PO order
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        CheckDuplicateRequest request = new CheckDuplicateRequest();
        request.setBb_id(bbid);
        request.setImei_no(imei_no);
        request.setSerial_no(serial_no);

        Call<CheckDuplicateResponse> call = api.check_duplicate_ids(request);

        call.enqueue(new Callback<CheckDuplicateResponse>() {
            @Override
            public void onResponse(Call<CheckDuplicateResponse> call, Response<CheckDuplicateResponse> response) {
                String response_status = response.body().getStatus();
                if (response_status.equals("success")) {
                    is_bbid_duplicate = response.body().getDuplicateList().isIs_bb_id_duplicate();
                    is_imei_no_duplicate = response.body().getDuplicateList().isIs_imei_duplicate();
                    is_serial_no_duplicate = response.body().getDuplicateList().isIs_serial_no_duplicate();
                    if (response.body().getDuplicateList().isIs_bb_id_duplicate() ||
                            response.body().getDuplicateList().isIs_imei_duplicate() ||
                            response.body().getDuplicateList().isIs_serial_no_duplicate()) {

                        if (is_bbid_duplicate && display_bbid) {
                            is_duplicate = true;
                            bbid_no.setError("Already exist");
                        }
                        if (is_imei_no_duplicate && display_imei_no) {
                            is_duplicate = true;
                            imei_txt_no.setError("Already exist");
                        }
                        if (is_serial_no_duplicate && display_serial_no) {
                            is_duplicate = true;
                            serial_txt_no.setError("Already exist");
                        }
                        if (!is_duplicate) {
                            is_duplicate = false;
                            boolean show_hide = update_product_qty(dialog,bbid, imei_no, serial_no, edit_bbid, edit_imei, edit_serial, dialog);
//                            if (!show_hide)
//                                dialog.dismiss();
                            hideKeyboard();
                        }

                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    } else {
                        is_duplicate = false;
                        boolean show_hide = update_product_qty(dialog,bbid, imei_no, serial_no, edit_bbid, edit_imei, edit_serial, dialog);
//                        if (!show_hide)
//                            dialog.dismiss();
                        hideKeyboard();
                    }

                }

            }

            @Override
            public void onFailure(Call<CheckDuplicateResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
        //return new boolean[]{is_duplicate, is_bbid_duplicate, is_imei_no_duplicate, is_serial_no_duplicate};


    }

//    private boolean update_product_qty(String bbid, String imei_no, String serial_no, EditText edit_bbid, EditText edit_imei, EditText edit_serial, Dialog dialog) {
//        Scanner_List p = new Scanner_List(bbid, imei_no, serial_no);
//        boolean show_dialog = false;
//        if (qr_scanner_list.containsKey(Selected_product_id)) {
//            List<Scanner_List> already_added_list = qr_scanner_list.get(Selected_product_id);
//            if (!already_added_list.contains(p)) {
//                already_added_list.add(p);
//                show_dialog = false;
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Scanned Already!", Toast.LENGTH_LONG).show();
//                edit_bbid.setError("Already scanned");
//                edit_imei.setError("Already scanned");
//                edit_serial.setError("Already scanned");
//                show_dialog = true;
//            }
//        } else {
//            List<Scanner_List> new_added_list = new ArrayList<>();
//            new_added_list.add(p);
//            qr_scanner_list.put(Selected_product_id, new_added_list);
//            show_dialog = false;
//        }
//
//        for (int i = 0; i < Product_list.size(); i++) {
//            if (Product_list.get(i).getProduct_id().equals(Selected_product_id.toString())) {
//                if (Product_list.get(i).getRecevied_qty() <= qr_scanner_list.get(Selected_product_id).size()) {
//                    int total_qty = (qr_scanner_list.get(Selected_product_id).size());
//
//                    Product_list.get(i).setRecevied_qty(total_qty);
//                    //Product_list.get(i).setBarcode_value("");
//                    Product_list.get(i).setScanner_list(qr_scanner_list.get(Selected_product_id));
//                    break;
//                } else {
//                    Toast.makeText(getApplicationContext(), "You cannot Scan more than Ordered qty!", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }
//        recyclerViewAdapter.setData(Product_list);
//
//        return show_dialog;
//    }

    private boolean update_product_qty(Dialog current_dialog,String bbid, String imei_no, String serial_no, EditText edit_bbid, EditText edit_imei, EditText edit_serial, Dialog dialog) {
        Scanner_List p = new Scanner_List(bbid, imei_no, serial_no);
        boolean show_dialog = false;
        if (qr_scanner_list.containsKey(Selected_product_id)) {
            List<Scanner_List> already_added_list = qr_scanner_list.get(Selected_product_id);
            if (!already_added_list.contains(p)) {
                already_added_list.add(p);
                show_dialog = false;

            } else {
                Toast.makeText(getApplicationContext(), "Scanned Already!", Toast.LENGTH_LONG).show();
                edit_bbid.setError("Already scanned");
                edit_imei.setError("Already scanned");
                edit_serial.setError("Already scanned");
                show_dialog = true;
            }
        } else {
            List<Scanner_List> new_added_list = new ArrayList<>();
            new_added_list.add(p);
            qr_scanner_list.put(Selected_product_id, new_added_list);
            show_dialog = false;
        }
        if(!show_dialog) {
            String pos = null;

            for (int i = 0; i < Product_list.size(); i++) {
                if (Product_list.get(i).getProduct_id().equals(Selected_product_id.toString())) {
                    pos = (String.valueOf(i));
                    if (Product_list.get(i).getRecevied_qty() <= qr_scanner_list.get(Selected_product_id).size()) {
                        int total_qty = (qr_scanner_list.get(Selected_product_id).size());

                        Product_list.get(i).setRecevied_qty(total_qty);
                        Product_list.get(i).setIs_product_model_match(true);

                        // Product_list.get(i).setBarcode_value("");
                        Product_list.get(i).setScanner_list(qr_scanner_list.get(Selected_product_id));
                        break;
                    } else {
                        Toast.makeText(getApplicationContext(), "You cannot Scan more than Ordered qty!", Toast.LENGTH_LONG).show();
                    }

                }
            }
            if (pos != null) {
                // recyclerViewAdapter.UpdateData(Integer.parseInt(pos),Product_list.get(Integer.parseInt(pos)));
                // recyclerViewAdapter.notifyItemChanged(Integer.parseInt(pos));
                current_dialog.dismiss();
                recyclerViewAdapter.update_position(pos);
                recyclerViewAdapter.setData(Product_list);
                hideKeyboard();
            }
        }


        // recyclerViewAdapter.setData(Product_list);

        return show_dialog;
    }


    void ScannerAction(String product_id, int request_code, List<Scanner_List> list_ids) {
        Dexter.withActivity(GrnDetailsActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(getApplicationContext(), QrScannerActivity.class);
                    intent.putExtra("product_id", product_id);
                    Selected_product_id = Integer.parseInt(product_id);
                    setResult(request_code, intent);
                    startActivityForResult(intent, request_code);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

            }


        }).check();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QTY_BY_BBID || requestCode == REQUEST_QTY_BY_IMEI || requestCode == REQUEST_QTY_BY_Serail) {
            String scaned_code = resultCode == RESULT_OK
                    ? data.getExtras().getString(QrScannerActivity.QR_RESULT_STR)
                    : "Scanned Nothing!";
            String scanResult = scaned_code;
            if (requestCode == REQUEST_QTY_BY_BBID) {
                EditText bbid_no = (EditText) popUpView.findViewById(R.id.bbid_number_scan_value);
                bbid_no.setText(scaned_code);
            }

            if (requestCode == REQUEST_QTY_BY_IMEI) {
                EditText imei_no = (EditText) popUpView.findViewById(R.id.imei_scan_value);
                imei_no.setText(scaned_code);
            }
            if (requestCode == REQUEST_QTY_BY_Serail) {
                EditText serial_txt = (EditText) popUpView.findViewById(R.id.serial_number_scan_value);
                serial_txt.setText(scaned_code);
            }

        }


    }

    String Dateformat_custom(String date) {
        Date entered_date = new Date("01-04-2020");
        // Specify the desired date format
        String DATE_FORMAT = "dd MMM yyyy";
        // Create object of SimpleDateFormat and pass the desired date format.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String formatted_date = sdf.format(entered_date);
        return formatted_date;

    }

    private void load_product_list() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        GrnDetailRequest request = new GrnDetailRequest();
        request.setGrn_no(grn_number);


        Call<GrnDetailResponse> call = api.get_grn_detail_product_list(request);

        call.enqueue(new Callback<GrnDetailResponse>() {
            @Override
            public void onResponse(Call<GrnDetailResponse> call, Response<GrnDetailResponse> response) {
                String response_status = response.body().getStatus();

                response.body().getGrn_details().getProductmodel();
                if (response_status.equals("success")) {
                    grnResponse = response.body().getGrn_details();
                    Product_list = grnResponse.getProductmodel();
                    SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);

                    String user_id = prefs.getString("login_user_id", "");
                    supplier_name.setText(supplier_name_value);
                    order_number.setText(grnResponse.getPurchase_order_id());
                    grn_no.setText((grnResponse.getGrn_id() == null) ? "" : "GRN - " + grnResponse.getGrn_id());
                    String order_date_value = grnResponse.getPo_created_at();
                    order_date.setText((grnResponse.getPo_created_at() == null) ? "" : grnResponse.getPo_created_at());

                    grn_date.setText((grnResponse.getGrn_date() == null) ? "" : grnResponse.getGrn_date());
                    invoice_date.setText(grnResponse.getInvoice_date());
                    invoice_number.setText(grnResponse.getInvoice_number());


                    grnResponse.setLogin_user_id(user_id);
                    if (grnResponse.getProductmodel() != null)
                        recyclerViewAdapter.setData(grnResponse.getProductmodel());
                    hideKeyboard();

                    // progressDialog.dismiss();

                }
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<GrnDetailResponse> call, Throwable t) {
                //progressDialog.dismiss();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @OnClick({R.id.submit})
    void submitaction() {
        ListViewValidation();
    }

    private void ListViewValidation() {
        if (grnResponse != null) {
            if (add_min_qty) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                String json = gson.toJson(grnResponse);
                saveaction(json);
            } else {
                ViewDialog ss = new ViewDialog();
                ss.displayError("Min 1 qty required to proceed. ");
            }

        }

    }

    void saveaction(String json) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        Api api = retrofit.create(Api.class);
        Call<ProductReceivedResponse> call = api.create_grn(grnResponse);

        call.enqueue(new Callback<ProductReceivedResponse>() {
            @Override
            public void onResponse(Call<ProductReceivedResponse> call, Response<ProductReceivedResponse> response) {
                String response_status = response.body().getStatus();
                String Grn_no = response.body().getGrn_no();
                String api_msg = response.body().getApi_message();
                if (response_status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "GRN Created" + Grn_no, Toast.LENGTH_SHORT).show();
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(GrnDetailsActivity.this, "Sucesss", api_msg);

                } else {
                    Toast.makeText(getApplicationContext(), api_msg, Toast.LENGTH_SHORT).show();
                    ViewDialog alert = new ViewDialog();
                    alert.showDialog(GrnDetailsActivity.this, "Failure", api_msg);
                }

            }

            @Override
            public void onFailure(Call<ProductReceivedResponse> call, Throwable t) {
                // progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();


            }

        });
    }

    public class ViewDialog {

        public void displayError(String errmsg) {

            AlertDialog alertDialog = new AlertDialog.Builder(GrnDetailsActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(errmsg);
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dail, int w) {
                    // getActivity().finish();
                    alertDialog.hide();
                }
            });
            alertDialog.show();
        }

        public void showDialog(Activity activity, String msg, String api_msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            if (msg.equals("Failure")) {
                dialog.setContentView(R.layout.failure_page);
                TextView grn_text = (TextView) dialog.findViewById(R.id.text);
                grn_text.setText(api_msg);


            } else {
                dialog.setContentView(R.layout.success_page);
                // TextView po_text = (TextView) dialog.findViewById(R.id.Po_text);
                TextView grn_text = (TextView) dialog.findViewById(R.id.text);
                // po_text.setText("Purchase Order -" + Po_no);
                grn_text.setText(api_msg);
            }
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (!msg.equals("Failure")) {
                        Intent i = new Intent(GrnDetailsActivity.this, GrnListActivity.class);
                        i.putExtra("login_user_id", grnResponse.getLogin_user_id());
                        startActivity(i);
                    }
                }
            });
            dialog.show();

        }
    }

}
