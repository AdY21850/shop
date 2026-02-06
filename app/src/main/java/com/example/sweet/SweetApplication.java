package com.example.sweet;

import android.app.Application;
import android.content.Context;

public class SweetApplication extends Application {

    private static SweetApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
