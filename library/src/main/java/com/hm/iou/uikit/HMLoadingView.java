package com.hm.iou.uikit;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hjy on 18/5/2.<br>
 */

public class HMLoadingView extends RelativeLayout {

    private Context mContext;
    private String mDefaultFailMsg;

    private View mLayoutEmpty;
    private View mLayoutFail;
    private View mLayoutLoading;
    private ImageView mIvLoading;

    private OnClickListener mRetryClickListener;

    public HMLoadingView(Context context) {
        this(context, null);
    }

    public HMLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mDefaultFailMsg = mContext.getString(R.string.uikit_network_disconnected);
    }

    public void showDataLoading() {
        if (mLayoutEmpty != null) {
            mLayoutEmpty.setVisibility(View.GONE);
        }
        if (mLayoutFail != null) {
            mLayoutFail.setVisibility(View.GONE);
        }
        if (mLayoutLoading == null) {
            addDataLoadingView();
        }
        mLayoutLoading.setVisibility(View.VISIBLE);
        setVisibility(View.VISIBLE);
        startLoadingAnim();
    }

    /**
     * 显示数据为空
     *
     * @param tips 数据为空的文案
     */
    public void showDataEmpty(CharSequence tips) {
        if (mLayoutEmpty == null) {
            addDataEmptyView();
        }
        mLayoutEmpty.setVisibility(View.VISIBLE);
        if (mLayoutFail != null) {
            mLayoutFail.setVisibility(View.GONE);
        }
        if (mLayoutLoading != null) {
            mLayoutLoading.setVisibility(View.GONE);
            stopLoadingAnim();
        }
        TextView tvEmpty = mLayoutEmpty.findViewById(R.id.tv_data_empty);
        if (tvEmpty != null && !TextUtils.isEmpty(tips)) {
            tvEmpty.setText(tips);
        }
        setVisibility(View.VISIBLE);
    }

    public void showDataEmpty(CharSequence tips, int emptyImgResId, CharSequence extAction, OnClickListener listener) {
        showDataEmpty(tips);
        if (emptyImgResId > 0) {
            ImageView ivEmpty = mLayoutEmpty.findViewById(R.id.iv_data_empty);
            ivEmpty.setImageResource(emptyImgResId);
        }
        TextView tvExtAction = mLayoutEmpty.findViewById(R.id.tv_data_empty_extaction);
        tvExtAction.setText(extAction);
        tvExtAction.setVisibility(View.VISIBLE);
        tvExtAction.setOnClickListener(listener);
    }

    public void showDataEmpty(CharSequence tips, int emptyImgResId, CharSequence extAction, int extActionBgResId, OnClickListener listener) {
        showDataEmpty(tips);
        if (emptyImgResId > 0) {
            ImageView ivEmpty = mLayoutEmpty.findViewById(R.id.iv_data_empty);
            ivEmpty.setImageResource(emptyImgResId);
        }
        TextView tvExtAction = mLayoutEmpty.findViewById(R.id.tv_data_empty_extaction);
        if (extActionBgResId > 0) {
            tvExtAction.setBackgroundResource(extActionBgResId);
        }
        tvExtAction.setText(extAction);
        tvExtAction.setVisibility(View.VISIBLE);
        tvExtAction.setOnClickListener(listener);
    }

    /**
     * 显示数据为空
     *
     * @param viewEmpty 数据为空的文案
     */
    public void showDataEmpty(View viewEmpty) {
        if (mLayoutEmpty == null) {
            addDataEmptyView(viewEmpty);
        }
        mLayoutEmpty.setVisibility(View.VISIBLE);
        if (mLayoutFail != null) {
            mLayoutFail.setVisibility(View.GONE);
        }
        if (mLayoutLoading != null) {
            mLayoutLoading.setVisibility(View.GONE);
            stopLoadingAnim();
        }
        setVisibility(View.VISIBLE);
    }

    /**
     * 显示数据加载失败
     *
     * @param tips     加载失败的文案
     * @param listener 点击重试监听事件
     */
    public void showDataFail(CharSequence tips, OnClickListener listener) {
        mRetryClickListener = listener;
        if (mLayoutEmpty != null) {
            mLayoutEmpty.setVisibility(View.GONE);
        }
        if (mLayoutFail == null) {
            addDataFailView();
        }
        mLayoutFail.setVisibility(View.VISIBLE);
        ((TextView) mLayoutFail.findViewById(R.id.tv_data_fail)).setText(tips);
        if (mLayoutLoading != null) {
            mLayoutLoading.setVisibility(View.GONE);
            stopLoadingAnim();
        }
        setVisibility(View.VISIBLE);
    }

    /**
     * 显示数据加载失败，默认失败提示文案为"糟糕，与外界失联了，快去诊断网络"
     *
     * @param listener
     */
    public void showDataFail(OnClickListener listener) {
        showDataFail(mDefaultFailMsg, listener);
    }

    private void addDataLoadingView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mLayoutLoading = inflater.inflate(R.layout.uikit_view_data_loading, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        addView(mLayoutLoading, params);
        mIvLoading = mLayoutLoading.findViewById(R.id.iv_loading);
    }

    private void addDataEmptyView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mLayoutEmpty = inflater.inflate(R.layout.uikit_view_data_empty, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        addView(mLayoutEmpty, params);
    }

    private void addDataEmptyView(View viewEmpty) {
        mLayoutEmpty = viewEmpty;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        addView(mLayoutEmpty, params);
    }

    private void addDataFailView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mLayoutFail = inflater.inflate(R.layout.uikit_view_data_fail, null);
        mLayoutFail.findViewById(R.id.tv_data_fail_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryClickListener != null) {
                    mRetryClickListener.onClick(v);
                }
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        addView(mLayoutFail, params);

    }

    public void startLoadingAnim() {
        AnimationDrawable animationDrawable = (AnimationDrawable) mIvLoading.getDrawable();
        animationDrawable.start();
    }

    public void stopLoadingAnim() {
        AnimationDrawable animationDrawable = (AnimationDrawable) mIvLoading.getDrawable();
        animationDrawable.stop();
    }

}
