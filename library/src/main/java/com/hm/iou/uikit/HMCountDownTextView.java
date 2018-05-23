package com.hm.iou.uikit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.hm.iou.uikit.handler.WeakReferenceHandler;

import java.util.Timer;
import java.util.TimerTask;

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
     * 开始执行计时的类，可以在每秒实行间隔任务
     */
    private Timer mTimer;
    /**
     * 每秒时间到了之后所执行的任务
     */
    private TimerTask mTimerTask;
    /**
     * 在点击按钮之前按钮所显示的文字，默认是获取验证码
     */
    private String mStrText;
    /**
     * 在开始倒计时之后那个秒数数字之后所要显示的字，默认是秒
     */
    private String mStrTextCountDown = getResources().getString(R.string.uikit_get_check_code_count_down);

    /**
     * 更新显示的文本
     */
    private WeakReferenceHandler<HMCountDownTextView> mHandler;

    public HMCountDownTextView(Context context) {
        this(context, null);
    }

    public HMCountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMCountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initHandler(this);
    }

    /**
     * 防止在倒计时的过程中，按钮被设置为true，开启二次倒计时
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (mTimerTask != null) {
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
        clearTimer();
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
    }


    @SuppressLint("HandlerLeak")
    private void initHandler(final HMCountDownTextView countDownButton) {
        mHandler = new WeakReferenceHandler<HMCountDownTextView>(countDownButton) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (countDownButton != null) {
                    String strCountDown = String.format(mStrTextCountDown, mLength / 1000);
                    countDownButton.setText(strCountDown);
                    mLength -= 1000;
                    if (mLength < 0) {
                        countDownButton.setEnabled(true);
                        countDownButton.setText(mStrText);
                        clearTimer();
                        mLength = 60 * 1000;
                    }
                }
            }
        };
    }

    /**
     * 初始化时间
     */
    private void initTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
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
        this.setEnabled(false);
        this.setText(mLength / 1000 + mStrTextCountDown);
        initTimer();
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    /**
     * 清除倒计时
     */
    private void clearTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}