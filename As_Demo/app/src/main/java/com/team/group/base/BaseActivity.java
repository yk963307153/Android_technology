package com.team.group.base;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team.group.R;
import com.team.group.ourlibrary.utils.SystemBarTintManager;
import com.team.group.utils.DialogUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Subscription;

/**
 * Created by TranGuility on 16/11/7.
 */

public abstract class BaseActivity extends SwipeBackActivity {

    public String TAG = getClass().getSimpleName();
    public String mLoaction = getClass().getName();

    private View mLoadingView;
    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;
    protected static final int REQUEST_TAKE_PHOTO = 111;
    protected static final int IMAGE_PICKER_SELECT = 222;
    protected static final int PHOTO_REQUEST_CUT = 333;
    public Subscription mSubscription;
    protected SystemBarTintManager tintManager;
    private Uri imgUri;
    public Dialog dialog;

    RelativeLayout rlRight;
    RelativeLayout rlLeft;
    public ActionBar actionBar;
    public TextView tvTitle;
    TextView tvLeft;
    TextView tvRight;
    public ImageView ivLeft;
    ImageView ivRight;

    public LinearLayout llBar;

    private Toast mToast = null;


    /**
     * 所有已存在的Activity
     */
    protected static ConcurrentLinkedQueue<Activity> allActivity = new ConcurrentLinkedQueue<Activity>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResID());
        // 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明底部导航栏
//            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//            tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            //设置状态栏与标题栏颜色保持一致
//            tintManager.setStatusBarTintResource(R.color.color_white);
//        }
        getSwipeBackLayout().setEdgeSize(100);//设置滑动返回的范围
        ButterKnife.bind(this);
        allActivity.add(this);
        initActionbar();
        setupView();
        setTitleBar();
    }

    private void initActionbar() {
        View view = getLayoutInflater().inflate(R.layout.toolbar_layout, null);
        llBar = (LinearLayout) view.findViewById(R.id.toolbar_main);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvLeft = (TextView) view.findViewById(R.id.tv_left);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        ivLeft = (ImageView) view.findViewById(R.id.iv_left);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        rlLeft = (RelativeLayout) view.findViewById(R.id.rl_left);
        rlRight = (RelativeLayout) view.findViewById(R.id.rl_right);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(view, params);
        rlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftAction();
            }
        });
        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightAction();
            }
        });

    }

    //左侧无 中间标题 右侧无
    public void setTitleBar(int title) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.GONE);
        tvLeft.setVisibility(View.GONE);
        tvTitle.setText(getString(title));
    }

    //左侧返回 中间标题 右侧无
    public void setLeftBackTitleBar(int title) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setImageResource(R.mipmap.icon_back);
        tvTitle.setText(getString(title));
    }

    //左侧返回 中间标题 右侧无
    public void setLeftBackTitleBar(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setImageResource(R.mipmap.icon_back);
        tvTitle.setText(title);
    }

    //左侧图片 中间标题 右侧无
    public void setLeftImgTitleBar(int title, int lImg) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setImageResource(lImg);
        tvTitle.setText(getString(title));
    }

    //左侧图片 中间标题 右侧无
    public void setLeftImgTitleBar(String title, int lImg) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setImageResource(lImg);
        tvTitle.setText(title);
    }

    //左侧返回 中间标题 右侧文字
    public void setLeftBackRightTxtTitleBar(int title, int rTxt) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setImageResource(R.mipmap.icon_back);
        tvTitle.setText(getString(title));
        tvRight.setText(getString(rTxt));
    }

    //左侧图片 中间标题 右侧图片
    public void setLeftImgRightImgTitleBar(int lImg, int title, int rImg) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setImageResource(lImg);
        tvTitle.setText(getString(title));
        ivRight.setImageResource(rImg);
    }

    //左侧返回 中间标题 右侧图片
    public void setLeftBackRightImgTitleBar(int title, int rImg) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setImageResource(R.mipmap.icon_back);
        tvTitle.setText(getString(title));
        ivRight.setImageResource(rImg);
    }

    //左侧返回 中间标题 右侧图片
    public void setLeftBackRightImgTitleBar(String title, int rImg) {
        tvTitle.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.GONE);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setImageResource(R.mipmap.icon_back);
        tvTitle.setText(title);
        ivRight.setImageResource(rImg);
    }

    public void rightAction() {

    }

    public void leftAction() {//只需要返回的情况下不需要覆写
        finish();

    }

    protected abstract void setupView();

    public abstract int setLayoutResID();

    protected abstract void setTitleBar();

    @Override
    protected void onDestroy() {
        if (null != mSubscription) {
            mSubscription.unsubscribe();
        }
        if (mToast != null) {
            mToast.cancel();
        }
        super.onDestroy();
    }


    /**
     * 结束所有activity
     */
    public static void finishAll() {
        // 结束Activity
        for (Activity act : allActivity) {
            act.finish();
        }
    }


    public void toast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public void showLoading() {
        dialog = DialogUtils.createLoadingDialog(this, true);
    }

    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
