package com.hm.iou.uikit;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;


/**
 * Created by hjy on 18/4/29.<br>
 */

public class HMTopBarView extends RelativeLayout implements View.OnClickListener {

    /**
     * 返回键点击监听事件
     */
    public static interface OnTopBarBackClickListener {
        void onClickBack();
    }

    /**
     * 右边操作按钮点击监听事件
     */
    public static interface OnTopBarMenuClickListener {

        void onClickTextMenu();

        void onClickImageMenu();
    }

    private Context mContext;
    private String mTitleTextStr;
    private int mTitleTextSize;
    private int mTitleTextColor;
    private Drawable mBackDrawable;
    private String mRightText;
    private int mRightTextSize;
    private ColorStateList mRightTextColor;
    private Drawable mBgDrawable;
    private Drawable mRightIconDrawable;
    private boolean mBottomDividerIsShow;

    private TextView mTvTitle;
    private ImageView mIvBack;
    private boolean mIvBackIsShow;
    private TextView mTvRight;
    private ImageView mIvRight;
    private LinearLayout mLayoutRightContainer;
    private View mViewDivider;
    private View mViewStatusBarPlaceHolder;
    private View mLayout;
    private RelativeLayout mRlBarContent;

    //实际获取到的高度
    private int mStatusBarHeight;
    //在布局文件里默认设置状态栏高度
    private int mDefaultStatusBarHeight;
    private int mBarHeight;

    private OnTopBarBackClickListener mBackListener;
    private OnTopBarMenuClickListener mMenuListener;

    public HMTopBarView(Context context) {
        this(context, null);
    }

    public HMTopBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMTopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HmTopBar);

        mTitleTextStr = ta.getString(R.styleable.HmTopBar_titleText);
        mTitleTextSize = ta.getDimensionPixelSize(R.styleable.HmTopBar_titleSize,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())));
        mTitleTextColor = ta.getColor(R.styleable.HmTopBar_titleColor, getResources().getColor(R.color.uikit_title_center_text));
        mBackDrawable = ta.getDrawable(R.styleable.HmTopBar_backIcon);
        mIvBackIsShow = ta.getBoolean(R.styleable.HmTopBar_backIconIsShow, true);

        mRightText = ta.getString(R.styleable.HmTopBar_rightText);
        mRightTextSize = ta.getDimensionPixelSize(R.styleable.HmTopBar_rightTextSize,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
        mRightTextColor = ta.getColorStateList(R.styleable.HmTopBar_rightTextColor);
        mBottomDividerIsShow = ta.getBoolean(R.styleable.HmTopBar_bottomDividerIsShow, true);
        if (mRightTextColor == null) {
            mRightTextColor = ContextCompat.getColorStateList(context, R.color.uikit_title_right_text);
        }

        mBgDrawable = ta.getDrawable(R.styleable.HmTopBar_titleBackground);
        if (mBgDrawable == null) {
            mBgDrawable = new ColorDrawable(context.getResources().getColor(R.color.uikit_title_bg_color));
        }
        mRightIconDrawable = ta.getDrawable(R.styleable.HmTopBar_rightIcon);
        ta.recycle();

        initView(context);
    }


    private void initView(Context context) {
        mLayout = LayoutInflater.from(context).inflate(R.layout.uikit_top_bar, null);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        addView(mLayout, params);

        mTvTitle = mLayout.findViewById(R.id.tv_topbar_title);
        mIvBack = mLayout.findViewById(R.id.iv_topbar_back);
        mLayoutRightContainer = mLayout.findViewById(R.id.ll_topbar_rightcontianer);
        mTvRight = mLayout.findViewById(R.id.tv_topbar_right);
        mIvRight = mLayout.findViewById(R.id.iv_topbar_right);
        mViewDivider = mLayout.findViewById(R.id.view_topbar_divider);
        mViewStatusBarPlaceHolder = mLayout.findViewById(R.id.view_statusbar_placeholder);
        mRlBarContent = mLayout.findViewById(R.id.rl_topbar_content);

        mStatusBarHeight = getStatusBarHeight(context);
        mDefaultStatusBarHeight = (int) (context.getResources().getDisplayMetrics().density * 24);
        mBarHeight = (int) (context.getResources().getDisplayMetrics().density * 48);
        //能够获取到状态栏高度
        if (mStatusBarHeight > 0) {
            RelativeLayout.LayoutParams statusBarParams = (RelativeLayout.LayoutParams) mViewStatusBarPlaceHolder.getLayoutParams();
            statusBarParams.height = mStatusBarHeight;
            mViewStatusBarPlaceHolder.setLayoutParams(statusBarParams);
        }
        RelativeLayout.LayoutParams barParams = (RelativeLayout.LayoutParams) mRlBarContent.getLayoutParams();
        barParams.height = mBarHeight;
        mRlBarContent.setLayoutParams(barParams);

        if (!TextUtils.isEmpty(mTitleTextStr))
            mTvTitle.setText(mTitleTextStr);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mTvTitle.setTextColor(mTitleTextColor);
        if (mIvBackIsShow) {
            if (mBackDrawable != null)
                mIvBack.setImageDrawable(mBackDrawable);
        } else {
            mIvBack.setVisibility(INVISIBLE);
        }


        if (TextUtils.isEmpty(mRightText)) {
            mTvRight.setVisibility(View.GONE);
        } else {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(mRightText);
        }
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        mTvRight.setTextColor(mRightTextColor);

        if (mBgDrawable != null) {
            mLayout.setBackground(mBgDrawable);
        }
        if (!mBottomDividerIsShow) {
            mViewDivider.setVisibility(GONE);
        }

        if (mRightIconDrawable != null) {
            mIvRight.setVisibility(VISIBLE);
            mIvRight.setImageDrawable(mRightIconDrawable);
        } else {
            mIvRight.setVisibility(View.GONE);
        }

        // <= 6.0的系统，不能改变状态栏字体的颜色，我们这里统一设置成灰色
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mBgDrawable instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) mBgDrawable;
                if (colorDrawable.getColor() == Color.WHITE || colorDrawable.getColor() == Color.TRANSPARENT) {
                    mViewStatusBarPlaceHolder.setBackgroundColor(0xffa0a0a0);
                }
            }
        }

        mIvBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int h = 0;
        if (mViewStatusBarPlaceHolder.getVisibility() == View.GONE) {
            h = mBarHeight;
        } else {
            h = mBarHeight + (mStatusBarHeight > 0 ? mStatusBarHeight : mDefaultStatusBarHeight);
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(h , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 設置背景顏色
     *
     * @param color
     */
    public void setHMBackground(int color) {
        mBgDrawable = new ColorDrawable(color);
        mLayout.setBackground(mBgDrawable);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    /**
     * 获取标题
     *
     * @return
     */
    public CharSequence getTitle() {
        return mTvTitle.getText();
    }

    /**
     * 设置右边显示操作item文字
     *
     * @param rightText 为空则隐藏
     */
    public void setRightText(CharSequence rightText) {
        mTvRight.setText(rightText);
        if (TextUtils.isEmpty(rightText)) {
            mTvRight.setVisibility(View.GONE);
        } else {
            mTvRight.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置右边显示操作item文字
     *
     * @param resId 为0则隐藏
     */
    public void setRightText(int resId) {
        if (resId > 0) {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(resId);
        } else {
            mTvRight.setVisibility(View.GONE);
        }
    }

    public void setRightIcon(int resId) {
        if (resId > 0) {
            mIvRight.setVisibility(View.VISIBLE);
            mIvRight.setImageResource(resId);
        } else {
            mIvRight.setVisibility(View.GONE);
        }
    }

    public void setRightIcon(Drawable drawable) {
        if (drawable != null) {
            mIvRight.setVisibility(View.VISIBLE);
            mIvRight.setImageDrawable(drawable);
        } else {
            mIvRight.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏返回图标
     */
    public void hideBackIcon() {
        mIvBack.setVisibility(View.GONE);
    }

    /**
     * 显示返回按钮
     */
    public void showBackIcon() {
        mIvBack.setVisibility(View.VISIBLE);
    }

    /**
     * 设置返回按钮的图片
     *
     * @param resId
     */
    public void setBackIcon(int resId) {
        mIvBack.setImageResource(resId);
        invalidate();
    }

    /**
     * 右边添加更多菜单按钮
     *
     * @param view
     * @param listener
     */
    public void addRightMenu(View view, OnClickListener listener) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setOnClickListener(listener);
        mLayoutRightContainer.addView(view, 0, params);
    }

    public View getDividerView() {
        return mViewDivider;
    }

    public void showDivider(boolean show) {
        if (show) {
            mViewDivider.setVisibility(View.VISIBLE);
        } else {
            mViewDivider.setVisibility(View.GONE);
        }
    }

    public View getStatusBarPlaceHolder() {
        return mViewStatusBarPlaceHolder;
    }

    public void setOnBackClickListener(OnTopBarBackClickListener listener) {
        mBackListener = listener;
    }

    public void setOnMenuClickListener(OnTopBarMenuClickListener listener) {
        mMenuListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == mIvBack) {
            if (mBackListener != null) {
                mBackListener.onClickBack();
            } else {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).onBackPressed();
                }
            }
        } else if (v == mTvRight) {
            if (mMenuListener != null) {
                mMenuListener.onClickTextMenu();
            }
        } else if (v == mIvRight) {
            if (mMenuListener != null) {
                mMenuListener.onClickImageMenu();
            }
        }
    }

    private static int getStatusBarHeight(Context context) {
        int h = getStatusBar1(context);
        if (h == 0) {
            h = getStatusBar2(context);
        }
        return h;
    }

    private static int getStatusBar1(Context context){
        int result = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int getStatusBar2(Context context) {
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            int statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



}