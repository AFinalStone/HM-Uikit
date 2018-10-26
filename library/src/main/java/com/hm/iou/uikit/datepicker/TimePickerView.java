package com.hm.iou.uikit.datepicker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.hm.iou.uikit.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by syl on 2018/10/23.
 */

public class TimePickerView extends FrameLayout {

    private RecyclerView mRvYear;//年
    private RecyclerView mRvMonth;//月
    private RecyclerView mRvDay;//日
    private RecyclerView mRvHour;//时
    private RecyclerView mRvMinute;//分

    private List<String> mListYear = new ArrayList<>();

    public TimePickerView(@NonNull Context context) {
        super(context);
        init();
    }

    public TimePickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TimePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.uikit_view_time_pick, this, true);
        mRvYear = findViewById(R.id.rv_year);

        PickerLayoutManager mPlYear = new PickerLayoutManager(getContext(), mRvYear, PickerLayoutManager.VERTICAL, false, 3, 0.4f, true);

        mRvYear.setLayoutManager(mPlYear);
        initData();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 100; i++) {
            int year = calendar.get(Calendar.YEAR);
            calendar.add(Calendar.YEAR, 1);
            mListYear.add(String.valueOf(year));
        }
        mRvYear.setAdapter(new TimePickerAdapter(mListYear));

    }
}



















