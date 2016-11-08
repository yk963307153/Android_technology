package com.team.group.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.team.group.R;
import com.team.group.adapter.GroupActiveAdapter;
import com.team.group.api.response.GroupResponse;
import com.team.group.base.BaseListAdapter;
import com.team.group.base.BaseListFragment;
import com.team.group.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;


public  class Fragmentb extends BaseListFragment<GroupResponse> {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            DialogUtils.errorDialog(getActivity(), "第二个页面出现在屏幕!");
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    private List<GroupResponse> mResponses = new ArrayList<>();

    @Override
    protected BaseListAdapter<GroupResponse> getListAdapter() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogUtils.successDialog(getActivity(), "页面2");
            }
        });

        return new GroupActiveAdapter(getActivity(), mResponses, R.layout.item_group);
    }

    @Override
    protected void loadData() {
        List<GroupResponse> groupResponses = new ArrayList<>();
        int a = start + 10;
        while (a > start) {
            GroupResponse re = new GroupResponse();
            re.setName("页面2:---");
            groupResponses.add(re);
            a--;
        }
        setState(groupResponses, "暂无信息");
    }

}
