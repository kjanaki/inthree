package com.src.inthree;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.src.inthree.API.Api;
import com.src.inthree.model.LoginRequest;
import com.src.inthree.model.LoginResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_password)
    EditText mPassword;

    @BindView(R.id.edt_username)
    EditText mUsername;
    @BindView(R.id.login_error)
    TextView login_error;

    @BindView(R.id.login_button)

    Button mLoginButton;
    ProgressDialog progressDialog;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);
        editor = prefs.edit();
        init_page();

    }

    private void init_page() {
        SharedPreferences prefs = getSharedPreferences("WHApp", Context.MODE_PRIVATE);
        String user_id = prefs.getString("login_user_id", "");
        if (user_id != "" && user_id != null) {
            Intent i = new Intent(MainActivity.this, FirstPage.class);
            i.putExtra("login_user_id", user_id);
            editor.putString("login_user_id", user_id);
            editor.commit();
            startActivity(i);
        }


    }

    @OnTextChanged({R.id.edt_password, R.id.edt_username})
    void text_change() {
        login_error.setVisibility(View.GONE);
    }

    @OnClick({R.id.login_button})
    void login(View v) {
        if (!mUsername.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()) {
            hideKeyboard();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("ProgressDialog");
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMax(100);
            progressDialog.getProgress();
            progressDialog.setCancelable(false);
            progressDialog.show();
            Check_login();

        } else {
            login_error.setVisibility(View.VISIBLE);
        }
    }


    // Check Login
    private void Check_login() {
        hideKeyboard();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BoonBox_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        LoginRequest request = new LoginRequest();
        LoginRequest.Data requestData = new LoginRequest.Data();
        requestData.setUser_name(mUsername.getText().toString().trim());
        requestData.setPassword(mPassword.getText().toString().trim());
        request.setmData(requestData);

        Call<LoginResponse> call = api.get_authincation(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String login_status = response.body().getStatus();
                progressDialog.dismiss();
                String user_id = response.body().getUser_id();
                if (login_status.equals("success")) {
                   // Toast.makeText(getApplicationContext(), "Login Success!!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, FirstPage.class);
                    i.putExtra("login_user_id", user_id);
                    editor.putString("login_user_id", user_id);
                    editor.commit();
                    startActivity(i);
                } else {
                    progressDialog.dismiss();
                    login_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();

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


}
