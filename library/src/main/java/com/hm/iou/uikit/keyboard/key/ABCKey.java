package com.hm.iou.uikit.keyboard.key;

import android.content.Context;

import com.hm.iou.uikit.R;


/**
 * @author syl
 * @time 2018/8/16 下午4:51
 * 数字输入键盘
 */
public class ABCKey extends BaseKey {

    public ABCKey(Context context) {
        super(context, R.xml.uikit_keyboard_abc);
        setKeyStyle(new ABCKeyStyle(context));
    }

}
