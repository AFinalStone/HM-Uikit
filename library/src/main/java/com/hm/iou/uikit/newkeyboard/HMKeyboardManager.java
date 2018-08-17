package com.hm.iou.uikit.newkeyboard;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.keyboard.HMInputNumberView;
import com.hm.iou.uikit.newkeyboard.key.BaseKey;
import com.hm.iou.uikit.newkeyboard.key.DefaultKeyStyle;


/**
 * @author syl
 * @time 2018/8/16 下午4:57
 */
public class HMKeyboardManager {

    protected Activity mActivity;
    protected HMKeyboardView mKeyboardView;
    protected ViewGroup mRootView;
    protected BaseKey mKeyboard;

    public HMKeyboardManager(Activity activity) {
        this.mActivity = activity;
        mRootView = mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void bindToEditor(@NonNull EditText editText, @NonNull BaseKey keyboard) {
        //初始化editText
        editText.setInputType(InputType.TYPE_NULL);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (v instanceof EditText) {
                    if (hasFocus) {
                        hideSystemSoftKeyboard();
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showSoftKeyboard();
                            }
                        }, 300);
                    } else {
                        hideSoftKeyboard();
                    }
                }
            }
        });
        //初始化Keyboard
        if (keyboard.getKeyStyle() == null) {
            keyboard.setKeyStyle(new DefaultKeyStyle(mActivity.getApplication()));
        }
        keyboard.setEditText(editText);
        mKeyboard = keyboard;
    }

    public void bindToEditor(@NonNull HMInputNumberView inputNumberView, @NonNull BaseKey keyboard) {
        //初始化Keyboard
        if (keyboard.getKeyStyle() == null) {
            keyboard.setKeyStyle(new DefaultKeyStyle(mActivity.getApplication()));
        }
        mKeyboard = keyboard;
    }


    public void showSoftKeyboard() {
        if (mRootView.indexOfChild(mKeyboardView) == -1) {
            mRootView.addView(getKeyBoardView());
        } else {
            mKeyboardView.setVisibility(View.VISIBLE);
            mKeyboardView.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.down_to_up));
        }
    }


    public void hideSoftKeyboard() {
        if (mKeyboardView != null) {
            mKeyboardView.setVisibility(View.GONE);
            mKeyboardView.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.up_to_hide));
        }
    }

    private View getKeyBoardView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.uikit_layout_keyoard_view, null);
        mKeyboardView = view.findViewById(R.id.keyboard);
        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(mKeyboard);
        return view;
    }

    private void hideSystemSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
