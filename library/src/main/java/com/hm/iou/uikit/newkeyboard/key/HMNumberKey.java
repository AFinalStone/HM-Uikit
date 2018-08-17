package com.hm.iou.uikit.newkeyboard.key;

import android.content.Context;
import android.text.Editable;

import com.hm.iou.uikit.R;


/**
 * @author syl
 * @time 2018/8/16 下午4:51
 * 数字输入键盘
 */
public class HMNumberKey extends BaseKey {

    public static final int DEFAULT_NUMBER_XML_LAYOUT = R.xml.uikit_keyboard_number;

    public HMNumberKey(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public HMNumberKey(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public HMNumberKey(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public HMNumberKey(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    public boolean handleSpecialKey(int primaryCode) {
        Editable editable = getEditText().getText();
        int start = getEditText().getSelectionStart();
        //小数点
        if (primaryCode == mContext.getResources().getInteger(R.integer.uikit_keyboard_point)) {
            if (!editable.toString().contains(".")) {
                if (!editable.toString().startsWith(".")) {
                    editable.insert(start, Character.toString((char) primaryCode));
                } else {
                    editable.insert(start, "0" + Character.toString((char) primaryCode));
                }
            }
            return true;
        }
        return false;
    }

}
