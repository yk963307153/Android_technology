package com.team.group.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.team.group.R;
import com.team.group.ourlibrary.utils.NetUtils;
import com.team.group.ourlibrary.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;


/**
 * List Fragment 抽象类
 * 需要在子类定义数据类型
 * Created by lee on 16/9/13
 */

public abstract class BaseListFragment<T> extends Fragment {


    /**
     * 下拉
     */
    public static final String REFRESH = "refresh";

    /**
     * 上提
     */
    public static final String LOADMORE = "load_more";

    /**
     * 每页显示数
     */
    public int start = 0;
    /**
     * 当前数据长度
     */
    public int size = 10;
    public String type;

    public String mLoaction = getClass().getName();


    /**
     * List View 适配器
     */
    public BaseListAdapter<T> mAdapter;

    @BindView(R.id.listview)
    protected ListView mListView;
    @BindView(R.id.custom_view)
    public XRefreshView refreshView;
    @BindView(R.id.tv_notice)
    TextView tvContent;
    @BindView(R.id.iv_notice)
    ImageView ivContent;
    public boolean isEnable = true;//是否第一次刷新
    public boolean isFirstRefresh = true;//是否第一次刷新
    public boolean isRefresh = true;//是否下来刷新
    public boolean isLoadmore = true;//是否加载更多

    /**
     * 子类重写获取适配器
     */
    protected abstract BaseListAdapter<T> getListAdapter();


    /**
     * 所有刷新页面用refresh list view；
     */


    private Unbinder unbinder;
    protected Subscription mSubscription;
    public String TAG = getClass().getSimpleName();

    //设置listview边距
    public void setParam(int left, int top, int right, int down) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(left, top, right, down);//4个参数按顺序分别是左上右下

        mListView.setLayoutParams(layoutParams);
    }

    /**
     * 拿到listview,设置属性
     */
    protected ListView getListView() {
        return mListView;
    }

    /**
     * Fragment 可见时操作
     */
    protected void onVisible() {


        mAdapter = getListAdapter();
        mListView.setAdapter(mAdapter);
        setRefresh();

        refreshView.setEnabled(isEnable);
        refreshView.startRefresh();
        refreshView.setPinnedTime(300);
        refreshView.setPullLoadEnable(isRefresh);
        refreshView.setAutoLoadMore(false);
        refreshView.setLoadComplete(false);
        refreshView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                start = 0;
                type = REFRESH;
                if (tvContent != null) {
                    tvContent.setVisibility(View.GONE);
                    ivContent.setVisibility(View.GONE);
                }

                loadServerData();

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                if (!refreshView.mLoadMore) {
                    start += 10;
                    type = LOADMORE;
                    loadServerData();
                }
            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }

    public void setRefresh() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refresh_list_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        onVisible();
        return view;

    }


    @Override
    public void onDestroyView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    protected void showProgress() {
        ((BaseActivity) getActivity()).showLoading();
    }

    protected void hideProgress() {
        ((BaseActivity) getActivity()).hideLoading();
    }

    protected void showToast(String msg) {
        if (!StringUtils.isNullOrEmpty(msg)) {

            ((BaseActivity) getActivity()).ToastShort(msg);
        }

    }


    protected void loadServerData() {
        if (!NetUtils.isNetworkAvailable(getActivity())) {
            refreshView.stopRefresh();
            tvContent.setVisibility(View.VISIBLE);
            ivContent.setVisibility(View.VISIBLE);
//            ivContent.setImageResource(R.mipmap.icon_web_nonentity);
            tvContent.setText("网络错误");
            refreshView.setPullLoadEnable(false);
            return;
        }

        loadData();
    }


    /**
     * 请求成功调用的方法
     */
    protected void setState(List<T> mDataSource, String message) {
        if (refreshView != null) {
            refreshView.setEnabled(true);
            if (type.equals(REFRESH)) {

                refreshView.stopRefresh();
                if (mDataSource.size() == 0) {
                    ivContent.setVisibility(View.VISIBLE);
                    tvContent.setVisibility(View.VISIBLE);
//                    ivContent.setImageResource(R.mipmap.icon_ad_nonentity);
                    tvContent.setText(message);
                } else {
                    tvContent.setVisibility(View.GONE);
                }
                if (mDataSource.size() < size) {
                    refreshView.setPullLoadEnable(false);
                } else {
                    refreshView.setPullLoadEnable(true);
                }

                mAdapter.setData(mDataSource);
                mAdapter.notifyDataSetChanged();
            } else if (type.equals(LOADMORE)) {
                refreshView.stopLoadMore();

                if (mDataSource.size() < size) {
                    refreshView.setPullLoadEnable(false);
                } else {
                    refreshView.setPullLoadEnable(true);
                }
                mAdapter.addData(mDataSource);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 请求失败调用方法
     */
    protected void handleState() {

        if (refreshView != null) {
            refreshView.setEnabled(true);
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText("网络错误");
//            ivContent.setImageResource(R.mipmap.icon_web_nonentity);

            if (type.equals(REFRESH)) {
                refreshView.stopRefresh();
            } else if (type.equals(LOADMORE)) {
                start = 10;
                refreshView.stopLoadMore();
            }
            refreshView.setPullLoadEnable(false);
        }

    }

    /**
     * 调用Service获取数据，
     * 需要在子类中重写
     */
    protected abstract void loadData();


    @Override
    public void onStop() {
        super.onStop();
        if (refreshView != null) {
            refreshView.stopLoadMore();
        }
    }

    @Override
    public void onDestroy() {
        if (refreshView != null) {
            refreshView.stopLoadMore();
        }
        super.onDestroy();
    }
}
