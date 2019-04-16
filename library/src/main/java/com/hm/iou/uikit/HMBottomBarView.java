package com.hm.iou.uikit;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
    private String mBackTextStr;
    private int mBackTextSize;
    private int mBackTextColor;
    private int mTitleTextSize;
    private int mTitleTextColor;
    private Drawable mBackDrawable;
    private Drawable mTitleBackgroundDrawable;
    private int mIconWidth;
    private boolean mEnable;

    private ImageView mIvBack;
    private TextView mTvBack;
    private TextView mTvTitle;

    private TextView mSecondTitle;

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
        //兼容以前的版本，属性名暂不改,右侧按钮文字描述
        mTitleTextStr = ta.getString(R.styleable.HmBottomBar_bottomTitleText);
        mTitleTextSize = ta.getDimensionPixelSize(R.styleable.HmBottomBar_bottomTitleSize,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics())));
        mTitleTextColor = ta.getColor(R.styleable.HmBottomBar_bottomTitleColor, -1);
        //左侧返回文字描述
        mBackTextStr = ta.getString(R.styleable.HmBottomBar_bottomBackText);
        mBackTextSize = ta.getDimensionPixelSize(R.styleable.HmBottomBar_bottomBackTextSize,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics())));
        mBackTextColor = ta.getColor(R.styleable.HmBottomBar_bottomBackTextColor, -1);

        //左侧返回icon
        mBackDrawable = ta.getDrawable(R.styleable.HmBottomBar_bottomBackIcon);
        mIconWidth = ta.getDimensionPixelSize(R.styleable.HmBottomBar_bottomIconWidth, 0);
        //右侧按钮背景颜色
        mTitleBackgroundDrawable = ta.getDrawable(R.styleable.HmBottomBar_bottomTitleBackground);
        mEnable = ta.getBoolean(R.styleable.HmBottomBar_enable, true);
        ta.recycle();

        init(context);
    }

    private void init(Context context) {
        mContext = context;
        float density = mContext.getResources().getDisplayMetrics().density;

        //左侧返回icon
        mIvBack = new ImageView(context);
        mIvBack.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LayoutParams params = new LayoutParams(mIconWidth != 0 ? mIconWidth : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mBackDrawable != null) {
            mIvBack.setImageDrawable(mBackDrawable);
        } else {
            mIvBack.setImageResource(R.mipmap.uikit_ic_arrow_left_black);
        }
        mIvBack.setBackgroundResource(R.drawable.uikit_bg_item_ripple);
        mIvBack.setPadding((int) (density * 18), 0, (int) (density * 15), 0);
        addView(mIvBack, params);
        mIvBack.setId(R.id.hm_bottom_bar_back_icon);
        mIvBack.setOnClickListener(this);

        //左侧返回文字
        if (!TextUtils.isEmpty(mBackTextStr)) {
            mTvBack = new TextView(context);
            mTvBack.setGravity(Gravity.CENTER);
            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (density * 30));
            params.addRule(RelativeLayout.RIGHT_OF, mIvBack.getId());
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            mTvBack.setText(mBackTextStr);
            mTvBack.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBackTextSize);
            if (mBackTextColor != -1) {
                mTvBack.setTextColor(mBackTextColor);
            } else {
                mTvBack.setTextColor(getResources().getColorStateList(R.color.uikit_selector_btn_minor));
            }
            addView(mTvBack, params);
        }


        //右侧按钮的文字
        if (!TextUtils.isEmpty(mTitleTextStr)) {
            mTvTitle = new TextView(context);
            mTvTitle.setGravity(Gravity.CENTER);
            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (density * 30));
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.rightMargin = (int) (density * 12);
            mTvTitle.setText(mTitleTextStr);
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
            if (mTitleTextColor != -1) {
                mTvTitle.setTextColor(mTitleTextColor);
            } else {
                mTvTitle.setTextColor(getResources().getColorStateList(R.color.uikit_selector_btn_main));
            }
            if (mTitleBackgroundDrawable != null) {
                mTvTitle.setBackground(mTitleBackgroundDrawable);
            } else {
                mTvTitle.setBackgroundResource(R.drawable.uikit_selector_btn_main_small);
            }
            int pad = (int) (density * 10);
            mTvTitle.setPadding(pad, 0, pad, 0);
            addView(mTvTitle, params);
            mTvTitle.setId(R.id.hm_bottom_bar_title);
            mTvTitle.setOnClickListener(this);
        }

        setBackgroundColor(Color.WHITE);
        setEnabled(mEnable);
    }

    public void updateTitle(CharSequence title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    public void setOnBackClickListener(OnBackClickListener listener) {
        mListener = listener;
    }

    public void showSecondButton(CharSequence title, OnClickListener listener) {
        if (mSecondTitle == null) {
            mSecondTitle = new TextView(mContext);
            mSecondTitle.setGravity(Gravity.CENTER);
            float density = mContext.getResources().getDisplayMetrics().density;
            RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (density * 30));
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.LEFT_OF, R.id.hm_bottom_bar_title);
            params.rightMargin = (int) (density * 12);
            mSecondTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
            mSecondTitle.setTextColor(getResources().getColor(R.color.uikit_text_sub_content));
            mSecondTitle.setBackgroundResource(R.drawable.uikit_selector_btn_bordered_small);
            int pad = (int) (density * 10);
            mSecondTitle.setPadding(pad, 0, pad, 0);
            addView(mSecondTitle, params);
        }
        mSecondTitle.setText(title);
        mSecondTitle.setOnClickListener(listener);
    }

    public void hideSecondButton() {
        if (mSecondTitle != null)
            mSecondTitle.setVisibility(View.GONE);
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mTitleClickListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mTvTitle != null) {
            mTvTitle.setEnabled(enabled);
        }
    }

    /**
     * 设置主button 背景
     *
     * @param bgResId
     */
    public void setTitleBackgournd(int bgResId) {
        if (mTvTitle != null) {
            mTvTitle.setBackgroundResource(bgResId);
        }
    }

    /**
     * 设置住button 文字颜色
     *
     * @param colorResId
     */
    public void setTitleTextColor(int colorResId) {
        if (mTvTitle != null) {
            mTvTitle.setTextColor(getResources().getColor(colorResId));
        }
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
