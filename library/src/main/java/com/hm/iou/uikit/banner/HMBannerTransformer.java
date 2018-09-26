package com.hm.iou.uikit.banner;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by hjy on 2018/9/19.
 */

public class HMBannerTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.80f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
            page.setPivotY(page.getHeight() / 2);
            page.setScaleY(MIN_SCALE);
        } else if (position <= 1) { // [-1,1]
            if (position < 0) {
                float scaleX = 1 + 0.2f * position;
                page.setPivotY(page.getHeight() / 2);
                page.setScaleY(scaleX);
            } else {
                float scaleX = 1 - 0.2f * position;
                page.setPivotY(page.getHeight() / 2);
                page.setScaleY(scaleX);
            }
        }
    }
}