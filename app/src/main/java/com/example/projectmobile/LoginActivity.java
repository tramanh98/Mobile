package com.example.projectmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.projectmobile.database.getDATA;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    CardView btnLogin;
    EditText edtUsername;
    EditText edtPassword;
    String password;
    String username;
    private Cursor comprobar;
    getDATA getDT;
    Intent calendar;
    Handler mHandler;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String old_token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
        HttpUtils.cancelRequest();
        if (old_token != null){
            Intent intent =new Intent(getApplicationContext(), Home.class);
            intent.putExtra("IDENT",username);
            startActivity(intent);
            finish();
        }
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        HttpUtils.renew();
        setContentView(R.layout.login_uit_activity);
        btnLogin = (CardView) findViewById(R.id.login);
        edtUsername = (EditText)findViewById(R.id.username);
        edtPassword = (EditText)findViewById(R.id.password);
        onHandlerlistview(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });



    }
    private void callSendHandler(String mess){
        Bundle bundle = new Bundle();
        bundle.putString("key_1", mess);
        Message progressMsg = new Message();
        progressMsg.setData(bundle);
        mHandler.sendMessage(progressMsg);
    }
    public void onHandlerlistview(final Context ct) {
        mHandler = new Handler(){
            String a;
            //            Integer i =0;
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                Log.d("content", bundle.getString("key_1").toString());
                a = bundle.getString("key_1").toString();
                if(a=="1")
                {
                    getDT.deleteDB(ct);
                    getDT.createDB(ct);
                }
                if(a=="2")
                {
                    HttpUtils.cancelRequest();
                    getDT.setCourses();
                }
                if(a=="3")
                {
                    HttpUtils.cancelRequest();
                    getDT.setCourseDeadline(0);
                }
                if(a=="doneDL")
                {
                    progressDialog.dismiss();
                    HttpUtils.cancelRequest();
                    Intent intent =new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    finish();
                }
                if(a.length() ==8)
                {
                    getSharedPreferences("AUTH_TOKEN",0).edit().putString("MSSV", a).commit();
                }
                if(a.length() > 10)
                {
                    getSharedPreferences("AUTH_TOKEN",0).edit().putString("FULLNAME", a).commit();

                }
            }
        };
    }


    private  void handleLogin()
    {
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();


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
                                        getDT = new getDATA(token, mHandler);

                                        callSendHandler("1");
                                        HttpUtils.cancelRequest();
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
                                Log.d("response login","retryyyyyyyyyy");
                                if(retryNo == 4){
                                    HttpUtils.cancelRequest();
                                }
                            }

                            @Override
                            public void onFinish() {
                            }
                        });
                    }
                }, 0);
    }
}
