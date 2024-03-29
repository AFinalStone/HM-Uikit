package com.hm.iou.uikit.decoration.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hm.iou.uikit.decoration.listener.OnGroupTitleClickListener;
import com.hm.iou.uikit.decoration.listener.PowerGroupListener;
import com.hm.iou.uikit.decoration.util.CacheUtil;
import com.hm.iou.uikit.decoration.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用分割线实现悬浮
 * @author syl
 * @time 2019/3/13 3:24 PM
 */

public class PowerfulStickyDecoration extends BaseDecoration {

    private Paint mGroutPaint;

    /**
     * 缓存图片
     */
    private CacheUtil<Bitmap> mBitmapCache = new CacheUtil<>();

    /**
     * 缓存View
     */
    private CacheUtil<View> mHeadViewCache = new CacheUtil<>();

    private PowerGroupListener mGroupListener;

    private PowerfulStickyDecoration(PowerGroupListener groupListener) {
        super();
        this.mGroupListener = groupListener;
        //设置悬浮栏的画笔---mGroutPaint
        mGroutPaint = new Paint();
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //绘制
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);
            if (isFirstInGroup(position) || isFirstInRecyclerView(position, i)) {
                int viewBottom = childView.getBottom();
                //top 决定当前顶部第一个悬浮Group的位置
                int bottom = Math.max(mGroupTitleHeight, childView.getTop() + parent.getPaddingTop());
                if (position + 1 < itemCount) {
                    //下一组的第一个View接近头部
                    if (isLastLineInGroup(parent, position) && viewBottom < bottom) {
                        bottom = viewBottom;
                    }
                }
                drawDecoration(c, position, left, right, bottom);
            } else {
                //绘制分割线
                drawDivide(c, parent, childView, position, left, right);
            }
        }
    }

    /**
     * 绘制悬浮框
     *
     * @param c        Canvas
     * @param position position
     * @param left     left
     * @param right    right
     * @param bottom   bottom
     */
    private void drawDecoration(Canvas c, int position, int left, int right, int bottom) {
        c.drawRect(left, bottom - mGroupTitleHeight, right, bottom, mGroutPaint);
        //根据position获取View
        View groupView;
        int firstPositionInGroup = getFirstInGroupWithCash(position);
        if (mHeadViewCache.get(firstPositionInGroup) == null) {
            groupView = getGroupTitleView(firstPositionInGroup);
            if (groupView == null) {
                return;
            }
            measureAndLayoutView(groupView, left, right);
            mHeadViewCache.put(firstPositionInGroup, groupView);
        } else {
            groupView = mHeadViewCache.get(firstPositionInGroup);
        }
        Bitmap bitmap;
        if (mBitmapCache.get(firstPositionInGroup) != null) {
            bitmap = mBitmapCache.get(firstPositionInGroup);
        } else {
            bitmap = Bitmap.createBitmap(groupView.getDrawingCache());
            mBitmapCache.put(firstPositionInGroup, bitmap);
        }
        c.drawBitmap(bitmap, left, bottom - mGroupTitleHeight, null);
        if (mOnGroupTitleClickListener != null) {
            setClickInfo(groupView, left, bottom, position);
        }
    }

    /**
     * 对view进行测量和布局
     *
     * @param groupView groupView
     * @param left      left
     * @param right     right
     */
    private void measureAndLayoutView(View groupView, int left, int right) {
        groupView.setDrawingCacheEnabled(true);
        //手动对view进行测量，指定groupView的高度、宽度
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(right, mGroupTitleHeight);
        groupView.setLayoutParams(layoutParams);
        groupView.measure(
                View.MeasureSpec.makeMeasureSpec(right, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(mGroupTitleHeight, View.MeasureSpec.EXACTLY));
        groupView.layout(left, 0 - mGroupTitleHeight, right, 0);
    }

    /**
     * 点击的位置信息
     *
     * @param groupView
     * @param parentBottom
     * @param position
     */
    private void setClickInfo(View groupView, int parentLeft, int parentBottom, int position) {
        int parentTop = parentBottom - mGroupTitleHeight;
        List<ClickInfo.DetailInfo> list = new ArrayList<>();
        List<View> viewList = ViewUtil.getChildViewWithId(groupView);
        for (View view : viewList) {
            int top = view.getTop() + parentTop;
            int bottom = view.getBottom() + parentTop;
            int left = view.getLeft() + parentLeft;
            int right = view.getRight() + parentLeft;
            list.add(new ClickInfo.DetailInfo(view.getId(), left, right, top, bottom));
        }
        ClickInfo clickInfo = new ClickInfo(parentBottom, list);
        clickInfo.mGroupId = groupView.getId();
        stickyHeaderPosArray.put(position, clickInfo);
    }

    /**
     * 获取组名
     *
     * @param position position
     * @return 组名
     */
    @Override
    String getGroupName(int position) {
        if (mGroupListener != null) {
            return mGroupListener.getGroupName(position);
        } else {
            return null;
        }
    }

    /**
     * 获取组View
     *
     * @param position position
     * @return 组名
     */
    private View getGroupTitleView(int position) {
        if (mGroupListener != null) {
            return mGroupListener.getGroupTitleView(position);
        } else {
            return null;
        }
    }

    /**
     * 是否使用缓存
     *
     * @param b b
     */
    public void setCacheEnable(boolean b) {
        mHeadViewCache.isCacheable(b);
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        mHeadViewCache.clean();
        mBitmapCache.clean();
    }

    /**
     * 通知重新绘制
     * 使用场景：网络图片加载后调用
     *
     * @param recyclerView recyclerView
     * @param position     position
     */
    public void notifyRedraw(RecyclerView recyclerView, View viewGroup, int position) {
        viewGroup.setDrawingCacheEnabled(false);
        int firstPositionInGroup = getFirstInGroupWithCash(position);
        mBitmapCache.remove(firstPositionInGroup);
        mHeadViewCache.remove(firstPositionInGroup);
        int left = recyclerView.getPaddingLeft();
        int right = recyclerView.getWidth() - recyclerView.getPaddingRight();
        measureAndLayoutView(viewGroup, left, right);
        mHeadViewCache.put(firstPositionInGroup, viewGroup);
        recyclerView.invalidate();
    }

    public static class Builder {
        PowerfulStickyDecoration mDecoration;

        private Builder(PowerGroupListener listener) {
            mDecoration = new PowerfulStickyDecoration(listener);
        }

        public static Builder init(PowerGroupListener listener) {
            return new Builder(listener);
        }

        /**
         * 设置Group高度
         *
         * @param groutHeight 高度
         * @return this
         */
        public Builder setGroupTitleHeight(int groutHeight) {
            mDecoration.mGroupTitleHeight = groutHeight;
            return this;
        }


        /**
         * 设置Group背景
         *
         * @param background 背景色
         */
        public Builder setGroupTitleBackground(@ColorInt int background) {
            mDecoration.mGroupTitleBackground = background;
            mDecoration.mGroutPaint.setColor(mDecoration.mGroupTitleBackground);
            return this;
        }

        /**
         * 设置分割线高度
         *
         * @param height 高度
         * @return this
         */
        public Builder setDivideHeight(int height) {
            mDecoration.mDivideHeight = height;
            return this;
        }

        /**
         * 设置分割线颜色
         *
         * @param color color
         * @return this
         */
        public Builder setDivideColor(@ColorInt int color) {
            mDecoration.mDivideColor = color;
            return this;
        }

        /**
         * 设置点击事件
         *
         * @param listener 点击事件
         * @return this
         */
        public Builder setOnClickListener(OnGroupTitleClickListener listener) {
            mDecoration.setOnGroupTitleClickListener(listener);
            return this;
        }

        /**
         * 重置span
         *
         * @param recyclerView      recyclerView
         * @param gridLayoutManager gridLayoutManager
         * @return this
         */
        public Builder resetSpan(RecyclerView recyclerView, GridLayoutManager gridLayoutManager) {
            mDecoration.resetSpan(recyclerView, gridLayoutManager);
            return this;
        }

        /**
         * 是否使用缓存
         *
         * @param b
         * @return
         */
        public Builder setCacheEnable(boolean b) {
            mDecoration.setCacheEnable(b);
            return this;
        }

        /**
         * 设置头部数量
         * 用于顶部Header不需要设置悬浮的情况（仅LinearLayoutManager）
         *
         * @param headerCount
         * @return
         */
        public Builder setHeaderCount(int headerCount) {
            if (headerCount >= 0) {
                mDecoration.mHeaderCount = headerCount;
            }
            return this;
        }

        public PowerfulStickyDecoration build() {
            return mDecoration;
        }
    }

}