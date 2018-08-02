package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hm.iou.uikit.keyboard.HMInputNumberView;
import com.hm.iou.uikit.keyboard.HMInputPasswordView;
import com.hm.iou.uikit.keyboard.HMKeyBoardView;

/**
 * @author syl
 * @time 2018/5/21 下午7:14
 */
public class InputPsdActivity extends AppCompatActivity {

    HMKeyBoardView mHMKeyBoardView;
    HMInputPasswordView mInputPasswordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_psd);
        mInputPasswordView = findViewById(R.id.inputPsdView);
        mHMKeyBoardView = findViewById(R.id.keyboardView);
        mInputPasswordView.bindKeyBoardView(mHMKeyBoardView);
        mInputPasswordView.setOnInputNumberListener(new HMInputPasswordView.OnInputNumberListener() {
            @Override
            public void onInputNumberFinish(String number) {
                Toast.makeText(getApplicationContext(), "输入的数字" + number, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_checkFailed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputPasswordView.setError();
                mInputPasswordView.clearInputCode();
            }
        });
    }
}
