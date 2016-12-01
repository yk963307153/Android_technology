package com.team.demo.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.team.demo.R;
import com.team.demo.ourlibrary.utils.ToastUtils;
import com.team.demo.ourlibrary.widget.ContainsEmojiEditText;
import com.team.demo.utils.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionSuccess;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.id_navigationview)
    NavigationView idNavigationview;

    @BindView(R.id.activity_main)
    DrawerLayout drawerlayoutHome;

    @BindView(R.id.emjet_demo)
    ContainsEmojiEditText emjetDemo;


    private Handler handler = new Handler();
    private int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // applyKitKatTranslucency();
        initNavigationView();
//        setPerMiss();
//每隔一秒运行一次
        handler.postDelayed(myRunnable, 1000);
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1);
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            emjetDemo.setText("update Threafd");
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//        new FileDownloadThread().start();
        ;
    }

    private MyRunnable myRunnable = new MyRunnable();

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            index++;
            index = index % 3;
            emjetDemo.setText(String.valueOf(index));
            handler.postDelayed(myRunnable, 1000);
        }
    }

//
//    public class FileDownloadThread extends Thread {
//        @Override
//        public void run() {
//            Message message = new Message();
//            message.what = 1;
//            myHandler.sendMessage(message);
//        }
//    }
//
//    Handler myHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            if (1 == msg.what) {
////                ...
//            }
//            super.handleMessage(msg);
//        }
//    };
/*
    @UiThread
    protected void uirefreh() {
    }*/

    /**
     * 侧滑
     */
    private void initNavigationView() {

        toolbar.setBackgroundColor(ThemeUtils.getToolBarColor());

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerlayoutHome, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();

        idNavigationview.inflateHeaderView(R.layout.header_nav);
        View headerView = idNavigationview.getHeaderView(0);
        CircleImageView sdvHeader = (CircleImageView) headerView.findViewById(R.id.sdv_avatar);
        sdvHeader.setImageResource(R.drawable.ic_avtar);
        idNavigationview.inflateMenu(R.menu.menu_nav);
        idNavigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_feedback:
                        ToastUtils.showShortToast(MainActivity.this, "测试");
                        break;
                    case R.id.nav_menu_setting:
                        ToastUtils.showShortToast(MainActivity.this, "测试2");
                        // do something
                        break;
                    default:
                        break;

                }
                return false;
            }
        });
    }


    @OnClick({R.id.bt_home, R.id.bt_handler})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_home:
                startActivity(new Intent(MainActivity.this, DemoListActivity.class));
            case R.id.bt_handler:
                startActivity(new Intent(MainActivity.this, HandlerThreadActivity.class));
            default:
                ToastUtils.showShortToast(this, "超出范围");
                break;
        }
    }


//    private void setPerMiss() {
//        PermissionGen.with(this)
//                .addRequestCode(100)
//                .permissions(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.RECORD_AUDIO,
//                        Manifest.permission.CAMERA
//                )
//                .request();
//    }
    /*
    activity中使用
     getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, chatFragment)
                .commitAllowingStateLoss();

                  // 拍照
                    String[] needPermissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    PermissionGen.needPermission(getActivity(), 100, needPermissions);

                  // 图库选择图片

                    PermissionGen.needPermission(getActivity(), 200, Manifest.permission.WRITE_EXTERNAL_STORAGE);
     */


    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        ToastUtils.showShortToast(this, "权限成功回调");
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        {
            ToastUtils.showShortToast(this, "未权限成功回调");
        }

    }
}