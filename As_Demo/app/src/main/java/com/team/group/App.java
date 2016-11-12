package com.team.group;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Application
 */
public class App extends Application {

    public static Context appContext;
    private static App sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        sInstance = this;

    }

    public static App getAppContext() {
        return sInstance;
    }

    public static Context getContext() {
        return appContext;
    }

    public static App getInstance() {
        return sInstance;
    }

    public static String getApplicationName() {
        String appName = "";
        try {
            PackageManager packManager = sInstance.getPackageManager();
            ApplicationInfo appInfo = sInstance.getApplicationInfo();
            appName = (String) packManager.getApplicationLabel(appInfo);
        } catch (Exception e) {
        }
        return appName;
    }

    /**
     * 版本名
     */
    public static String getVersionName() {
        return getPackageInfo().versionName;
    }

    /**
     * 版本号
     */
    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            PackageManager packageManager = appContext.getPackageManager();
            pi = packageManager.getPackageInfo(appContext.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 设备唯一标示
     */
    public static String getAndroidID() {
        String uuid = "";
        try {
            //Android系统为开发者提供的用于标识手机设备的串号，且唯一性良好。(仅且设备是手机)注意全是0或者星号
            TelephonyManager telephonyManager = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
            String device_Id = telephonyManager.getDeviceId();
            //手机设备第一次启动随即产生的一个数字，如果系统改变，该号可能会改变(不适用与Android2.2的版本,当设备被wipe后该值会被重置)
            String android_Id = Settings.Secure.getString(appContext.getContentResolver(), Settings.Secure.ANDROID_ID);

            if (device_Id != null && -1 == device_Id.indexOf("*") && device_Id.replace("0", "").length() > 0) {
                return uuid = device_Id;
            }
            if (android_Id != null && !"9774d56d682e549c".equals(android_Id)) {
                uuid = android_Id;
            } else {
                uuid = UUID.randomUUID().toString();
            }
            return uuid;
        } catch (Exception e) {
//            Log.e("App Error", "getAndroidID");
            uuid = UUID.randomUUID().toString();
        }

        return uuid;
    }


    /**
     * 当前手机型号
     */
    public static Map<String, Object> getModel() {
        Map<String, Object> map = new HashMap<String, Object>();
        //手机型号
        map.put("MODEL", android.os.Build.MODEL);
        map.put("DEVICE", android.os.Build.DEVICE);
        map.put("PRODUCT", android.os.Build.PRODUCT);
        map.put("SDK", android.os.Build.VERSION.SDK);
        //系统版本
        map.put("RELEASE", android.os.Build.VERSION.RELEASE);
        return map;
    }

    /**
     * 判断当前设备是手机还是平板,App for Android
     *
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTabletDevice() {
        try {
            return (appContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        } catch (Exception e) {
            return false;
        }
    }



//    // 创建服务用于捕获崩溃异常
//    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
//        public void uncaughtException(Thread thread, Throwable ex) {
//            restartApp();//发生崩溃异常时,重启应用
//        }
//    };
//    public void restartApp(){
//        Intent intent = new Intent(sInstance,IWGMainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        sInstance.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
//    }
}
