package com.team.group.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lee on 16/9/14
 */


    public class ViewHolder {

        private SparseArray<View> mViews;
        private View mConvertView;

        //构造函数
        private ViewHolder(Context context, int resLayoutId, ViewGroup parent) {
            this.mViews = new SparseArray<View>();
            this.mConvertView = LayoutInflater.from(context).inflate(resLayoutId, parent, false);
            this.mConvertView.setTag(this);
        }

        //获取一个ViewHolder
        public static ViewHolder getHolder(Context context, int resLayoutId, View convertView, ViewGroup parent) {
            if (convertView == null) {
                return new ViewHolder(context, resLayoutId, parent);
            }
            return (ViewHolder) convertView.getTag();
        }

        //通过控件的id获取对应的控件，如果没有则加入mViews;记住 <T extends View> T 这种用法
        public <T extends View> T getItemView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        //获得一个convertView
        public View getmConvertView() {
            return mConvertView;
        }

        /**
         * 为TextView赋值
         */
        public ViewHolder setTextView(int viewId, String text) {
            TextView view = getItemView(viewId);
            view.setText(text);
            return this;
        }

        /**
         * 为ImageView赋值——drawableId
         */
        public ViewHolder setImageResource(int viewId, int drawableId) {
            ImageView view = getItemView(viewId);
            view.setImageResource(drawableId);
            return this;
        }

        /**
         * 为ImageView赋值——bitmap
         */
        public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
            ImageView view = getItemView(viewId);
            view.setImageBitmap(bitmap);
            return this;
        }

    }


