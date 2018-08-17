package com.hm.iou.uikit.keyboard.input;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.keyboard.HMKeyboardView;
import com.hm.iou.uikit.keyboard.key.BaseKey;
import com.hm.iou.uikit.keyboard.key.DefaultKeyStyle;

/**
 * Created by syl on 2018/8/1.
 */

public class HMInputCodeView extends FrameLayout implements KeyboardView.OnKeyboardActionListener {

    //和自定义输入法相关的变量
    protected Window mWindow;                //当前窗口
    protected LinearLayout mLlKeyboardView;  //自定义输入法键盘
    protected ViewGroup mRootView;           //自定义输入法所在的根布局文件
    protected BaseKey mKeyboard;             //按键

    private TextView[] mTvList = new TextView[6];//用数组保存6个TextView
    private ImageView[] mIvList = new ImageView[6];//用数组保存6个ImageView
    private int mCurrentIndex = 0;
    private OnInputCodeListener mOnInputCodeListener;
    private boolean mIsHideCode = false;

    public HMInputCodeView(@NonNull Context context) {
        super(context);
        initView();
    }

    public HMInputCodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HMInputCodeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.uikit_layout_keyboard_input_code_view, this, true);
        mTvList[0] = findViewById(R.id.tv_code1);
        mTvList[1] = findViewById(R.id.tv_code2);
        mTvList[2] = findViewById(R.id.tv_code3);
        mTvList[3] = findViewById(R.id.tv_code4);
        mTvList[4] = findViewById(R.id.tv_code5);
        mTvList[5] = findViewById(R.id.tv_code6);
        mTvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {

                    String strNumber = "";//每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱

                    for (int i = 0; i < 6; i++) {
                        strNumber += mTvList[i].getText().toString().trim();
                    }
                    if (mOnInputCodeListener != null) {
                        mOnInputCodeListener.onInputCodeFinish(strNumber);
                    }
                }

            }
        });

        for (int i = 0; i < mTvList.length; i++) {
            mTvList[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRootView != null) {
                        //隐藏系统键盘
                        hideSystemSoftKeyboard();
                        //显示自定义键盘
                        showSoftKeyboard();
                    }
                }
            });
        }
        //默认第一个输入框被选中
        mTvList[mCurrentIndex].setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_selected);

        //黑色原点控件
        mIvList[0] = findViewById(R.id.iv_hide1);
        mIvList[1] = findViewById(R.id.iv_hide2);
        mIvList[2] = findViewById(R.id.iv_hide3);
        mIvList[3] = findViewById(R.id.iv_hide4);
        mIvList[4] = findViewById(R.id.iv_hide5);
        mIvList[5] = findViewById(R.id.iv_hide6);
    }

    public void initKeyBoardView(Window window, BaseKey baseKey) {
        mWindow = window;
        mRootView = mWindow.getDecorView().findViewById(android.R.id.content);
        //初始化Keyboard
        if (baseKey.getKeyStyle() == null) {
            baseKey.setKeyStyle(new DefaultKeyStyle(getContext()));
        }
        mKeyboard = baseKey;
        showSoftKeyboard();
    }

    public void setHideCode(boolean isHideCode) {
        mIsHideCode = isHideCode;
    }

    public void clearInputCode() {
        for (int i = 0; i < 6; i++) {
            mTvList[i].setText("");
            mTvList[i].setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_normal);
            mIvList[i].setVisibility(INVISIBLE);
        }
        mCurrentIndex = 0;
        mTvList[mCurrentIndex].setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_selected);
    }

    public void setError() {
        for (int i = 0; i < 6; i++) {
            mTvList[i].setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_error);
        }
    }

    public void setOnInputCodeListener(OnInputCodeListener onInputCodeListener) {
        this.mOnInputCodeListener = onInputCodeListener;
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

        if (primaryCode == getResources().getInteger(R.integer.uikit_keyboard_delete)) {
            if (mCurrentIndex >= 1 && mCurrentIndex <= mTvList.length) {
                //判断是否删除完毕————要小心数组越界
                mCurrentIndex--;
                mTvList[mCurrentIndex].setText("");
                mIvList[mCurrentIndex].setVisibility(INVISIBLE);
                for (int i = 0; i < mTvList.length; i++) {
                    if (i == mCurrentIndex) {
                        mTvList[i].setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_selected);
                    } else {
                        mTvList[i].setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_normal);
                    }
                }
            }
        } else if (primaryCode == getResources().getInteger(R.integer.uikit_keyboard_point)) {

        } else {
            if (mCurrentIndex >= 0 && mCurrentIndex < mTvList.length) {
                //判断输入位置————要小心数组越界
                String number = Character.toString((char) primaryCode);
                mTvList[mCurrentIndex].setText(number);
                if (mIsHideCode) {
                    mIvList[mCurrentIndex].setVisibility(VISIBLE);
                }
                mCurrentIndex++;
                for (int i = 0; i < mTvList.length; i++) {
                    if (i == mCurrentIndex) {
                        mTvList[i].setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_selected);
                    } else {
                        mTvList[i].setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_normal);
                    }
                }
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
