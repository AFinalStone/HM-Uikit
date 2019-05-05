package com.hm.iou.uikit.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private RecyclerView mRvList;
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
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(attrs);
    }

    private void initContent() {
        mTvTitle = findViewById(R.id.tv_dialog_title);
        mRvList = findViewById(R.id.rv_dialog_list);
        if (mBuilder == null) {
            return;
        }
        if (TextUtils.isEmpty(mBuilder.mTitle)) {
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mBuilder.mTitle);
        }

        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SheetListAdapter(getContext(), mBuilder.mDataList, mBuilder.mCanSelected);
        mAdapter.setSelected(mBuilder.mSelectedIndex);
        mRvList.setAdapter(mAdapter);

        if (mBuilder.mSelectedIndex >= 0) {
            mRvList.getLayoutManager().scrollToPosition(mBuilder.mSelectedIndex);
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
        private OnItemClickListener mListener;
        private boolean mCanSelected = true;        //选中之后，选中的会有打勾标记，否则没有

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

        public HMActionSheetDialog create() {
            final HMActionSheetDialog dialog = new HMActionSheetDialog(this, R.style.UikitAlertDialogStyle_FromBottom);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return dialog;
        }

    }

}