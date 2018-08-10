package com.hm.iou.uikit.keyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;

/**
 * Created by syl on 2018/8/1.
 */

public class HMInputNumberView extends FrameLayout {

    protected TextView[] mTvList = new TextView[6];//用数组保存6个TextView
    protected int mCurrentIndex = 0;
    protected HMKeyBoardView mKeyBoardView;
    protected OnInputCodeListener mOnInputCodeListener;
    protected OnItemClickListener mOnItemClickListener;

    public HMInputNumberView(@NonNull Context context) {
        super(context);
        initView();
    }

    public HMInputNumberView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HMInputNumberView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.uikit_layout_keyboard_input_number_view, this, true);
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
            final int position = i;
            mTvList[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mKeyBoardView != null) {
                        mKeyBoardView.setVisibility(VISIBLE);
                    }
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position);
                    }
                }
            });
        }
        mTvList[mCurrentIndex].setSelected(true);
    }

    public void bindKeyBoardView(HMKeyBoardView keyBoardView) {
        mKeyBoardView = keyBoardView;
        keyBoardView.addOnItemClickListener(new HMKeyBoardView.OnItemClickListener() {
            @Override
            public void onNumberCodeClick(String number) {
                if (mCurrentIndex >= 0 && mCurrentIndex < mTvList.length) {
                    //判断输入位置————要小心数组越界
                    mTvList[mCurrentIndex].setText(number);
                    mCurrentIndex++;
                    for (int i = 0; i < mTvList.length; i++) {
                        mTvList[i].setEnabled(true);
                        if (i == mCurrentIndex) {
                            mTvList[mCurrentIndex].setSelected(true);
                        } else {
                            mTvList[i].setSelected(false);
                        }
                    }
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
                        if (i == mCurrentIndex) {
                            mTvList[i].setSelected(true);
                        } else {
                            mTvList[i].setSelected(false);
                        }
                    }
                }
            }
        });
    }

    public void setError() {
        for (int i = 0; i < 6; i++) {
            mTvList[i].setEnabled(false);
        }
    }

    public void clearInputCode() {
        for (int i = 0; i < 6; i++) {
            mTvList[i].setText("");
        }
        mCurrentIndex = 0;
        mTvList[mCurrentIndex].setSelected(true);
    }

    public void setOnInputCodeListener(OnInputCodeListener onInputCodeListener) {
        this.mOnInputCodeListener = onInputCodeListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
