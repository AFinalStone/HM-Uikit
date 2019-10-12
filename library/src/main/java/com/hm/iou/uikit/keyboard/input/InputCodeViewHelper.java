package com.hm.iou.uikit.keyboard.input;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.keyboard.key.BaseKey;

/**
 * Created by syl on 2018/8/17.
 */

public class InputCodeViewHelper {

    public interface KeyClickListener {
        void onKey(int primaryCode, int[] keyCodes);
    }

    private Context mContext;
    //和自定义输入法相关的变量
    private Window mWindow;                //当前窗口
    private LinearLayout mLlKeyboardView;  //自定义输入法键盘
    private LinearLayout mLlKeyboardTitle;  //自定义输入法键盘标题
    private ViewGroup mRootView;           //自定义输入法所在的根布局文件
    private BaseKey mKeyboard;             //按键
    private KeyClickListener mKeyClickListener;

    public InputCodeViewHelper(Context mContext, Window mWindow, BaseKey baseKey) {
        this.mContext = mContext;
        this.mWindow = mWindow;
        this.mKeyboard = baseKey;
        mRootView = mWindow.getDecorView().findViewById(android.R.id.content);
        mKeyboard = baseKey;
    }

    /**
     * 设置键盘标题隐藏或者显示
     *
     * @param isShow
     */
    public void setTitleVisibility(boolean isShow) {
        if (isShow) {
            mLlKeyboardTitle.setVisibility(View.VISIBLE);
        } else {
            mLlKeyboardTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置键盘被点击的监听对象
     *
     * @param keyClickListener
     */
    public void setKeyClickListener(KeyClickListener keyClickListener) {
        this.mKeyClickListener = keyClickListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftKeyboard() {
        hideSystemSoftKeyboard();
        if (mLlKeyboardView == null) {
            mRootView.addView(getKeyBoardView());
        } else {
            mLlKeyboardView.setVisibility(View.VISIBLE);
            mLlKeyboardView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.uikit_keyboard_down_to_up));
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftKeyboard() {
        if (mLlKeyboardView != null) {
            mLlKeyboardView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.uikit_keyboard_up_to_hide));
            mLlKeyboardView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    private View getKeyBoardView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.uikit_layout_keyoard_view, null);
        mLlKeyboardView = view.findViewById(R.id.ll_keyboard);
        mLlKeyboardTitle = view.findViewById(R.id.ll_title);
        KeyboardView keyboardView = view.findViewById(R.id.keyboard);
        keyboardView.setKeyboard(mKeyboard);
        keyboardView.setEnabled(true);
        //禁用键盘的按键的放大效果
        keyboardView.setPreviewEnabled(false);
        //设置按钮点击的监听事件
        keyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        return view;
    }

    /**
     * 隐藏系统键盘
     */
    private void hideSystemSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mWindow.getDecorView().getWindowToken(), 0);
        }
    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (mKeyClickListener != null) {
                mKeyClickListener.onKey(primaryCode, keyCodes);
            }
            if (primaryCode == Keyboard.KEYCODE_CANCEL || primaryCode == Keyboard.KEYCODE_DONE) {
                hideSoftKeyboard();
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
    };


}
