package com.example.flavi.client;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {
    private static AppContext context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        context = this;
        super.onCreate();
    }
}
