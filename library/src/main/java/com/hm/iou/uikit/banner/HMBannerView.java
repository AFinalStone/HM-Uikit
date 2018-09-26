package com.hm.iou.uikit.banner;

import android.content.Context;
import android.util.AttributeSet;

import com.hm.iou.uikit.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.view.BannerViewPager;

/**
 * Created by hjy on 2018/9/19.
 */

public class HMBannerView extends Banner {

    BannerViewPager mViewPager;

    public HMBannerView(Context context) {
        this(context, null);
    }

    public HMBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMBannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mViewPager = findViewById(R.id.bannerViewPager);
        mViewPager.setPageMargin(dip2px(context, 7));
        setBannerStyle(BannerConfig.NOT_INDICATOR);
    }


    private static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

}