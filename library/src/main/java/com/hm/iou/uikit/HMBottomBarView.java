package com.hm.iou.uikit;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hjy on 2018/10/10.
 */

public class HMBottomBarView extends RelativeLayout implements View.OnClickListener {

    public interface OnBackClickListener {
        void onClickBack();
    }

    public interface OnTitleClickListener {
        void onClickTitle();
    }

    private Context mContext;
    private String mTitleTextStr;
    private int mTitleTextSize;
    private Drawable mBackDrawable;
    private int mIconWidth;
    private boolean mEnable;

    private ImageView mIvBack;
    private TextView mTvTitle;

    private OnBackClickListener mListener;
    private OnTitleClickListener mTitleClickListener;

    public HMBottomBarView(Context context) {
        this(context, null);
    }

    public HMBottomBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMBottomBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HmBottomBar);
        //兼容以前的版本，属性名暂不改，TODO 后续需要修正
        mTitleTextStr = ta.getString(R.styleable.HmBottomBar_bottomTitleText);
        mTitleTextSize = ta.getDimensionPixelSize(R.styleable.HmBottomBar_bottomTitleSize,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics())));
        mBackDrawable = ta.getDrawable(R.styleable.HmBottomBar_bottomBackIcon);
        mIconWidth = ta.getDimensionPixelSize(R.styleable.HmBottomBar_bottomIconWidth, 0);
        mEnable = ta.getBoolean(R.styleable.HmBottomBar_enable, true);
        ta.recycle();

        init(context);
    }

    private void init(Context context) {
        mContext = context;
        float density = mContext.getResources().getDisplayMetrics().density;

        mIvBack = new ImageView(context);
        mIvBack.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LayoutParams params = new LayoutParams(mIconWidth != 0 ? mIconWidth : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mBackDrawable != null) {
            mIvBack.setImageDrawable(mBackDrawable);
        } else {
            mIvBack.setImageResource(R.mipmap.uikit_ic_menu_back_black);
        }
        mIvBack.setBackgroundResource(R.drawable.uikit_bg_item_ripple);
        mIvBack.setPadding((int) (density * 18), 0, (int) (density * 15), 0);
        addView(mIvBack, params);

        mTvTitle = new TextView(context);
        mTvTitle.setGravity(Gravity.CENTER);
        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (density * 30));
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.rightMargin = (int) (density * 12);
        mTvTitle.setText(mTitleTextStr);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mTvTitle.setTextColor(getResources().getColorStateList(R.color.uikit_selector_btn_main));
        mTvTitle.setBackgroundResource(R.drawable.uikit_selector_btn_main_small);
        int pad = (int) (density * 10);
        mTvTitle.setPadding(pad, 0, pad, 0);
        addView(mTvTitle, params);

        mTvTitle.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        setBackgroundColor(Color.WHITE);
        setEnabled(mEnable);
    }

    public void updateTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setOnBackClickListener(OnBackClickListener listener) {
        mListener = listener;
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mTitleClickListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mTvTitle.setEnabled(enabled);
    }

    @Override
    public void onClick(View v) {
        if (v == mTvTitle) {
            if (mTitleClickListener != null) {
                mTitleClickListener.onClickTitle();
            }
        } else if (v == mIvBack) {
            if (mListener != null) {
                mListener.onClickBack();
                return;
            }
            if (mContext != null && mContext instanceof Activity) {
                ((Activity) mContext).onBackPressed();
            }
        }
    }
}
