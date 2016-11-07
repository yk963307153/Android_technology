package com.team.group.model;

/**
 * Created by Ocean on 6/20/16.
 */
public class TabMenu {
    private int mIndex;
    private int mResourceForTitle;
    private int mResourceForSelector;
    private Class<?> mCls;

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public int getResourceForTitle() {
        return mResourceForTitle;
    }

    public void setResourceForTitle(int mResourceForTitle) {
        this.mResourceForTitle = mResourceForTitle;
    }

    public int getResourceForSelector() {
        return mResourceForSelector;
    }

    public void setResourceForSelector(int mResourceForSelector) {
        this.mResourceForSelector = mResourceForSelector;
    }

    public Class<?> getCls() {
        return mCls;
    }

    public void setCls(Class<?> mCls) {
        this.mCls = mCls;
    }

    public TabMenu(int mIndex, int mResourceForTitle, int mResourceForSelector, Class<?> mCls) {
        this.mIndex = mIndex;
        this.mResourceForTitle = mResourceForTitle;
        this.mResourceForSelector = mResourceForSelector;
        this.mCls = mCls;
    }
}
