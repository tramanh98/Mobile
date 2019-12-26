package com.example.projectmobile;

import android.os.Bundle;
import android.widget.TextView;
import com.example.projectmobile.database.DatabaseHelper_Courses;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    TextView edtFullname;
    TextView edtUsername;
    TextView edtEmail;
    private DatabaseHelper_Courses dtbCourses;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dtbCourses = new DatabaseHelper_Courses(this);
        edtFullname = (TextView)findViewById(R.id.fullname);
        edtUsername = (TextView)findViewById(R.id.username);
        edtEmail = (TextView)findViewById(R.id.email);
        getInfor();
    }
    private  void getInfor()
    {
        String username = getSharedPreferences("AUTH_TOKEN",0).getString("FULLNAME",null);
        String mssv = getSharedPreferences("AUTH_TOKEN",0).getString("MSSV",null);
        edtFullname.setText(username);
        edtEmail.setText(mssv + "@gm.uit.edu.vn");
        edtUsername.setText(mssv);

    }

}
