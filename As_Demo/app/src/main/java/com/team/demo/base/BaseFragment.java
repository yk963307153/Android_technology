package com.team.demo.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * Created by lee on 16/9/13
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    protected Subscription mSubscription;
    public String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setResID(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;

    }


    protected abstract void initView();
    protected abstract int setResID();


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

    protected void showProgress(boolean isCancle, String msg) {
        ((BaseActivity) getActivity()).showLoading(isCancle, msg);
    }

    protected void hideProgress() {
        ((BaseActivity) getActivity()).hideLoading();
    }

    protected void showShortToast(String msg) {
        ((BaseActivity) getActivity()).ToastShort(msg);
    }

    protected void showShortToast(int resId) {
        ((BaseActivity) getActivity()).ToastShort(resId);
    }

}
