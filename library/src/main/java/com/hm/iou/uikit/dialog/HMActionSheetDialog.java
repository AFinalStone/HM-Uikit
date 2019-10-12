package com.hm.iou.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.uikit.MaxHeightRecyclerView;
import com.hm.iou.uikit.R;

import java.util.List;

/**
 * Created by hjy on 2019/1/14.
 */

public class HMActionSheetDialog extends Dialog {

    public interface OnItemClickListener {
        void onItemClick(int index, String value);
    }

    private Builder mBuilder;

    private TextView mTvTitle;
    private MaxHeightRecyclerView mRvList;
    private SheetListAdapter mAdapter;

    private HMActionSheetDialog(Builder builder, int style) {
        super(builder.mContext, style);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_dialog_actionsheet_list);

        initWindow();
        initContent();
    }

    private void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.width = WindowManager.LayoutParams.MATCH_PARENT;
        attrs.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(mBuilder.mDialogGravity);
        window.setAttributes(attrs);
    }

    private void initContent() {
        mTvTitle = findViewById(R.id.tv_dialog_title);
        mRvList = findViewById(R.id.rv_dialog_list);
        if (-1 != mBuilder.getListMaxHeight()) {
            mRvList.setMaxHeight(mBuilder.getListMaxHeight());
        }
        if (mBuilder == null) {
            return;
        }
        if (TextUtils.isEmpty(mBuilder.mTitle)) {
            mTvTitle.setVisibility(View.GONE);
            findViewById(R.id.view_dialog_divider).setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mBuilder.mTitle);
        }

        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SheetListAdapter(getContext(), mBuilder.mDataList, mBuilder.mCanSelected);
        mAdapter.setSelected(mBuilder.mSelectedIndex);
        mRvList.setAdapter(mAdapter);

        mRvList.addItemDecoration(new ActionSheetDivider(mBuilder.mContext));

        if (mBuilder.mSelectedIndex >= 0) {
            mRvList.getLayoutManager().scrollToPosition(mBuilder.mSelectedIndex);
        }
    }

    class ActionSheetDivider extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        private final Rect mBounds = new Rect();

        public ActionSheetDivider(Context context) {
            mDivider = ContextCompat.getDrawable(context, R.drawable.uikit_comm_divider_gray);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (parent.getLayoutManager() == null || mDivider == null) {
                return;
            }
            drawVertical(c, parent);
        }

        private void drawVertical(Canvas canvas, RecyclerView parent) {
            canvas.save();
            final int left;
            final int right;
            if (parent.getClipToPadding()) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                canvas.clipRect(left, parent.getPaddingTop(), right,
                        parent.getHeight() - parent.getPaddingBottom());
            } else {
                left = 0;
                right = parent.getWidth();
            }

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                Integer pos = (Integer) child.getTag();
                if (pos == mBuilder.mDividerPos) {
                    parent.getDecoratedBoundsWithMargins(child, mBounds);
                    final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                    final int top = bottom - mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
            }
            canvas.restore();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (mDivider == null) {
                outRect.set(0, 0, 0, 0);
                return;
            }
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }


    class SheetListAdapter extends RecyclerView.Adapter<SheetListAdapter.SheetListViewHolder> implements View.OnClickListener {

        Context mContext;
        List<String> mDataList;
        int mSelectedIndex = -1;
        boolean mCanSelected;

        public SheetListAdapter(Context context, List<String> list, boolean canSelected) {
            mContext = context;
            mDataList = list;
            mCanSelected = canSelected;
        }

        @Override
        public void onClick(View v) {
            dismiss();
            Integer pos = (Integer) v.getTag();
            if (pos == null)
                return;
            mSelectedIndex = pos;
            notifyDataSetChanged();
            if (mBuilder != null && mBuilder.mListener != null) {
                mBuilder.mListener.onItemClick(mSelectedIndex, mDataList.get(mSelectedIndex));
            }
        }

        public void setSelected(int index) {
            mSelectedIndex = index;
            notifyDataSetChanged();
        }

        @Override
        public SheetListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.uikit_item_dialog_actionsheet, null);
            SheetListViewHolder viewHolder = new SheetListViewHolder(view);
            viewHolder.itemView.setOnClickListener(this);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SheetListViewHolder holder, int position) {
            holder.tvName.setText(mDataList.get(position));
            if (mCanSelected) {
                holder.tvName.setTextColor(mSelectedIndex == position ?
                        mContext.getResources().getColor(R.color.uikit_text_main_content) :
                        mContext.getResources().getColor(R.color.uikit_text_sub_content));
            } else {
                holder.tvName.setTextColor(mContext.getResources().getColor(R.color.uikit_text_sub_content));
            }
            if (mCanSelected) {
                holder.ivSelected.setVisibility(mSelectedIndex == position ? View.VISIBLE : View.GONE);
            } else {
                holder.ivSelected.setVisibility(View.GONE);
            }
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mDataList != null ? mDataList.size() : 0;
        }


        class SheetListViewHolder extends RecyclerView.ViewHolder {

            TextView tvName;
            ImageView ivSelected;

            public SheetListViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_item_title);
                ivSelected = itemView.findViewById(R.id.iv_item_selected);
            }
        }

    }

    public static final class Builder {

        private Context mContext;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private CharSequence mTitle;
        private List<String> mDataList;
        private int mSelectedIndex = -1;
        private int mListMaxHeight = -1;//最大高度
        private OnItemClickListener mListener;
        private boolean mCanSelected = true;        //选中之后，选中的会有打勾标记，否则没有
        private int mDividerPos = -1;               //支持在任意位置设置一个分割线，满足某些特殊需求
        private int mStyle = R.style.UikitAlertDialogStyle_FromBottom;
        private int mDialogGravity = Gravity.BOTTOM;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setCancelable(boolean flagCancelable) {
            mCancelable = flagCancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean flagCanceledOnTouchOutside) {
            mCanceledOnTouchOutside = flagCanceledOnTouchOutside;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder setTitle(int titleResId) {
            mTitle = mContext.getString(titleResId);
            return this;
        }

        public Builder setActionSheetList(List<String> dataList) {
            mDataList = dataList;
            return this;
        }

        public Builder setSelectedIndex(int index) {
            mSelectedIndex = index;
            return this;
        }

        public Builder setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setCanSelected(boolean canSelected) {
            mCanSelected = canSelected;
            return this;
        }

        public Builder setDividerPos(int index) {
            mDividerPos = index;
            return this;
        }

        public Builder setListMaxHeight(int listMaxHeight) {
            this.mListMaxHeight = listMaxHeight;
            return this;
        }


        public Builder setStyle(int style) {
            mStyle = style;
            return this;
        }

        public Builder setDialogGravity(int dialogGravity) {
            mDialogGravity = dialogGravity;
            return this;
        }

        public int getListMaxHeight() {
            return mListMaxHeight;
        }

        public HMActionSheetDialog create() {
            final HMActionSheetDialog dialog = new HMActionSheetDialog(this, mStyle);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return dialog;
        }

    }

}