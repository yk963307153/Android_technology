package com.team.demo.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.team.demo.R;
import com.team.demo.adapter.GroupActiveAdapter;
import com.team.demo.api.response.GroupResponse;
import com.team.demo.base.BaseListAdapter;
import com.team.demo.base.BaseListFragment;
import com.team.demo.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 开发中用到的listview
 */
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
