package com.hm.iou.uikit.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.uikit.decoration.StickyRecyclerView;
import com.hm.iou.uikit.decoration.listener.PowerGroupListener;
import com.hm.iou.uikit.decoration.view.PowerfulStickyDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syl
 * @time 2019/7/16 1:42 PM
 */

public class StickyRecyclerActivity extends AppCompatActivity {

    StickyRecyclerView mRvSticky;
    BaseQuickAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_recycler);
        mRvSticky = findViewById(R.id.stickyRecyclerView);
        mRvSticky.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            list.add("悬浮条目测试" + i);
//        }
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.activity_sticky_recycler_list_item, list) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.text, item);
            }
        };
        View viewHeader = LayoutInflater.from(StickyRecyclerActivity.this).inflate(R.layout.activity_sticky_recycler_header, null);
        mAdapter.addHeaderView(viewHeader);
        PowerfulStickyDecoration pfd = PowerfulStickyDecoration.Builder.init(new PowerGroupListener() {
            @Override
            public View getGroupTitleView(int position) {
                View view = LayoutInflater.from(StickyRecyclerActivity.this).inflate(R.layout.activity_sticky_recycler_list_sticky_item, null);
                return view;
            }

            @Override
            public String getGroupName(int position) {
                return "";
            }
        }).setHeaderCount(mAdapter.getHeaderLayoutCount()).build();
        mRvSticky.addItemDecoration(pfd);
        mRvSticky.setAdapter(mAdapter);
    }
}
