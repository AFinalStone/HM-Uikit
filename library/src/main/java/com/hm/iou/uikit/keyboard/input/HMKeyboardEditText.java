package com.hm.iou.uikit.keyboard.input;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Window;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.keyboard.key.BaseKey;

/**
 * @author syl
 * @time 2018/8/20 上午10:04
 */
public class HMKeyboardEditText extends android.support.v7.widget.AppCompatEditText {


    InputCodeViewHelper mInputCodeViewHelper;

    public HMKeyboardEditText(Context context) {
        super(context);
        initView();
    }

    public HMKeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HMKeyboardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void bindKeyBoardView(Window window, BaseKey baseKey) {
        mInputCodeViewHelper = new InputCodeViewHelper(getContext(), window, baseKey);
        mInputCodeViewHelper.setKeyClickListener(new InputCodeViewHelper.KeyClickListener() {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                if (hasFocus()) {
                    Editable editable = getText();
                    int start = getSelectionStart();
                    int end = getSelectionEnd();
                    if (end > start) {
                        editable.delete(start, end);
                    }
                    if (primaryCode == getResources().getInteger(R.integer.uikit_keyboard_delete)) {
                        if (!TextUtils.isEmpty(editable)) {
                            if (start > 0) {
                                editable.delete(start - 1, start);
                            }
                        }
                    } else {
                        editable.insert(start, Character.toString((char) primaryCode));
                    }
                }
            }
        });
    }


    private void initView() {
        setInputType(InputType.TYPE_NULL);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            mInputCodeViewHelper.showSoftKeyboard();
        } else {
            mInputCodeViewHelper.hideSoftKeyboard();
        }
    }


}