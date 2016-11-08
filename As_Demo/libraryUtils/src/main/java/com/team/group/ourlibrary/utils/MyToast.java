package com.team.group.ourlibrary.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team.group.ourlibrary.R;


public class MyToast {
    protected static Toast mToast;
    protected static int resource;
    public static TextView mToastMsg;

    public static Toast createToast(Context context, String type, String msg, int time) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.toast_layout, null);
        mToastMsg = (TextView) v.findViewById(R.id.tv_toast_view);
        LinearLayout llView = (LinearLayout) v.findViewById(R.id.ll_toast_view);
        mToastMsg.setText(msg);
        switch (type) {
            case Constants.SUCCESS:
                resource = R.drawable.shape_toast_blue;
                break;
            case Constants.WARM:
                resource = R.drawable.shape_toast_yellow;
                break;
            case Constants.ERROR:
                resource = R.drawable.shape_toast_red;
                break;
            case Constants.NORMAL:
                resource = R.drawable.shape_toast_grey;
                break;

        }
        llView.setBackgroundResource(resource);
        mToast = new Toast(context);
        mToast.setDuration(time);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setView(v);
//        mToast.show();
        return mToast;
    }
}
