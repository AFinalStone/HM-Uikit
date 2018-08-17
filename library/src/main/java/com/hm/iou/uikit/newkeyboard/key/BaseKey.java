package com.hm.iou.uikit.newkeyboard.key;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.IntegerRes;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hm.iou.uikit.R;

/**
 * @author syl
 * @time 2018/8/16 下午4:51
 */
public abstract class BaseKey extends Keyboard implements KeyboardView.OnKeyboardActionListener {

    private EditText mEditText;

    private View mNextFocusView;

    private KeyStyle mKeyStyle;

    protected Context mContext;

    public BaseKey(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
        mContext = context;
    }

    public BaseKey(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
        mContext = context;
    }

    public BaseKey(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
        mContext = context;
    }

    public BaseKey(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
        mContext = context;
    }

    public void setEditText(EditText editText) {
        mEditText = editText;
    }

    public void setNextFocusView(View nextFocusView) {
        mNextFocusView = nextFocusView;
    }

    public void setKeyStyle(KeyStyle keyStyle) {
        mKeyStyle = keyStyle;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public View getNextFocusView() {
        return mNextFocusView;
    }

    public KeyStyle getKeyStyle() {
        return mKeyStyle;
    }

    public int getKeyCode(@IntegerRes int redId) {
        return mContext.getResources().getInteger(redId);
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (null != mEditText && mEditText.hasFocus() && !handleSpecialKey(primaryCode)) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            int end = mEditText.getSelectionEnd();
            if (end > start) {
                editable.delete(start, end);
            }
            if (primaryCode == mContext.getResources().getInteger(R.integer.uikit_keyboard_delete)) {
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

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    /**
     * @param primaryCode
     * @return true if handle the key
     * false no handle and dispatch
     */
    public abstract boolean handleSpecialKey(int primaryCode);


    public Padding getPadding() {
        return new Padding(0, 0, 0, 0);
    }



}
