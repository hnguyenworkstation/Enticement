package com.greencode.enticement_android.Enticement;

import android.app.Application;

import java.util.Calendar;

/**
 * Created by Hung Nguyen on 11/15/2016.
 */

public class EnticementApplication extends Application {
    public static final String TAG = EnticementApplication.class
            .getSimpleName();
    private static EnticementApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = EnticementApplication.this;
    }

    public static synchronized EnticementApplication getInstance() {
        return mInstance;
    }
}
