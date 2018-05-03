package com.hm.iou.uikit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;

/**
 * Created by hjy on 18/5/2.<br>
 *
 * RecyclerView中灰色的分隔线
 */

public class HMGrayDividerItemDecoration extends DividerItemDecoration {

    public HMGrayDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.uikit_comm_divider_gray);
        setDrawable(drawable);
    }

}
