package com.hm.iou.uikit.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.uikit.CircleIndicator;
import com.hm.iou.uikit.datepicker.WheelPicker;
import com.hm.iou.uikit.tabbar.BottomTabBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syl
 * @time 2019/4/1 6:27 PM
 */

public class CircleViewActivity extends AppCompatActivity {

    CircleIndicator mIndicator;
    ViewPager mViewPager;
    HMPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_view);
        mViewPager = findViewById(R.id.viewPager);
        mIndicator = findViewById(R.id.indicator);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("当前索引===" + i);
        }
        mAdapter = new HMPagerAdapter(this, list);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
    }


    public class HMPagerAdapter extends PagerAdapter {

        private List<String> mListData = new ArrayList<>();
        private Context mContext;

        public HMPagerAdapter(Context context, List<String> list) {
            this.mContext = context;
            this.mListData = list;
        }

        @Override
        public int getCount() {
            if (mListData == null) {
                return 0;
            }
            return mListData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(mContext);
            textView.setText(mListData.get(position));
            return textView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

    }
}
