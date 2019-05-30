package com.hm.iou.uikit.demo;

import android.inputmethodservice.Keyboard;
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


    CheckBox mCheckBoxHidePsd;
    CheckBox mCheckHideKeyboardTitle;

    HMInputCodeView mInputCodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        mCheckBoxHidePsd = findViewById(R.id.check_hideCode);
        mCheckHideKeyboardTitle = findViewById(R.id.check_hideKeyboardTitle);
        mCheckBoxHidePsd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mInputCodeView.setHideCode(isChecked);
            }
        });
        mCheckHideKeyboardTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mInputCodeView.isHideKeyBoardTitle(isChecked);
            }
        });
        mInputCodeView = findViewById(R.id.inputCodeVie);
        mInputCodeView.bindKeyBoardView(getWindow(), new NumberKey(this));
        mInputCodeView.setOnInputCodeListener(new OnInputCodeListener() {
            @Override
            public void onInputCodeFinish(String code) {
                ToastUtil.showMessage(InputCodeActivity.this, code);
            }

            @Override
            public void onDelete() {
                ToastUtil.showMessage(InputCodeActivity.this, "删除按钮被点击");
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
