package com.hm.iou.uikit.keyboard.input;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.uikit.R;

/**
 * Created by syl on 2018/8/1.
 */

public class HMInputCodeView extends FrameLayout {

    InputCodeViewHelper mInputCodeViewHelper;

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
                    if (mInputCodeViewHelper != null) {
                        mInputCodeViewHelper.showSoftKeyboard();
                    }
                }
            });
        }
        //默认第一个输入框被选中
        setSelectedBackground(mTvList[mCurrentIndex]);
        //黑色原点控件
        mIvList[0] = findViewById(R.id.iv_hide1);
        mIvList[1] = findViewById(R.id.iv_hide2);
        mIvList[2] = findViewById(R.id.iv_hide3);
        mIvList[3] = findViewById(R.id.iv_hide4);
        mIvList[4] = findViewById(R.id.iv_hide5);
        mIvList[5] = findViewById(R.id.iv_hide6);
    }

    public void setOnInputCodeListener(OnInputCodeListener onInputCodeListener) {
        this.mOnInputCodeListener = onInputCodeListener;
    }

    public void isHideKeyBoardTitle(boolean isShow) {
        mInputCodeViewHelper.setTitleVisibility(!isShow);
    }


    public void bindKeyBoardView(Window window, Keyboard baseKey) {
        mInputCodeViewHelper = new InputCodeViewHelper(getContext(), window, baseKey);
        mInputCodeViewHelper.showSoftKeyboard();
        mInputCodeViewHelper.setKeyClickListener(new InputCodeViewHelper.KeyClickListener() {
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
                                setSelectedBackground(mTvList[i]);
                            } else {
                                setNormalBackground(mTvList[i]);
                            }
                        }
                        if (mOnInputCodeListener != null) {
                            mOnInputCodeListener.onDelete();
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
                                setSelectedBackground(mTvList[i]);
                            } else {
                                setNormalBackground(mTvList[i]);
                            }
                        }
                    }
                }
            }
        });
    }

    public void setHideCode(boolean isHideCode) {
        mIsHideCode = isHideCode;
    }

    public void clearInputCode() {
        for (int i = 0; i < 6; i++) {
            mTvList[i].setText("");
            setNormalBackground(mTvList[i]);
            mIvList[i].setVisibility(INVISIBLE);
        }
        mCurrentIndex = 0;
        setSelectedBackground(mTvList[mCurrentIndex]);
    }

    public void setError() {
        for (int i = 0; i < 6; i++) {
            setErrorBackground(mTvList[i]);
        }
    }

    private void setSelectedBackground(TextView textView) {
        textView.setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_selected);
    }

    private void setNormalBackground(TextView textView) {
        textView.setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_normal);
    }

    private void setErrorBackground(TextView textView) {
        textView.setBackgroundResource(R.drawable.uikit_keyboard_input_code_bg_error);
    }

}
