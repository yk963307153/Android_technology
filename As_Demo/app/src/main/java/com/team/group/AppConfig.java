package com.team.group;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;


/**
 * SharedPreferences
 */
public class AppConfig {

    private static AppConfig config;

    private String mString;
    private int mInt;
    private boolean isBoolean;

    public static AppConfig sharedInstance() {
        if (config == null) {
            config = new AppConfig();
        }

//        SharedPreferences prefs = new SecurePreferences(App.getAppContext(), "", prefsFile);
        SharedPreferences prefs = App.getAppContext().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        if (BuildConfig.DEBUG) {
            SecurePreferences.setLoggingEnabled(true);
        }

        config.isBoolean = prefs.getBoolean("isBoolean", false);
        config.mInt = prefs.getInt("mInt", 0);
        config.mString = prefs.getString("mString", "");
        return config;
    }


    public void savePreferences() {
//        SharedPreferences prefs = new SecurePreferences(App.getAppContext(), "", prefsFile);
        SharedPreferences prefs = App.getAppContext().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isBoolean", config.isBoolean);
        editor.putString("mString", config.mString);
        editor.putInt("mInt", config.mInt);
        editor.commit();
    }
}