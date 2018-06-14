package com.hm.iou.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;


/**
 * @author syl
 * @time 2018/6/11 下午7:50
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
        private View mView;
        private String mPositiveButtonText;
        private int mPositiveButtonTextColor = -1;
        private String mNegativeButtonText;
        private int mNegativeButtonTextColor = -1;
        private OnClickListener mPositiveButtonClickListener;
        private OnClickListener mNegativeButtonClickListener;
        private boolean flagCancelable = true;
        private int mMsgGravity = Gravity.NO_GRAVITY;

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
         * 设置自定义的View
         *
         * @param view
         */
        public Builder setView(View view) {
            this.mView = view;
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

        /**
         * 设置确定按钮的颜色
         *
         * @param positiveButtonTextColor
         * @return
         */
        public Builder setPositiveButtonTextColor(@ColorInt int positiveButtonTextColor) {
            this.mPositiveButtonTextColor = positiveButtonTextColor;
            return this;
        }

        /**
         * 设置取消按钮的颜色
         *
         * @param negativeButtonTextColor
         * @return
         */
        public Builder setNegativeButtonTextColor(@ColorInt int negativeButtonTextColor) {
            this.mNegativeButtonTextColor = negativeButtonTextColor;
            return this;
        }

        public Builder setCancelable(boolean flagCancelable) {
            this.flagCancelable = flagCancelable;
            return this;
        }

        public Builder setGravity(int gravity) {
            mMsgGravity = gravity;
            return this;
        }

        /**
         * 创建dialog
         *
         * @return
         */
        public IOSAlertDialog create() {

            final IOSAlertDialog dialog = new IOSAlertDialog(mContext, R.style.UikitAlertDialogStyle);
            // 获取Dialog布局
            View layout = LayoutInflater.from(mContext).inflate(
                    R.layout.uikit_layout_ios_alert_dialog, null);
            LinearLayout lLayout_bg = layout.findViewById(R.id.lLayout_bg);
            ImageView ivLine01 = layout.findViewById(R.id.iv_line01);
            ImageView ivLine02 = layout.findViewById(R.id.iv_line02);
            if (mTitle != null) {
                layout.findViewById(R.id.txt_title).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.txt_title)).setText(mTitle);
            }
            if (mMessage != null) {
                layout.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);
                TextView tvMsg = layout.findViewById(R.id.txt_msg);
                tvMsg.setText(mMessage);

                if (mMsgGravity != Gravity.NO_GRAVITY) {
                    tvMsg.setGravity(mMsgGravity);
                }
            }
            if (mView != null) {
                FrameLayout frameLayout = layout.findViewById(R.id.fl_view);
                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.addView(mView);
            }
            if (mPositiveButtonText != null) {
                ivLine01.setVisibility(View.VISIBLE);
                Button btnPos = layout.findViewById(R.id.btn_pos);
                if (mPositiveButtonTextColor != -1) {
                    btnPos.setTextColor(mPositiveButtonTextColor);
                }
                btnPos.setVisibility(View.VISIBLE);
                btnPos.setText(mPositiveButtonText);
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
                ivLine01.setVisibility(View.VISIBLE);
                Button btnNeg = layout.findViewById(R.id.btn_neg);
                if (mNegativeButtonTextColor != -1) {
                    btnNeg.setTextColor(mNegativeButtonTextColor);
                }
                btnNeg.setVisibility(View.VISIBLE);
                btnNeg.setText(mNegativeButtonText);
                if (mNegativeButtonClickListener != null) {
                    (layout.findViewById(R.id.btn_neg)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();
                            mNegativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            if (mNegativeButtonText != null && mPositiveButtonText != null) {
                ivLine02.setVisibility(View.VISIBLE);
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
