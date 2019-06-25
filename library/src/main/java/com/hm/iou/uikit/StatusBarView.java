package com.hm.iou.uikit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * Created by syl on 2019/6/23.
 */

public class StatusBarView extends View {

    private Drawable mBgDrawable;

    private int mStatusBarHeight;
    //在布局文件里默认设置状态栏高度
    private int mDefaultStatusBarHeight;

    public StatusBarView(Context context) {
        super(context);
        init(context);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mStatusBarHeight = getStatusBarHeight(context);
        mDefaultStatusBarHeight = (int) (context.getResources().getDisplayMetrics().density * 24);
        //能够获取到状态栏高度
        if (mStatusBarHeight > 0) {
            ViewGroup.LayoutParams statusBarParams = getLayoutParams();
            if (statusBarParams != null) {
                statusBarParams.height = mStatusBarHeight;
                setLayoutParams(statusBarParams);
            }
        }

        // <= 6.0的系统，不能改变状态栏字体的颜色，我们这里统一设置成灰色
        mBgDrawable = getBackground();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mBgDrawable instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) mBgDrawable;
                if (colorDrawable.getColor() == Color.WHITE || colorDrawable.getColor() == Color.TRANSPARENT) {
                    setBackgroundColor(0xffa0a0a0);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int h = mStatusBarHeight > 0 ? mStatusBarHeight : mDefaultStatusBarHeight;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getStatusBarHeight(Context context) {
        int h = getStatusBar1(context);
        if (h == 0) {
            h = getStatusBar2(context);
        }
        return h;
    }

    public int getStatusBar1(Context context) {
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

    public int getStatusBar2(Context context) {
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
