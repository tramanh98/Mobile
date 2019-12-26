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

import com.example.projectmobile.database.Courses;
import com.example.projectmobile.database.DatabaseHelper_Courses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class List_courses extends AppCompatActivity {

    ListView listView;
    HashMap<String,Integer> courses_id;
    Handler mHandler;
    public List<Courses> List_courses = new ArrayList<Courses>();
    private DatabaseHelper_Courses dtbCourses;
    private List<String> LstCourse = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        listView = findViewById(R.id.listView);
        dtbCourses = new DatabaseHelper_Courses(this);
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

    private  void doOnclicklistview()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView myTitle = view.findViewById(R.id.textView);
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
        List_courses.addAll(dtbCourses.getListCourses());
        for(int i =0;i<List_courses.size();i++)
        {
            LstCourse.add(List_courses.get(i).getName_course());
            courses_id.put(List_courses.get(i).getName_course(),List_courses.get(i).getId_course());
        }
        MyAdapter adapter = new MyAdapter(context, LstCourse);
        listView.setAdapter(adapter);
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rTitle = new ArrayList<String>();

        MyAdapter (Context c, List<String> title) {
            super(c, R.layout.row1, R.id.textView, title);
            this.context = c;
            this.rTitle = title;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row1, parent, false);
            TextView myTitle = row.findViewById(R.id.textView);

            myTitle.setText(rTitle.get(position));

            return row;
        }
    }

}
