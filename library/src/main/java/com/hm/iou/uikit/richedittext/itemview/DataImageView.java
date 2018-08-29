package com.hm.iou.uikit.richedittext.itemview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 自定义ImageView，可以存放Bitmap和Path等信息
 */
public class DataImageView extends android.support.v7.widget.AppCompatImageView {

    //    private boolean showBorder = false; //是否显示边框
//    private int borderColor = Color.GRAY;//边框颜色
//    private int borderWidth = 5;//边框大小

    RichItemData mRichItemData;

    public RichItemData getRichItemData() {
        return mRichItemData;
    }

    public void setRichItemData(RichItemData richItemData) {
        this.mRichItemData = richItemData;
    }

    public DataImageView(Context context) {
        this(context, null);
    }

    public DataImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        if (showBorder) {
//            //画边框
//            Rect rec = canvas.getClipBounds();
//            // 这两句可以使底部和右侧边框更大
//            //rec.bottom -= 2;
//            //rec.right -= 2;
//            //画笔
//            Paint paint = new Paint();
//            paint.setColor(borderColor);//设置颜色
//            paint.setStrokeWidth(borderWidth);//设置画笔的宽度
//            paint.setStyle(Paint.Style.STROKE);//设置画笔的风格-不能设成填充FILL否则看不到图片了
//            canvas.drawRect(rec, paint);
//        }
//    }
}
