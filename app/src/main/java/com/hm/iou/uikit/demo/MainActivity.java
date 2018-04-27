package com.hm.iou.uikit.demo;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.uikit.loading.LoadingDialogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_loading_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialogUtil.showLoading(MainActivity.this);
            }
        });

        findViewById(R.id.btn_alert_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IOSAlertDialog.Builder(MainActivity.this)
                        .setTitle("测试标题")
                        .setMessage("这是文本内容")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }
}
