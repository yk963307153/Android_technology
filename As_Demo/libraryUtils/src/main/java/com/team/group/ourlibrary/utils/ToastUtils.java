package com.team.group.ourlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import static com.team.group.ourlibrary.utils.MyToast.mToastMsg;

/**
 * 吐司相关工具类
 * 防止重复操作
 */
public class ToastUtils {
    private static Toast mToast = null;


    public static Toast getmToast() {
        return mToast;
    }
    //-----------------------------------默认类型 star--------------------------------------

    /**
     * 短时间吐司
     *
     * @param context
     * @param resId   资源id
     */
    public static void showShortToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间吐司
     *
     * @param context
     * @param text
     */
    public static void showShortToast(Context context, String text) {
        if (!TextUtils.isEmpty(text) && !"".equals(text.trim())) {
            showToast(context, text, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 长时间吐司
     *
     * @param context
     * @param resId   资源id
     */
    public static void showLongToast(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_LONG);
    }

    /**
     * 长时间吐司
     *
     * @param context
     * @param text
     */
    public static void showLongToast(Context context, String text) {
        if (!TextUtils.isEmpty(text) && !"".equals(text.trim())) {
            showToast(context, text, Toast.LENGTH_LONG);
        }
    }

    public static void showToast(Context context, int resId, int duration) {
        if (context == null) {
            return;
        }
        if (context != null && context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        String text = context.getString(resId);
        showToast(context, text, duration);

    }

    public static void showToast(Context context, String text, int duration) {
        if (context == null) {
            return;
        }
        if (context != null && context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (!TextUtils.isEmpty(text) && !"".equals(text.trim())) {
//			Toast toast = Toast.makeText(context, text, duration);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.show();
//            Toast.makeText(context, text, duration).show();

            if (mToast == null) {
                mToast = Toast.makeText(context, text, duration);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    //-----------------------------------默认类型 end--------------------------------------
//-----------------------------------自定义类型 star--------------------------------------


    /**
     * 自定义类型
     *
     * @param context
     * @param type    Constants.SUCCESS。。。
     * @param msg
     */
    public static void showShortToast(Context context, String type, String msg) {
        if (!TextUtils.isEmpty(msg) && !"".equals(msg.trim())) {
            showMyToast(context, type, msg,Toast.LENGTH_SHORT);
        }
    }

    /**
     * 自定义类型
     *
     * @param context
     * @param type    Constants.SUCCESS。。。
     * @param resId   资源id
     */
    public static void showShortToast(Context context, String type, int resId) {
        String msg = context.getString(resId);
        if (!TextUtils.isEmpty(msg) && !"".equals(msg.trim())) {
            showMyToast(context, type, msg,Toast.LENGTH_SHORT);
        }
    }

    /**
     * 自定义类型
     *
     * @param context
     * @param type    Constants.SUCCESS。。。
     * @param msg
     */
    public static void showLongToast(Context context, String type, String msg) {
        if (!TextUtils.isEmpty(msg) && !"".equals(msg.trim())) {
            showMyToast(context, type, msg,Toast.LENGTH_LONG);
        }
    }

    /**
     * 自定义类型
     *
     * @param context
     * @param type    Constants.SUCCESS。。。
     * @param resId   资源id
     */
    public static void showLongToast(Context context, String type, int resId) {
        String msg = context.getString(resId);
        if (!TextUtils.isEmpty(msg) && !"".equals(msg.trim())) {
            showMyToast(context, type, msg,Toast.LENGTH_LONG);
        }
    }

    public static void showMyToast(Context context, String type, String text,int duration) {
        if (context == null) {
            return;
        }
        if (context != null && context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (!TextUtils.isEmpty(text) && !"".equals(text.trim())) {
            if (mToast == null) {
                mToast = MyToast.createToast(context, type, text, duration);
            } else {
                mToastMsg.setText(text);
            }
            mToast.show();
        }
    }

//-----------------------------------自定义类型 end--------------------------------------

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}