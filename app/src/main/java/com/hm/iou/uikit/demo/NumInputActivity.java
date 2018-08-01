package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.TextView;

import com.hm.iou.uikit.keyboard.HMKeyBoardAdapter;
import com.hm.iou.uikit.keyboard.HMKeyBoardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author syl
 * @time 2018/5/21 下午7:14
 */
public class NumInputActivity extends AppCompatActivity {

    private static final int mDefaultIndex = -1;

    HMKeyBoardView mHMKeyBoardView;

//    private GridView mGridView;
//    private ArrayList<Map<String, String>> mListValue;
//
//    private TextView[] mTvList = new TextView[6];//用数组保存6个TextView
//    private int mCurrentIndex = mDefaultIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_input);
//
//        findViewById(R.id.btn_checkFailed).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCurrentIndex = mDefaultIndex;
//                for (int i = 0; i < mTvList.length; i++) {
//                    mTvList[i].setEnabled(false);
//                    mTvList[i].setText("");
//                }
//            }
//        });
//
//        mHMKeyBoardView = findViewById(R.id.keyboardView);
//        mTvList[0] = findViewById(R.id.tv_code1);
//        mTvList[1] = findViewById(R.id.tv_code2);
//        mTvList[2] = findViewById(R.id.tv_code3);
//        mTvList[3] = findViewById(R.id.tv_code4);
//        mTvList[4] = findViewById(R.id.tv_code5);
//        mTvList[5] = findViewById(R.id.tv_code6);
//
//        mGridView = mHMKeyBoardView.getGridView();
//        mListValue = new ArrayList<>();
//
//        // 初始化按钮上应该显示的数字
//        for (int i = 1; i < 13; i++) {
//            Map<String, String> map = new HashMap<String, String>();
//            if (i < 10) {
//                map.put("name", String.valueOf(i));
//            } else if (i == 10) {
//                map.put("name", "");
//            } else if (i == 11) {
//                map.put("name", String.valueOf(0));
//            } else if (i == 12) {
//                map.put("name", "");
//            }
//            mListValue.add(map);
//        }
//        // 这里、重新为数字键盘mGridView设置了Adapter
//        HMKeyBoardAdapter keyBoardAdapter = new HMKeyBoardAdapter(this, mListValue);
//        mGridView.setAdapter(keyBoardAdapter);
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position < 11 && position != 9) {    //点击0~9按钮
//                    if (mCurrentIndex >= mDefaultIndex && mCurrentIndex < 5) {      //判断输入位置————要小心数组越界
//                        ++mCurrentIndex;
//                        mTvList[mCurrentIndex].setText(mListValue.get(position).get("name"));
//                    }
//                } else {
//                    if (position == 11) {      //点击退格键
//                        if (mCurrentIndex - 1 >= mDefaultIndex) {      //判断是否删除完毕————要小心数组越界
//                            mTvList[mCurrentIndex].setText("");
//                            mCurrentIndex--;
//                        }
//                    }
//                }
//                for (int i = 0; i < mTvList.length; i++) {
//                    mTvList[i].setEnabled(true);
//                    if (i == mCurrentIndex) {
//                        mTvList[mCurrentIndex].setSelected(true);
//                    } else {
//                        mTvList[i].setSelected(false);
//                    }
//                }
//            }
//        });
    }
}
