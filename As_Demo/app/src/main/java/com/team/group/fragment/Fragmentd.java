package com.team.group.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.team.group.R;
import com.team.group.adapter.DemoRecycleAdapter;
import com.team.group.base.BaseFragment;
import com.team.group.model.DemoDataModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Recycle实现聊天等多样式
 */
public class Fragmentd extends BaseFragment {

    @BindView(R.id.recycle)
    RecyclerView recyclerView;

    private DemoRecycleAdapter mAdapter;

    int colors[] = {android.R.color.holo_red_dark,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,};

    @Override
    protected void initView() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//设置布局管理器
//        recyclerView.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
//        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new DemoRecycleAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        initDate();
    }

    private void initDate() {
        List<DemoDataModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int type = (int) ((Math.random() * 3) + 1);
            DemoDataModel data = new DemoDataModel();

            data.avatarColor = colors[type - 1];
            data.type = type;
            data.name = "name :" + i;
            data.content = "coenten :" + i;
            data.contentColor = colors[(type + 1) % 3];
            list.add(data);
        }

        mAdapter.addlist(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int setResID() {
        return R.layout.recycle_view;
    }

}
