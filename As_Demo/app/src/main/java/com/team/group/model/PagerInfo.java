package com.team.group.model;

import android.support.v4.app.Fragment;

public class PagerInfo {
    private Fragment fragment;
    private int titleResId;
    private String mTitle = "";

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public void setTitleResId(int titleResId) {
        this.titleResId = titleResId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public PagerInfo(Fragment fragment, int titleResId) {
        this.fragment = fragment;
        this.titleResId = titleResId;
    }

    public PagerInfo(Fragment fragment, String titleResId) {
        this.fragment = fragment;
        this.mTitle = titleResId;
    }
}
