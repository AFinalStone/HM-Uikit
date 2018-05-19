package com.hm.iou.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author : syl
 * @Date : 2018/5/19 17:35
 * @E-Mail : shiyaolei@dafy.com
 */
public class HMPasswordEditTextOld extends LinearLayout {

    private String mTextHint;
    private int mTextSize;
    private int mTextColor;
    private Drawable mleftIcomDrawable;
    private Drawable mRightOpenIcomDrawable;
    private Drawable mRightCloseIcomDrawable;

    private EditText mEditText;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;

    public HMPasswordEditTextOld(Context context) {
        this(context, null);
    }

    public HMPasswordEditTextOld(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HMPasswordEditTextOld(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HmPasswordEditText);
        mTextHint = ta.getString(R.styleable.HmPasswordEditText_passwordTextHint);
        mTextSize = ta.getDimensionPixelSize(R.styleable.HmPasswordEditText_passwordTextSize,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())));
        mTextColor = ta.getColor(R.styleable.HmPasswordEditText_passwordTextColor, getResources().getColor(R.color.uikit_password_text_color));
        mleftIcomDrawable = ta.getDrawable(R.styleable.HmPasswordEditText_leftIcon);
        mRightOpenIcomDrawable = ta.getDrawable(R.styleable.HmPasswordEditText_rightOpenIcon);
        mRightCloseIcomDrawable = ta.getDrawable(R.styleable.HmPasswordEditText_rightCloseIcon);

        View layout = LayoutInflater.from(context).inflate(R.layout.uikit_top_bar, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        addView(layout, params);
        mEditText = layout.findViewById(R.id.et_password);
        mLeftImageView = layout.findViewById(R.id.iv_leftIcon);
        mRightImageView = layout.findViewById(R.id.iv_rightIcon);

        if (!TextUtils.isEmpty(mTextHint))
            mEditText.setHint(mTextHint);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mEditText.setTextColor(mTextColor);

        if (mleftIcomDrawable != null)
            mLeftImageView.setImageDrawable(mleftIcomDrawable);

        if (mleftIcomDrawable != null)
            mLeftImageView.setImageDrawable(mleftIcomDrawable);
    }


}
