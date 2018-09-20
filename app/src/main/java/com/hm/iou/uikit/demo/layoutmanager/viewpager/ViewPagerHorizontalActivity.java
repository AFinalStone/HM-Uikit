package com.hm.iou.uikit.demo.layoutmanager.viewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import com.hm.iou.uikit.demo.R;
import com.hm.iou.uikit.demo.layoutmanager.layoutmanagergroup.align.AlignHorizontallyLayoutManager;
import com.hm.iou.uikit.demo.layoutmanager.layoutmanagergroup.align.OnAlignLayoutManagerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * AlignHorizontallyLayoutManager
 */
public class ViewPagerHorizontalActivity extends AppCompatActivity {
    private static final String TAG = "ViewPagerActivity";
    private RecyclerView mRecyclerView;
    private ScrollViewPagerAdapter mAdapter;
    AlignHorizontallyLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_manager_view_pager);

        initView();

        initListener();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new AlignHorizontallyLayoutManager(this, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ScrollViewPagerAdapter();
        List<IViewPagerBean> list = new ArrayList<>();
        ViewPagerBean bean = new ViewPagerBean();
        bean.setImageResId(R.mipmap.bg_1);
        list.add(bean);
        bean = new ViewPagerBean();
        bean.setImageResId(R.mipmap.bg_2);
        list.add(bean);
        bean = new ViewPagerBean();
        bean.setImageResId(R.mipmap.bg_3);
        list.add(bean);
        bean = new ViewPagerBean();
        bean.setImageResId(R.mipmap.bg_4);
        list.add(bean);
        mAdapter.setNewData(list);
        mAdapter.setHeaderView(LayoutInflater.from(this).inflate(R.layout.item_layout_manager_view_pager_header_footer, null));
        mAdapter.setFooterView(LayoutInflater.from(this).inflate(R.layout.item_layout_manager_view_pager_header_footer, null));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        mLayoutManager.setOnAlignLayoutListener(new OnAlignLayoutManagerListener() {

            @Override
            public void onInitComplete() {
                Log.e(TAG, "onInitComplete");
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }

            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e(TAG, "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
            }


        });
    }


}
