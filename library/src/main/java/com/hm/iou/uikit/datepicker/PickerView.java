package com.hm.iou.uikit.datepicker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.uikit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjy on 2019/1/15.
 */

public class PickerView extends WheelPicker implements WheelPicker.OnWheelChangeListener {

    public interface OnPickerItemSelectedListener {
        void onItemSelected(int index, Object value);
    }

    private Context mContext;
    private List<IPickerItem> mDataList;
    private OnPickerItemSelectedListener mListener;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setOnWheelChangeListener(this);
    }

    @Override
    public void onWheelSelected(Object item, int position) {
        if (mDataList != null && mListener != null) {
            mListener.onItemSelected(position, mDataList.get(position).getItemValue());
        }
    }

    public void setOnPickerItemSelectedListener(OnPickerItemSelectedListener listener) {
        mListener = listener;
    }

    public void setPickerList(List<IPickerItem> pickerList) {
        if (pickerList == null)
            pickerList = new ArrayList<>();
        mDataList = pickerList;
        setDataList(pickerList);
    }

    public void setSelectedIndex(int index) {
        setCurrentPosition(index);
    }

    public void setPickerListWithString(List<String> pickerList) {
        List<IPickerItem> list = new ArrayList<>();
        if (pickerList != null) {
            for (final String str : pickerList) {
                list.add(new IPickerItem() {
                    @Override
                    public String getItemName() {
                        return str;
                    }

                    @Override
                    public Object getItemValue() {
                        return str;
                    }
                });
            }
        }
        setPickerList(list);
    }

    public void setPickerListWithInteger(List<Integer> pickerList) {
        List<IPickerItem> list = new ArrayList<>();
        if (pickerList != null) {
            for (final Integer item : pickerList) {
                list.add(new IPickerItem() {
                    @Override
                    public String getItemName() {
                        return item + "";
                    }

                    @Override
                    public Object getItemValue() {
                        return item;
                    }
                });
            }
        }
        setPickerList(list);
    }

}