package com.hm.iou.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;


/**
 * @author syl
 * @time 2018/5/22 下午5:57
 */
public class DialogCommonKnow extends Dialog {

    private DialogCommonKnow(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context mContext;
        private String mTitle;
        private String mMsg;
        private String mBtnText;
        private boolean mCancelable = true;
        private boolean mCancelableOnTouchOutside = true;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMsg(String msg) {
            this.mMsg = msg;
            return this;
        }

        public Builder setBtnText(String btnText) {
            this.mBtnText = btnText;
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

        private DialogCommonKnow createDialog() {

            final DialogCommonKnow mDialog = new DialogCommonKnow(mContext, R.style.UikitAlertDialogStyle);

            View view = LayoutInflater.from(mContext).inflate(R.layout.uikit_dialog_common_konw, null);
            View background = view.findViewById(R.id.background);
            TextView tv_title = view.findViewById(R.id.tv_title);
            TextView tv_msg = view.findViewById(R.id.tv_msg);
            TextView tv_know = view.findViewById(R.id.tv_know);
            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            tv_know.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            if (!TextUtils.isEmpty(mTitle)) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(mTitle);
            }
            if (!TextUtils.isEmpty(mMsg)) {
                tv_msg.setVisibility(View.VISIBLE);
                tv_msg.setText(mMsg);
            }
            if (!TextUtils.isEmpty(mBtnText)) {
                tv_know.setText(mBtnText);
            }
            // 定义Dialog布局和参数
            // 调整dialog背景大小
            Window dialogWindow = mDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);

            mDialog.setContentView(view);
            mDialog.setCancelable(mCancelable);
            mDialog.setCanceledOnTouchOutside(mCancelableOnTouchOutside);
            // 调整dialog背景大小
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            background.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));

            return mDialog;
        }

        public DialogCommonKnow show() {
            DialogCommonKnow dialog = createDialog();
            dialog.show();
            return dialog;
        }

    }


}
