package com.hm.iou.uikit.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hm.iou.tools.ToastUtil;
import com.hm.iou.uikit.HMBottomBarView;
import com.hm.iou.uikit.HMCountDownTextView;
import com.hm.iou.uikit.HMDotTextView;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.PullDownRefreshImageView;
import com.hm.iou.uikit.datepicker.CityPickerDialog;
import com.hm.iou.uikit.datepicker.TimePickerDialog;
import com.hm.iou.uikit.demo.layoutmanager.viewpager.ViewPagerHorizontalActivity;
import com.hm.iou.uikit.demo.layoutmanager.viewpager.ViewPagerVerticalActivity;
import com.hm.iou.uikit.demo.tabview.BottomTabViewActivity;
import com.hm.iou.uikit.dialog.HMActionSheetDialog;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.uikit.dialog.HMBottomDialog;
import com.hm.iou.uikit.dialog.HMButtonActionSheetDialog;
import com.hm.iou.uikit.keyboard.input.HMKeyboardEditText;
import com.hm.iou.uikit.keyboard.key.ABCKey;
import com.hm.iou.uikit.keyboard.key.NumberKey;
import com.hm.iou.uikit.loading.LoadingDialogUtil;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class MainActivity extends AppCompatActivity {

    EditText mEtClear;
    HMKeyboardEditText mEtTestInputNum;
    HMKeyboardEditText mEtTestInputABC;

    HMBottomBarView mBottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        initStatusBarDarkFont(true);
        setContentView(R.layout.activity_main);

        EditText etInputMobile = findViewById(R.id.et_inputMobile);
        etInputMobile.setSelection(etInputMobile.length());
        etInputMobile.requestFocus();

        mEtClear = findViewById(R.id.et_clear);
        mEtTestInputNum = findViewById(R.id.edit_testInputNum);
        mEtTestInputNum.bindKeyBoardView(getWindow(), new NumberKey(this));
        mEtTestInputABC = findViewById(R.id.edit_testInputABC);
        mEtTestInputABC.bindKeyBoardView(getWindow(), new ABCKey(this));

        findViewById(R.id.btn_loading_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialogUtil.showLoading(MainActivity.this);
            }
        });

        findViewById(R.id.btn_alert_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HMAlertDialog.Builder(MainActivity.this)
                        .setTitle("标题")
                        .setMessage("这是文本内容")
                        .setSubTitle("这是副标题")
                        .setMessageGravity(Gravity.CENTER)
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(false)
                        .setPositiveButton("确定")
                        .setNegativeButton("取消")
                        .setOnClickListener(new HMAlertDialog.OnClickListener() {
                            @Override
                            public void onPosClick() {
                                Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNegClick() {
                                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();
            }
        });
        findViewById(R.id.btn_dialogTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View bottomView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_layout_bottom_view, null, false);
                new HMBottomDialog.Builder(MainActivity.this)
                        .setTitle("顶部对话框")
                        .setBottomView(bottomView)
                        .setDialogGravity(Gravity.TOP)
                        .setStyle(R.style.UikitAlertDialogStyle_FromTop)
                        .create()
                        .show();
            }
        });
        findViewById(R.id.btn_dialogBottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View bottomView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_layout_bottom_view, null, false);
                new HMBottomDialog.Builder(MainActivity.this)
                        .setTitle("底部对话框")
                        .setBottomView(bottomView)
                        .create()
                        .show();
            }
        });

        PullDownRefreshImageView pullDownRefreshImageView = findViewById(R.id.pd_iv);

        final HMTopBarView topBarView = findViewById(R.id.topbar);
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
                hmCountDownTextView.setLength(10000);
                hmCountDownTextView.startCountDown();
            }
        });
        findViewById(R.id.btn_showDatePick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv = new ImageView(MainActivity.this);
                iv.setImageResource(R.mipmap.ic_launcher);
                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                new TimePickerDialog.Builder(MainActivity.this)
                        .setTitle("还款时间选择")
                        .setHeaderView(iv)
                        .setScrollType(TimePickerDialog.SCROLL_TYPE.MINUTE)
                        .setTimeRange("1978-05-12 12:00:00", "2042-01-18 16:23:39")
//                        .setSelectedTime("2012-11-04 12:23:34")
                        .setOnPickerConfirmListener(new TimePickerDialog.OnTimeConfirmListener() {
                            @Override
                            public void onConfirm(int year, int month, int day, int hour, int minute) {
                                System.out.println(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                            }
                        })
                        .create()
                        .show();
            }
        });

        findViewById(R.id.btn_actionsheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                list.add("一月一付");
                list.add("一季一付");
                list.add("半年一付");
                for (int j = 0; j < 20; j++) {
                    list.add("半年一付");
                }
                new HMActionSheetDialog.Builder(MainActivity.this)
                        .setActionSheetList(list)
                        .setTitle("还款方式")
                        .setSelectedIndex(2)
//                        .setListMaxHeight(0)//可以控制列表的高度，0自适应，其他高度则为最大高度
                        .setCancelable(false)
                        .setDividerPos(0)
                        .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(int index, String value) {
                                Toast.makeText(MainActivity.this, index + " = " + value, Toast.LENGTH_LONG).show();

                            }
                        }).create().show();
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
                        loadingView.showDataEmpty("没有数据额，打个借条吧。");
                    }
                });
            }
        }, 3000);


        findViewById(R.id.btn_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        findViewById(R.id.btn_horizontalRecyclerView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewPagerHorizontalActivity.class));
            }
        });
        findViewById(R.id.btn_verticalRecyclerView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewPagerVerticalActivity.class));
            }
        });
        findViewById(R.id.btn_timePickerDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog.Builder(MainActivity.this).create().show();
            }
        });

        initWheelView();


        mBottomNavView = findViewById(R.id.bottom_navview);
        mBottomNavView.setOnTitleClickListener(new HMBottomBarView.OnTitleClickListener() {
            @Override
            public void onClickTitle() {
                System.out.println("click title");
                mBottomNavView.setEnabled(false);
            }
        });

//        mBottomNavView.showSecondButton("删除", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.showMessage(MainActivity.this, "删除...");
//            }
//        });

        findViewById(R.id.btn_circleView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CircleViewActivity.class));
            }
        });
        HMDotTextView dotTextView = findViewById(R.id.dotText);
        dotTextView.showMoreText();

        findViewById(R.id.btn_open_shape_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShapeViewActivity.class));
            }
        });

        TextView tvBottomLine = findViewById(R.id.tv_bottom_line);
    }

    private void initWheelView() {

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

    public void showCityPicker(View view) {
        new CityPickerDialog.Builder(this)
                .setTitle("城市选择")
                .setOnCityConfirmListener(new CityPickerDialog.OnCityConfirmListener() {
                    @Override
                    public void onConfirm(String province, String city, String district) {
                        System.out.println(province + ", " + city + ", " + district);
                    }
                })
                .create().show();
    }

    public void showButtonActionSheet(View v) {
        new HMButtonActionSheetDialog.Builder(this)
                .addAction(new Pair("事实属实，签署欠条", HMButtonActionSheetDialog.ButtonStyle.Yellow),
                        new Pair("事实属实，签署欠条", HMButtonActionSheetDialog.ButtonStyle.BlackBordered),
                        new Pair("事实属实，签署欠条", HMButtonActionSheetDialog.ButtonStyle.BlackBordered))
                .setOnItemClickListener(new HMButtonActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        ToastUtil.showMessage(MainActivity.this, "click pos: " + position);
                    }
                })
                .setTitle("测试")
                .create().show();
    }

}
