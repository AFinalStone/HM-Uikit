package com.hm.iou.uikit.richedittext.listener;

import com.hm.iou.uikit.richedittext.model.ImageData;

import java.util.List;

public interface OnRtValueListener {

    void onRtEditTextChangeListener(String editValue);

    void onRtDataImageChangeListener(List<ImageData> list);
}