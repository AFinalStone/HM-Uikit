package com.hm.iou.uikit.demo.layoutmanager.viewpager;

/**
 * Created by syl on 2018/9/14.
 */

public class ViewPagerBean implements IViewPagerBean {

    int imageResId;

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    @Override
    public int getIImageResId() {
        return imageResId;
    }
}
