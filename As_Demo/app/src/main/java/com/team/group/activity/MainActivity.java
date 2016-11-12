package com.team.group.activity;


import android.content.Intent;

import com.team.group.R;
import com.team.group.base.BaseActivity;

import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends BaseActivity {
    //测试拉取下来的项目是否可以正常运行
    @Override
    protected void setupView() {
        setSwipeBackEnable(false);//设置不能滑动返回
//        setPerMiss();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void setTitleBar() {
        {
            setTitleBar("主页面");
        }
    }

    @OnClick(R.id.bt_home)
    public void onClick() {
        startActivity(new Intent(MainActivity.this, DemoListActivity.class));
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
        showShorToast("权限成功回调");
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        showShorToast("未权限成功回调");
    }

}
