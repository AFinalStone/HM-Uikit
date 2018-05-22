package com.hm.iou.uikit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.hm.iou.uikit.handler.WeakReferenceHandler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 倒计时控件
 *
 * @author syl
 * @time 2018/5/21 上午10:15
 */
public class HMCountDownTextView extends AppCompatTextView implements View.OnClickListener {
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
    private String mStrTextCountDown;
    /**
     * 按钮点击事件
     */
    private OnClickListener mOnClickListener;

    /**
     * 更新显示的文本
     */
    private WeakReferenceHandler<HMCountDownTextView> mHandler;

    public HMCountDownTextView(Context context) {
        super(context);
        initFields(null, 0);
        initView();
        initHandler(this);
    }

    public HMCountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFields(attrs, 0);
        initView();
        initHandler(this);
    }

    public HMCountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFields(attrs, defStyleAttr);
        initView();
        initHandler(this);
    }

    public void initFields(AttributeSet attrs, int defStyleAttr) {

        if (attrs != null) {
            TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HMCountDownTextView, defStyleAttr, 0);
            try {
                mStrText = getText().toString().trim();
                mStrTextCountDown = styledAttributes.getString(R.styleable.HMCountDownTextView_textCountDown);
            } finally {
                styledAttributes.recycle();
            }
        }

        if (TextUtils.isEmpty(mStrText)) {
            mStrText = getResources().getString(R.string.uikit_get_check_code);
        }
        if (TextUtils.isEmpty(mStrTextCountDown)) {
            mStrTextCountDown = getResources().getString(R.string.uikit_get_check_code_count_down);
        }
    }

    /**
     * 初始化操作
     */
    private void initView() {
        setText(mStrText);
        setOnClickListener(this);
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
    public void setLength(long mLength) {
        this.mLength = mLength;
    }

    /**
     * 设置未点击时显示的文字
     *
     * @param mStrText
     */
    public void setBeforeText(String mStrText) {
        this.mStrText = mStrText;
    }

    /**
     * 设置未点击后显示的文字
     *
     * @param mStrText
     */
    public void setAfterText(String mStrText) {
        this.mStrTextCountDown = mStrTextCountDown;
    }

    /**
     * 设置监听按钮点击事件
     *
     * @param onclickListener
     */
    @Override
    public void setOnClickListener(OnClickListener onclickListener) {
        if (onclickListener instanceof HMCountDownTextView) {
            super.setOnClickListener(onclickListener);
        } else {
            this.mOnClickListener = onclickListener;
        }
    }

    /**
     * 点击按钮后的操作
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        start();
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    /**
     * 开始倒计时
     */
    public void start() {
        initTimer();
        this.setText(mLength / 1000 + mStrTextCountDown);
        this.setEnabled(false);
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
}