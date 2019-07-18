package com.hm.iou.uikit.decoration;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hm.iou.uikit.decoration.view.BaseDecoration;


/**
 * 支持顶部View悬浮，并且支持View点击的控件
 *
 * @author syl
 * @time 2019/3/13 3:26 PM
 */

public class StickyRecyclerView extends RecyclerView {

    private BaseDecoration mDecoration;

    public StickyRecyclerView(Context context) {
        super(context);
    }

    public StickyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        if (decor != null && decor instanceof BaseDecoration) {
            mDecoration = (BaseDecoration) decor;
        }
        super.addItemDecoration(decor);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mDecoration != null) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDecoration.onEventDown(e);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mDecoration.onEventUp(e)) {
                        return true;
                    }
                    break;
                default:
            }
        }
        return super.onInterceptTouchEvent(e);
    }
}
