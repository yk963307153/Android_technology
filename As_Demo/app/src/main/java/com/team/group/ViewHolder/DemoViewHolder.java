package com.team.group.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.group.R;
import com.team.group.model.DemoDataModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 一种item对应一个viewHolder(以后要写个抽象BaseViewHolder在onBindViewHolder中使用)
 * 以前adapter中写的ViewHolder在RecyclerView里作为内置
 * Created by TranGuility on 16/11/12.
 */

public class DemoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)
    protected TextView mName;
    @BindView(R.id.avator)
    protected ImageView mAvator;

    public DemoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindHolder(DemoDataModel model) {
        //取得是R文件的id 颜色值赋的是R文件里的id
//       error: mAvator.setBackground(model.avatarColor); 取得是资源文件id
        mAvator.setBackgroundResource(model.avatarColor);
        mName.setText(model.name);
    }
}
