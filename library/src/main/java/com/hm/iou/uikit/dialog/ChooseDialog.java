package com.hm.iou.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.uikit.R;
import com.hm.iou.uikit.SmoothCheckBox;

import java.util.ArrayList;
import java.util.List;


public class ChooseDialog extends Dialog {

    private ChooseDialog(@NonNull Context context) {
        super(context);
    }

    private ChooseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    private ChooseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        private Context mContext;

        private String mTitle;
        private String mTitleAction;
        private boolean mCancelable = true;
        private boolean mCancelableOnTouchOutside = true;
        private View.OnClickListener mTitleActionClickListener;
        private List<MenuItem> mList;
        private int mCheckedIndex = -1;
        private OnClickListener mItemClickListener;

        public Builder(Context context) {
            mContext = context;
            mList = new ArrayList<>();
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setTitleAction(String titleActionLabel, View.OnClickListener listener) {
            mTitleAction = titleActionLabel;
            mTitleActionClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder setCancelableOnTouchOutside(boolean cancelableOnTouchOutside) {
            mCancelableOnTouchOutside = cancelableOnTouchOutside;
            return this;
        }

        public Builder addItem(MenuItem item) {
            mList.add(item);
            return this;
        }

        public Builder addItem(String title, String desc) {
            return addItem(new MenuItem(title, desc));
        }

        public Builder setCheckedIndex(int index) {
            mCheckedIndex = index;
            return this;
        }

        public Builder setOnItemClickListener(OnClickListener listener) {
            mItemClickListener = listener;
            return this;
        }

        /**
         * 创建dialog
         *
         * @return
         */
        public ChooseDialog create() {
            final ChooseDialog dialog = new ChooseDialog(mContext, com.hm.iou.uikit.R.style.UikitActionSheetDialogStyle);
            // 获取Dialog布局
            View view = LayoutInflater.from(mContext).inflate(R.layout.uikit_dialog_choose_interest, null);
            TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
            TextView tvAction = view.findViewById(R.id.tv_dialog_action);
            tvTitle.setText(mTitle);
            if (!TextUtils.isEmpty(mTitleAction)) {
                tvAction.setText(mTitleAction);
                tvAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (mTitleActionClickListener != null) {
                            mTitleActionClickListener.onClick(v);
                        }
                    }
                });
            }

            //获取设备的宽度
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            view.setMinimumWidth(dm.widthPixels);

            RecyclerView recyclerView = view.findViewById(R.id.recyclerview_dialog);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            final MenuAdapter menuAdapter = new MenuAdapter(mList);
            recyclerView.setAdapter(menuAdapter);
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.set(0, 0, 0, dip2px(mContext, 16));
                }
            });
            menuAdapter.setCheckedIndex(mCheckedIndex);
            menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    menuAdapter.setCheckedIndex(position);
                    if (mItemClickListener != null) {
                        mItemClickListener.onClick(dialog, position);
                    }
                    dialog.dismiss();
                }
            });

            // 定义Dialog布局和参数
            dialog.setContentView(view);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCancelableOnTouchOutside);

            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
            return dialog;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    private static class MenuAdapter extends BaseQuickAdapter<MenuItem, BaseViewHolder> {

        private int mCheckedIndex = -1;

        public MenuAdapter(@Nullable List<MenuItem> data) {
            super(R.layout.uikit_dialog_item_choose, data);
        }

        public void setCheckedIndex(int index) {
            mCheckedIndex = index;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(BaseViewHolder helper, MenuItem item) {
            helper.setText(R.id.tv_dialog_item_title, item.title);
            helper.setText(R.id.tv_dialog_item_desc, item.desc);
            helper.setChecked(R.id.cb_dialog_item, mCheckedIndex == helper.getAdapterPosition() ? true : false);
            SmoothCheckBox smoothCheckBox = helper.getView(R.id.cb_dialog_item);
            smoothCheckBox.setOnClickListener(null);
        }
    }

    public static class MenuItem {

        public String title;
        public String desc;

        public MenuItem(String title, String desc) {
            this.title = title;
            this.desc = desc;
        }
    }

}
