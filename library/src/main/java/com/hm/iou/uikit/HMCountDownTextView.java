package com.hm.iou.uikit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.hm.iou.uikit.handler.WeakReferenceHandler;

/**
 * 倒计时控件
 *
 * @author syl
 * @time 2018/5/21 上午10:15
 */
public class HMCountDownTextView extends AppCompatTextView {
    /**
     * 倒计时时长，默认倒计时时间60秒；
     */
    private long mLength = 60 * 1000;
    /**
     * 在点击按钮之前按钮所显示的文字，默认是获取验证码
     */
    private String mStrText;
    /**
     * 在开始倒计时之后那个秒数数字之后所要显示的字，默认是秒
     */
    private String mStrTextCountDown = getResources().getString(R.string.uikit_get_check_code_count_down);

    /**
     * 是否已经开始倒计时
     */
    private boolean mIsStartCountDown = false;

    /**
     * 更新显示的文本
     */
    private WeakReferenceHandler<HMCountDownTextView> mHandler;

    private Drawable mBg;
    private int mPaddingTop;

    public HMCountDownTextView(Context context) {
        this(context, null);
    }

    public HMCountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMCountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initHandler();
    }

    /**
     * 防止在倒计时的过程中，按钮被设置为true，开启二次倒计时
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (mIsStartCountDown) {
            return;
        }
        super.setEnabled(enabled);
    }

    /**
     * 记得一定要在activity或者fragment消亡的时候清除倒计时，
     * 因为如果倒计时没有完的话子线程还在跑，
     * 这样的话就会引起内存溢出
     */
    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDetachedFromWindow();
    }

    /**
     * 初始化操作
     */
    private void initView() {
        mStrText = getText().toString().trim();
        if (TextUtils.isEmpty(mStrText)) {
            mStrText = getResources().getString(R.string.uikit_get_check_code);
        }
        setText(mStrText);
        mBg = getResources().getDrawable(R.drawable.uikit_btn_get_code_selector);
        float density = getResources().getDisplayMetrics().density;
        mPaddingTop = (int) (density * 10);
    }


    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new WeakReferenceHandler<HMCountDownTextView>(this) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                HMCountDownTextView countDownTextView = mWeakReferenceObject.get();
                if (countDownTextView != null) {
                    String strCountDown = String.format(countDownTextView.mStrTextCountDown, countDownTextView.mLength / 1000 + "");
                    countDownTextView.setText(strCountDown);
                    countDownTextView.mLength -= 1000;
                    if (countDownTextView.mLength < 0) {
                        countDownTextView.mLength = 60 * 1000;
                        countDownTextView.mIsStartCountDown = false;
                        countDownTextView.setEnabled(true);
                        countDownTextView.setText(countDownTextView.mStrText);
                    } else {
                        sendEmptyMessageDelayed(1, 1000);
                    }
                }
            }
        };
    }

    /**
     * 设置倒计时时长
     *
     * @param mLength 默认毫秒
     */
    public void setLength(@NonNull long mLength) {
        this.mLength = mLength;
    }

    /**
     * 设置未点击时显示的文字
     *
     * @param mStrText
     */
    public void setBeforeText(@NonNull String mStrText) {
        this.mStrText = mStrText;
    }

    /**
     * 设置未点击后显示的文字
     *
     * @param mStrText
     */
    public void setAfterText(@NonNull String mStrText) {
        mStrTextCountDown = mStrText;
    }

    /**
     * 开始倒计时
     */
    public void startCountDown() {
        if (mHandler != null) {
            this.setEnabled(false);
            String strCountDown = String.format(mStrTextCountDown, mLength / 1000 + "");
            this.setText(strCountDown);
            mIsStartCountDown = true;
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBg.setBounds(0, mPaddingTop, getWidth(), getHeight() - mPaddingTop);
        mBg.draw(canvas);
        super.onDraw(canvas);
    }
}