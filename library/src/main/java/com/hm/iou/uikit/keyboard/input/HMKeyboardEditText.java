package com.hm.iou.uikit.keyboard.input;

import android.content.Context;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.keyboard.HMKeyboardView;
import com.hm.iou.uikit.keyboard.key.BaseKey;
import com.hm.iou.uikit.keyboard.key.DefaultKeyStyle;

public class HMKeyboardEditText extends android.support.v7.widget.AppCompatEditText implements KeyboardView.OnKeyboardActionListener {


    protected Window mWindow;
    protected LinearLayout mLlKeyboardView;
    protected ViewGroup mRootView;
    protected BaseKey mKeyboard;

    public HMKeyboardEditText(Context context) {
        super(context);
    }

    public HMKeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HMKeyboardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initKeyBoardView(Window window, BaseKey baseKey) {
        mWindow = window;
        mRootView = mWindow.getDecorView().findViewById(android.R.id.content);
        //设置点击edit不弹出系统键盘
        setInputType(InputType.TYPE_NULL);
        //初始化Keyboard
        if (baseKey.getKeyStyle() == null) {
            baseKey.setKeyStyle(new DefaultKeyStyle(getContext()));
        }
        mKeyboard = baseKey;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (mRootView != null) {
            if (focused) {
                //隐藏系统键盘
                hideSystemSoftKeyboard();
                //显示自定义键盘
                showSoftKeyboard();
            } else {
                hideSoftKeyboard();
            }
        }
    }

    private void showSoftKeyboard() {
        if (mLlKeyboardView == null) {
            mRootView.addView(getKeyBoardView());
        } else {
            mLlKeyboardView.setVisibility(View.VISIBLE);
            mLlKeyboardView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.down_to_up));
        }
    }


    private void hideSoftKeyboard() {
        if (mLlKeyboardView != null) {
            mLlKeyboardView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.up_to_hide));
            mLlKeyboardView.setVisibility(View.GONE);
        }
    }

    private View getKeyBoardView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.uikit_layout_keyoard_view, null);
        mLlKeyboardView = view.findViewById(R.id.ll_keyboard);
        view.findViewById(R.id.iv_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });
        HMKeyboardView keyboardView = view.findViewById(R.id.keyboard);
        keyboardView.setKeyboard(mKeyboard);
        keyboardView.setEnabled(true);
        //禁用键盘的按键的放大效果
        keyboardView.setPreviewEnabled(false);
        //设置按钮点击的监听事件
        keyboardView.setOnKeyboardActionListener(this);
        return view;
    }

    private void hideSystemSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mWindow.getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

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
}