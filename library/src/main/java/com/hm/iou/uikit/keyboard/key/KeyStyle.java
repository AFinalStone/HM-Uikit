package com.hm.iou.uikit.keyboard.key;

import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;

public interface KeyStyle {

        public Drawable getKeyBackground(Key key);

        public Float getKeyTextSize(Key key);

        public Integer getKeyTextColor(Key key);

        public CharSequence getKeyLabel(Key key);
    }