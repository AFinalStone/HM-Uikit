package com.hm.iou.uikit.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hm.iou.uikit.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjy on 2019/6/13.
 */

public class CityPickerDialog extends Dialog {

    public interface OnCityConfirmListener {
        void onConfirm(String province, String city, String district);
    }

    private class ProvinceInfo implements WheelPicker.IPickerItem {

        String code;
        String name;

        List<WheelPicker.IPickerItem> cityList;

        public ProvinceInfo(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public void addCityInfo(CityInfo cityInfo) {
            if (cityList == null)
                cityList = new ArrayList<>();
            cityList.add(cityInfo);
        }

        @Override
        public String getItemName() {
            return name;
        }

        @Override
        public Object getItemValue() {
            return this;
        }

    }

    private class CityInfo implements WheelPicker.IPickerItem {

        String code;
        String name;

        List<WheelPicker.IPickerItem> districtList;

        public CityInfo(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public void addDistrictInfo(DistrictInfo districtInfo) {
            if (districtList == null)
                districtList = new ArrayList<>();
            districtList.add(districtInfo);
        }

        @Override
        public String getItemName() {
            return name;
        }

        @Override
        public Object getItemValue() {
            return this;
        }

    }

    private class DistrictInfo implements WheelPicker.IPickerItem {

        String code;
        String name;

        @Override
        public String getItemName() {
            return name;
        }

        @Override
        public Object getItemValue() {
            return this;
        }

        public DistrictInfo(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    private Builder mBuilder;

    private TextView mTvTitle;
    private PickerView[] mPickers;

    private ProvinceInfo mSelectedProvince;
    private CityInfo mSelectedCity;
    private DistrictInfo mSelectedDistrict;

    private List<WheelPicker.IPickerItem> mProvinceList;

    private CityPickerDialog(Builder builder, int style) {
        super(builder.mContext, style);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_dialog_city_picker);

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
                    mBuilder.mListener.onConfirm(
                            mSelectedProvince != null ? mSelectedProvince.name : null,
                            mSelectedCity != null ? mSelectedCity.name : null,
                            mSelectedDistrict != null ? mSelectedDistrict.name : null
                    );
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

        PickerView pickerProvince = findViewById(R.id.pv_dialog_province);
        PickerView pickerCity = findViewById(R.id.pv_dialog_city);
        PickerView pickerDistrict = findViewById(R.id.pv_dialog_district);
        mPickers = new PickerView[]{pickerProvince, pickerCity, pickerDistrict};

        initData();
        initPicker();
    }

    private void initData() {
        try {
            InputStream is = getContext().getAssets().open("uikit_city.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer);

            Map<String, Map<String, String>> map = new Gson().fromJson(json, new TypeToken<LinkedHashMap<String, LinkedHashMap<String, String>>>() {
            }.getType());
            Map<String, String> provinceMap = map.get("province_list");
            Map<String, String> cityMap = map.get("city_list");
            Map<String, String> districtMap = map.get("county_list");


            Map<String, ProvinceInfo> pMap = new LinkedHashMap<>();
            Map<String, CityInfo> cMap = new LinkedHashMap<>();
            Map<String, DistrictInfo> dMap = new LinkedHashMap<>();
            mProvinceList = new ArrayList<>();
            for (Map.Entry<String, String> entry : provinceMap.entrySet()) {
                ProvinceInfo provinceInfo = new ProvinceInfo(entry.getKey(), entry.getValue());
                pMap.put(entry.getKey(), provinceInfo);
                mProvinceList.add(provinceInfo);
            }
            for (Map.Entry<String, String> entry : cityMap.entrySet()) {
                cMap.put(entry.getKey(), new CityInfo(entry.getKey(), entry.getValue()));
            }
            for (Map.Entry<String, String> entry : districtMap.entrySet()) {
                dMap.put(entry.getKey(), new DistrictInfo(entry.getKey(), entry.getValue()));
            }

            for (CityInfo cityInfo : cMap.values()) {
                String provinceCode = cityInfo.code.substring(0, 2) + "0000";
                ProvinceInfo provinceInfo = pMap.get(provinceCode);
                if (provinceInfo != null)
                    provinceInfo.addCityInfo(cityInfo);
            }

            for (DistrictInfo districtInfo : dMap.values()) {
                String cityCode = districtInfo.code.substring(0, 4) + "00";
                CityInfo cityInfo = cMap.get(cityCode);
                if (cityInfo != null)
                    cityInfo.addDistrictInfo(districtInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initPicker() {
        if (mProvinceList == null)
            return;

        mPickers[0].setPickerList(mProvinceList);
        mPickers[0].setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
            @Override
            public void onItemSelected(int index, Object value) {
                onProvinceChange((ProvinceInfo) value);
            }
        });

        mPickers[1].setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
            @Override
            public void onItemSelected(int index, Object value) {
                onCityChange((CityInfo) value);
            }
        });

        mPickers[2].setOnPickerItemSelectedListener(new PickerView.OnPickerItemSelectedListener() {
            @Override
            public void onItemSelected(int index, Object value) {
                onDistrictChange((DistrictInfo) value);
            }
        });

        mPickers[0].setSelectedIndex(0);
        //开始联动
        onProvinceChange((ProvinceInfo) mProvinceList.get(0));
    }

    private void onProvinceChange(ProvinceInfo provinceInfo) {
        mSelectedProvince = provinceInfo;
        if (provinceInfo.cityList == null || provinceInfo.cityList.isEmpty()) {
            //如果没有数据，则构造一个空数据
            provinceInfo.addCityInfo(new CityInfo("", ""));
        }
        mPickers[1].setPickerList(provinceInfo.cityList);
        mPickers[1].setSelectedIndex(0);
        onCityChange((CityInfo) provinceInfo.cityList.get(0));
    }

    private void onCityChange(CityInfo cityInfo) {
        mSelectedCity = cityInfo;
        if (cityInfo.districtList == null || cityInfo.districtList.isEmpty()) {
            cityInfo.addDistrictInfo(new DistrictInfo("", ""));
        }
        mPickers[2].setPickerList(cityInfo.districtList);
        mPickers[2].setSelectedIndex(0);
        onDistrictChange((DistrictInfo) cityInfo.districtList.get(0));
    }

    private void onDistrictChange(DistrictInfo districtInfo) {
        mSelectedDistrict = districtInfo;
    }

    public static final class Builder {

        private Context mContext;
        private boolean mCancelable = true;
        private boolean mCanceledOnTouchOutside = true;
        private CharSequence mTitle;

        private OnCityConfirmListener mListener;

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

        public Builder setOnCityConfirmListener(OnCityConfirmListener listener) {
            mListener = listener;
            return this;
        }

        public CityPickerDialog create() {
            final CityPickerDialog dialog = new CityPickerDialog(this, R.style.UikitAlertDialogStyle_FromBottom);
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return dialog;
        }

    }

}