package com.hm.iou.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by hjy on 2019/1/18.
 */

public class MaxHeightRecyclerView extends RecyclerView {

    private int mMaxHeight;

    public MaxHeightRecyclerView(Context context) {
        this(context, null);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView);
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxHeightRecyclerView_maxListHeight, mMaxHeight);
        arr.recycle();
    }

    public void setMaxHeight(int listMaxHeight) {
        this.mMaxHeight = listMaxHeight;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}