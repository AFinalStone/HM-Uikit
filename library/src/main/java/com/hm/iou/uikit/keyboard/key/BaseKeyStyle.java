package com.hm.iou.uikit.keyboard.key;

import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;

public interface BaseKeyStyle {

    Drawable getKeyBackground(Key key);

    Float getKeyTextSize(Key key);

    Integer getKeyTextColor(Key key);

    CharSequence getKeyLabel(Key key);
}