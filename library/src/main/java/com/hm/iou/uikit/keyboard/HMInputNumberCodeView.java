package com.hm.iou.uikit.keyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.hm.iou.uikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by syl on 2018/8/1.
 */

public class HMInputNumberCodeView extends FrameLayout {

    private TextView[] mTvList = new TextView[6];//用数组保存6个TextView
    private int mCurrentIndex = 0;
    private OnInputNumberListener mOnInputNumberListener;

    public HMInputNumberCodeView(@NonNull Context context) {
        super(context);
        initView();
    }

    public HMInputNumberCodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HMInputNumberCodeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.uikit_layout_keyboard_input_number_code_view, this, true);
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
                    if (mOnInputNumberListener != null) {
                        mOnInputNumberListener.onInputNumberFinish(strNumber);
                    }
                }

            }
        });
    }

    public void bindKeyBoardView(HMKeyBoardView keyBoardView) {
        keyBoardView.setOnItemClickListener(new HMKeyBoardView.OnItemClickListener() {
            @Override
            public void onNumberCodeClick(String number) {
                if (mCurrentIndex >= 0 && mCurrentIndex < mTvList.length) {
                    //判断输入位置————要小心数组越界
                    mTvList[mCurrentIndex].setText(number);
                    for (int i = 0; i < mTvList.length; i++) {
                        mTvList[i].setEnabled(true);
                        if (i == mCurrentIndex) {
                            mTvList[mCurrentIndex].setSelected(true);
                        } else {
                            mTvList[i].setSelected(false);
                        }
                    }
                    mCurrentIndex++;
                }

            }

            @Override
            public void onDeleteClick() {
                if (mCurrentIndex >= 1 && mCurrentIndex <= mTvList.length) {
                    //判断是否删除完毕————要小心数组越界
                    mCurrentIndex--;
                    mTvList[mCurrentIndex].setText("");
                    for (int i = 0; i < mTvList.length; i++) {
                        mTvList[i].setEnabled(true);
                        if (i == mCurrentIndex - 1) {
                            mTvList[i].setSelected(true);
                        } else {
                            mTvList[i].setSelected(false);
                        }
                    }
                }
            }
        });
    }

    public void setCheckFailed() {
        for (int i = 0; i < 6; i++) {
            mTvList[i].setText("");
            mTvList[i].setEnabled(false);
        }
        mCurrentIndex = 0;
    }

    public void setOnInputNumberListener(OnInputNumberListener onInputNumberListener) {
        this.mOnInputNumberListener = onInputNumberListener;
    }

    public interface OnInputNumberListener {
        /**
         * 6位数字的密码输入完毕
         *
         * @param number
         */
        void onInputNumberFinish(String number);

    }

}
