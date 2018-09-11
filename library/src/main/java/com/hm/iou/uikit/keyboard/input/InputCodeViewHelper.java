package com.hm.iou.uikit.keyboard.input;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
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
import com.hm.iou.uikit.keyboard.key.NumberKeyStyle;

/**
 * Created by syl on 2018/8/17.
 */

public class InputCodeViewHelper implements KeyboardView.OnKeyboardActionListener {

    private Context mContext;
    //和自定义输入法相关的变量
    protected Window mWindow;                //当前窗口
    protected LinearLayout mLlKeyboardView;  //自定义输入法键盘
    protected LinearLayout mLlKeyboardTitle;  //自定义输入法键盘标题
    protected ViewGroup mRootView;           //自定义输入法所在的根布局文件
    protected BaseKey mKeyboard;             //按键
    protected KeyClickListener mKeyClickListener;

    public InputCodeViewHelper(Context mContext, Window mWindow, BaseKey baseKey) {
        this.mContext = mContext;
        this.mWindow = mWindow;
        this.mKeyboard = baseKey;
        mRootView = mWindow.getDecorView().findViewById(android.R.id.content);
        //初始化Keyboard
        if (baseKey.getKeyStyle() == null) {
            baseKey.setKeyStyle(new NumberKeyStyle(mContext));
        }
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
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
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

    /**
     * 隐藏系统键盘
     */
    private void hideSystemSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
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
        if (mKeyClickListener != null) {
            mKeyClickListener.onKey(primaryCode, keyCodes);
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


    public interface KeyClickListener {
        void onKey(int primaryCode, int[] keyCodes);
    }
}
