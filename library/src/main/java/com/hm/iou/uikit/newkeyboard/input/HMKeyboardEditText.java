package com.hm.iou.uikit.newkeyboard.input;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.newkeyboard.HMKeyboardView;
import com.hm.iou.uikit.newkeyboard.key.BaseKey;
import com.hm.iou.uikit.newkeyboard.key.DefaultKeyStyle;

public class HMKeyboardEditText extends AppCompatEditText implements KeyboardView.OnKeyboardActionListener {

    protected Activity mActivity;
    protected HMKeyboardView mKeyboardView;
    protected ViewGroup mRootView;
    protected BaseKey mKeyboard;

    public HMKeyboardEditText(Activity context) {
        super(context);
    }

    public HMKeyboardEditText(Activity context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HMKeyboardEditText(Activity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initKeyBoadView(BaseKey baseKey) {
        mRootView = mActivity.getWindow().getDecorView().findViewById(android.R.id.content);

        //初始化editText
        setInputType(InputType.TYPE_NULL);
        setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (v instanceof EditText) {
                    if (hasFocus) {
                        hideSystemSoftKeyboard();

                    } else {
                        hideSoftKeyboard();
                    }
                }
            }
        });
        //初始化Keyboard
        if (baseKey.getKeyStyle() == null) {
            baseKey.setKeyStyle(new DefaultKeyStyle(mActivity.getApplication()));
        }
        baseKey.setEditText(this);
        mKeyboard = baseKey;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);
    }

    public void showSoftKeyboard() {
        if (mRootView.indexOfChild(mKeyboardView) == -1) {
            mRootView.addView(getKeyBoardView());
        } else {
            mKeyboardView.setVisibility(View.VISIBLE);
            mKeyboardView.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.down_to_up));
        }
    }


    public void hideSoftKeyboard() {
        if (mKeyboardView != null) {
            mKeyboardView.setVisibility(View.GONE);
            mKeyboardView.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.up_to_hide));
        }
    }

    private View getKeyBoardView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.uikit_layout_keyoard_view, null);
        mKeyboardView = view.findViewById(R.id.keyboard);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setEnabled(true);
        //禁用键盘的按键的放大效果
        mKeyboardView.setPreviewEnabled(false);
        //设置按钮点击的监听事件
        mKeyboardView.setOnKeyboardActionListener(this);
        return view;
    }

    private void hideSystemSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);
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