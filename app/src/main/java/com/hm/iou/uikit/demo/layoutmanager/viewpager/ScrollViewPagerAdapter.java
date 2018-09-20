package com.hm.iou.uikit.demo.layoutmanager.viewpager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.uikit.demo.R;

/**
 * Created by syl on 2018/9/14.
 */

public class ScrollViewPagerAdapter extends BaseQuickAdapter<IViewPagerBean, BaseViewHolder> {

    public ScrollViewPagerAdapter() {
        super(R.layout.item_layout_manager_view_pager);
    }

    @Override
    protected void convert(BaseViewHolder helper, IViewPagerBean item) {
        helper.setImageResource(R.id.imageView, item.getIImageResId());
    }
}
