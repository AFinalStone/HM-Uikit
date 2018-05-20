package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hm.iou.uikit.keyboard.PasswordView;
import com.hm.iou.uikit.tabbar.BottomTabBar;

/**
 * Created by hjy on 18/5/1.<br>
 */

public class PasswordActivity extends AppCompatActivity {

    PasswordView passwordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        passwordView = findViewById(R.id.passwordView);
        passwordView.setTitleText("请输入交易密码");
        passwordView.setOnPasswordViewListener(new PasswordView.OnPasswordViewListener() {
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
