package com.example.projectmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Course extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView listView;

    List<String> listDataHeader;
    HashMap<String,List<String>> listHashMap;
    int valueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent intent = getIntent();
        valueID = intent.getIntExtra("ID", 0);
        listView=findViewById(R.id.exp_list_view);
        getModule(this);
    }

    private  void getModule(final Context context)
    {

            String endpoint = "webservice/rest/server.php";
            String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
            final RequestParams requestParams = new RequestParams();
            requestParams.add("wstoken",token);
            requestParams.add("moodlewsrestformat","json");
            requestParams.add("wsfunction","core_course_get_contents");
            requestParams.add("courseid",Integer.toString(valueID));
            HttpUtils.get(endpoint, requestParams, new JsonHttpResponseHandler() {

                // dữ liệu được trả về
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                    Log.d("response", "onSuccess");

                    if(responseBody.length()> 1) {
                        intializeData(responseBody);
                        listAdapter = new ExpandableListAdapter(context, listDataHeader,listHashMap);
                        listView.setAdapter(listAdapter);
                    }
                }
            });

    }

/*
* cho vào hàm dưới 1 mảng JSON, nó sẽ xử lý dữ liệu và đầu ra là một danh sách các tên chủ đề, trong mỗi
* tên chủ đề có các sự kiện nhỏ
* */
    private void intializeData(JSONArray responseBody) {
        listDataHeader=new ArrayList<>();
        listHashMap=new HashMap<>();

        for (int i=0; i<responseBody.length();i++)
        {
            List<String> childList=new ArrayList<>(); // danh sách các sự kiện nhỏ cho mỗi chủ đề
            try {
                JSONObject object = responseBody.getJSONObject(i);
                String HeaderList = (String) object.get("name");
                listDataHeader.add(HeaderList);

                JSONArray moduleCourse_Array =  object.getJSONArray("modules");
                for(int j =0; j<moduleCourse_Array.length();j++)
                {
                    object = moduleCourse_Array.getJSONObject(j);
                    String childListStr = (String) object.get("name");
                    childList.add(childListStr);
                }
                listHashMap.put(listDataHeader.get(i),childList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
/*
* Dưới đây là code cho giao diện
* */
    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        Context context;
        List<String> listDataHeader;
        HashMap<String,List<String>> listHashMap;

        public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listHashMap = listHashMap;
        }

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return listHashMap.get(listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle=(String)getGroup(groupPosition);
            View view= LayoutInflater.from(context).inflate(R.layout.group,parent,false);
            TextView lv_list_header=view.findViewById(R.id.lv_list_group);
            lv_list_header.setTypeface(null, Typeface.BOLD);
            lv_list_header.setText(headerTitle);
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String childText=(String)getChild(groupPosition,childPosition);
            View view=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            TextView textChild=view.findViewById(R.id.lv_list_item);
            textChild.setText(childText);

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
