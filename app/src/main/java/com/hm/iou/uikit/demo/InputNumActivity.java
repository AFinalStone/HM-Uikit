package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hm.iou.uikit.keyboard.HMInputNumberView;
import com.hm.iou.uikit.keyboard.HMKeyBoardView;

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
        mInputNumView.setOnInputNumberListener(new HMInputNumberView.OnInputNumberListener() {
            @Override
            public void onInputNumberFinish(String number) {
                Toast.makeText(getApplicationContext(), "输入的数字" + number, Toast.LENGTH_SHORT).show();
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
