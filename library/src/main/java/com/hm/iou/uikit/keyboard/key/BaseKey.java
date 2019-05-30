package com.hm.iou.uikit.keyboard.key;

import android.content.Context;
import android.inputmethodservice.Keyboard;

/**
 * @author syl
 * @time 2018/8/16 下午4:51
 */
public abstract class BaseKey extends Keyboard {

    public BaseKey(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public BaseKey(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public BaseKey(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public BaseKey(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }
}
