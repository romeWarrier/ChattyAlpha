package com.example.asus.chatty.sample.main;


import android.app.Application;

import com.sendbird.android.SendBird;
import com.example.asus.chatty.sample.utils.PreferenceUtils;

public class BaseApp extends Application {

    private static final String APP_ID = "C1DA4433-0B48-45EB-ACB6-0E9F76BDD0C3"; // US-1 Demo
    public static final String VERSION = "2.0";

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(getApplicationContext());

        SendBird.init(APP_ID, getApplicationContext());
    }
}
