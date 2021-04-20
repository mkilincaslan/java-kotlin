package com.example.parseapp;

import android.app.Application;

import com.parse.Parse;

public class ParseStarter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG); // Define logcat setting

        Parse.initialize(
            new Parse
                .Configuration
                .Builder(this)
                .applicationId(getString(R.string.applicationId))
                .clientKey(getString(R.string.clientKey))
                .server(getString(R.string.server))
                .build()
        );
    }
}