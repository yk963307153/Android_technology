package com.team.group.fragment;

import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.team.group.R;
import com.team.group.adapter.PopAdapter;
import com.team.group.api.ServiceGenerator;
import com.team.group.api.click.DemoClick;
import com.team.group.api.response.APIErrorResponse;
import com.team.group.api.response.FilmDetail;
import com.team.group.base.BaseFragment;
import com.team.group.ourlibrary.utils.MD5Utils;
import com.team.group.ourlibrary.utils.StringUtils;
import com.team.group.utils.CountDownUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.team.group.R.id.bt_check;

;


/**
 * 一个普通的fragment
 */
public class Fragmenta extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.ed_check)
    EditText mTVReply;

    @BindView(bt_check)
    Button mbtn;

    private PopAdapter mAdapter;

    private static PopupWindow popView;
//https://developers.douban.com/wiki/?title=movie_v2   api

    @Override
    protected void initView() {
        tvTitle.append("\n" + MD5Utils.getMD5Convert("a"));
        tvTitle.append("\n" + MD5Utils.sha1("a".getBytes()));
        //1秒钟之内只取一个点击事件，防抖操作
//        RxView.clicks(mbtn).debounce(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                showShortToast("1231231");
//            }
//        });
        mTVReply.setText("26770773");
        RxView.clicks(mTVReply).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // 此处为得到焦点时的处理内容
                List<Spanned> mList = new ArrayList<Spanned>();
                mList.add(Html.fromHtml("<font color='#FF551B'>" + "3025375" + "</FONT>"));
                mList.add(Html.fromHtml("<font color='#FF551B'>" + "22266320" + "</FONT>"));
                mList.add(Html.fromHtml("<font color='#FF551B'>" + "25983044" + "</FONT>"));
                mList.add(Html.fromHtml("<font color='#FF551B'>" + "25921812" + "</FONT>"));
                mList.add(Html.fromHtml("<font color='#FF551B'>" + "26598021" + "</FONT>"));

                setAdvisers(mTVReply, mList);
            }
        });

    }

    @Override
    protected int setResID() {
        return R.layout.fragment_a;
    }


    private void demoClick() {

        DemoClick client = ServiceGenerator.createRetrofitService(DemoClick.class);
        Observable<FilmDetail> observable = client.getFilmDetail(mTVReply.getText().toString());
        mSubscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FilmDetail>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        APIErrorResponse error = ServiceGenerator.handle(e);
                        if (!StringUtils.isNullOrEmpty(error.getMsg())) {
                            showShortToast(error.getMsg());
                        }

                    }

                    @Override
                    public void onNext(FilmDetail aVoid) {
                        //这里只做部分解析
                        String str = "电影名称:" + aVoid.getOriginal_title() + "\n简介：" + aVoid.getSummary();
                        int fstart = str.indexOf(":");
                        int fend = str.length();
                        SpannableStringBuilder style = new SpannableStringBuilder(str);
//                        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_orange)), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_orange)), 0, fstart + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                        tvTitle.setText(style);
                    }
                });
    }


    /**
     * pop下拉
     */
    private void setAdvisers(final EditText edText, final List<Spanned> list) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.pop_dropdown_list, null);
        ListView mListview = (ListView) view.findViewById(R.id.lvResults);

        mAdapter = new PopAdapter(getActivity(), list);

        mListview.setAdapter(mAdapter);
//        edText.getWidth()

        popView = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, true);

        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popView.setBackgroundDrawable(dw);
        popView.showAsDropDown(edText);

        //popwindow中list点击
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String str = list.get(position).toString();
                edText.setText(str);
                popView.dismiss();
            }
        });
    }


    @OnClick(bt_check)
    protected void btClick() {
        CountDownUtils.startCoutDown(getActivity(), mbtn);
        demoClick();
    }

}
