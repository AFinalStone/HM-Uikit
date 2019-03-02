package com.hm.iou.uikit.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;

/**
 * Created by hjy on 2019/1/14.
 */

public class HMAlertDialog extends Dialog implements View.OnClickListener {

    public static final int BUTTON_TYPE_MAIN = 1;           //主button
    public static final int BUTTON_TYPE_SECONDARY = 2;      //次button

    public interface OnClickListener {
        void onPosClick();

        void onNegClick();
    }

    private Builder mBuilder;

    private TextView mTvTitle;
    private TextView mTvSubTitle;
    private TextView mTvMsg;
    private FrameLayout mFlCustomView;
    private Button mBtnNeg;
    private Button mBtnPos;

    private HMAlertDialog(Builder builder, int style) {
        super(builder.mContext, style);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_dialog_comm_alert);

        initWindow();
        setBgRadius();

        initContent();
    }

    private void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.8);
        attrs.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attrs);
    }

    /**
     * 设置dialog圆角
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setBgRadius() {
        LinearLayout layoutContent = findViewById(R.id.ll_dialog_content);
        final int bgRadius = (int) (getContext().getResources().getDisplayMetrics().density * 4);
        layoutContent.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), bgRadius);
            }
        });
        layoutContent.setClipToOutline(true);
    }

    private void initContent() {
        mTvTitle = findViewById(R.id.tv_dialog_title);
        mTvSubTitle = findViewById(R.id.tv_dialog_subtitle);
        mTvMsg = findViewById(R.id.txt_dialog_msg);
        mFlCustomView = findViewById(R.id.fl_dialog_custom);
        mBtnPos = findViewById(R.id.btn_dialog_pos);
        mBtnNeg = findViewById(R.id.btn_dialog_neg);

        mBtnPos.setOnClickListener(this);
        mBtnNeg.setOnClickListener(this);

        if (mBuilder == null) {
            return;
        }
        if (TextUtils.isEmpty(mBuilder.mTitle)) {
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mBuilder.mTitle);

            //如果只有标题，没有内容，则调整上下边距
            if (TextUtils.isEmpty(mBuilder.mMessage)) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvTitle.getLayoutParams();
                float density = getContext().getResources().getDisplayMetrics().density;
                params.topMargin = (int) density * 26;
                params.bottomMargin = (int) density * 46;
                mTvTitle.setLayoutParams(params);
            }
        }

        if (TextUtils.isEmpty(mBuilder.mSubTitle)) {
            mTvSubTitle.setVisibility(View.GONE);
        } else {
            mTvSubTitle.setVisibility(View.VISIBLE);
            mTvSubTitle.setText(mBuilder.mSubTitle);
        }

        if (TextUtils.isEmpty(mBuilder.mMessage)) {
            mTvMsg.setVisibility(View.GONE);
        } else {
            mTvMsg.setVisibility(View.VISIBLE);
            mTvMsg.setText(mBuilder.mMessage);
        }

        if (mBuilder.mMessageGravity != 0) {
            mTvMsg.setGravity(mBuilder.mMessageGravity);
        }

        if (mBuilder.mCustomContentView != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mFlCustomView.addView(mBuilder.mCustomContentView, params);
        }

        if (TextUtils.isEmpty(mBuilder.mBtnPosText)) {
            mBtnPos.setVisibility(View.GONE);
        } else {
            mBtnPos.setVisibility(View.VISIBLE);
            mBtnPos.setText(mBuilder.mBtnPosText);
        }

        if (TextUtils.isEmpty(mBuilder.mBtnNegText)) {
            mBtnNeg.setVisibility(View.GONE);

        } else {
            mBtnNeg.setVisibility(View.VISIBLE);
            mBtnNeg.setText(mBuilder.mBtnNegText);
        }

        int btnPosStyle = mBuilder.mBtnPosStyleType;
        //pos button默认设置为主色
        if (btnPosStyle == BUTTON_TYPE_SECONDARY) {
            mBtnPos.setBackgroundResource(R.drawable.uikit_selector_btn_dialog_secondary);
            mBtnPos.setTextColor(mBuilder.mContext.getResources().getColor(R.color.uikit_text_auxiliary));
        } else {
            mBtnPos.setBackgroundResource(R.drawable.uikit_selector_btn_dialog_main);
            mBtnPos.setTextColor(mBuilder.mContext.getResources().getColor(R.color.uikit_text_main_content));
        }


        int btnNegStyle = mBuilder.mBtnNegStyleType;
        //neg button默认设置为次要色
        if (btnNegStyle == BUTTON_TYPE_MAIN) {
            mBtnNeg.setBackgroundResource(R.drawable.uikit_selector_btn_dialog_main);
            mBtnNeg.setTextColor(mBuilder.mContext.getResources().getColor(R.color.uikit_text_main_content));
        } else {
            mBtnNeg.setBackgroundResource(R.drawable.uikit_selector_btn_dialog_secondary);
            mBtnNeg.setTextColor(mBuilder.mContext.getResources().getColor(R.color.uikit_text_auxiliary));
        }
    }

    @Override
    public void onClick(View v) {
        if (mBuilder == null)
            return;
        if (mBuilder.mDismissedOnClickBtn)
            dismiss();
        if (v == mBtnPos) {
            if (mBuilder.mOnClickListener != null) {
                mBuilder.mOnClickListener.onPosClick();
            }
        } else if (v == mBtnNeg) {
            if (mBuilder.mOnClickListener != null) {
                mBuilder.mOnClickListener.onNegClick();
            }
        }
    }

    public static final class Builder {

        private Context mContext;
        private int mStyleResId;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private CharSequence mTitle;
        private CharSequence mSubTitle;
        private CharSequence mMessage;
        private View mCustomContentView;
        private CharSequence mBtnPosText;
        private CharSequence mBtnNegText;
        private int mBtnPosStyleType;
        private int mBtnNegStyleType;
        private OnClickListener mOnClickListener;
        private int mMessageGravity;
        private boolean mDismissedOnClickBtn = true;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setStyle(int styleResId) {
            mStyleResId = styleResId;
            return this;
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

        public Builder setSubTitle(CharSequence title) {
            mSubTitle = title;
            return this;
        }

        public Builder setSubTitle(int titleResId) {
            mSubTitle = mContext.getString(titleResId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder setMessage(int messageResId) {
            mMessage = mContext.getString(messageResId);
            return this;
        }

        public Builder setCustomView(View view) {
            mCustomContentView = view;
            return this;
        }

        public Builder setPositiveButton(CharSequence text) {
            mBtnPosText = text;
            return this;
        }

        public Builder setNegativeButton(CharSequence text) {
            mBtnNegText = text;
            return this;
        }

        public Builder setPositiveButton(int textResId) {
            mBtnPosText = mContext.getString(textResId);
            return this;
        }

        public Builder setNegativeButton(int textResId) {
            mBtnNegText = mContext.getString(textResId);
            return this;
        }

        public Builder setPositiveButtonStyle(int style) {
            mBtnPosStyleType = style;
            return this;
        }

        public Builder setNegativeButtonStyle(int style) {
            mBtnNegStyleType = style;
            return this;
        }

        public Builder setOnClickListener(OnClickListener listener) {
            mOnClickListener = listener;
            return this;
        }

        public Builder setMessageGravity(int gravity) {
            mMessageGravity = gravity;
            return this;
        }

        public Builder setDismessedOnClickBtn(boolean dismissedOnClickBtn) {
            mDismissedOnClickBtn = dismissedOnClickBtn;
            return this;
        }

        public HMAlertDialog create() {
            final HMAlertDialog dialog = new HMAlertDialog(this, mStyleResId != 0 ? mStyleResId : R.style.UikitAlertDialogStyle);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return dialog;
        }

    }

}