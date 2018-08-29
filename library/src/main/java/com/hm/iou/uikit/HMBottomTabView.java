package com.hm.iou.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by hjy on 18/4/29.<br>
 */

public class HMBottomTabView extends LinearLayout {

    private String mTabText;
    private double mTabTextSize;
    private int mTabTextSelectColor;
    private int mTabTextUnSelectColor;
    private double mTabTextHeight;
    private double mTabTextWidth;

    private Drawable mTabImageSelectDrawable;
    private Drawable mTabImageUnSelectDrawable;
    private double mTabImageHeight;
    private double mTabImageWidth;

    private boolean mIsSelect;
    private Context mContext;


    private ImageView mIvTab;
    private TextView mTvTab;

//    OnSelectChangeListener mOnSelectChangeListener;

    public HMBottomTabView(Context context) {
        super(context);
        initView(context, null);
    }

    public HMBottomTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public HMBottomTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HmBottomTabView);
        if (attributes != null) {
            mTabText = attributes.getString(R.styleable.HmBottomTabView_tabText);
            mTabTextSize = attributes.getDimension(R.styleable.HmBottomTabView_tabTextSize, 10);
            mTabTextSelectColor = attributes.getColor(R.styleable.HmBottomTabView_tabTextSelectColor, getResources().getColor(R.color.uikit_text_common_color));
            mTabTextUnSelectColor = attributes.getColor(R.styleable.HmBottomTabView_tabTextUnSelectColor, getResources().getColor(R.color.uikit_text_common_color));
            mTabTextHeight = attributes.getDimension(R.styleable.HmBottomTabView_tabTextHeight, 10);
            mTabTextWidth = attributes.getDimension(R.styleable.HmBottomTabView_tabTextWidth, 10);

            mTabImageSelectDrawable = attributes.getDrawable(R.styleable.HmBottomTabView_tabImageSelectDrawable);
            mTabImageUnSelectDrawable = attributes.getDrawable(R.styleable.HmBottomTabView_tabImageUnSelectDrawable);
            mTabImageHeight = attributes.getDimension(R.styleable.HmBottomTabView_tabImageHeight, 20);
            mTabImageWidth = attributes.getDimension(R.styleable.HmBottomTabView_tabImageWidth, 20);

            mIsSelect = attributes.getBoolean(R.styleable.HmBottomTabView_isSelect, false);
        }

        LayoutInflater.from(context).inflate(R.layout.uikit_bottom_tab_view, this, true);
        mIvTab = findViewById(R.id.iv_tab);
        mTvTab = findViewById(R.id.tv_tab);
        if (!TextUtils.isEmpty(mTabText)) {
            mTvTab.setText(mTabText);
        }
        if (mTabTextSize != 0) {
            mTvTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) mTabTextSize);
        }

        if (mIsSelect) {
            if (mTabTextSelectColor != 0) {
                mTvTab.setTextColor(mTabTextSelectColor);
            }
            if (mTabImageSelectDrawable != null) {
                mIvTab.setImageDrawable(mTabImageSelectDrawable);
            }
        } else {
            if (mTabTextUnSelectColor != 0) {
                mTvTab.setTextColor(mTabTextUnSelectColor);
            }
            if (mTabImageUnSelectDrawable != null) {
                mIvTab.setImageDrawable(mTabImageUnSelectDrawable);
            }
        }
    }

    private void refreshSelectStatus() {
        if (mIsSelect) {
            if (mTabTextSelectColor != 0) {
                mTvTab.setTextColor(mTabTextSelectColor);
            }
            if (mTabImageSelectDrawable != null) {
                mIvTab.setImageDrawable(mTabImageSelectDrawable);
            }
        } else {
            if (mTabTextUnSelectColor != 0) {
                mTvTab.setTextColor(mTabTextUnSelectColor);
            }
            if (mTabImageUnSelectDrawable != null) {
                mIvTab.setImageDrawable(mTabImageUnSelectDrawable);
            }
        }
    }

//    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
//        this.mOnSelectChangeListener = onSelectChangeListener;
//    }

    public boolean isSelect() {
        return mIsSelect;
    }

    public void setSelect(boolean isSelect) {
        this.mIsSelect = isSelect;
        refreshSelectStatus();
    }

//    public interface OnSelectChangeListener {
//
//        void onSelectListener();
//    }
}