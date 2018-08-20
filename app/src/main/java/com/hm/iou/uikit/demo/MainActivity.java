package com.hm.iou.uikit.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hm.iou.uikit.HMCountDownTextView;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.PullDownRefreshImageView;
import com.hm.iou.uikit.datepicker.CustomDatePicker;
import com.hm.iou.uikit.demo.tabview.BottomTabViewActivity;
import com.hm.iou.uikit.dialog.DialogCommonKnow;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.uikit.dialog.PermissionDialog;
import com.hm.iou.uikit.keyboard.key.ABCKey;
import com.hm.iou.uikit.loading.LoadingDialogUtil;
import com.hm.iou.uikit.keyboard.input.HMKeyboardEditText;
import com.hm.iou.uikit.keyboard.key.NumberKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    CustomDatePicker mDatePicker;
    String TIME_TODAY;
    EditText mEtClear;
    HMKeyboardEditText mEtTestInputNum;
    HMKeyboardEditText mEtTestInputABC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        initStatusBarDarkFont(true);
        setContentView(R.layout.activity_main);

        mEtClear = findViewById(R.id.et_clear);
        mEtTestInputNum = findViewById(R.id.edit_testInputNum);
        mEtTestInputNum.bindKeyBoardView(getWindow(), new NumberKey(this));
        mEtTestInputABC = findViewById(R.id.edit_testInputABC);
        mEtTestInputABC.bindKeyBoardView(getWindow(), new ABCKey(this));
        initDatePick();

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
        findViewById(R.id.btn_showDatePick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker.show(TIME_TODAY);
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


        findViewById(R.id.btn_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PermissionDialog.Builder(MainActivity.this)
                        .setTitle("开启位置权限")
                        .setMessage("我们需要获得该权限，才能为您提供省市头条信息及附近律师。")
                        .setPermissionIcon(R.mipmap.uikit_icon_header_man)
                        .setOnClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    Toast.makeText(MainActivity.this, "允许", Toast.LENGTH_SHORT).show();
                                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    Toast.makeText(MainActivity.this, "不允许", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setCancelable(false)
                        .create().show();
            }
        });

        findViewById(R.id.btn_inputCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InputCodeActivity.class));
            }
        });
        findViewById(R.id.btn_bottomTabView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BottomTabViewActivity.class));
            }
        });
    }

    private void initDatePick() {
        //初始化时间选择控件
//        final TimeUtil timeUtil = TimeUtil.getInstance(TimeUtil.SimpleDateFormatEnum.DateFormatForApp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date today = new Date(System.currentTimeMillis());
        TIME_TODAY = simpleDateFormat.format(today);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, 10);
        Date endTime = calendar.getTime();
        String TIME_END = simpleDateFormat.format(endTime);

        mDatePicker = new CustomDatePicker(MainActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                String strTime = time.split(" ")[0];
                Toast.makeText(MainActivity.this, strTime, Toast.LENGTH_SHORT).show();
            }
        }, TIME_TODAY, TIME_END); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        mDatePicker.showSpecificTime(false); // 不显示时和分
        mDatePicker.setIsLoop(false); // 不允许循环滚动
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
