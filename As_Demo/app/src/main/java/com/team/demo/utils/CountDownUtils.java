package com.team.demo.utils;

import android.content.Context;
import android.os.Build;
import android.widget.Button;

import com.team.demo.R;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by TranGuility on 16/11/8.
 */

public class CountDownUtils {

    //获取验证码倒计时
    public static void startCoutDown(final Context context, final Button mBtnSendSmsCode) {
        RxCountDown.countDown(6).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mBtnSendSmsCode.setText(String.format("(%s)秒后重试", 6));

                if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mBtnSendSmsCode.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shap_gray));
                } else {
                    mBtnSendSmsCode.setBackground(context.getResources().getDrawable(R.drawable.shap_gray));
                }

            }
        }).subscribe(new Subscriber<Integer>() {

            @Override
            public void onCompleted() {
                mBtnSendSmsCode.setText("获取验证码");

                if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mBtnSendSmsCode.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shap_orange));
                } else {
                    mBtnSendSmsCode.setBackground(context.getResources().getDrawable(R.drawable.shap_orange));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                mBtnSendSmsCode.setText(String.format("(%s)秒后重试", integer));
            }
        });
    }
}
