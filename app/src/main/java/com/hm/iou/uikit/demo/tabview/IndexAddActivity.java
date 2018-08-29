package com.hm.iou.uikit.demo.tabview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.hm.iou.uikit.demo.R;

/**
 * @author syl
 * @time 2018/8/7 下午7:47
 */
public class IndexAddActivity extends AppCompatActivity {

    LinearLayout mLlBottom;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_add);
        mLlBottom = findViewById(R.id.ll_bottom);

    }

    @Override
    public void onResume() {
        super.onResume();
        mLlBottom.setVisibility(View.VISIBLE);
        mLlBottom.setAnimation(moveToViewLocation());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    /**
     * 从控件的底部移动到控件所在位置
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }
}
