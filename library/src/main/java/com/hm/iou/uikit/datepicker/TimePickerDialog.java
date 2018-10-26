package com.hm.iou.uikit.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.hm.iou.uikit.dialog.DialogCommonKnow;

/**
 * Created by syl on 2018/10/23.
 */

public class TimePickerDialog extends Dialog {

    private TimePickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context mContext;
        private String mTitle;
        private int mTitleTextColor;
        private String mLeftBtnText;
        private int mLeftBtnTextColor;
        private String mRightBtnText;
        private int mRightBtnTextColor;

        public Builder(Context context) {
            this.mContext = context;
        }

        public TimePickerDialog.Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public TimePickerDialog.Builder setTitleTextColor(int colorRGB) {
            this.mTitleTextColor = colorRGB;
            return this;
        }

        public void setLeftBtnText(String leftBtnText) {
            this.mLeftBtnText = leftBtnText;
        }

        public void setRightBtnText(String rightBtnText) {
            this.mRightBtnText = rightBtnText;
        }

        private TimePickerDialog createDialog() {

            final TimePickerDialog dialog = new TimePickerDialog(mContext, R.style.UikitAlertDialogStyle);

            View view = LayoutInflater.from(mContext).inflate(R.layout.uikit_dialog_time_picker, null);
//            View background = view.findViewById(R.id.background);
//            TextView tv_title = view.findViewById(R.id.tv_title);
//            TextView tv_msg = view.findViewById(R.id.tv_msg);
//            TextView tv_know = view.findViewById(R.id.tv_know);
//            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDialog.dismiss();
//                }
//            });
//            tv_know.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDialog.dismiss();
//                }
//            });
//            if (!TextUtils.isEmpty(mTitle)) {
//                tv_title.setVisibility(View.VISIBLE);
//                tv_title.setText(mTitle);
//            }
//            if (!TextUtils.isEmpty(mLeftBtnText)) {
//                tv_msg.setVisibility(View.VISIBLE);
//                tv_msg.setText(mMsg);
//            }
//            if (!TextUtils.isEmpty(mRightBtnText)) {
//                tv_know.setText(mBtnText);
//            }
            // 定义Dialog布局和参数
            dialog.setContentView(view);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
            //获取设备的宽度
            WindowManager windowManager = dialogWindow.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            // 设置Dialog最小宽度为屏幕宽度
            view.setMinimumWidth(display.getWidth());

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            return dialog;
        }

        public TimePickerDialog show() {
            TimePickerDialog dialog = createDialog();
            dialog.show();
            return dialog;
        }

    }
}
