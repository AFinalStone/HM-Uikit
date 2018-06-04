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
        Context context;
        String title;
        String msg;

        public Builder(Context context) {
            this.context = context;
        }

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        private DialogCommonKnow CreateDialog() {

            final DialogCommonKnow mDialog = new DialogCommonKnow(context, R.style.UikitAlertDialogStyle);

            View view = LayoutInflater.from(context).inflate(R.layout.uikit_dialog_common_konw, null);
            View background = view.findViewById(R.id.background);
            TextView tv_title = view.findViewById(R.id.tv_title);
            TextView tv_msg = view.findViewById(R.id.tv_msg);
            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_know).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            if (!TextUtils.isEmpty(title)) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(title);
            }
            if (!TextUtils.isEmpty(msg)) {
                tv_msg.setVisibility(View.VISIBLE);
                tv_msg.setText(msg);
            }
            // 定义Dialog布局和参数
            // 调整dialog背景大小
            Window dialogWindow = mDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);

            mDialog.setContentView(view);
            // 调整dialog背景大小
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            background.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));

            return mDialog;
        }

        public void show() {
            CreateDialog().show();
        }

    }


}
