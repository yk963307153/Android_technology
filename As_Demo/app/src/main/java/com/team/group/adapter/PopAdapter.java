package com.team.group.adapter;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team.group.R;

import java.util.List;

import butterknife.BindView;

public class PopAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Spanned> mList;

    public PopAdapter(Context context, List<Spanned> lmist) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mList = lmist;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != mList) {
            count = mList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
//        String person = null;
//        if (null != mList) {
//            person = mList.get(position);
//        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.pop_dropdown_item, null);
            holder = new ViewHolder();
            holder.mText1 = (TextView) convertView.findViewById(R.id.item_pop_tv1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position < mList.size()) {
            holder.mText1.setText(mList.get(position));
        }
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.item_pop_tv1)
        protected TextView mText1;
//        @BindView(R.id.item_pop_tv2)
//        protected TextView mText2;
//        @BindView(R.id.item_pop_tv3)
//        protected TextView mText3;
//        public ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
    }
}