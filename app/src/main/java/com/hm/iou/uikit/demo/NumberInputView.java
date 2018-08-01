package com.hm.iou.uikit.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by syl on 2018/8/1.
 */

public class NumberInputView extends FrameLayout {

    private static final int mDefaultIndex = -1;

    private GridView mGridView;
    private ArrayList<Map<String, String>> mListValue;

    private TextView[] mTvList = new TextView[6];//用数组保存6个TextView
    private int mCurrentIndex = mDefaultIndex;

    public NumberInputView(@NonNull Context context) {
        super(context);
        initView();
    }

    public NumberInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NumberInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_num_input_view, this, false);
        addView(view);
    }

}
