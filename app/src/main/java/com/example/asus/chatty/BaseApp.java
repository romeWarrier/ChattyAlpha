package com.example.asus.chatty;

import android.app.Application;

import com.example.asus.chatty.utils.PreferenceUtils;
import com.sendbird.android.SendBird;

public class BaseApp extends Application {

    private static final String APP_ID = "C1DA4433-0B48-45EB-ACB6-0E9F76BDD0C3";

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(getApplicationContext());
        SendBird.init(APP_ID, getApplicationContext());
    }
}
