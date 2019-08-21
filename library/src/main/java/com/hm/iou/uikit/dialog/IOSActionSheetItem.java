package com.hm.iou.uikit.dialog;

import android.content.DialogInterface;
import android.widget.CheckBox;

import com.hm.iou.uikit.SmoothCheckBox;

@Deprecated
public class IOSActionSheetItem {

    String name;
    DialogInterface.OnClickListener itemClickListener;
    SheetItemColor itemColor;
    SheetItemColor checkColor;
    int itemIcon = 0;
    SmoothCheckBox.OnCheckedChangeListener OnCheckedChangeListener;
    SmoothCheckBox smoothCheckBox;


    private IOSActionSheetItem() {
    }

    public IOSActionSheetItem setItemClickListener(DialogInterface.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public IOSActionSheetItem setItemTextColor(SheetItemColor color) {
        this.itemColor = color;
        return this;
    }

    public SmoothCheckBox getSmoothCheckBox() {
        return smoothCheckBox;
    }

    public IOSActionSheetItem setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
        return this;
    }

    public IOSActionSheetItem setOnCheckedChangeListener(SmoothCheckBox.OnCheckedChangeListener onCheckedChangeListener) {
        OnCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

    public IOSActionSheetItem setCheckColor(SheetItemColor checkColor) {
        this.checkColor = checkColor;
        return this;

    }

    public static IOSActionSheetItem create(String name) {
        IOSActionSheetItem iosActionSheetItem = new IOSActionSheetItem();
        iosActionSheetItem.name = name;
        return iosActionSheetItem;
    }

    public enum SheetItemColor {
        Blue("#037BFF"), Red("#FD4A2E"), Green("#55AC48"), Black("#FF222222"), Gray("#848597");

        private String name;

        SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CheckBox checkBox, boolean isChecked);
    }

}