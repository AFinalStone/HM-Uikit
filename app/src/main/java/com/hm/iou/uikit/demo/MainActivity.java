package com.hm.iou.uikit.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.PullDownRefreshImageView;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
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

        PullDownRefreshImageView pullDownRefreshImageView = findViewById(R.id.pd_iv);

        HMTopBarView topBarView = findViewById(R.id.topbar);
        topBarView.setOnBackClickListener(new HMTopBarView.OnTopBarBackClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }
        });
        topBarView.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                Toast.makeText(MainActivity.this, "click text", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClickImageMenu() {
                Toast.makeText(MainActivity.this, "click image", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_tab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TabActivity.class));
            }
        });

        findViewById(R.id.btn_actionsheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IOSActionSheetTitleDialog.Builder(MainActivity.this)
                        .addSheetItem(IOSActionSheetItem.create("垃圾广告").setItemClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }))
                        .addSheetItem(IOSActionSheetItem.create("政治谣言").setItemClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }))
                        .addSheetItem(IOSActionSheetItem.create("色情图片").setItemClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }))
                        .show();
            }
        });

        final HMLoadingView loadingView = findViewById(R.id.hmloadingview);
        loadingView.showDataLoading();
        loadingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingView.showDataFail("网络加载失败，请检查网络", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "重试。。。。", Toast.LENGTH_LONG).show();
                        loadingView.showDataEmpty("");
                    }
                });
            }
        }, 3000);
    }
}
