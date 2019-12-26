package com.example.projectmobile.database;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectmobile.HttpUtils;
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
    private DatabaseHelper_Courses db_Courses;

    private Course_content ct;
    HashMap<String,Integer> courses_id;
    Handler mHandler;
    ExecutorService executor;
    String token;
    int idUSER;

    // list for 3 preference
    private List<Courses> listCourses;
    private List<Course_content> lCourseContent;
    private List<Course_deadline> lCourseDeadline;
    public  getDATA()
    {

    }
    public void deleteDB(Context context)
    {
        db_Courses = new DatabaseHelper_Courses(context);
        db_Courses.deleteAllData();
    }
    public getDATA(String token, Handler mHandler)
    {
        this.token = token;
        this.mHandler = mHandler;
    }

    public void createDB(Context context){

        courses_id=new HashMap<>();
        executor = Executors.newSingleThreadExecutor();

        listCourses = new ArrayList<>();
        lCourseContent = new ArrayList<>();
        lCourseDeadline = new ArrayList<>();

        getInfor();
    }
    private void callSendHandler(String mess){
        Bundle bundle = new Bundle();
        bundle.putString("key_1", mess);
        Message progressMsg = new Message();
        progressMsg.setData(bundle);
        mHandler.sendMessage(progressMsg);
    }

    public  void getInfor()  // lấy dữ liệu
    {
        String endpoint = "webservice/rest/server.php";
        final RequestParams requestParams = new RequestParams();
        requestParams.add("wstoken",this.token);
        requestParams.add("moodlewsrestformat","json");
        requestParams.add("wsfunction","core_webservice_get_site_info");
        HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {

                try {
                    String username = responseBody.getString("fullname");
                    String mssv = responseBody.getString("username");
                    callSendHandler(username);
                    callSendHandler(mssv);
                    idUSER = responseBody.getInt("userid");
                    callSendHandler("2");
                    HttpUtils.cancelRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFinish() {
            }
        });
    }

// gọi API lấy danh sách môn học, và lưu vào database
    public void setCourses() {

        String endpoint = "webservice/rest/server.php";
        final RequestParams requestParams = new RequestParams();
        requestParams.add("wstoken",this.token);
        requestParams.add("moodlewsrestformat","json");
        requestParams.add("wsfunction","core_enrol_get_users_courses");
        requestParams.add("userid",String.valueOf(idUSER));
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
                                executor.execute(new RequestDBCourses(id,fn));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callSendHandler("3");
                    Log.d("so mon: ", String.valueOf(listCourses.size()));
                }
            }
            @Override
            public void onFinish() {
            }
        });
    }
//lấy danh sách deadline và lưu vào DB
    public void setCourseDeadline(final int i)
    {
        String endpoint = "webservice/rest/server.php";
        final RequestParams requestParams = new RequestParams();
        requestParams.add("wstoken",token);
        requestParams.add("moodlewsrestformat","json");
        requestParams.add("wsfunction","core_calendar_get_calendar_events");
        requestParams.add("events[courseids][]",String.valueOf(listCourses.get(i).getId_course()));
        HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
            try {
                JSONArray jsa = responseBody.getJSONArray("events");
                setDeadline(jsa);
                if(listCourses.size()-1 == i)
                {
                    HttpUtils.cancelRequest();
                    callSendHandler("doneDL");
                }
                else
                {
                    HttpUtils.cancelRequest();
                    setCourseDeadline(i+1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFinish() {
        }
    });

    }
    private  void setDeadline(final JSONArray jsa)
    {
        for(int j=0;j<jsa.length();j++)
        {
            try {
                executor.execute(new RequestDBDeadline(jsa.getJSONObject(j).getInt("courseid"), jsa.getJSONObject(j).getInt("id"),jsa.getJSONObject(j).getString("name"),jsa.getJSONObject(j).getLong("timestart")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class RequestDBCourses implements Runnable {
        int id;
        String name;
        public RequestDBCourses(int id,String name){
            this.id =id;
            this.name = name;
        }
        @Override
        public void run() {
                db_Courses.addCourse(id,name);
        }
    }
    public class RequestDBDeadline implements Runnable {
        int id_course;
        int id_event;
        String name;
        Long timeStart;
        public RequestDBDeadline(int id_course, int id_event, String name, Long timeStart){
            this.id_course =id_course;
            this.name = name;
            this.id_event = id_event;
            this.timeStart = timeStart;
        }

        @Override
        public void run() {
            db_Courses.addDeadline(id_course,id_event,name,timeStart);

        }
    }
}
