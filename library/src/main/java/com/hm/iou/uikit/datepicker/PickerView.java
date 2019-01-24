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

public class PickerView extends RecyclerView implements PickerLayoutManager.OnSelectedViewListener {

    public interface OnPickerItemSelectedListener {
        void onItemSelected(int index, Object value);
    }

    public interface IPickerItem {

        String getItemName();

        Object getItemValue();

    }

    private Context mContext;
    private PickerAdapter mAdapter;
    private List<IPickerItem> mDataList;
    private OnPickerItemSelectedListener mListener;

    private PickerLayoutManager mLayoutManager;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void setOnPickerItemSelectedListener(OnPickerItemSelectedListener listener) {
        mListener = listener;
    }

    public void setPickerList(List<IPickerItem> pickerList) {
        mDataList = pickerList;
        if (mLayoutManager == null) {
            mLayoutManager= new PickerLayoutManager(getContext(), this, PickerLayoutManager.VERTICAL, false, 5, 0.4f, true);
            setLayoutManager(mLayoutManager);
            mLayoutManager.setOnSelectedViewListener(this);
        }
        if (mAdapter == null) {
            mAdapter = new PickerAdapter(pickerList);
            setAdapter(mAdapter);
        } else {
            mAdapter.setNewData(pickerList);
        }
    }

    public void setSelectedIndex(int index) {
        if (mLayoutManager != null) {
            mLayoutManager.scrollToPosition(index);
        }
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

    @Override
    public void onSelectedView(View view, int position) {
        if (mListener != null && mDataList != null && position < mDataList.size()) {
            mListener.onItemSelected(position, mDataList.get(position).getItemValue());
        }
    }

    public class PickerAdapter extends BaseQuickAdapter<IPickerItem, BaseViewHolder> {

        public PickerAdapter(@Nullable List<IPickerItem> data) {
            super(R.layout.uikit_item_picker_single, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, IPickerItem item) {
            helper.setText(R.id.tv_picker_content, item.getItemName());
        }
    }

}