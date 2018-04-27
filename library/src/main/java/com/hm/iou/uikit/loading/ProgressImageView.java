package com.hm.iou.uikit.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.hm.iou.uikit.R;


/**
 * Created by AFinalStone on 16/9/27.
 */

public class ProgressImageView extends AppCompatImageView {

    private float rotateDegrees;
    private int frameTime;
    private boolean needToUpdateView;
    private Runnable updateViewRunnable;

    public ProgressImageView(Context context) {
        this(context, null);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setImageResource(R.mipmap.uikit_ios_progress);
        frameTime = 1000 / 12;
        updateViewRunnable = new Runnable() {
            @Override
            public void run() {
                rotateDegrees += 30;
                rotateDegrees = rotateDegrees < 360 ? rotateDegrees : rotateDegrees - 360;
                invalidate();
                if (needToUpdateView) {
                    postDelayed(this, frameTime);
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(rotateDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        needToUpdateView = true;
        post(updateViewRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        needToUpdateView = false;
        super.onDetachedFromWindow();
    }
}
