package com.hm.iou.uikit;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * Created by hjy on 18/4/28.<br>
 */

public class HMLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.uikit_layout_comm_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
