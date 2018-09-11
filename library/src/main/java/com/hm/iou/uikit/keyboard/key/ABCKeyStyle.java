package com.hm.iou.uikit.keyboard.key;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.hm.iou.uikit.R;

public class ABCKeyStyle implements BaseKeyStyle {

    private Context mContext;

    public ABCKeyStyle(Context context) {
        this.mContext = context;
    }

    @Override
    public Drawable getKeyBackground(Keyboard.Key key) {
        if (key.iconPreview != null) {
            return key.iconPreview;
        } else {
            return ContextCompat.getDrawable(mContext, R.drawable.uikit_keyboard_bg_number_bg);
        }
    }

    @Override
    public Float getKeyTextSize(Keyboard.Key key) {
        return convertSpToPixels(mContext, 18f);
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