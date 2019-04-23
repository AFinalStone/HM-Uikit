package com.hm.iou.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * @author syl
 * @time 2019/4/23 7:47 PM
 */
public class ShapedImageView extends android.support.v7.widget.AppCompatImageView {

    public static final int SHAPE_RECT = 0;
    public static final int SHAPE_CIRCLE = 2;
    public static final int SHAPE_ROUND_RECT = 1;

    private static final int DEFAULT_SHAPE = SHAPE_RECT;
    private static final int DEFAULT_BACKGROUND = Color.WHITE;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;
    private static final int DEFAULT_RADIUS = 0xA;
    private static final int DEFAULT_TEXT_SIZE = 0xE;
    private static final int DEFAULT_MIN_PADDING = 0x5;

    private int mShapeMode;
    private int mRadius;
    private int mTextSize;
    private int mTextColor;
    private int mTextStyle;
    private int mBorderWidth;
    private int mBorderColor;
    private int mMinimumSize;
    private int mBackgroundColor;
    private CharSequence mText;

    private Rect mTargetRect;
    private Typeface mTypeface;
    private Paint mPaint, mBorderPaint;
    private TextPaint mTextPaint;

    public ShapedImageView(Context context) {
        this(context, null);
    }

    public ShapedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HmShapedImageView);
        mShapeMode = ta.getInt(R.styleable.HmShapedImageView_shape_mode, DEFAULT_SHAPE);
        mTextStyle = ta.getInt(R.styleable.HmShapedImageView_textStyle, Typeface.NORMAL);
        mTextColor = ta.getColor(R.styleable.HmShapedImageView_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = ta.getDimensionPixelSize(R.styleable.HmShapedImageView_textSize, sp2px(context, DEFAULT_TEXT_SIZE));
        mText = ta.getString(R.styleable.HmShapedImageView_text);
        mBackgroundColor = ta.getColor(R.styleable.HmShapedImageView_backgroundColor, DEFAULT_BACKGROUND);
        mRadius = ta.getDimensionPixelSize(R.styleable.HmShapedImageView_round_radius, DEFAULT_RADIUS);
        mBorderWidth = ta.getDimensionPixelSize(R.styleable.HmShapedImageView_borderWidth, 0);
        mBorderColor = ta.getColor(R.styleable.HmShapedImageView_borderColor, DEFAULT_BORDER_COLOR);
        ta.recycle();
        initValues();
    }

    private void initValues() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setText(CharSequence s) {
        mText = s;
        invalidate();
    }

    public String getText() {
        return TextUtils.isEmpty(mText) ? "" : mText.toString();
    }

    public void setShapeType(Shape type) {
        if (Shape.Circle == type) {
            mShapeMode = SHAPE_CIRCLE;
            invalidate();
            return;
        }
        if (Shape.Rect == type) {
            mShapeMode = SHAPE_RECT;
            invalidate();
            return;
        }
        if (Shape.RoundRect == type) {
            mShapeMode = SHAPE_ROUND_RECT;
            invalidate();
            return;
        }
        mShapeMode = DEFAULT_SHAPE;
        invalidate();
    }

    public int getShapeMode() {
        return mShapeMode;
    }

    public void setBorderWidth(int dp) {
        mBorderWidth = dp2px(getContext(), dp);
    }

    public void setBorderWidthPx(int px) {
        mBorderWidth = px;
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
    }

    public void setRadius(int dp) {
        mRadius = dp2px(getContext(), dp);
    }

    public void setRadiusPx(int px) {
        mRadius = px;
    }

    public void setBackgroundColor(int bgColor) {
        mBackgroundColor = bgColor;
    }

    public void setTextSize(int sp) {
        mTextSize = sp2px(getContext(), sp);
    }

    public void setTextStyle(int textStyle) {
        mTextStyle = textStyle;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        boolean isAtMost = (widthMode < 0 || heightMode < 0);

        int suggestedMinimumHeight = getMeasuredHeight() == 0 ? getMinimumSize() : getMeasuredHeight() + (mBorderWidth << 1);
        int width = measureDimension(getMeasuredWidth() + (mBorderWidth << 1), widthMeasureSpec);
        int height = measureDimension(suggestedMinimumHeight, heightMeasureSpec);

        if (getDrawable() != null && TextUtils.isEmpty(getText()) && isAtMost) {
            Bitmap src = ((BitmapDrawable) getDrawable()).getBitmap();
            width = Math.max(src.getWidth(), getMinimumSize());
            height = Math.max(src.getHeight(), getMinimumSize());
        }

        if (mShapeMode == SHAPE_CIRCLE) {
            width = Math.min(width, height);
            height = Math.min(width, height);
        }
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int measuredSize, int measureSpec) {
        int result;
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.EXACTLY:
                result = measuredSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(getMinimumSize(), specSize);
                break;
            default:
                result = specSize;
                break;
        }
        return result;
    }

    private int getMinimumSize() {
        if (mMinimumSize == 0) {
            Paint textPaint = new TextPaint();
            textPaint.setTextSize(mTextSize);
            int textHeight = (int) (textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top);
            int textWidth = (int) textPaint.measureText(getText());
            int padding = dp2px(getContext(), (DEFAULT_MIN_PADDING << 1));
            mMinimumSize = Math.max(textWidth, textHeight) + padding + (mBorderWidth << 1);
        }
        return mMinimumSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawShape(canvas);
        drawText(canvas);
    }

    private void drawShape(Canvas canvas) {
        switch (getShapeMode()) {
            case SHAPE_CIRCLE:
                drawCircle(canvas);
                break;
            case SHAPE_RECT:
                drawRect(canvas);
                break;
            case SHAPE_ROUND_RECT:
                drawRoundRect(canvas);
                break;
            default:
                drawCircle(canvas);
                break;
        }
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(mBackgroundColor);
        float innerStartX = getMeasuredWidth() >> 1;
        float innerStartY = getMeasuredHeight() >> 1;
        float innerRadius = (getMeasuredHeight() - 2 * mBorderWidth) >> 1;
        canvas.drawCircle(innerStartX, innerStartY, innerRadius, mPaint);
        drawBitmap(canvas, Shape.Circle);

        float startX = getMeasuredWidth() >> 1;
        float startY = getMeasuredHeight() >> 1;
        float radius = ((getMeasuredHeight() - 2 * mBorderWidth) >> 1) + mBorderWidth / 2;
        if (mBorderWidth > 0) {
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
            canvas.drawCircle(startX, startY, radius, mBorderPaint);
        }
    }


    private void drawBitmap(Canvas canvas, Shape shape) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            canvas.save();
            /**
             * 把默认的src图片资源作为画布来进行缩放绘制
             */
            //获取drawable的宽和高
            int dWidth = drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight();
            //创建bitmap
            Bitmap src = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            float scale = 1.0f;
            //创建画布
            Canvas drawCanvas = new Canvas(src);
            //按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；
            if (mShapeMode == SHAPE_ROUND_RECT) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                scale = Math.max(getWidth() * 1.0f / dWidth, getHeight()
                        * 1.0f / dHeight);
            } else {
                scale = getWidth() * 1.0F / Math.min(dWidth, dHeight);
            }
            //根据缩放比例，设置bounds，相当于缩放图片了
            drawable.setBounds(0, 0, (int) (scale * dWidth),
                    (int) (scale * dHeight));
            drawable.draw(drawCanvas);

            src = zoom(src, getMeasuredWidth(), getMeasuredHeight());
            Bitmap shadeBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), src.getConfig());
            Canvas shadeCanvas = new Canvas(shadeBitmap);

            if (shape == Shape.Circle) {
                shadeCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, getMeasuredWidth() / 2, mPaint);
            }

            if (shape == Shape.Rect) {
                float innerStartX = mBorderWidth == 0 ? 0.0f : (mBorderWidth >> 1);
                float innerStartY = mBorderWidth == 0 ? 0.0f : (mBorderWidth >> 1);
                shadeCanvas.drawRect(innerStartX, innerStartY, getMeasuredWidth(), getMeasuredHeight(), mPaint);
            }

            if (shape == Shape.RoundRect) {
                float innerStartX = mBorderWidth == 0 ? 0.0f : mBorderWidth;
                float innerStartY = mBorderWidth == 0 ? 0.0f : mBorderWidth;
                float innerEndX = getMeasuredWidth() - mBorderWidth;
                float innerEndY = getMeasuredHeight() - mBorderWidth;
                shadeCanvas.drawRoundRect(new RectF(innerStartX, innerStartY, innerEndX, innerEndY), mRadius, mRadius, mPaint);
            }

            canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.drawBitmap(src, 0, 0, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(shadeBitmap, 0, 0, mPaint);
            mPaint.setXfermode(null);
            canvas.restore();
        }

    }

    private void drawRect(Canvas canvas) {
        mPaint.setColor(mBackgroundColor);
        float innerStartX = mBorderWidth == 0 ? 0.0f : (mBorderWidth >> 1);
        float innerStartY = mBorderWidth == 0 ? 0.0f : (mBorderWidth >> 1);
        canvas.drawRect(innerStartX, innerStartY, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        drawBitmap(canvas, Shape.Rect);

        float startX = mBorderWidth == 0 ? 0.0f : (mBorderWidth >> 1);
        float startY = mBorderWidth == 0 ? 0.0f : (mBorderWidth >> 1);
        float endX = getMeasuredWidth() - (mBorderWidth >> 1);
        float endY = getMeasuredHeight() - (mBorderWidth >> 1);
        if (mBorderWidth > 0) {
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
            canvas.drawRect(startX, startY, endX, endY, mBorderPaint);
        }
    }

    private void drawRoundRect(Canvas canvas) {
        mPaint.setColor(mBackgroundColor);
        float startX = mBorderWidth == 0 ? 0.0f : ((float) mBorderWidth) / 2;
        float startY = mBorderWidth == 0 ? 0.0f : ((float) mBorderWidth) / 2;
        float endX = getMeasuredWidth() - (((float) mBorderWidth) / 2);
        float endY = getMeasuredHeight() - (((float) mBorderWidth) / 2);

        if (mBorderWidth > 0) {
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
            canvas.drawRoundRect(new RectF(startX, startY, endX, endY), mRadius, mRadius, mBorderPaint);
        }

        float innerStartX = mBorderWidth == 0 ? 0.0f : mBorderWidth;
        float innerStartY = mBorderWidth == 0 ? 0.0f : mBorderWidth;
        float innerEndX = getMeasuredWidth() - mBorderWidth;
        float innerEndY = getMeasuredHeight() - mBorderWidth;
        int innerRadius = mRadius + px2dp(getContext(), mRadius / 2);
        canvas.drawRoundRect(new RectF(innerStartX, innerStartY, innerEndX, innerEndY), innerRadius, innerRadius, mPaint);
        drawBitmap(canvas, Shape.RoundRect);
    }

    private void drawText(Canvas canvas) {
        if (mTargetRect == null) {
            mTargetRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
        if (mTextPaint == null) {
            mTextPaint = new TextPaint();
        }

        Typeface typeface = (mTypeface == null) ? Typeface.create(Typeface.SANS_SERIF, mTextStyle) : mTypeface;
        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (mTargetRect.bottom + mTargetRect.top - fontMetrics.bottom - fontMetrics.top) >> 1;
        canvas.drawText(getText(), mTargetRect.centerX(), baseline, mTextPaint);
    }

    public enum Shape {
        Circle, Rect, RoundRect;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static Bitmap zoom(Bitmap bitmap, int targetWidth, int targetHeight) {
        float scaleVal = getScaleVal(bitmap, targetWidth, targetHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleVal, scaleVal);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    private static float getScaleVal(Bitmap bitmap, float targetWidth, float targetHeight) {
        if (bitmap == null) {
            return 0;
        }
        try {
            int srcWidth = bitmap.getWidth();
            int srcHeight = bitmap.getHeight();
            float scaleVal = Math.max(targetWidth / srcWidth, targetHeight / srcHeight);
            return scaleVal;
        } catch (Exception e) {
            return 0;
        }

    }
}
