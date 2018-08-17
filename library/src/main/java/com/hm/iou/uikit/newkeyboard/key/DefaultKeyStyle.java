package com.hm.iou.uikit.newkeyboard.key;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.hm.iou.uikit.R;

public class DefaultKeyStyle implements KeyStyle {

    private Context mContext;

    public DefaultKeyStyle(Context context) {
        this.mContext = context;
    }

    @Override
    public Drawable getKeyBackground(Keyboard.Key key) {
        if (key.iconPreview != null) {
            return key.iconPreview;
        } else {
            return ContextCompat.getDrawable(mContext, R.drawable.keyboard_bg_number_bg);
        }
    }

    @Override
    public Float getKeyTextSize(Keyboard.Key key) {
        return convertSpToPixels(mContext, 26f);
    }

    @Override
    public Integer getKeyTextColor(Keyboard.Key key) {
        return Color.BLACK;
    }

    @Override
    public CharSequence getKeyLabel(Keyboard.Key key) {
        return key.label;
    }

    private float convertSpToPixels(Context context, float sp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}