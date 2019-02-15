package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hm.iou.uikit.datepicker.WheelPicker;
import com.hm.iou.uikit.tabbar.BottomTabBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjy on 18/5/1.<br>
 */

public class TabActivity extends AppCompatActivity {

    BottomTabBar bottomTabBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        bottomTabBar = findViewById(R.id.bottom_tab_bar);

        bottomTabBar.init(getSupportFragmentManager(), 1080, 1920)
//                .setImgSize(70, 70)
//                .setFontSize(14)
//                .setTabPadding(5, 0, 5)
//                .setChangeColor(Color.parseColor("#FF00F0"),Color.parseColor("#CCCCCC"))
                .addTabItem("第一项", R.mipmap.tab_ic_tab_home_sel, R.mipmap.tab_ic_tab_home_un, OneFragment.class)
                .addTabItem("第二项", R.mipmap.tab_ic_tab_mine_sel, R.mipmap.tab_ic_tab_mine_un, TwoFragment.class)
//                .isShowDivider(true)
//                .setDividerColor(Color.parseColor("#FF0000"))
//                .setTabBarBackgroundColor(Color.parseColor("#00FF0000"))
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name, View view) {

                    }
                })
                .setSpot(1, true);



        WheelPicker picker = findViewById(R.id.custom_picker);
        List<WheelPicker.IPickerItem> list = new ArrayList<>();
        for (int i=0; i<30; i++) {
            final int j = i;
            list.add(new WheelPicker.IPickerItem() {
                @Override
                public String getItemName() {
                    return j + "日";
                }

                @Override
                public Object getItemValue() {
                    return j;
                }
            });
        }
        picker.setDataList(list);
        picker.setCurrentPosition(14);
        picker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelSelected(Object item, int position) {
                System.out.println("onWheelSelected: " + position);
            }
        });

    }
}
