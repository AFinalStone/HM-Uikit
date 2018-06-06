package com.hm.iou.uikit.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hm.iou.uikit.HMCountDownTextView;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.PullDownRefreshImageView;
import com.hm.iou.uikit.dialog.DialogCommonKnow;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
import com.hm.iou.uikit.loading.LoadingDialogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        initStatusBarDarkFont(true);
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
        findViewById(R.id.btn_dialogCommon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogCommonKnow.Builder(MainActivity.this)
                        .setMsg("正文提示信息")
                        .setTitle("标题")
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
        findViewById(R.id.btn_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PasswordCustomerInputActivity.class));
            }
        });
        findViewById(R.id.btn_hindShowPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PasswordHideShowActivity.class));
            }
        });

        findViewById(R.id.tv_getCheckCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HMCountDownTextView hmCountDownTextView = findViewById(R.id.tv_getCheckCode);
                hmCountDownTextView.startCountDown();
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

    /**
     * 使状态栏透明
     */
    protected void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置状态栏字体为深色
     */
    protected void initStatusBarDarkFont(boolean isDarkFont) {
        //全屏
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDarkFont) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }
}
