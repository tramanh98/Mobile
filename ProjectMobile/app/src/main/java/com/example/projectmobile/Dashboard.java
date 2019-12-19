package com.example.projectmobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Dashboard extends AppCompatActivity {

    TextView edtFullname;
    TextView edtUsername;
    TextView edtEmail;
    TextView edtUserid;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
//        toolbar = (Toolbar)findViewById(R.id.toolbar12);
//        setSupportActionBar(toolbar);
//        if(getSupportActionBar() != null)
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtFullname = (TextView)findViewById(R.id.fullname);
        edtUsername = (TextView)findViewById(R.id.username);
        edtEmail = (TextView)findViewById(R.id.email);
        edtUserid = (TextView)findViewById(R.id.userid);
        ImageView img = (ImageView)findViewById(R.id.imgUser);
        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getInfor();
            }
        });
    }
    private  void getInfor()
    {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        String endpoint = "webservice/rest/server.php";
                        String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
                        final RequestParams requestParams = new RequestParams();
                        requestParams.add("wstoken",token);
                        requestParams.add("moodlewsrestformat","json");
                        requestParams.add("wsfunction","core_webservice_get_site_info");

                        HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                                Log.d("response", "onSuccess");
                                Log.d("response ", responseBody.toString());
                                try{
                                    if(responseBody.has("fullname")) {
                                        String fn = (String) responseBody.get("fullname");
                                        edtFullname.setText(fn);
                                    }
                                    if(responseBody.has("username")) {
                                        String un = (String) responseBody.get("username");
                                        edtUsername.setText(un);
                                    }
                                    if(responseBody.has("userid")) {
                                        String uid = responseBody.get("userid").toString();
                                        edtUserid.setText(uid);
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onRetry(int retryNo) {
//                                super.onRetry(retryNo);
                                Log.d("onRetry","retry");
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                                super.onFailure(statusCode, headers, responseString, throwable);
                                Log.d("onFailure","fail");
                            }

                            @Override
                            public void onFinish() {
//                                super.onFinish();
                                Log.d("onFinish","dashboard");

                            }
                        });
                    }
                }, 3000);
    }

}
