package com.hm.iou.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;


/**
 * Created by liuling on 2017/11/28.
 * <p>
 * 该类为项目公用Dialog弹窗，包含单按钮、双按钮，使用规则和AlertDialog相似（如果只有一个按钮时，请实现setPositiveButton方法）
 */

public class IOSAlertDialog extends Dialog {

    private IOSAlertDialog(@NonNull Context context) {
        super(context);
    }

    private IOSAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    private IOSAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public static class Builder {

        private Context mContext;
        private String mTitle;
        private String mMessage;
        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private OnClickListener mPositiveButtonClickListener;
        private OnClickListener mNegativeButtonClickListener;
        private boolean flagCancelable = true;


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

        /**
         * 设置确定按钮
         *
         * @param positiveButtonText          确认按钮的文字
         * @param positiveButtonClickListener 确认按钮的回调监听
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText, OnClickListener positiveButtonClickListener) {
            this.mPositiveButtonText = positiveButtonText;
            this.mPositiveButtonClickListener = positiveButtonClickListener;
            return this;
        }

        /**
         * 设置取消按钮
         *
         * @param negativeButtonText          取消按钮的文字
         * @param negativeButtonClickListener 取消按钮的回调监听
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText, OnClickListener negativeButtonClickListener) {
            this.mNegativeButtonText = negativeButtonText;
            this.mNegativeButtonClickListener = negativeButtonClickListener;
            return this;
        }


        public Builder setCancelable(boolean flagCancelable) {
            this.flagCancelable = flagCancelable;
            return this;
        }

        /**
         * 创建dialog
         *
         * @return
         */
        private IOSAlertDialog create() {

            final IOSAlertDialog dialog = new IOSAlertDialog(mContext, R.style.UikitAlertDialogStyle);
            // 获取Dialog布局
            View layout = LayoutInflater.from(mContext).inflate(
                    R.layout.uikit_layout_ios_alert_dialog, null);
            LinearLayout lLayout_bg = (LinearLayout) layout.findViewById(R.id.lLayout_bg);

            if (mTitle != null) {
                layout.findViewById(R.id.txt_title).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.txt_title)).setText(mTitle);
            }
            if (mMessage != null) {
                layout.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.txt_msg)).setText(mMessage);
            }
            if (mPositiveButtonText != null) {
                layout.findViewById(R.id.btn_pos).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.btn_pos)).setText(mPositiveButtonText);
                if (mPositiveButtonClickListener != null) {
                    layout.findViewById(R.id.btn_pos).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            mPositiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            }
            if (mNegativeButtonText != null) {
                layout.findViewById(R.id.btn_neg).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.btn_neg)).setText(mNegativeButtonText);
                if (mNegativeButtonClickListener != null) {
                    (layout.findViewById(R.id.btn_neg)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            mNegativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            dialog.setCancelable(flagCancelable);
            dialog.setContentView(layout);
            // 调整dialog背景大小
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                    .getWidth() * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));
            return dialog;
        }

        public IOSAlertDialog show() {
            final IOSAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
