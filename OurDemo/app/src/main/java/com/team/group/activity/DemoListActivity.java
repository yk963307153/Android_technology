package com.team.group.activity;


import com.team.group.R;
import com.team.group.base.BaseTabActivity;
import com.team.group.fragment.Fragmenta;
import com.team.group.fragment.Fragmentb;
import com.team.group.model.PagerInfo;

import java.util.ArrayList;
import java.util.List;

public class DemoListActivity extends BaseTabActivity {


    @Override
    public List<PagerInfo> getPagerList() {
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.color_white));

        mTabTextColorSelector = R.color.tab_orange_selector;

        List<PagerInfo> pagerList = new ArrayList<PagerInfo>();

        PagerInfo listActiveness = new PagerInfo(new Fragmenta(), R.string.fragemnt_a);
        pagerList.add(listActiveness);

        PagerInfo listNumber = new PagerInfo(new Fragmentb(), R.string.fragemnt_b);
        pagerList.add(listNumber);

        return pagerList;
    }

    @Override
    protected void setTitleBar() {
        setLeftBackTitleBar("viewpager页面");
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_group_list;
    }

}
