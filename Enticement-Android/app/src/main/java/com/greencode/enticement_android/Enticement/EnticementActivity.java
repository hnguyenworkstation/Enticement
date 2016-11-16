package com.greencode.enticement_android.Enticement;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Hung Nguyen on 11/15/2016.
 */

public class EnticementActivity extends AppCompatActivity {
    public static final String TAG = EnticementActivity.class.getSimpleName();
    private static EnticementActivity mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
    }

}
