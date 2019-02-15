package com.hm.iou.uikit.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.iou.uikit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hjy on 2019/1/14.
 */

public class TimePickerDialog extends Dialog {

    public enum SCROLL_TYPE {
        DAY(1),        //显示 年、月、日 联动滚轮
        MINUTE(2);      //显示 年、月、日、时、分 联动滚轮

        SCROLL_TYPE(int value) {
            this.value = value;
        }

        public int value;
    }

    public interface OnTimeConfirmListener {
        /**
         * @param year
         * @param month [0, 11]
         * @param day   [1, 31]
         * @param hour 无小时选择时，为0
         * @param minute 无分钟选择时，为0
         */
        void onConfirm(int year, int month, int day, int hour, int minute);
    }

    private Builder mBuilder;

    private TextView mTvTitle;
    private PickerView[] mPickers;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Calendar mStartCalendar;
    private Calendar mEndCalendar;
    private Calendar mSelectedCalendar;

    private int mSelectedYear;
    private int mSelectedMonth;     // 范围 [0, 11]
    private int mSelectedDay;
    private int mSelectedHour;
    private int mSelectedMinute;

    private List<PickerView.IPickerItem> mYearList, mMonthList, mDayList, mHourList, mMinuteList;

    private TimePickerDialog(Builder builder, int style) {
        super(builder.mContext, style);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_dialog_time_picker);

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
        findViewById(R.id.tv_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mBuilder != null && mBuilder.mListener != null) {
                    mBuilder.mListener.onConfirm(mSelectedYear, mSelectedMonth, mSelectedDay, mSelectedHour, mSelectedMinute);
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

        PickerView pickerYear = findViewById(R.id.pv_dialog_year);
        PickerView pickerMonth = findViewById(R.id.pv_dialog_month);
        PickerView pickerDay = findViewById(R.id.pv_dialog_day);
        PickerView pickerHour = findViewById(R.id.pv_dialog_hour);
        PickerView pickerMinute = findViewById(R.id.pv_dialog_minute);
        if (mBuilder.mScrollType == SCROLL_TYPE.DAY) {
            mPickers = new PickerView[]{pickerYear, pickerMonth, pickerDay};
        } else if (mBuilder.mScrollType == SCROLL_TYPE.MINUTE) {
            mPickers = new PickerView[]{pickerYear, pickerMonth, pickerDay, pickerHour, pickerMinute};
            pickerHour.setVisibility(View.VISIBLE);
            pickerMinute.setVisibility(View.VISIBLE);
        }

        initPicker();
    }

    private void initPicker() {
        String startTime = mBuilder.mStartTime;
        String endTime = mBuilder.mEndTime;
        mStartCalendar = Calendar.getInstance();
        mEndCalendar = Calendar.getInstance();
        mSelectedCalendar = Calendar.getInstance();

        if (!TextUtils.isEmpty(mBuilder.mSelectedTime)) {
            try {
                mSelectedCalendar.setTime(mDateFormat.parse(mBuilder.mSelectedTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //默认区间：[1970-01-01 00:00:00, 2049-12-31 23:59:59]
        if (!TextUtils.isEmpty(startTime)) {
            try {
                Date date = mDateFormat.parse(startTime);
                mStartCalendar.setTime(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mStartCalendar.set(1970, 0, 1, 0, 0, 0);
        }
        if (!TextUtils.isEmpty(endTime)) {
            try {
                Date date = mDateFormat.parse(endTime);
                mEndCalendar.setTime(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mEndCalendar.set(2049, 11, 31, 23, 59, 59);
        }

        mYearList = new ArrayList<>();
        mMonthList = new ArrayList<>();
        mDayList = new ArrayList<>();

        int startYear = mStartCalendar.get(Calendar.YEAR);
        int endYear = mEndCalendar.get(Calendar.YEAR);
        for (int i = startYear; i <= endYear; i++) {
            final int y = i;
            mYearList.add(new PickerView.IPickerItem() {
                @Override
                public String getItemName() {
                    return y + "年";
                }

                @Override
                public Object getItemValue() {
                    return y;
                }
            });
        }
        if (mYearList.isEmpty()) {
            return;
        }
        mPickers[0].setPickerList(mYearList);

        //年
        mPickers[0].setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
            @Override
            public void onItemSelected(int index, Object value) {
                onYearChange(index, (Integer) value);
            }
        });

        //月
        mPickers[1].setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
            @Override
            public void onItemSelected(int index, Object value) {
                onMonthChange(index, (Integer) value);
            }
        });

        //日
        mPickers[2].setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
            @Override
            public void onItemSelected(int index, Object value) {
                onDayChange(index, (Integer) value);
            }
        });

        //时
        if (mBuilder.mScrollType == SCROLL_TYPE.MINUTE) {
            mHourList = new ArrayList<>();
            mMinuteList = new ArrayList<>();

            for (int i = 0; i < 24; i++) {
                final int h = i;
                mHourList.add(new PickerView.IPickerItem() {
                    @Override
                    public String getItemName() {
                        return h + "时";
                    }

                    @Override
                    public Object getItemValue() {
                        return h;
                    }
                });
            }

            for (int i=0; i<60; i++) {
                final int m = i;
                mMinuteList.add(new PickerView.IPickerItem() {
                    @Override
                    public String getItemName() {
                        return m + "分";
                    }

                    @Override
                    public Object getItemValue() {
                        return m;
                    }
                });
            }
            mPickers[3].setPickerList(mHourList);
            mPickers[4].setPickerList(mMinuteList);

            //时、分，都是固定的，不会随着日期的变化而变化
            mSelectedHour = mSelectedCalendar.get(Calendar.HOUR_OF_DAY);
            mSelectedMinute = mSelectedCalendar.get(Calendar.MINUTE);
            mPickers[3].setSelectedIndex(mSelectedHour);
            mPickers[4].setSelectedIndex(mSelectedMinute);

            mPickers[3].setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
                @Override
                public void onItemSelected(int index, Object value) {
                    mSelectedHour = (Integer) value;
                }
            });

            mPickers[4].setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
                @Override
                public void onItemSelected(int index, Object value) {
                    mSelectedMinute = (Integer) value;
                }
            });
        }

        //设置默认选中项
        int sy = mSelectedCalendar.get(Calendar.YEAR);
        int defIndex = 0;
        for (int i=0; i<mYearList.size(); i++) {
            if (sy == (Integer) mYearList.get(i).getItemValue()) {
                defIndex = i;
                break;
            }
        }
        mPickers[0].setSelectedIndex(defIndex);
        //开始联动
        onYearChange(defIndex, (Integer) mYearList.get(defIndex).getItemValue());
    }

    private void onYearChange(int index, int year) {
        mSelectedYear = year;
        mMonthList.clear();
        int startM = 0;
        int endM = 11;
        if (year == mStartCalendar.get(Calendar.YEAR)) {
            startM = mStartCalendar.get(Calendar.MONTH);
        }
        if (year == mEndCalendar.get(Calendar.YEAR)) {
            endM = mEndCalendar.get(Calendar.MONTH);
        }

        for (int i = startM; i <= endM; i++) {
            final int m = i;
            mMonthList.add(new PickerView.IPickerItem() {
                @Override
                public String getItemName() {
                    return (m + 1) + "月";
                }

                @Override
                public Object getItemValue() {
                    return m;
                }
            });
        }

        mPickers[1].setPickerList(mMonthList);
        if (mMonthList.isEmpty()) {
            return;
        }
        int defIndex = 0;
        if (mSelectedCalendar.get(Calendar.YEAR) == mSelectedYear) {
            int sm = mSelectedCalendar.get(Calendar.MONTH);
            for (int i = 0; i < mMonthList.size(); i++) {
                if (sm == (Integer) mMonthList.get(i).getItemValue()) {
                    defIndex = i;
                    break;
                }
            }
        }
        mPickers[1].setSelectedIndex(defIndex);
        onMonthChange(defIndex, (Integer) mMonthList.get(defIndex).getItemValue());
    }

    private void onMonthChange(int index, int month) {
        mSelectedMonth = month;
        mDayList.clear();
        int startD = -1;
        int endD = -1;
        if (mSelectedYear == mStartCalendar.get(Calendar.YEAR) && month == mStartCalendar.get(Calendar.MONTH)) {
            startD = mStartCalendar.get(Calendar.DAY_OF_MONTH);
        }
        if (mSelectedYear == mEndCalendar.get(Calendar.YEAR) && month == mEndCalendar.get(Calendar.MONTH)) {
            endD = mEndCalendar.get(Calendar.DAY_OF_MONTH);
        }
        if (startD == -1) {
            startD = 1;
        }
        if (endD == -1) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, mSelectedYear);
            calendar.set(Calendar.MONTH, month);
            endD = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        for (int i = startD; i <= endD; i++) {
            final int d = i;
            mDayList.add(new PickerView.IPickerItem() {
                @Override
                public String getItemName() {
                    return d + "日";
                }

                @Override
                public Object getItemValue() {
                    return d;
                }
            });
        }

        mPickers[2].setPickerList(mDayList);
        if (mDayList.isEmpty()) {
            return;
        }

        int defIndex = 0;
        if (mSelectedCalendar.get(Calendar.YEAR) == mSelectedYear && mSelectedCalendar.get(Calendar.MONTH) == mSelectedMonth) {
            int sd = mSelectedCalendar.get(Calendar.DAY_OF_MONTH);
            for (int i = 0; i < mDayList.size(); i++) {
                if (sd == (Integer) mDayList.get(i).getItemValue()) {
                    defIndex = i;
                    break;
                }
            }
        }
        mPickers[2].setSelectedIndex(defIndex);
        onDayChange(defIndex, (Integer) mDayList.get(defIndex).getItemValue());
    }

    private void onDayChange(int index, int day) {
        mSelectedDay = day;
    }

    public static final class Builder {

        private Context mContext;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private CharSequence mTitle;
        private SCROLL_TYPE mScrollType = SCROLL_TYPE.DAY;     //默认只显示 年月日
        private String mStartTime;
        private String mEndTime;
        private String mSelectedTime;

        private OnTimeConfirmListener mListener;

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

        public Builder setScrollType(SCROLL_TYPE type) {
            if (type == null) {
                type = SCROLL_TYPE.DAY;
            }
            mScrollType = type;
            return this;
        }

        public Builder setOnPickerConfirmListener(OnTimeConfirmListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * 设置时间区间范围，可以为空，默认区间为：[1970-01-01 00:00:00, 2049-12-31 23:59:59]
         * 只对设置的 年、月、日 有效
         *
         * @param startTime yyyy-MM-dd HH:mm:ss格式的字符串
         * @param endTime   yyyy-MM-dd HH:mm:ss格式的字符串
         * @return
         */
        public Builder setTimeRange(String startTime, String endTime) {
            mStartTime = startTime;
            mEndTime = endTime;
            return this;
        }

        /**
         * 设置默认选中的日期
         *
         * @param time yyyy-MM-dd HH:mm:ss 格式的时间字符串
         * @return
         */
        public Builder setSelectedTime(String time) {
            mSelectedTime = time;
            return this;
        }

        public TimePickerDialog create() {
            final TimePickerDialog dialog = new TimePickerDialog(this, R.style.UikitAlertDialogStyle_FromBottom);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return dialog;
        }

    }

}