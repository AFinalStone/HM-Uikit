package com.hm.iou.uikit.demo.tabview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.tools.ToastUtil;
import com.hm.iou.uikit.HMBottomTabView;
import com.hm.iou.uikit.demo.R;

/**
 * @author syl
 * @time 2018/8/7 下午7:47
 */
public class BottomTabViewActivity extends AppCompatActivity {

    HMBottomTabView mBottomTabHome;
    HMBottomTabView mBottomTabNews;
    HMBottomTabView mBottomTabRecommend;
    HMBottomTabView mBottomTabPersonal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab_view);
        mBottomTabHome = findViewById(R.id.btv_home);
        mBottomTabNews = findViewById(R.id.btv_news);
        mBottomTabRecommend = findViewById(R.id.btv_recommend);
        mBottomTabPersonal = findViewById(R.id.btv_personal);

        mBottomTabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBottomTabHome.isSelect()) {
                    mBottomTabHome.setSelect(true);
                    mBottomTabNews.setSelect(false);
                    mBottomTabRecommend.setSelect(false);
                    mBottomTabPersonal.setSelect(false);
                }
            }
        });
        mBottomTabNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBottomTabNews.isSelect()) {
                    mBottomTabHome.setSelect(false);
                    mBottomTabNews.setSelect(true);
                    mBottomTabRecommend.setSelect(false);
                    mBottomTabPersonal.setSelect(false);
                }
            }
        });
        mBottomTabRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBottomTabRecommend.isSelect()) {
                    mBottomTabHome.setSelect(false);
                    mBottomTabNews.setSelect(false);
                    mBottomTabRecommend.setSelect(true);
                    mBottomTabPersonal.setSelect(false);
                }
            }
        });
        mBottomTabPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBottomTabPersonal.isSelect()) {
                    mBottomTabHome.setSelect(false);
                    mBottomTabNews.setSelect(false);
                    mBottomTabRecommend.setSelect(false);
                    mBottomTabPersonal.setSelect(true);
                }
            }
        });

        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BottomTabViewActivity.this, IndexAddActivity.class));
            }
        });
    }


}
