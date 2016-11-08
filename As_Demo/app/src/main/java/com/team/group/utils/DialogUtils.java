package com.team.group.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team.group.ourlibrary.R;


/**
 * 弹出 loading  确认框等
 * 灵活领比较大 未写到依赖中 可扩展行比较大
 */
public class DialogUtils {

    //dialog中不能使用
//    private static Context mContext = App.getAppContext();

    //系统等待提示框
    private static ProgressDialog pd;
    //自定义等待提示框
    private static Dialog dialog;

    private static TextView vLoading_text;
    private static String mMsg = "正在加载···";

    static AnimationDrawable frameAnim;


//==================================================================

    /**
     * 默认提示无标题加载框（Loading。。。）返回不可用
     *
     * @param getActivity
     * @param title
     * @param content
     */
    public static void showLoaingDialog(Context getActivity, String title, String content) {
        pd = new ProgressDialog(getActivity);
        pd.setTitle(title);
        pd.setMessage(content);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        // pd = ProgressDialog.show(getActivity, title, content);

    }

    /**
     * 自定义提示无标题加载框
     * 默认文字为：正在加载···
     *
     * @param context
     */
    public static void showLoaingDialog(final Context context) {
        showLoaingDialog(context, mMsg);
    }

    /**
     * 自定义提示加载框
     *
     * @param context
     * @param mMsgs   提示内容
     */
    public static void showLoaingDialog(final Context context, String mMsgs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_loading, null);
        vLoading_text = (TextView) view.findViewById(R.id.loading_text);
        vLoading_text.setText(mMsgs);
        if (null == dialog) {
            DialogUtils.dialog = new Dialog(context, R.style.MyLoadDialog);
            //空白是否退出
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view);
            //返回键是否退出
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ((Activity) context).finish();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogs) {
                    dialog = null;
                }
            });
            dialog.show();
        }
    }
//==================================================================

    /**
     * 提示
     *
     * @param context 上下文
     * @param tiele   标题
     * @param content 正文
     * @param tips    按钮内容
     */
    public static void backDialog(Context context, String tiele, String content,
                                  String tips) {
        // TODO Auto-generated method stub
        // // THEME_DEVICE_DEFAULT_LIGHT THEME_HOLO_LIGHT
        new AlertDialog.Builder(context).setTitle(tiele).setMessage(content)
                // .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton(tips, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

    /**
     * 错误提示
     *
     * @param title 提示内容无标题
     */
    public static void errorDialog(Context context, String title) {
        // TODO Auto-generated method stub
        // // THEME_DEVICE_DEFAULT_LIGHT THEME_HOLO_LIGHT
        new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

    // new
    // AlertDialog.Builder(MainActivity.this).setTitle("您选择的是").setMessage(ts.trim().substring(0,
    // ts.length()-1).toString()).setPositiveButton("关闭", null).show();

    /**
     * 成功提示
     *
     * @param title 提示标题
     */
    public static void successDialog(final Context context, String title) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                })
                .setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // 点击“确认”后的操作
                                ((Activity) context).finish();
                            }
                        }).show();

    }
//==================================================================

    //============================自定义======================================

    /**
     * 建议使用这一种 动画等内容可灵活自定义
     *
     * @param context
     * @param flag    返回是否可以取消
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg, boolean flag) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_load, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        spaceshipImage.setImageResource(R.drawable.play_loading);
        frameAnim = (AnimationDrawable) spaceshipImage
                .getDrawable();
        frameAnim.start();
//        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                context, );
        // 使用ImageView显示动画

        tipTextView.setText(msg);// 设置加载信息
//        if (dialog == null) {
        dialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        dialog.setCancelable(false);// 不可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(flag);//false 不可取消
        dialog.show();
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
//        }
        return dialog;

    }

    /**
     * 关闭自定义加载框
     */
    public static void closeDialog() {
        try {
            if (null != dialog) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * 关闭系统加载框
     */
    public static void closeMethod() {
        try {
            if (null != pd) {
                pd.dismiss();
            }
        } catch (Exception e) {
        }
    }

    public interface ClickListenerInterface {
        void doConfirm();

    }

}
