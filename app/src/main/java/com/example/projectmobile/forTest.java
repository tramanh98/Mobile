package com.example.projectmobile;
import com.example.projectmobile.database.getDATA;
import com.example.projectmobile.database.DatabaseHelper_Courses;
import com.example.projectmobile.database.DatabaseHelper_GetDeadline;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class forTest extends AppCompatActivity {
    CompactCalendarView compactCalendar;
    private DrawerLayout drawer;
    private getDATA data = new getDATA();
    private DatabaseHelper_Courses dtbCourses;
    private DatabaseHelper_GetDeadline dtbDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation);
        String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
        //dtbCourses = new DatabaseHelper_Courses(this, "khongbit",null,1);// lấy deadline
        //dtbDeadline = new DatabaseHelper_GetDeadline(this,"khongbit",null,1);
//        data.createDB(this);
//        data.setCourses(token);
        Log.d("complete: ", "done");
        //data.setCourseDeadline(token);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        //handlerDeadline();

        //Set an event for Teachers' Professional Day 2016 which is 21st of October
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                if (dateClicked.toString().compareTo("Fri Oct 21 00:00:00 AST 2016") == 0) {
                    Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
            }
        });




    }



//    private void handlerDeadline()
//    {
//        List<Long> startTIME = dtbDeadline.getDeadline();
//        for(int i=0;i<startTIME.size();i++)
//        {
//            Log.d("data", startTIME.get(i).toString());
//            Event ev1 = new Event(Color.RED, startTIME.get(i)*1000L, "Teachers' Professional Day");
//            compactCalendar.addEvent(ev1);
//        }
//
//    }
}
