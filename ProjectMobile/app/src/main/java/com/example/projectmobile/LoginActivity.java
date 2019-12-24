package com.example.projectmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.projectmobile.database.getDATA;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    CardView btnLogin;
    EditText edtUsername;
    EditText edtPassword;
    String password;
    String username;
    private Cursor comprobar;
    Intent calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_uit_activity);
        btnLogin = (CardView) findViewById(R.id.login);
        edtUsername = (EditText)findViewById(R.id.username);
        edtPassword = (EditText)findViewById(R.id.password);


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }
    private  void handleLogin()
    {
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String endpoint = "login/token.php";
                        final RequestParams requestParams = new RequestParams();
                        requestParams.add("username",username);
                        requestParams.add("password",password);
                        requestParams.add("service","moodle_mobile_app");

                        HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                                Log.d("response login",responseBody.toString());
                                try {
                                    if(responseBody.has("token")){
                                        edtUsername.getText().clear();
                                        edtPassword.getText().clear();
                                        String token = (String) responseBody.get("token");
                                        getSharedPreferences("AUTH_TOKEN",0).edit().putString("TOKEN", token).commit();
                                        Intent intent =new Intent(getApplicationContext(), getDATA.class);
                                        intent.putExtra("IDENT",username);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        finish();
                                    }
                                    else{
                                        String error = (String)responseBody.get("error");
                                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                        alertDialog.setTitle("Error");
                                        alertDialog.setMessage(error);
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        progressDialog.dismiss();

                                    }
                                }
                                catch (Exception e){
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onRetry(int retryNo) {
//                                super.onRetry(retryNo);
                                if(retryNo == 4){
                                    HttpUtils.cancelRequest();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Log.d("Failer","On Failure");
                                throwable.printStackTrace();
                            }

                            @Override
                            public void onFinish() {
//                                super.onFinish();
                                Log.d("onFinish","set invisible here");
                                progressDialog.dismiss();
                                //show error dialog
                            }
                        });
                    }
                }, 3000);

    }

}
