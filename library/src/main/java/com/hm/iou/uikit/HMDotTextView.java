package com.hm.iou.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by syl on 2019/4/8.
 */

public class HMDotTextView extends View {


    private String mDotText;
    private String mDotMoreText;
    private boolean mIsShowMoreText;
    private int mDotTextColor;
    private int mDotTextSize;
    private Rect mDotTextBound;
    private Rect mDotMoreTextBound;
    private int mDotBackgroundColor;
    private int mRoundWidth;
    private int mRoundHeight;

    private Paint mDotTextPaint;
    private Paint mRoundRectPaint;
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;

    public HMDotTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public HMDotTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public HMDotTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                mRoundWidth = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            default:
                mRoundWidth = getPaddingLeft() + getPaddingRight() + mDotTextBound.width() + dip2px(5);
                if (mIsShowMoreText) {
                    mRoundWidth = getPaddingLeft() + getPaddingRight() + mDotMoreTextBound.width() + dip2px(5);
                }
                if (mRoundWidth < dip2px(16)) {
                    mRoundWidth = dip2px(16);
                }
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                mRoundHeight = getPaddingTop() + getPaddingBottom() + specSize;
                break;
            default:
                mRoundHeight = getPaddingTop() + getPaddingBottom() + mDotTextBound.height() + dip2px(5);
                if (mIsShowMoreText) {
                    mRoundHeight = getPaddingLeft() + getPaddingRight() + mDotMoreTextBound.height() + dip2px(5);
                }
                if (mRoundHeight < dip2px(16)) {
                    mRoundHeight = dip2px(16);
                }
        }
        if (mDotText.length() > 1) {
            int textWidth = mDotTextBound.width();
            mRoundWidth = textWidth + mRoundHeight - textWidth / mDotText.length();
        }
        if (mIsShowMoreText) {
            int textWidth = mDotMoreTextBound.width();
            mRoundWidth = textWidth + mRoundHeight - textWidth / mDotMoreText.length();
        }
        setMeasuredDimension(mRoundWidth, mRoundHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画矩形
        RectF roundRectF = new RectF();
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        roundRectF.set(0, 0, mRoundWidth, mRoundHeight);
        canvas.drawRoundRect(roundRectF, mRoundHeight / 2, mRoundHeight / 2, mRoundRectPaint);

        //画字
        Paint.FontMetrics fontMetrics = mDotTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLintY = (int) (roundRectF.centerY() - top / 2 - bottom / 2);
        if (mIsShowMoreText) {
            baseLintY = (int) roundRectF.centerY();
            canvas.drawText(mDotMoreText, roundRectF.centerX(), baseLintY, mDotTextPaint);
        } else {
            canvas.drawText(mDotText, roundRectF.centerX(), baseLintY, mDotTextPaint);
        }
    }


    public void setText(String dotText) {
        this.mDotText = dotText;
        requestLayout();
    }

    public void showMoreText() {
        mIsShowMoreText = true;
        requestLayout();
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HmDotTextView, defStyleAttr, 0);
        int length = a.getIndexCount();
        for (int i = 0; i < length; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.HmDotTextView_dotText) {
                mDotText = a.getString(attr);
                Log.d("mDotText", mDotText);

            } else if (attr == R.styleable.HmDotTextView_dotTextColor) {
                mDotTextColor = a.getInt(attr, Color.WHITE);
                Log.d("mDotTextColor", "" + mDotTextColor);

            } else if (attr == R.styleable.HmDotTextView_dotTextSize) {// 默认设置为12sp，TypeValue也可以把sp转化为px
                mDotTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                Log.d("mDotTextSize", "" + mDotTextSize);

            } else if (attr == R.styleable.HmDotTextView_dotBackgroundColor) {
                mDotBackgroundColor = a.getInt(attr, getContext().getResources().getColor(R.color.uikit_function_exception));
                Log.d("mBackgroundColor", "" + mDotBackgroundColor);

            } else if (attr == R.styleable.HmDotTextView_dotMoreText) {
                mDotMoreText = a.getString(attr);
                Log.d("mDotMoreText", "" + mDotMoreText);
            } else if (attr == R.styleable.HmDotTextView_dotIsShowMoreText) {
                mIsShowMoreText = a.getBoolean(attr, false);
                Log.d("mIsShowMoreText", "" + mIsShowMoreText);
            }
        }
        a.recycle();
        if (TextUtils.isEmpty(mDotText)) {
            mDotText = "";
        }
        if (TextUtils.isEmpty(mDotMoreText)) {
            mDotMoreText = "...";
        }
        //背景画笔
        mRoundRectPaint = new Paint();
        mRoundRectPaint.setStyle(Paint.Style.FILL);
        mRoundRectPaint.setAntiAlias(true);
        mRoundRectPaint.setColor(mDotBackgroundColor);
        //文字画笔
        mDotTextPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        mDotTextPaint.setColor(mDotTextColor);
        mDotTextPaint.setTextSize(mDotTextSize);
        mDotTextPaint.setStyle(Paint.Style.FILL);
        mDotTextPaint.setAntiAlias(true);
        mDotTextPaint.setTextAlign(Paint.Align.CENTER);
        mDotTextBound = new Rect();
        mDotTextPaint.getTextBounds(mDotText, 0, mDotText.length(), mDotTextBound);
        mDotMoreTextBound = new Rect();
        mDotTextPaint.getTextBounds(mDotMoreText, 0, mDotMoreText.length(), mDotMoreTextBound);
        //实现抗锯齿
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    /**
     * dip转换成px
     */
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
