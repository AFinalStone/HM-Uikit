package com.hm.iou.uikit.richedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.richedittext.itemview.DataImageView;
import com.hm.iou.uikit.richedittext.itemview.DataTextView;
import com.hm.iou.uikit.richedittext.itemview.RichItemData;
import com.hm.iou.uikit.richedittext.listener.OnRtImageListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * @author syl
 * @time 2018/8/28 下午6:20
 */
public class HMRichTextView extends ScrollView {
    /**
     * 自定义属性
     **/
    private double rtTextSize;
    private int rtTextColor;

    private LinearLayout mParentView; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup

    private OnRtImageListener mOnRtImageListener;

    public HMRichTextView(Context context) {
        super(context);
        initView(null);
    }

    public HMRichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public HMRichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        //获取自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HMRichEditText);
        rtTextSize = ta.getDimension(R.styleable.HMRichTextView_rt_text_text_size, 16);
        rtTextColor = ta.getColor(R.styleable.HMRichTextView_rt_text_text_color, getResources().getColor(R.color.uikit_text_common_color));

        ta.recycle();

        // 1. 初始化mParentView
        mParentView = new LinearLayout(getContext());
        mParentView.setOrientation(LinearLayout.VERTICAL);
        //mParentView.setBackgroundColor(Color.WHITE);
        //setupLayoutTransitions();//禁止载入动画
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mParentView.setPadding(50, 15, 50, 15);//设置间距，防止生成图片时文字太靠边，不能用margin，否则有黑边
        addView(mParentView, layoutParams);

    }

    /**
     * 在末尾插入文本展示框/图片
     */
    public void insertViewOfEnd(List<RichItemData> listData) {
        for (RichItemData data : listData) {
            if (!TextUtils.isEmpty(data.getText())) {
                insertTextViewOfEnd(data);
                continue;
            }
            if (!TextUtils.isEmpty(data.getSrc())) {
                insertImageViewOfEnd(data);
                continue;
            }
        }
    }

    /**
     * 在末尾插入文本展示框
     */
    public void insertTextViewOfEnd(RichItemData data) {
        if (TextUtils.isEmpty(data.getText())) {
            return;
        }
        DataTextView textView = (DataTextView) LayoutInflater.from(getContext()).inflate(R.layout.uikit_rich_textview, mParentView, false);
        textView.setRichItemData(data);
        if (rtTextSize != 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) rtTextSize);
        }
        textView.setText(data.getText());
        textView.setTextColor(rtTextColor);
        mParentView.addView(textView);
    }

    /**
     * 在末尾插入图片
     */
    public void insertImageViewOfEnd(RichItemData data) {
        if (TextUtils.isEmpty(data.getSrc())) {
            return;
        }
        DataImageView imageView = (DataImageView) LayoutInflater.from(getContext()).inflate(R.layout.uikit_rich_text_imageview, mParentView, false);
        imageView.setRichItemData(data);
        //图片被点击
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("图片", "被点击");
                if (mOnRtImageListener != null) {
                    mOnRtImageListener.onRtImageClick((DataImageView) v);
                }
            }
        });
        if (data.getWidth() != 0 && data.getHeight() != 0) {
            int imageWidth = getResources().getDisplayMetrics().widthPixels - 100;
            int imageHeight = imageWidth * data.getHeight() / data.getWidth();
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = imageWidth;
            layoutParams.height = imageHeight;
            imageView.setLayoutParams(layoutParams);
        }
        Picasso.get().load(data.getSrc()).placeholder(R.drawable.uikit_bg_pic_loading_place).error(R.drawable.uikit_bg_pic_loading_error).into(imageView);
        mParentView.addView(imageView);
    }

    /**
     * 获取当前全部的数据列表
     */
    public List<RichItemData> getAllRichItemData() {
        List<RichItemData> dataList = new ArrayList<RichItemData>();
        int num = mParentView.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = mParentView.getChildAt(index);
            if (itemView instanceof DataTextView) {
                DataTextView item = (DataTextView) itemView;
                RichItemData data = item.getRichItemData();
                dataList.add(data);
            } else if (itemView instanceof DataImageView) {
                DataImageView item = (DataImageView) itemView;
                RichItemData data = item.getRichItemData();
                dataList.add(data);
            }
        }
        return dataList;
    }

}
