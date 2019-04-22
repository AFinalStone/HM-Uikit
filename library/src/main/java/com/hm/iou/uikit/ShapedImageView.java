package com.hm.iou.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

/**
 * 可以实现圆角和圆角矩形的ImageView
 * SHI
 * 2016年5月12日 19:39:16
 */
public class ShapedImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int SHAPE_MODE_ROUND_RECT = 1;
    private static final int SHAPE_MODE_CIRCLE = 2;

    private int mShapeMode = 0;
    //    private float mRadius = 0;
    private Shape mShape;
    private Paint mPaint;

    /**
     * 圆角大小的默认值
     */
    private static final int BODER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大小
     */
    private float mBorderRadius;


    public ShapedImageView(Context context) {
        super(context);
        init(null);
    }

    public ShapedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShapedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HmShapedImageView);
            mShapeMode = a.getInt(R.styleable.HmShapedImageView_shape_mode, 0);
            mBorderRadius = a.getDimension(R.styleable.HmShapedImageView_round_radius, SHAPE_MODE_CIRCLE);
            a.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_HARDWARE, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (mShapeMode == SHAPE_MODE_CIRCLE) {
            int min = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(min, min);
        }

    }


    private WeakReference<Bitmap> mWeakBitmap;
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;

    @Override
    protected void onDraw(Canvas canvas) {
        //在缓存中取出bitmap
        Bitmap bitmap = mWeakBitmap == null ? null : mWeakBitmap.get();

        if (null == bitmap || bitmap.isRecycled()) {
            //拿到Drawable
            Drawable drawable = getDrawable();
            //获取drawable的宽和高
            int dWidth = drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight();

            if (drawable != null) {
                //创建bitmap
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                        Bitmap.Config.ARGB_8888);
                float scale = 1.0f;
                //创建画布
                Canvas drawCanvas = new Canvas(bitmap);
                //按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；
                if (mShapeMode == SHAPE_MODE_ROUND_RECT) {
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
                if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
                    mMaskBitmap = getBitmap();
                }
                // Draw Bitmap.
                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(mXfermode);
                //绘制形状
                drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
                mPaint.setXfermode(null);
                //将准备好的bitmap绘制出来
                canvas.drawBitmap(bitmap, 0, 0, null);
                //bitmap缓存起来，避免每次调用onDraw，分配内存
                mWeakBitmap = new WeakReference<Bitmap>(bitmap);
            }
        }
        //如果bitmap还存在，则直接绘制即可
        if (bitmap != null) {
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
            return;
        }

    }

    /**
     * 绘制形状
     *
     * @return
     */
    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (mShapeMode == SHAPE_MODE_ROUND_RECT) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                    mBorderRadius, mBorderRadius, paint);
        } else {
            int min = Math.min(getWidth(), getHeight());
            float radius = (float) min / 2;
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        }

        return bitmap;
    }
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        if (changed) {
//            switch (mShapeMode) {
//                case SHAPE_MODE_ROUND_RECT:
//                    break;
//                case SHAPE_MODE_CIRCLE:
//                    int min = Math.min(getWidth(), getHeight());
//                    mRadius = (float) min / 2;
//                    break;
//            }
//            if (mShape == null) {
//                float[] radius = new float[8];
//                Arrays.fill(radius, mRadius);
//                mShape = new RoundRectShape(radius, null, null);
//            }
//            Log.d("mShape", "width======" + mShape.getWidth() + "height======" + mShape.getHeight());
//            mShape.resize(getWidth(), getHeight());
//        }
//    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        int saveCount = canvas.getSaveCount();
//        canvas.save();
//        switch (mShapeMode) {
//            case SHAPE_MODE_ROUND_RECT:
//            case SHAPE_MODE_CIRCLE:
//                if (mShape != null) {
//                    mShape.draw(canvas, mPaint);
//                }
//                break;
//        }
//        canvas.restoreToCount(saveCount);
//        super.onDraw(canvas);
//    }

}
