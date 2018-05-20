package com.hm.iou.uikit.keyboard;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hm.iou.uikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 弹框里面的View
 */
public class PasswordView extends RelativeLayout {

    Context mContext;
    private VirtualKeyboardView mVirtualKeyboardView;
    private TextView[] mTvList;      //用数组保存6个TextView，
    private ImageView[] mIvList;      //用数组保存6个ImageView
    private GridView mGridView;
    private TextView mTvTitle;
    private TextView mTvForgetPsd;
    private ArrayList<Map<String, String>> mListValue;
    private int mCurrentIndex = -1;    //用于记录当前输入密码格位置
    private ImageView mIvClose;
    private OnPasswordViewListener mOnPasswordViewListener;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        View view = View.inflate(context, R.layout.layout_virtual_keyboard_password, null);

        mVirtualKeyboardView = view.findViewById(R.id.virtualKeyboardView);
        mIvClose = view.findViewById(R.id.iv_close);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvForgetPsd = view.findViewById(R.id.tv_forgetPsd);
        mGridView = mVirtualKeyboardView.getGridView();

        initValueList();

        initView(view);

        setupView();

        addView(view);
    }

    private void initView(View view) {


        mTvList = new TextView[6];

        mIvList = new ImageView[6];

        mTvList[0] = view.findViewById(R.id.tv_psd1);
        mTvList[1] = view.findViewById(R.id.tv_psd2);
        mTvList[2] = view.findViewById(R.id.tv_psd3);
        mTvList[3] = view.findViewById(R.id.tv_psd4);
        mTvList[4] = view.findViewById(R.id.tv_psd5);
        mTvList[5] = view.findViewById(R.id.tv_psd6);


        mIvList[0] = view.findViewById(R.id.iv_psd1);
        mIvList[1] = view.findViewById(R.id.iv_psd2);
        mIvList[2] = view.findViewById(R.id.iv_psd3);
        mIvList[3] = view.findViewById(R.id.iv_psd4);
        mIvList[4] = view.findViewById(R.id.iv_psd5);
        mIvList[5] = view.findViewById(R.id.iv_psd6);


        mTvForgetPsd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPasswordViewListener != null) {
                    mOnPasswordViewListener.onForgetPsdClick();
                }
            }
        });

        mIvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordView.this.setVisibility(GONE);
                if (mOnPasswordViewListener != null) {
                    mOnPasswordViewListener.onCloseClick();
                }
            }
        });

    }

    // 这里，我们没有使用默认的数字键盘，因为第10个数字不显示.而是空白
    private void initValueList() {

        mListValue = new ArrayList<>();

        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<String, String>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", "");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", "");
            }
            mListValue.add(map);
        }
    }


    private void setupView() {

        // 这里、重新为数字键盘mGridView设置了Adapter
        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(mContext, mListValue);
        mGridView.setAdapter(keyBoardAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 11 && position != 9) {    //点击0~9按钮
                    if (mCurrentIndex >= -1 && mCurrentIndex < 5) {      //判断输入位置————要小心数组越界

                        ++mCurrentIndex;
                        mTvList[mCurrentIndex].setText(mListValue.get(position).get("name"));
                        mTvList[mCurrentIndex].setVisibility(View.INVISIBLE);
                        mIvList[mCurrentIndex].setVisibility(View.VISIBLE);
                    }
                } else {
                    if (position == 11) {      //点击退格键
                        if (mCurrentIndex - 1 >= -1) {      //判断是否删除完毕————要小心数组越界

                            mTvList[mCurrentIndex].setText("");

                            mTvList[mCurrentIndex].setVisibility(View.VISIBLE);
                            mIvList[mCurrentIndex].setVisibility(View.INVISIBLE);

                            mCurrentIndex--;
                        }
                    }
                }
            }
        });

        mTvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (s.toString().length() == 1) {

                    String strPsd = "";     //每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱

                    for (int i = 0; i < 6; i++) {
                        strPsd += mTvList[i].getText().toString().trim();
                    }

                    //校验支付密码
                    if (mOnPasswordViewListener != null) {
                        mOnPasswordViewListener.onPasswordInputFinish(strPsd);    //接口中要实现的方法，完成密码输入完成后的响应逻辑
                    }
                }
            }
        });
    }

    public void setOnPasswordViewListener(OnPasswordViewListener onPasswordViewListener) {
        this.mOnPasswordViewListener = onPasswordViewListener;
    }

    public VirtualKeyboardView getVirtualKeyboardView() {

        return mVirtualKeyboardView;
    }

    public void setTitleText(CharSequence text) {
        if (mTvTitle != null) {
            mTvTitle.setText(text);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE) {
            for (int i = 5; i >= 0; i--) {
                mTvList[i].setText("");
                mTvList[i].setVisibility(View.VISIBLE);
                mIvList[i].setVisibility(View.INVISIBLE);
            }
            mCurrentIndex = -1;
        }
        super.setVisibility(visibility);
    }

    public interface OnPasswordViewListener {
        /**
         * 6位数字的密码输入完毕
         *
         * @param passWord
         */
        void onPasswordInputFinish(String passWord);

        /**
         * 找回密码回调事件
         */
        void onForgetPsdClick();

        /**
         * 关闭控件回调事件
         */
        void onCloseClick();
    }
}
