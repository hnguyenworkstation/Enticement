package com.greencode.enticement_android.Enticement;

import android.content.Context;
import android.content.SharedPreferences;

import com.greencode.enticement_android.Models.MyProfile;

/**
 * Created by Hung Nguyen on 12/1/2016.
 */

public class EnticementPreferenceManager {
    private String TAG = EnticementPreferenceManager.class.getSimpleName();

    private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEditor;
    private Context mContext;

    private MyProfile mProfile;

    // Sharedpref file name
    private static final String PREF_NAME = "5p46h@7";
    private int PRIVATE_MODE = 0;

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";

    public EnticementPreferenceManager(Context context) {
        this.mContext = context;
        mPref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mPrefEditor = mPref.edit();
        mProfile = new MyProfile();
    }

    public MyProfile getProfile() {
        return mProfile;
    }

    public void setProfile(MyProfile mProfile) {
        this.mProfile = mProfile;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public SharedPreferences.Editor getPrefEditor() {
        return mPrefEditor;
    }

    public void setPrefEditor(SharedPreferences.Editor mPrefEditor) {
        this.mPrefEditor = mPrefEditor;
    }

    public SharedPreferences getPref() {
        return mPref;
    }

    public void setPref(SharedPreferences mPref) {
        this.mPref = mPref;
    }

}
