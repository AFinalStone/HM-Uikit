package com.hm.iou.uikit;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.lang.reflect.Field;
import java.util.logging.Logger;


/**
 * @author syl
 * @time 2018/5/21 下午6:30
 */
public class ShowHidePasswordEditText extends AppCompatEditText {

    private static final String TAG = ShowHidePasswordEditText.class.getSimpleName();
    private static final String SHOWING_PASSWORD_STATE_KEY = "SHOWING_PASSWORD_STATE_KEY";
    private static final String SUPER_STATE_KEY = "SUPER_STATE_KEY";
    private boolean mIsShowingPassword;
    private Drawable mDrawableEnd;
    private boolean mLeftToRight = true;
    private int mTintColor = 0;
    private final int DEFAULT_ADDITIONAL_TOUCH_TARGET_SIZE = 80;


    @DrawableRes
    private int mPasswordHide;
    @DrawableRes
    private int mPasswordShow;

    private int mAdditionalTouchTargetSize = DEFAULT_ADDITIONAL_TOUCH_TARGET_SIZE;


    public ShowHidePasswordEditText(Context context) {
        super(context);
        init(null);
    }

    public ShowHidePasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShowHidePasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShowHidePasswordEditText);

            mPasswordHide = attrsArray.getResourceId(R.styleable.ShowHidePasswordEditText_drawablePasswordHide, R.mipmap.uikit_ic_eye_closed);
            mPasswordShow = attrsArray.getResourceId(R.styleable.ShowHidePasswordEditText_drawablePasswordShow, R.mipmap.uikit_ic_eye_open);
            mTintColor = attrsArray.getColor(R.styleable.ShowHidePasswordEditText_tintColor, 0);
            mAdditionalTouchTargetSize = attrsArray.getDimensionPixelSize(R.styleable.ShowHidePasswordEditText_additionalTouchTargetSize, DEFAULT_ADDITIONAL_TOUCH_TARGET_SIZE);

            //使用反射获取当前的输入类型
            mIsShowingPassword = false;
            int[] textAppearanceStyleArr = new int[0];
            int inputTypeStyle = 0;
            try {
                Class clasz = Class.forName("com.android.internal.R$styleable");
                Field field = clasz.getDeclaredField("TextView");
                field.setAccessible(true);
                textAppearanceStyleArr = (int[]) field.get(null);

                field = clasz.getDeclaredField("TextView_inputType");
                field.setAccessible(true);
                inputTypeStyle = (Integer) field.get(null);
                TypedArray a = getContext().obtainStyledAttributes(attrs, textAppearanceStyleArr);
                int itemInputTypeStyle = a.getInt(inputTypeStyle, EditorInfo.TYPE_NULL);
                int flagShowPassword = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                if (itemInputTypeStyle == flagShowPassword) {
                    mIsShowingPassword = true;
//                Log.e("itemInputTypeStyle==", "itemInputTypeStyle" + itemInputTypeStyle);
//                Log.e("flagShowPassword==", "flagShowPassword" + flagShowPassword);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            attrsArray.recycle();
        } else {
            mPasswordHide = R.mipmap.uikit_ic_eye_closed;
            mPasswordShow = R.mipmap.uikit_ic_eye_open;
        }

        mLeftToRight = isLeftToRight();

        //ensures by default this view is only line only
        setMaxLines(1);

        //note this must be set before hidePassword() otherwise it was undeo the passwordTransformation
        setSingleLine(true);

        //初始化密码显示还是隐藏
        if (mIsShowingPassword) {
            showPassword();
        } else {
            hidePassword();
        }
        //save the state of whether the password is being shown
        setSaveEnabled(true);

        if (!TextUtils.isEmpty(getText())) {
            showPasswordVisibilityIndicator(true);
        }

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    showPasswordVisibilityIndicator(true);
                } else {
                    showPasswordVisibilityIndicator(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isLeftToRight() {
        // If we are pre JB assume always LTR
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return true;
        }

        // Other methods, seemingly broken when testing though.
        // return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
        // return !ViewUtils.isLayoutRtl(this);

        Configuration config = getResources().getConfiguration();
        return !(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {

        //keep a reference to the right drawable so later on touch we can check if touch is on the drawable
        if (mLeftToRight && right != null) {
            mDrawableEnd = right;
        } else if (!mLeftToRight && left != null) {
            mDrawableEnd = left;
        }

        super.setCompoundDrawables(left, top, right, bottom);
    }

    public void setTintColor(@ColorInt int mTintColor) {
        this.mTintColor = mTintColor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && mDrawableEnd != null) {
            Rect bounds = mDrawableEnd.getBounds();

            int x = (int) event.getX();

            //take into account the padding and mAdditionalTouchTargetSize
            int drawableWidthWithPadding = bounds.width() + (mLeftToRight ? getPaddingRight() : getPaddingLeft()) + mAdditionalTouchTargetSize;

            //check if the touch is within bounds of mDrawableEnd icon
            if ((mLeftToRight && (x >= (this.getRight() - this.getLeft() - (drawableWidthWithPadding)))) ||
                    (!mLeftToRight && (x <= (this.getLeft() + (drawableWidthWithPadding))))) {

                togglePasswordVisibility();

                //use this to prevent the keyboard from coming up
                event.setAction(MotionEvent.ACTION_CANCEL);
            }
        }

        return super.onTouchEvent(event);
    }

    private void showPasswordVisibilityIndicator(boolean show) {
        //Log.d(TAG, "showPasswordVisibilityIndicator() called with: " + "show = [" + show + "]");
        //preserve and existing CompoundDrawables
        Drawable[] existingDrawables = getCompoundDrawables();
        Drawable left = existingDrawables[0];
        Drawable top = existingDrawables[1];
        Drawable right = existingDrawables[2];
        Drawable bottom = existingDrawables[3];

        if (show) {
            Drawable original = mIsShowingPassword ?
                    ContextCompat.getDrawable(getContext(), mPasswordShow) :
                    ContextCompat.getDrawable(getContext(), mPasswordHide);
            original.mutate();

            if (mTintColor == 0) {
                setCompoundDrawablesWithIntrinsicBounds(mLeftToRight ? left : original, top, mLeftToRight ? original : right, bottom);
            } else {
                Drawable wrapper = DrawableCompat.wrap(original);
                DrawableCompat.setTint(wrapper, mTintColor);
                setCompoundDrawablesWithIntrinsicBounds(mLeftToRight ? left : wrapper, top, mLeftToRight ? wrapper : right, bottom);
            }
        } else {
            setCompoundDrawablesWithIntrinsicBounds(mLeftToRight ? left : null, top, mLeftToRight ? null : right, bottom);
        }
    }


    //make it visible
    private void showPassword() {
        setTransformationMethod(null);
    }

    //hide it
    private void hidePassword() {
        setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void togglePasswordVisibility() {
        // Store the selection
        int selectionStart = this.getSelectionStart();
        int selectionEnd = this.getSelectionEnd();

        // Set transformation method to show/hide password
        if (mIsShowingPassword) {
            hidePassword();
        } else {
            showPassword();
        }

        // Restore selection
        this.setSelection(selectionStart, selectionEnd);

        // Toggle flag and show indicator
        mIsShowingPassword = !mIsShowingPassword;
        showPasswordVisibilityIndicator(true);
    }

    @Override
    protected void finalize() throws Throwable {
        mDrawableEnd = null;
        super.finalize();
    }

    /**
     * @param mAdditionalTouchTargetSize inPixels
     */
    public void setAdditionalTouchTargetSizePixels(int mAdditionalTouchTargetSize) {
        this.mAdditionalTouchTargetSize = mAdditionalTouchTargetSize;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState());
        bundle.putBoolean(SHOWING_PASSWORD_STATE_KEY, this.mIsShowingPassword);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mIsShowingPassword = bundle.getBoolean(SHOWING_PASSWORD_STATE_KEY, false);

            if (mIsShowingPassword) {
                showPassword();
            }
            state = bundle.getParcelable(SUPER_STATE_KEY);
        }
        super.onRestoreInstanceState(state);
    }


}