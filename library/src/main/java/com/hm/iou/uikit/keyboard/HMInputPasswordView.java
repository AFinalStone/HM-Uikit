package com.hm.iou.uikit.keyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.uikit.R;

/**
 * Created by syl on 2018/8/1.
 */

public class HMInputPasswordView extends HMInputNumberView {

    protected ImageView[] mIvList = new ImageView[6];//用数组保存6个ImageView

    public HMInputPasswordView(@NonNull Context context) {
        super(context);
    }

    public HMInputPasswordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HMInputPasswordView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void initView() {
        super.initView();
        //黑色原点控件
        mIvList[0] = findViewById(R.id.iv_hide1);
        mIvList[1] = findViewById(R.id.iv_hide2);
        mIvList[2] = findViewById(R.id.iv_hide3);
        mIvList[3] = findViewById(R.id.iv_hide4);
        mIvList[4] = findViewById(R.id.iv_hide5);
        mIvList[5] = findViewById(R.id.iv_hide6);
    }

    @Override
    public void bindKeyBoardView(HMKeyBoardView keyBoardView) {
        keyBoardView.addOnItemClickListener(new HMKeyBoardView.OnItemClickListener() {
            @Override
            public void onNumberCodeClick(String number) {
                if (mCurrentIndex >= 0 && mCurrentIndex < mTvList.length) {
                    //判断输入位置————要小心数组越界
                    mTvList[mCurrentIndex].setText(number);
                    mIvList[mCurrentIndex].setVisibility(VISIBLE);
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
                    mIvList[mCurrentIndex].setVisibility(INVISIBLE);
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

    @Override
    public void clearInputCode() {
        super.clearInputCode();
        for (int i = 0; i < 6; i++) {
            mIvList[i].setVisibility(INVISIBLE);
        }
        mCurrentIndex = 0;
    }

}
