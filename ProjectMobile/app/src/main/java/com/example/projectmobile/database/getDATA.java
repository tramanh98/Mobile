package com.example.projectmobile.database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectmobile.Dashboard;
import com.example.projectmobile.forTest;
import com.example.projectmobile.HttpUtils;
import com.example.projectmobile.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class getDATA extends AppCompatActivity{
    private DatabaseHelper_CourseGetContents db_CourseContent;
    private DatabaseHelper_GetDeadline db_Deadline;
    private DatabaseHelper_Courses db_Courses;

    private Course_content ct;
    HashMap<String,Integer> courses_id;
    private List<Integer> idc = new ArrayList<>();
    private List<String> namec = new ArrayList<>();
    Handler mHandler;
    private String tk;

    // list for 3 preference
    private List<Courses> listCourses;
    private List<Course_content> lCourseContent;
    private List<Course_deadline> lCourseDeadline;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDB(this);
        onHandlerlistview();
        getInfor();

    }

    public void createDB(Context context){
        db_CourseContent = new DatabaseHelper_CourseGetContents(context, "core_course_get_contents",null,1);
        db_Deadline = new DatabaseHelper_GetDeadline(context, "DEADLINE",null,1);
        db_Courses = new DatabaseHelper_Courses(context);
        courses_id=new HashMap<>();

        listCourses = new ArrayList<>();
        lCourseContent = new ArrayList<>();
        lCourseDeadline = new ArrayList<>();
    }
    private void callSendHandler(String mess){
        Bundle bundle = new Bundle();
        bundle.putString("key_1", mess);
        Message progressMsg = new Message();
        progressMsg.setData(bundle);
        mHandler.sendMessage(progressMsg);
    }
    public void onHandlerlistview() {
        mHandler = new Handler(){
            String a;
            Integer i =0;
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                Log.d("content", bundle.getString("key_1").toString());
                a = bundle.getString("key_1").toString();
                if(a=="getinfor")
                {
                    setCourses();
                }
                if(a=="courses")
                {
                    setCourseDeadline(0);
                }
                if(a=="done")
                {
                    i++;
                    setCourseDeadline(i);
                }
                if(a=="doneDL")
                {
                    //getSharedPreferences("AUTH_TOKEN",0).edit().putStringSet().commit();
                    Intent intent =new Intent(getApplicationContext(), forTest.class);
                    startActivity(intent);
                }
            }
        };
    }

    public  void getInfor()
    {
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

                try {
                    String username = responseBody.getString("fullname");
                    String mssv = responseBody.getString("username");
                    Integer userID = responseBody.getInt("userid");
                    getSharedPreferences("USER_ID",0).edit().putInt("USERID", userID).commit();
                    callSendHandler("getinfor");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFinish() {
//                                super.onFinish();
                Log.d("onFinish","dashboard");

            }
        });
    }

// lấy danh sách môn học
    public void setCourses() {

        String endpoint = "webservice/rest/server.php";
        String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
        Integer userid = getSharedPreferences("USER_ID",0).getInt("USERID",0);
        final RequestParams requestParams = new RequestParams();
        requestParams.add("wstoken",token);
        requestParams.add("moodlewsrestformat","json");
        requestParams.add("wsfunction","core_enrol_get_users_courses");
        requestParams.add("userid",String.valueOf(userid));
        HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                if(responseBody.length()> 2) {
                    for (int i=0; i<responseBody.length();i++)
                    {
                        try {
                            JSONObject object = responseBody.getJSONObject(i);
                            String fn = (String) object.get("fullname");
                            Integer id = object.getInt("id");
                            Integer timeend = object.getInt("enddate");
                            if(timeend == 1577646000)
                            {
                                Courses crs = new Courses(id,fn);
                                listCourses.add(crs);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callSendHandler("courses");
                    Log.d("so mon: ", String.valueOf(listCourses.size()));
                }
            }
            @Override
            public void onFinish() {
//                                super.onFinish();
                Log.d("onFinish","dashboard");
            }
        });
        Log.d("onFinish","helllllllllllllllo");
    }
//lấy danh sách deadline
    public void setCourseDeadline(final int i)
    {
        //final List<Integer> idCourse = db_Courses.getAllCourses();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String endpoint = "webservice/rest/server.php";
                        String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
                            final RequestParams requestParams = new RequestParams();
                            requestParams.add("wstoken",token);
                            requestParams.add("moodlewsrestformat","json");
                            requestParams.add("wsfunction","core_calendar_get_calendar_events");
                            requestParams.add("events[courseids][]",String.valueOf(listCourses.get(i).getId_course()));
                            HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                                Log.d("response", "onSuccess");
                                try {
                                    JSONArray jsa = responseBody.getJSONArray("events");
                                    setDeadline(jsa);
                                    if(listCourses.size()-1 == i)
                                    {
                                        callSendHandler("doneDL");
                                    }
                                    else callSendHandler("done");

                                    Log.d("so deadline: ",String.valueOf(lCourseDeadline.size()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFinish() {
//                                super.onFinish();
                                Log.d("onFinish","dashboard");

                            }
                        });
                    }
                },3000);
    }
    private  void setDeadline(final JSONArray jsa)  // không cần phân thread
    {
        Thread thread = new Thread(new Runnable() {
            private Course_deadline dl;
            @Override
            public void run() {
                for(int j=0;j<jsa.length();j++)
                {
                    try {
                        dl = new Course_deadline(jsa.getJSONObject(j).getInt("courseid"), jsa.getJSONObject(j).getInt("id"),jsa.getJSONObject(j).getString("name"),jsa.getJSONObject(j).getLong("timestart"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lCourseDeadline.add(dl);
                    while(db_Deadline.isOpenDB())
                    {

                    }
                    db_Deadline.addDeadline(dl);
                }
            }
        });
        thread.start();
    }
}
