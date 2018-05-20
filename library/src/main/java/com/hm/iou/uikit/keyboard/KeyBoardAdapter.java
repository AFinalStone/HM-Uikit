package com.hm.iou.uikit.keyboard;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hm.iou.uikit.R;

import java.util.ArrayList;
import java.util.Map;


/**
 * 九宫格键盘适配器
 */
public class KeyBoardAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<Map<String, String>> mListValue;

    public KeyBoardAdapter(Context mContext, ArrayList<Map<String, String>> mListValue) {
        this.mContext = mContext;
        this.mListValue = mListValue;
    }

    @Override
    public int getCount() {
        return mListValue.size();
    }

    @Override
    public Object getItem(int position) {
        return mListValue.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_virtual_keyboard_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.tvKeys = convertView.findViewById(R.id.tv_keys);
            viewHolder.rlDelete = convertView.findViewById(R.id.rl_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 9) {
            viewHolder.rlDelete.setVisibility(View.INVISIBLE);
            viewHolder.tvKeys.setVisibility(View.VISIBLE);
            viewHolder.tvKeys.setText(mListValue.get(position).get("name"));
            viewHolder.tvKeys.setBackgroundColor(Color.parseColor("#e0e0e0"));
        } else if (position == 11) {
            viewHolder.tvKeys.setBackgroundResource(R.mipmap.uikit_icon_keyboard_delete);
            viewHolder.rlDelete.setVisibility(View.VISIBLE);
            viewHolder.tvKeys.setVisibility(View.INVISIBLE);

        } else {
            viewHolder.rlDelete.setVisibility(View.INVISIBLE);
            viewHolder.tvKeys.setVisibility(View.VISIBLE);

            viewHolder.tvKeys.setText(mListValue.get(position).get("name"));
        }

        return convertView;
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView tvKeys;
        public RelativeLayout rlDelete;
    }
}
