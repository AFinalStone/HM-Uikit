package com.hm.iou.uikit.datepicker;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.uikit.R;

import java.util.List;

/**
 * Created by syl on 2018/10/23.
 */

public class TimePickerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TimePickerAdapter(@Nullable List<String> data) {
        super(R.layout.uikit_view_time_picker_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_time, item);
    }
}
