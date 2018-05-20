package com.hm.iou.uikit.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;


import com.hm.iou.uikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 虚拟键盘
 */
public class VirtualKeyboardView extends RelativeLayout {

    Context mContext;

    //因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）
    private GridView mGvKeyBoard;    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能

    private ArrayList<Map<String, String>> mListValue;    //有人可能有疑问，为何这里不用数组了？
    //因为要用Adapter中适配，用数组不能往adapter中填充


    public VirtualKeyboardView(Context mContext) {
        this(mContext, null);
    }

    public VirtualKeyboardView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);

        this.mContext = mContext;

        View view = View.inflate(mContext, R.layout.layout_virtual_keyboard, null);
        mListValue = new ArrayList<>();


        mGvKeyBoard = view.findViewById(R.id.gv_keyBoard);

        initValueList();

        setupView();

        addView(view);      //必须要，不然不显示控件
    }


    public ArrayList<Map<String, String>> getValueList() {
        return mListValue;
    }

    private void initValueList() {

        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", ".");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", ".");
            }
            mListValue.add(map);
        }
    }

    public GridView getGridView() {
        return mGvKeyBoard;
    }

    private void setupView() {
        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(mContext, mListValue);
        mGvKeyBoard.setAdapter(keyBoardAdapter);
    }

}
