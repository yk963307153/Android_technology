package com.team.group.base;


import android.support.v4.view.ViewPager;

import com.team.group.R;
import com.team.group.adapter.ViewPagerAdapter;
import com.team.group.model.PagerInfo;
import com.team.group.widegt.SlidingTabLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Ocean on 6/26/16
 */
public abstract class BaseTabActivity extends BaseActivity {
    protected List<PagerInfo> mPagerList;

    protected ViewPagerAdapter mAdapter;

    @BindView(R.id.vPager)
    protected ViewPager mViewPager;

    @BindView(R.id.sliding_tabs)
    protected SlidingTabLayout mSlidingTabLayout;

    protected int mTabTextColorSelector;

    public abstract List<PagerInfo> getPagerList();

    @Override
    protected void setupView() {
        setupPager();
    }

    public void setupPager() {
        mPagerList = getPagerList();
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mPagerList);
        mViewPager.setAdapter(mAdapter);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setTabTextColorSelector(mTabTextColorSelector);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.fr_tab_indicator));
        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_title);
        mSlidingTabLayout.setViewPager(mViewPager);

        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onSlidingTabPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                onSlidingTabPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                onSlidingTabPageScrollStateChanged(state);
            }
        });
    }

    public void onSlidingTabPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onSlidingTabPageSelected(int position) {
    }

    public void onSlidingTabPageScrollStateChanged(int state) {

    }

}
