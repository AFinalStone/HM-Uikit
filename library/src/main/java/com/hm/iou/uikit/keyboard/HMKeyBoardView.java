package com.hm.iou.uikit.keyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.hm.iou.uikit.R;

import java.util.ArrayList;


/**
 * 虚拟键盘
 */
public class HMKeyBoardView extends FrameLayout {

    private OnItemClickListener mItemClickListener;

    public HMKeyBoardView(@NonNull Context context) {
        super(context);
        initView();
    }

    public HMKeyBoardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HMKeyBoardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.uikit_layout_keyboard_view, this, true);

        GridView gvKeyBoard = findViewById(R.id.gv_keyBoard);
        final ArrayList<String> listValue = new ArrayList<>();
        //初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                listValue.add(String.valueOf(i));
            } else if (i == 10) {
                listValue.add("");
            } else if (i == 11) {
                listValue.add("0");
            } else if (i == 12) {
                listValue.add("");
            }
        }
        HMKeyBoardAdapter HMKeyBoardAdapter = new HMKeyBoardAdapter(getContext(), listValue);
        gvKeyBoard.setAdapter(HMKeyBoardAdapter);

        gvKeyBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 11 && position != 9) {    //点击0~9按钮
                    if (mItemClickListener != null) {
                        mItemClickListener.onNumberCodeClick(listValue.get(position));
                    }
                } else if (position == 11) {//点击退格键
                    if (mItemClickListener != null) {
                        mItemClickListener.onDeleteClick();
                    }
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        /**
         * 数字键盘被点击
         *
         * @param number
         */
        void onNumberCodeClick(String number);


        /**
         * 删除按钮被点击
         */
        void onDeleteClick();

    }

}
