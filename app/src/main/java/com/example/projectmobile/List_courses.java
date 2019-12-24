package com.example.projectmobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class List_courses extends AppCompatActivity {

    ListView listView;
    HashMap<String,Integer> courses_id;
    Handler mHandler;
    public List<String> List_courses = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        listView = findViewById(R.id.listView);
        courses_id=new HashMap<>();
        getCourses(this);
        doOnclicklistview();
        onHandlerlistview();

    }

    private void onHandlerlistview() {
        mHandler = new Handler(){
            int a;
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                Log.d("content", bundle.getString("key_1").toString());
                a = courses_id.get(bundle.getString("key_1").toString());
                Intent intent =new Intent(getApplicationContext(), Course.class);
                intent.putExtra("ID",a);
                startActivity(intent);
            }
        };
    }

    private  void doOnclicklistview()  // không cần phân thread
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView myTitle = view.findViewById(R.id.textView1);
                        Bundle bundle = new Bundle();
                        bundle.putString("key_1", myTitle.getText().toString());
                        Message progressMsg = new Message();
                        progressMsg.setData(bundle);
                        mHandler.sendMessage(progressMsg);
                    }
                });
            }
        });
        thread.start();
    }
    private  void getCourses(final Context context)
    {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String endpoint = "webservice/rest/server.php";
                        String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
                        final RequestParams requestParams = new RequestParams();
                        requestParams.add("wstoken",token);
                        requestParams.add("moodlewsrestformat","json");
                        requestParams.add("wsfunction","core_enrol_get_users_courses");
                        requestParams.add("userid","8588");
                        HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                                Log.d("response", "onSuccess");

                                //String responseData = responseBody.toString();
                                if(responseBody.length()> 2) {
                                    for (int i=0; i<responseBody.length();i++)
                                    {
                                        try {
                                            JSONObject object = responseBody.getJSONObject(i);
                                            String fn = (String) object.get("fullname");
                                            Integer id = object.getInt("id");
                                            List_courses.add(fn);
                                            courses_id.put(fn,id);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    MyAdapter adapter = new MyAdapter(context, List_courses);
                                    listView.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onRetry(int retryNo) {
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
                        Log.d("onFinish","helllllllllllllllo");
                    }
                },3000);

    }



    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rTitle = new ArrayList<String>();
//        String rDescription[];

        MyAdapter (Context c, List<String> title) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
//            this.rDescription = description;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView myTitle = row.findViewById(R.id.textView1);
//            TextView myDescription = row.findViewById(R.id.textView2);

            // now set our resources on views
            myTitle.setText(rTitle.get(position));
//            myDescription.setText(rDescription[position]);

            return row;
        }
    }

}
