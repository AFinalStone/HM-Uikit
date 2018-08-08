package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hm.iou.uikit.keyboard.HMInputNumberView;
import com.hm.iou.uikit.keyboard.HMKeyBoardView;
import com.hm.iou.uikit.keyboard.OnInputCodeListener;

/**
 * @author syl
 * @time 2018/5/21 下午7:14
 */
public class InputNumActivity extends AppCompatActivity {

    HMKeyBoardView mHMKeyBoardView;
    HMInputNumberView mInputNumView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_num);
        mInputNumView = findViewById(R.id.inputNumView);
        mHMKeyBoardView = findViewById(R.id.keyboardView);
        mInputNumView.bindKeyBoardView(mHMKeyBoardView);
        mInputNumView.setOnInputCodeListener(new OnInputCodeListener() {
            @Override
            public void onInputCodeFinish(String code) {
                Toast.makeText(getApplicationContext(), "输入的数字" + code, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_checkFailed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputNumView.setError();
                mInputNumView.clearInputCode();
            }
        });
    }
}
