package com.hm.iou.uikit;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EdgeEffect;

import java.lang.reflect.Field;

/**
 * Created by syl on 2018/9/25.
 * 去除ViewPager左滑和右滑到边缘的时候的过渡颜色
 */

public class NoEdgeColorViewPager extends ViewPager {


    private EdgeEffect mLeftEdgeEffect;
    private EdgeEffect mRightEdgeEffect;

    public NoEdgeColorViewPager(Context context) {
        super(context);
        init();
    }

    public NoEdgeColorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        try {
            Field leftEdgeField = getClass().getSuperclass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = getClass().getSuperclass().getDeclaredField("mRightEdge");
            Log.i("xinye", "=======leftEdgeField:" + leftEdgeField + ",rightEdgeField:" + rightEdgeField);
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                mLeftEdgeEffect = (EdgeEffect) leftEdgeField.get(this);
                mRightEdgeEffect = (EdgeEffect) rightEdgeField.get(this);
                Log.i("xinye", "=======OK啦，leftEdge:" + mLeftEdgeEffect + ",rightEdge:" + mRightEdgeEffect);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (mLeftEdgeEffect != null && mRightEdgeEffect != null) {
            mLeftEdgeEffect.finish();
            mRightEdgeEffect.finish();
            mLeftEdgeEffect.setSize(0, 0);
            mRightEdgeEffect.setSize(0, 0);
        }
        super.onPageScrolled(position, offset, offsetPixels);
    }
}
