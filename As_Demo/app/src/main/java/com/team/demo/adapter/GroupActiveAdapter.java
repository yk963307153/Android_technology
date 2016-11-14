package com.team.demo.adapter;

import android.content.Context;

import com.team.demo.R;
import com.team.demo.api.response.GroupResponse;
import com.team.demo.base.BaseListAdapter;
import com.team.demo.base.ViewHolder;

import java.util.List;


/**
 * 牛圈-adapter
 *
 * @author: Team Numbers_Li
 */
public class GroupActiveAdapter extends BaseListAdapter<GroupResponse> {


    public GroupActiveAdapter(Context mContext, List<GroupResponse> mDataSource, int mItemLayoutId) {
        super(mContext, mDataSource, mItemLayoutId);
    }


    @Override
    public void convert(int position, ViewHolder holder, final GroupResponse response) {
        holder.setTextView(R.id.item_group_name, response.getName() + position);
    }
}
