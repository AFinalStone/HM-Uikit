package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hm.iou.tools.ToastUtil;
import com.hm.iou.uikit.keyboard.input.HMInputCodeView;
import com.hm.iou.uikit.keyboard.input.OnInputCodeListener;
import com.hm.iou.uikit.keyboard.key.NumberKey;

/**
 * @author syl
 * @time 2018/5/21 下午7:14
 */
public class InputCodeActivity extends AppCompatActivity {


    CheckBox mCheckBox;

    HMInputCodeView mInputCodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        mCheckBox = findViewById(R.id.check_hideCode);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mInputCodeView.setHideCode(isChecked);
            }
        });
        mInputCodeView = findViewById(R.id.inputCodeVie);
        mInputCodeView.initKeyBoardView(getWindow(), new NumberKey(this));
        mInputCodeView.setOnInputCodeListener(new OnInputCodeListener() {
            @Override
            public void onInputCodeFinish(String code) {
                ToastUtil.showMessage(InputCodeActivity.this, code);
            }
        });
        findViewById(R.id.btn_checkFailed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputCodeView.setError();
            }
        });
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputCodeView.clearInputCode();
            }
        });
    }
}
