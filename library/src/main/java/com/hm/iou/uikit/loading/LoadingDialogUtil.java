package com.hm.iou.uikit.loading;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;

/**
 * description:弹窗浮动加载进度条
 * Created by ysl
 */
public class LoadingDialogUtil {

    /**
     * 显示加载对话框
     *
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showLoading(Activity context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.uikit_layout_ios_loading_dialog, null);
        TextView loadingText = view.findViewById(R.id.tv_loadingMsg);
        loadingText.setText(msg);

        Dialog mLoadingDialog = new Dialog(context, R.style.UikitAlertDialogStyle_LoadingProgress);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    /**
     * 显示默认的加载对话框，显示文案为"请稍等..."
     *
     * @param context
     * @return
     */
    public static Dialog showLoading(Activity context) {
        return showLoading(context, context.getString(R.string.uikit_loading_tip_desc), false);
    }

    /**
     * 关闭加载对话框
     *
     * @param dialog
     */
    public static void dismissLoading(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
