package com.example.projectmobile;

import android.app.IntentService;
import android.content.Intent;

public class IntentService_courses extends IntentService {

    public IntentService_courses()
    {
        super("IntentService_courses");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

    }
}
