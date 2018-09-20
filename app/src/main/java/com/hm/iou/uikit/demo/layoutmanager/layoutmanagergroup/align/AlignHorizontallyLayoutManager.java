package com.hm.iou.uikit.demo.layoutmanager.layoutmanagergroup.align;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */

public class AlignHorizontallyLayoutManager extends LinearLayoutManager {
    private static final String TAG = "AlignVerticalLayoutManager";
    private HorizontallySnapHelper mPagerSnapHelper;
    private OnAlignLayoutManagerListener mOnAlignLayoutManagerListener;
    private RecyclerView mRecyclerView;
    private int mDrift;//位移，用来判断移动方向

    public AlignHorizontallyLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
        init();
    }

    public AlignHorizontallyLayoutManager(Context context, boolean reverseLayout) {
        super(context, LinearLayoutManager.HORIZONTAL, reverseLayout);
        init();
    }

    private void init() {
        mPagerSnapHelper = new HorizontallySnapHelper();
    }


    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        this.mRecyclerView = view;
        mRecyclerView.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
//
    }

    /**
     * 滑动状态的改变
     * 缓慢拖拽-> SCROLL_STATE_DRAGGING
     * 快速滚动-> SCROLL_STATE_SETTLING
     * 空闲状态-> SCROLL_STATE_IDLE
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View viewIdle = mPagerSnapHelper.findSnapView(this);
                if (viewIdle != null) {
                    int positionIdle = getPosition(viewIdle);
                    if (mOnAlignLayoutManagerListener != null) {
                        mOnAlignLayoutManagerListener.onPageSelected(positionIdle, positionIdle == getItemCount() - 1);
                    }
                }

                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                View viewDrag = mPagerSnapHelper.findSnapView(this);
                if (viewDrag != null) {
                    int positionDrag = getPosition(viewDrag);
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                View viewSettling = mPagerSnapHelper.findSnapView(this);
                if (viewSettling != null) {
                    int positionSettling = getPosition(viewSettling);
                }
                break;

        }
    }


    /**
     * 监听竖直方向的相对偏移量
     *
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }


    /**
     * 监听水平方向的相对偏移量
     *
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnAlignLayoutListener(OnAlignLayoutManagerListener listener) {
        this.mOnAlignLayoutManagerListener = listener;
    }

    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnAlignLayoutManagerListener != null && getChildCount() == 1) {
                mOnAlignLayoutManagerListener.onInitComplete();
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
            if (mDrift >= 0) {
                if (mOnAlignLayoutManagerListener != null)
                    mOnAlignLayoutManagerListener.onPageRelease(true, getPosition(view));
            } else {
                if (mOnAlignLayoutManagerListener != null)
                    mOnAlignLayoutManagerListener.onPageRelease(false, getPosition(view));
            }

        }
    };
}
