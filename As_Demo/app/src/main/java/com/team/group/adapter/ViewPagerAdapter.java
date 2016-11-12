package com.team.group.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.team.group.App;
import com.team.group.model.PagerInfo;
import com.team.group.ourlibrary.utils.StringUtils;

import java.util.List;

/**
 * 分页的Adapter
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<PagerInfo> mDataSource;

    public ViewPagerAdapter(FragmentManager fm, List<PagerInfo> dataSource) {
        super(fm);
        mDataSource = dataSource;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < mDataSource.size()) {
            return mDataSource.get(position).getFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (StringUtils.isNullOrEmpty(mDataSource.get(position).getmTitle()))
            return  App.getAppContext().getString(mDataSource.get(position).getTitleResId());
        else
            return mDataSource.get(position).getmTitle();
    }
}
