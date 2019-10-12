package com.hm.iou.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;

/**
 * Created by hjy on 2019/1/14.
 */

public class HMBottomDialog extends Dialog {

    private Builder mBuilder;

    private TextView mTvTitle;
    private FrameLayout mFlBottomView;

    private HMBottomDialog(Builder builder, int style) {
        super(builder.mContext, style);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_dialog_bottom_view);

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
        mFlBottomView = findViewById(R.id.fl_bottom_view);
        if (mBuilder == null) {
            return;
        }
        if (TextUtils.isEmpty(mBuilder.mTitle)) {
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mBuilder.mTitle);
        }
        if (mBuilder.mBottomView == null) {
            mFlBottomView.setVisibility(View.GONE);
        } else {
            mFlBottomView.setVisibility(View.VISIBLE);
            mFlBottomView.addView(mBuilder.mBottomView);
        }
    }

    public static final class Builder {

        private Context mContext;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private CharSequence mTitle;
        private View mBottomView;
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

        public Builder setBottomView(View bottomView) {
            mBottomView = bottomView;
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

        public HMBottomDialog create() {
            final HMBottomDialog dialog = new HMBottomDialog(this, mStyle);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return dialog;
        }

    }

}