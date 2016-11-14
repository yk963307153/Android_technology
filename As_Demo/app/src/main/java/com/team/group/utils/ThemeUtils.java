package com.team.group.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.team.group.App;


/**
 * Created
 */
public class ThemeUtils {

    private static int defalutThemeColor = Color.rgb(135,206,235);
    private static Context context = App.getAppContext();

    public static void setThemeColor(int color) {
        SharedPreferences.Editor editor = context.getSharedPreferences("ThemeColor", context.MODE_PRIVATE).edit();
        editor.putInt("themeColor", color);
        editor.commit();
    }

    public static void setThemePosition(int position) {
        SharedPreferences.Editor editor = context.getSharedPreferences("ThemeColor", context.MODE_PRIVATE).edit();
        editor.putInt("position", position);

        editor.commit();
    }

    public static int getThemePosition() {
        SharedPreferences pref = context.getSharedPreferences("ThemeColor", context.MODE_PRIVATE);
        return pref.getInt("position", 0);
    }

    public static int getToolBarColor() {

        //if(MyApplication.getInstance().isNightMode()){
        // return context.getResources().getColor(R.color.green_dark);
        //   }else {
        return getThemeColor();
        // }
    }

    public static int getThemeColor() {
        SharedPreferences pref = context.getSharedPreferences("ThemeColor", context.MODE_PRIVATE);
        return pref.getInt("themeColor", defalutThemeColor);
    }

}
