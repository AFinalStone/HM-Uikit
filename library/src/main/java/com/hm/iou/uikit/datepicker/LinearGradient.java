package com.hm.iou.uikit.datepicker;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created by hjy on 2019/2/15.
 */

public class LinearGradient {

    private int mStartColor;
    private int mEndColor;
    private int mRedStart;
    private int mBlueStart;
    private int mGreenStart;
    private int mRedEnd;
    private int mBlueEnd;
    private int mGreenEnd;

    public LinearGradient(@ColorInt int startColor, @ColorInt int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        updateColor();
    }


    public void setStartColor(@ColorInt int startColor) {
        mStartColor = startColor;
        updateColor();
    }

    public void setEndColor(@ColorInt int endColor) {
        mEndColor = endColor;
        updateColor();
    }

    private void updateColor() {
        mRedStart = Color.red(mStartColor);
        mBlueStart = Color.blue(mStartColor);
        mGreenStart = Color.green(mStartColor);
        mRedEnd = Color.red(mEndColor);
        mBlueEnd = Color.blue(mEndColor);
        mGreenEnd = Color.green(mEndColor);
    }

    public int getColor(float ratio) {
        int red = (int) (mRedStart + ((mRedEnd - mRedStart) * ratio + 0.5));
        int greed = (int) (mGreenStart + ((mGreenEnd - mGreenStart) * ratio + 0.5));
        int blue = (int) (mBlueStart + ((mBlueEnd - mBlueStart) * ratio + 0.5));
        return Color.rgb(red, greed, blue);
    }
}
