package com.team.demo.activity;


import com.team.demo.R;
import com.team.demo.base.BaseTabActivity;
import com.team.demo.fragment.Fragmenta;
import com.team.demo.fragment.Fragmentb;
import com.team.demo.fragment.Fragmentc;
import com.team.demo.fragment.Fragmentd;
import com.team.demo.model.PagerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * viewpager 带滑动
 */
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


        PagerInfo listFragmentC = new PagerInfo(new Fragmentc(), "未封装");
        pagerList.add(listFragmentC);

        PagerInfo listFragmentD = new PagerInfo(new Fragmentd(), "RecycleView");
        pagerList.add(listFragmentD);

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
