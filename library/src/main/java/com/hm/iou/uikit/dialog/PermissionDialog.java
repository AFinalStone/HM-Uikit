package com.hm.iou.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;

/**
 * 权限提醒弹窗
 */
public class PermissionDialog extends Dialog {

    private PermissionDialog(Context context) {
        super(context);
    }

    private PermissionDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    private PermissionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        private Context mContext;
        private String mTitle;
        private String mMessage;
        private int mIconResId;
        private boolean mFlagCancelable = true;
        private DialogInterface.OnClickListener mOnClickListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置弹窗标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        /**
         * 设置弹窗内容
         *
         * @param message 内容
         * @return
         */
        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setPermissionIcon(int icon) {
            mIconResId = icon;
            return this;
        }

        public Builder setCancelable(boolean flagCancelable) {
            this.mFlagCancelable = flagCancelable;
            return this;
        }

        public Builder setOnClickListener(DialogInterface.OnClickListener listener) {
            mOnClickListener = listener;
            return this;
        }

        /**
         * 创建dialog
         *
         * @return
         */
        public PermissionDialog create() {
            final PermissionDialog dialog = new PermissionDialog(mContext, R.style.UikitAlertDialogStyle);
            // 获取Dialog布局
            View layout = LayoutInflater.from(mContext).inflate(R.layout.uikit_layout_permission_alert_dialog, null);

            ((TextView) layout.findViewById(R.id.tv_dialog_title)).setText(mTitle);
            ((TextView) layout.findViewById(R.id.tv_dialog_msg)).setText(mMessage);
            ((ImageView) layout.findViewById(R.id.iv_dialog_img)).setImageResource(mIconResId);

            layout.findViewById(R.id.btn_dialog_neg).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                }
            });

            layout.findViewById(R.id.btn_dialog_pos).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });

            dialog.setCancelable(mFlagCancelable);
            dialog.setContentView(layout);

            // 调整dialog背景大小
            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            layout.setLayoutParams(new FrameLayout.LayoutParams((int) (screenWidth * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));
            return dialog;
        }
    }
}
