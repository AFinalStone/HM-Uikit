package com.hm.iou.uikit;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public interface OnTitleIconClickListener {
        void onClickIcon();
    }

    private Context mContext;
    private String mTitleTextStr;
    private String mBackTextStr;
    private int mBackTextSize;
    private int mBackTextColor;
    private int mTitleTextSize;
    private int mTitleTextColor;
    private Drawable mBackIconDrawable;
    private Drawable mTitleIconDrawable;
    private Drawable mTitleBackgroundDrawable;
    private int mBackIconWidth;
    private int mTitleIconWidth;
    private boolean mEnable;
    private boolean mTitleIconIsShow;

    private ImageView mIvBackIcon;
    private ImageView mIvTitleIcon;
    private LinearLayout mLlBack;
    private TextView mTvBack;
    private TextView mTvTitle;

    private TextView mSecondTitle;

    private OnBackClickListener mListener;
    private OnTitleClickListener mTitleClickListener;
    private OnTitleIconClickListener mOnTitleIconClickListener;

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
        mBackIconDrawable = ta.getDrawable(R.styleable.HmBottomBar_bottomBackIcon);
        mBackIconWidth = ta.getDimensionPixelSize(R.styleable.HmBottomBar_bottomIconWidth, 0);
        //右侧标题icon
        mTitleIconDrawable = ta.getDrawable(R.styleable.HmBottomBar_bottomTitleIcon);
        mTitleIconWidth = ta.getDimensionPixelSize(R.styleable.HmBottomBar_bottomTitleIconWidth, 0);
        mTitleIconIsShow = ta.getBoolean(R.styleable.HmBottomBar_bottomTitleIconIsShow, true);

        //右侧按钮背景颜色
        mTitleBackgroundDrawable = ta.getDrawable(R.styleable.HmBottomBar_bottomTitleBackground);
        mEnable = ta.getBoolean(R.styleable.HmBottomBar_enable, true);
        ta.recycle();

        init(context);
    }

    private void init(Context context) {
        mContext = context;
        float density = mContext.getResources().getDisplayMetrics().density;

        /**
         * 左侧返回图标和标题
         */
        mLlBack = new LinearLayout(mContext);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLlBack.setBackgroundResource(R.drawable.uikit_bg_item_ripple);
        mLlBack.setOrientation(LinearLayout.HORIZONTAL);
        mLlBack.setGravity(Gravity.CENTER_VERTICAL);
        mLlBack.setOnClickListener(this);
        mLlBack.setPadding((int) (density * 18), 0, (int) (density * 18), 0);
        addView(mLlBack, params);
        //左侧返回Icon
        mIvBackIcon = new ImageView(context);
        mIvBackIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        params = new LayoutParams(mBackIconWidth != 0 ? mBackIconWidth : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mBackIconDrawable != null) {
            mIvBackIcon.setImageDrawable(mBackIconDrawable);
        } else {
            mIvBackIcon.setImageResource(R.mipmap.uikit_ic_arrow_left_black);
        }
        mIvBackIcon.setPadding(0, 0, (int) (density * 15), 0);
        mIvBackIcon.setId(R.id.hm_bottom_bar_back_icon);
        mLlBack.addView(mIvBackIcon, params);
        //左侧返回文字
        if (!TextUtils.isEmpty(mBackTextStr)) {
            mTvBack = new TextView(context);
            mTvBack.setGravity(Gravity.CENTER);
            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (density * 30));
            params.addRule(RelativeLayout.RIGHT_OF, mIvBackIcon.getId());
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            mTvBack.setText(mBackTextStr);
            mTvBack.setTextSize(TypedValue.COMPLEX_UNIT_PX, mBackTextSize);
            if (mBackTextColor != -1) {
                mTvBack.setTextColor(mBackTextColor);
            } else {
                mTvBack.setTextColor(getResources().getColorStateList(R.color.uikit_selector_btn_minor));
            }
            mLlBack.addView(mTvBack, params);
        }

        /**
         * 右侧标题和图标
         */
        mTvTitle = new TextView(context);
        mTvTitle.setGravity(Gravity.CENTER);
        params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (density * 30));
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.rightMargin = (int) (density * 12);
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
        mTvTitle.setId(R.id.hm_bottom_bar_title);
        mTvTitle.setOnClickListener(this);
        addView(mTvTitle, params);
        //右侧按钮的文字
        if (TextUtils.isEmpty(mTitleTextStr)) {
            mTvTitle.setVisibility(INVISIBLE);
        } else {
            mTvTitle.setText(mTitleTextStr);
            mTvTitle.setVisibility(VISIBLE);
        }
        //右侧Icon
        if (mTitleIconDrawable != null) {
            addTitleIcon();
        }
        setBackgroundColor(Color.WHITE);
        setEnabled(mEnable);
        //添加阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(density * 1);
        }
    }

    private void addTitleIcon() {
        mIvTitleIcon = new ImageView(mContext);
        mIvTitleIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mIvTitleIcon.setBackgroundResource(R.drawable.uikit_bg_item_ripple);
        RelativeLayout.LayoutParams params = new LayoutParams(mTitleIconWidth != 0 ? mTitleIconWidth : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mIvTitleIcon.setImageDrawable(mTitleIconDrawable);
        float density = mContext.getResources().getDisplayMetrics().density;
        mIvTitleIcon.setPadding((int) (density * 10), 0, (int) (density * 10), 0);
        mIvTitleIcon.setId(R.id.hm_bottom_bar_title_icon);
        mIvTitleIcon.setOnClickListener(this);
        addView(mIvTitleIcon, params);
        mIvTitleIcon.setVisibility(mTitleIconIsShow ? VISIBLE : INVISIBLE);

        if (mTitleIconIsShow) {
            RelativeLayout.LayoutParams params1 = (LayoutParams) mTvTitle.getLayoutParams();
            params1.addRule(RelativeLayout.LEFT_OF, R.id.hm_bottom_bar_title_icon);
            params1.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.rightMargin = (int) (density * 5);
            mTvTitle.setLayoutParams(params1);
        }
    }

    public void updateTitle(CharSequence title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    public void updateBackText(CharSequence backText) {
        if (mTvBack != null) {
            mTvBack.setText(backText);
        }
    }

    public void setTitleVisible(boolean isShow) {
        if (mTvTitle != null) {
            if (isShow) {
                mTvTitle.setVisibility(VISIBLE);
            } else {
                mTvTitle.setVisibility(INVISIBLE);
            }
        }
    }

    public void setTitleIconVisible(boolean isShow) {
        mTitleIconIsShow = isShow;
        if (mIvTitleIcon != null) {
            if (isShow) {
                mIvTitleIcon.setVisibility(VISIBLE);

                RelativeLayout.LayoutParams params = (LayoutParams) mTvTitle.getLayoutParams();
                float density = mContext.getResources().getDisplayMetrics().density;
                params.addRule(RelativeLayout.LEFT_OF, R.id.hm_bottom_bar_title_icon);
                params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.rightMargin = (int) (density * 5);
                mTvTitle.setLayoutParams(params);
            } else {
                mIvTitleIcon.setVisibility(INVISIBLE);

                RelativeLayout.LayoutParams params = (LayoutParams) mTvTitle.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.rightMargin = 0;
                mTvTitle.setLayoutParams(params);
            }
        }
    }

    public void setTitleIconDrawable(int drawableResId) {
        if (mIvTitleIcon != null) {
            mIvTitleIcon.setImageResource(drawableResId);
        } else {
            addTitleIcon();
            mIvTitleIcon.setImageResource(drawableResId);
        }
    }

    public void setTitleBtnBackground(int drawableResId) {
        if (mTvTitle != null) {
            mTvTitle.setBackgroundResource(drawableResId);
        }
    }

    public void setTitleBtnTextColor(int colorRGB) {
        if (mTvTitle != null) {
            mTvTitle.setTextColor(colorRGB);
        }
    }

    public void setTitleBtnTextColor(ColorStateList colorStateList) {
        if (mTvTitle != null) {
            mTvTitle.setTextColor(colorStateList);
        }
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mTitleClickListener = listener;
    }

    public void setOnTitleIconClickListener(OnTitleIconClickListener listener) {
        mOnTitleIconClickListener = listener;
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
        } else if (v == mIvTitleIcon) {
            if (mOnTitleIconClickListener != null) {
                mOnTitleIconClickListener.onClickIcon();
                return;
            }
            //兼容以前的，2个点击响应的是同一个事件
            if (mTitleClickListener != null) {
                mTitleClickListener.onClickTitle();
            }
        } else if (v == mLlBack) {
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
