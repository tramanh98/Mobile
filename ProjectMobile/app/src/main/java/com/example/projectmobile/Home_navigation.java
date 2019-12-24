package com.example.projectmobile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Home_navigation extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                try {
                    if(id==R.id.nav_profile)
                    {
                        Intent intent =new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("IDENT","hello");
                        startActivity(intent);
                    }
                    if(id==R.id.nav_courses)
                    {
                        Intent intent =new Intent(getApplicationContext(), List_courses.class);
                        intent.putExtra("IDENT","hello");
                        startActivity(intent);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    return true;
                }
            }
        } );

    }

//    private  void DeadlineHandler()
//    {
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//
//                        String endpoint = "webservice/rest/server.php";
//                        String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
//                        final RequestParams requestParams = new RequestParams();
//                        requestParams.add("wstoken",token);
//                        requestParams.add("moodlewsrestformat","json");
//                        requestParams.add("wsfunction","core_webservice_get_site_info");
//
//                        HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
//                                Log.d("response", "onSuccess");
//                                Log.d("response ", responseBody.toString());
//                                try{
//                                    if(responseBody.has("fullname")) {
//                                        String fn = (String) responseBody.get("fullname");
//                                        edtFullname.setText(fn);
//                                    }
//                                    if(responseBody.has("username")) {
//                                        String un = (String) responseBody.get("username");
//                                        edtUsername.setText(un);
//                                    }
//                                    if(responseBody.has("userid")) {
//                                        String uid = responseBody.get("userid").toString();
//                                        edtUserid.setText(uid);
//                                    }
//                                }
//                                catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                            @Override
//                            public void onRetry(int retryNo) {
////                                super.onRetry(retryNo);
//                                Log.d("onRetry","retry");
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
////                                super.onFailure(statusCode, headers, responseString, throwable);
//                                Log.d("onFailure","fail");
//                            }
//
//                            @Override
//                            public void onFinish() {
////                                super.onFinish();
//                                Log.d("onFinish","dashboard");
//
//                            }
//                        });
//                    }
//                }, 3000);
//    }
}