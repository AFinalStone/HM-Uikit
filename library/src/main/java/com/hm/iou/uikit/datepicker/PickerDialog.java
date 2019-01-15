package com.hm.iou.uikit.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjy on 2019/1/14.
 */

public class PickerDialog extends Dialog {

    public interface OnPickerConfirmListener {
        void onConfirm(int index, Object value);
    }

    private Builder mBuilder;

    private TextView mTvTitle;
    private PickerView mPickerView;

    private int mSelectedIndex;
    private Object mSelectedValue = null;

    private PickerDialog(Builder builder, int style) {
        super(builder.mContext, style);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_dialog_comm_picker);

        initWindow();
        initContent();
    }

    private void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.width = WindowManager.LayoutParams.MATCH_PARENT;
        attrs.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(attrs);
    }

    private void initContent() {
        mTvTitle = findViewById(R.id.tv_dialog_title);
        mPickerView = findViewById(R.id.picker_dialog);
        findViewById(R.id.tv_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mBuilder != null && mBuilder.mListener != null) {
                    mBuilder.mListener.onConfirm(mSelectedIndex, mSelectedValue);
                }
            }
        });

        if (mBuilder == null) {
            return;
        }
        if (TextUtils.isEmpty(mBuilder.mTitle)) {
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mBuilder.mTitle);
        }
        mPickerView.setPickerList(mBuilder.mDataList);
        int selectedIndex = mBuilder.mSelectedIndex;
        if ( selectedIndex >= 0 && mBuilder.mDataList != null && selectedIndex < mBuilder.mDataList.size()) {
            mSelectedIndex = selectedIndex;
            mSelectedValue = mBuilder.mDataList.get(selectedIndex).getItemValue();
        }
        mPickerView.setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
            @Override
            public void onItemSelected(int index, Object value) {
                mSelectedIndex = index;
                mSelectedValue = value;
            }
        });
        mPickerView.setSelectedIndex(mSelectedIndex);
    }

    public static final class Builder {

        private Context mContext;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private CharSequence mTitle;
        private List<PickerView.IPickerItem> mDataList;
        private int mSelectedIndex = 0;
        private OnPickerConfirmListener mListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setCancelable(boolean flagCancelable) {
            mCancelable = flagCancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean flagCanceledOnTouchOutside) {
            mCanceledOnTouchOutside = flagCanceledOnTouchOutside;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder setTitle(int titleResId) {
            mTitle = mContext.getString(titleResId);
            return this;
        }

        public Builder setPickerList(List<PickerView.IPickerItem> pickerList) {
            mDataList = pickerList;
            return this;
        }

        public Builder setPickerListWithString(List<String> pickerList) {
            List<PickerView.IPickerItem> list = new ArrayList<>();
            if (pickerList != null) {
                for (final String str : pickerList) {
                    list.add(new PickerView.IPickerItem() {
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
            mDataList = list;
            return this;
        }

        public Builder setPickerListWithInteger(List<Integer> pickerList) {
            List<PickerView.IPickerItem> list = new ArrayList<>();
            if (pickerList != null) {
                for (final Integer item : pickerList) {
                    list.add(new PickerView.IPickerItem() {
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
            mDataList = list;
            return this;
        }

        /**
         * 默认会选中第一个
         *
         * @param index
         * @return
         */
        public Builder setSelectedIndex(int index) {
            if (index < 0) {
                throw new IllegalArgumentException("Selected index must be >= 0");
            }
            mSelectedIndex = index;
            return this;
        }

        public Builder setOnPickerConfirmListener(OnPickerConfirmListener listener) {
            mListener = listener;
            return this;
        }

        public PickerDialog create() {
            final PickerDialog dialog = new PickerDialog(this, R.style.UikitAlertDialogStyle_FromBottom);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return dialog;
        }

    }

}