package com.team.group.activity;


import android.content.Intent;

import com.team.group.R;
import com.team.group.base.BaseActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    //测试拉取下来的项目是否可以正常运行

    @Override
    protected void setupView() {

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
}
