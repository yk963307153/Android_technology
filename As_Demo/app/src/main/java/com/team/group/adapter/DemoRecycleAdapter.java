package com.team.group.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.team.group.R;
import com.team.group.ViewHolder.DemoViewHolder;
import com.team.group.model.DemoDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TranGuility on 16/11/12.
 */

public class DemoRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;

    private List<DemoDataModel> mList = new ArrayList<>();

    public DemoRecycleAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case DemoDataModel.TYPE_ONR:
                //参数不固定 自行摸索
                return new DemoViewHolder(mLayoutInflater.inflate(R.layout.item_recycle_a, parent, false));
            case DemoDataModel.TYPE_TWO:
            case DemoDataModel.TYPE_THREE:
                return new DemoViewHolder(mLayoutInflater.inflate(R.layout.item_recycle_b, parent, false));
            default:
                return new DemoViewHolder(mLayoutInflater.inflate(R.layout.item_recycle_a, parent, false));
        }
//        return null;
    }

    public void addlist(List<DemoDataModel> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DemoViewHolder)holder).bindHolder(mList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
