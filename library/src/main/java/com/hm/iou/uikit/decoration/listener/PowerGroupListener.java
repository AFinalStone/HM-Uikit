package com.hm.iou.uikit.decoration.listener;

import android.view.View;

/**
 * 显示自定义View的Group监听
 *
 * @author syl
 * @time 2019/3/13 10:32 AM
 */

public interface PowerGroupListener extends GroupListener {

    View getGroupTitleView(int position);
}
