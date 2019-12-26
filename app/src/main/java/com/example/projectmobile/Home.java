package com.example.projectmobile;
import com.example.projectmobile.database.DatabaseHelper_Courses;
import com.example.projectmobile.database.Course_deadline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.navigation.NavigationView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Home extends AppCompatActivity {
    CompactCalendarView compactCalendar;
    private DrawerLayout drawer;
    private DatabaseHelper_Courses dtbCourses;
    ListView listView;
    NavigationView navigationView;
    List<Course_deadline> startTIME;
    TextView name;
    View inflatedView;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation);
        listView=(ListView)findViewById(R.id.listview);

        // lấy dữ liệu
        dtbCourses = new DatabaseHelper_Courses(this);
        String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);

        /*
        * phần thân của thanh điều hướng*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Calendar");

        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        /* Lấy dữ liệu deadline ở database*/
        handlerDeadline();
        handlerCalendar(calcul(new Date().getTime()));

        /*
         * Hiển thị tên ở header của thanh điều hướng*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        name =(TextView) header.findViewById(R.id.yourNAME);
        name.setText(getSharedPreferences("AUTH_TOKEN",0).getString("FULLNAME",null));

        /*Lắng nghe sự kiện ở thanh điều hướng*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                try {
                    if(id==R.id.nav_profile)
                    {
                        Intent intent =new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(intent);
                    }
                    if(id==R.id.nav_courses)
                    {
                        Intent intent =new Intent(getApplicationContext(), List_courses.class);
                        startActivity(intent);
                    }
                    if(id==R.id.nav_logOut)  // xóa token, username, mssv và DB sau đó qua về login
                    {
                        getSharedPreferences("AUTH_TOKEN", 0).edit().remove("TOKEN").commit();
                        getSharedPreferences("AUTH_TOKEN", 0).edit().remove("FULLNAME").commit();
                        getSharedPreferences("AUTH_TOKEN", 0).edit().remove("MSSV").commit();
                        dtbCourses.deleteAllData();
                        Intent intent =new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
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


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                Log.d("ten su kien: ", String.valueOf(calcul(new Date().getTime())));
//                Log.d("ten su kien: ", String.valueOf(dateClicked.getTime()));
                handlerCalendar(dateClicked.getTime());
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatForDisplaying.format(firstDayOfNewMonth));
            }
        });
    }
    private long calcul(Long time)
    {
        Date dt = new Date();  // current time
        int munite = dt.getMinutes();     // gets the current month
        int hours = dt.getHours(); // gets hour of day
        int second = dt.getSeconds();

        return time - (second*10 + munite*60 + 60*60*hours)*1000L;
    }
    private void handlerCalendar(Long time)
    {

        List<Course_deadline> LstDL = new ArrayList<Course_deadline>();
        for(int i=0;i<startTIME.size();i++)
        {
            Long bt = startTIME.get(i).getTime_Start()*1000L;
            if(bt - time < 86500*1000L && bt -time >=0)
            {
                LstDL.add(startTIME.get(i));
            }
        }
        MyAdapter1 adapter = new MyAdapter1(this, LstDL);
        listView.setAdapter(adapter);
    }

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    private void handlerDeadline()
    {
        startTIME = dtbCourses.getTableDeadline();
        for(int i=0;i<startTIME.size();i++)
        {
            Event ev1 = new Event(Color.RED, startTIME.get(i).getTime_Start()*1000L,startTIME.get(i).getName_event());
            compactCalendar.addEvent(ev1);
        }
    }

    class MyAdapter1 extends ArrayAdapter<Course_deadline> {

        Context context;
        List<Course_deadline> rTitle = new ArrayList<Course_deadline>();

        MyAdapter1 (Context c, List<Course_deadline> title) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView time = row.findViewById(R.id.textView1);
            TextView course = row.findViewById(R.id.textView2);
            TextView content = row.findViewById(R.id.textView3);
            time.setText(convertTime(rTitle.get(position).getTime_Start()*1000L));
            course.setText(dtbCourses.getCourse(rTitle.get(position).getId_course()));
            content.setText(rTitle.get(position).getName_event());

            return row;
        }
    }
}
