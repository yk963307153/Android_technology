package com.team.group.activity;


import android.Manifest;
import android.content.Intent;

import com.team.group.R;
import com.team.group.base.BaseActivity;

import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends BaseActivity {
    //测试拉取下来的项目是否可以正常运行
    @Override
    protected void setupView() {
        setPerMiss();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void setTitleBar() {
        {
            setLeftBackTitleBar("主页面");
        }
    }

    @OnClick(R.id.bt_home)
    public void onClick() {
        startActivity(new Intent(MainActivity.this, DemoListActivity.class));
    }


    private void setPerMiss() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA
                )
                .request();
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        showShorToast("权限成功回调");
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        showShorToast("未权限成功回调");
    }

}
