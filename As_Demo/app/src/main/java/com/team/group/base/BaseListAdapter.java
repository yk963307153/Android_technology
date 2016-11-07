package com.team.group.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * ListView 适配器基类
 * Created by lee on 16/9/13
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected int mItemLayoutId;
    public int mPosition;
    /**
     * 数据源
     */
    protected List<T> mDataSource = new ArrayList<T>();

    public BaseListAdapter(Context mContext, List<T> mDataSource, int mItemLayoutId) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mDataSource = mDataSource;
        this.mContext = mContext;
        this.mItemLayoutId = mItemLayoutId;
    }

    public int getDataSize() {
        return mDataSource.size();
    }

    public void setData(List<T> data) {
        mDataSource = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mDataSource == null ? (mDataSource = new ArrayList<T>()) : mDataSource;
    }


    public void addData(List<T> data) {
        if (mDataSource != null && data != null && !data.isEmpty()) {
            mDataSource.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addItem(T obj) {
        if (mDataSource != null) {
            mDataSource.add(obj);
        }
        notifyDataSetChanged();
    }

    public void addItem(int pos, T obj) {
        if (mDataSource != null) {
            mDataSource.add(pos, obj);
        }
        notifyDataSetChanged();
    }

    public void removeItem(Object obj) {
        mDataSource.remove(obj);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mDataSource.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataSource.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return getDataSize();
    }

    @Override
    public T getItem(int position) {
        if (mDataSource.size() > position) {
            return mDataSource.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //实例化一个ViewHolder
        ViewHolder holder = getViewHolder(position, convertView, parent);
        mPosition = position;
        //使用对外公开的convert方法，通过ViewHolder把View找到，通过Item设置值
        convert(position,holder, getItem(position));
        return holder.getmConvertView();
    }

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.getHolder(mContext, mItemLayoutId, convertView, parent);
    }

    /**
     * 对外公布了一个convert方法，并且还把ViewHolder和本item对应的Bean对象给传出去
     * 现在convert方法里面需要干嘛呢？通过ViewHolder把View找到，通过Item设置值
     */
    public abstract void convert(int position,ViewHolder holder, T item);
}
