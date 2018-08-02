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
import android.widget.Toast;

import com.hm.iou.uikit.keyboard.HMInputNumberCodeView;
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

    HMKeyBoardView mHMKeyBoardView;
    HMInputNumberCodeView mInputNumberCodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_input);
        mInputNumberCodeView = findViewById(R.id.inputNumberView);
        mHMKeyBoardView = findViewById(R.id.keyboardView);
        mInputNumberCodeView.bindKeyBoardView(mHMKeyBoardView);
        mInputNumberCodeView.setOnInputNumberListener(new HMInputNumberCodeView.OnInputNumberListener() {
            @Override
            public void onInputNumberFinish(String number) {
                Toast.makeText(getApplicationContext(), "输入的数字" + number, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_checkFailed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputNumberCodeView.setCheckFailed();
            }
        });
    }
}
