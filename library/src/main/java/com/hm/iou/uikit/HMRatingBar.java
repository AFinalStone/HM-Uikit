package com.hm.iou.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * 评分组件，范围从 [1, 5]
 */
public class HMRatingBar extends LinearLayout implements View.OnClickListener {

    private boolean mCanRating;
    private int mRating;

    private ImageView[] mIvStarArr;

    public HMRatingBar(Context context) {
        this(context, null);
    }

    public HMRatingBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMRatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        mIvStarArr = new ImageView[5];

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HMRatingBar);
        mCanRating = ta.getBoolean(R.styleable.HMRatingBar_canRating, true);
        mRating = ta.getInteger(R.styleable.HMRatingBar_rating, 1);
        ta.recycle();

        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(context);
            mIvStarArr[i] = imageView;
            imageView.setImageResource(R.mipmap.uikit_ic_star_selected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            addView(imageView, params);
            int padding = DensityUtil.dp2px(4);
            imageView.setPadding(padding, padding, padding, padding);

            imageView.setTag(i);
            imageView.setOnClickListener(this);
        }

        setRating(mRating);
    }

    @Override
    public void onClick(View v) {
        if (!mCanRating)
            return;
        int index = (Integer) v.getTag();
        setRating(index + 1);
    }

    public void setCanRating(boolean canRating) {
        mCanRating = canRating;
    }

    /**
     * 设置评分
     *
     * @param rating
     */
    public void setRating(int rating) {
        if (rating <= 0 && rating > 5) {
            return;
        }
        for (int i = 0; i < rating; i++) {
            mIvStarArr[i].setImageResource(R.mipmap.uikit_ic_star_selected);
        }
        for (int i = rating; i < mIvStarArr.length; i++) {
            mIvStarArr[i].setImageResource(R.mipmap.uikit_ic_start_unselected);
        }
        mRating = rating;
    }

    public int getRating() {
        return mRating;
    }

}
