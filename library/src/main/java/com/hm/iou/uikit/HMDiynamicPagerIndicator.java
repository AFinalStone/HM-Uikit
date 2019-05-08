package com.hm.iou.uikit;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kcrason.dynamicpagerindicatorlibrary.DynamicPagerIndicator;
import com.kcrason.dynamicpagerindicatorlibrary.PageTabView;

/**
 * Created by hjy on 18/4/30.<br>
 */

public class HMDiynamicPagerIndicator extends DynamicPagerIndicator {

    /**
     * 选中的tab上的字体是否需要加粗
     */
    private boolean mSelectedTextBold;

    public HMDiynamicPagerIndicator(Context context) {
        this(context, null);
    }

    public HMDiynamicPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMDiynamicPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置选中的tab上的字体变粗
     *
     * @param isBold
     */
    @Deprecated
    public void setSelectedTextBold(boolean isBold) {
        mSelectedTextBold = isBold;
        invalidate();
    }

    @Override
    public void updateTitleStyle(int position) {
        if (mTabParentView == null) {
            throw new RuntimeException("TitleParentView is null");
        }
        for (int i = 0; i < mTabParentView.getChildCount(); i++) {
            View childView = mTabParentView.getChildAt(i);
            if (childView instanceof PageTabView) {
                TextView textView = ((PageTabView) childView).getTitleTextView();
                if (textView != null) {
                    if (position == i) {
                        textView.setTextColor(mTabSelectedTextColor);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabSelectedTextSize);
/*                        if (mSelectedTextBold)
                            textView.setTypeface(Typeface.DEFAULT_BOLD);
                        else
                            textView.setTypeface(Typeface.DEFAULT);*/
                        textView.setTypeface(Typeface.DEFAULT);
                    } else {
                        textView.setTextColor(mTabNormalTextColor);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabNormalTextSize);
                        textView.setTypeface(Typeface.DEFAULT);
                    }
                }
            }
        }
    }

    public void updateTitle(int position, String title) {
        if (mTabParentView == null) {
            throw new RuntimeException("TitleParentView is null");
        }
        for (int i = 0; i < mTabParentView.getChildCount(); i++) {
            View childView = mTabParentView.getChildAt(i);
            if (childView instanceof PageTabView) {
                TextView textView = ((PageTabView) childView).getTitleTextView();
                if (textView != null && position == i) {
                    textView.setText(title);
                    break;
                }
            }
        }
    }

    /**
     * 设置一个TextView，用于显示标题，这是必不可少的一个View
     */
    @Override
    public void setTabTitleTextView(TextView textView, int position, PagerAdapter pagerAdapter) {
        if (position == 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabSelectedTextSize);
            textView.setTextColor(mTabSelectedTextColor);
            textView.setTypeface(Typeface.DEFAULT);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabNormalTextSize);
            textView.setTextColor(mTabNormalTextColor);
            textView.setTypeface(Typeface.DEFAULT);
        }
        textView.setGravity(Gravity.CENTER);
        String title = pagerAdapter.getPageTitle(position).toString();
        textView.setText(title);
    }

}
