package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hm.iou.uikit.keyboard.HMKeyBoardPasswordCodeView;


/**
 * @author syl
 * @time 2018/5/21 下午7:14
 */
public class PasswordActivity extends AppCompatActivity {

    HMKeyBoardPasswordCodeView HMKeyBoardPasswordCodeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        HMKeyBoardPasswordCodeView = findViewById(R.id.passwordView);
        HMKeyBoardPasswordCodeView.setTitleText("请输入交易密码");
        HMKeyBoardPasswordCodeView.setOnPasswordViewListener(new HMKeyBoardPasswordCodeView.OnPasswordViewListener() {
            @Override
            public void onPasswordInputFinish(String passWord) {
                Toast.makeText(PasswordActivity.this, passWord, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onForgetPsdClick() {
                Toast.makeText(PasswordActivity.this, "忘记密码", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCloseClick() {
                Toast.makeText(PasswordActivity.this, "关闭密码输入框", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
