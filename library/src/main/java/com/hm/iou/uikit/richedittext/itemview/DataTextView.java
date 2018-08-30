package com.hm.iou.uikit.richedittext.itemview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 可删除的EditText，主要用途是处理软键盘回删按钮backSpace时回调OnKeyListener
 */
public class DataTextView extends android.support.v7.widget.AppCompatTextView {

    RichItemData mRichItemData;

    public RichItemData getRichItemData() {
        return mRichItemData;
    }

    public void setRichItemData(RichItemData richItemData) {
        this.mRichItemData = richItemData;
    }

    public DataTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DataTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataTextView(Context context) {
        super(context);
    }

}