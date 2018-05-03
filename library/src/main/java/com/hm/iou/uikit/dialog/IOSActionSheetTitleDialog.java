package com.hm.iou.uikit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.SmoothCheckBox;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuling on 2017/11/28.
 * <p>
 * 该类为项目公用Dialog弹窗，包含单按钮、双按钮，使用规则和AlertDialog相似（如果只有一个按钮时，请实现setPositiveButton方法）
 */

public class IOSActionSheetTitleDialog extends Dialog {

    private IOSActionSheetTitleDialog(@NonNull Context context) {
        super(context);
    }

    private IOSActionSheetTitleDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    private IOSActionSheetTitleDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {

        private Context context;
        private String strTitle;
        private int cancelTextColor;
        private int titleTextColor;
        private boolean showTitle = false;
        private List<IOSActionSheetItem> IOSActionSheetItemList;
        private boolean flagCancelable = true;
        private boolean flagCanceledOnTouchOutside = true;
        Display mDisplay;

        public Builder(Context context) {
            this.context = context;
        }


        public Builder setTitle(String title) {
            this.strTitle = title;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }


        public Builder setCancelTextColor(int cancelTextColor) {
            this.cancelTextColor = cancelTextColor;
            return this;
        }


        public Builder setCancelable(boolean cancel) {
            flagCancelable = cancel;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            flagCanceledOnTouchOutside = cancel;
            return this;
        }


        public Builder addSheetItem(IOSActionSheetItem IOSActionSheetItem) {
            if (IOSActionSheetItemList == null) {
                IOSActionSheetItemList = new ArrayList<>();
            }
            IOSActionSheetItemList.add(IOSActionSheetItem);
            return this;
        }

        public void show() {
            final IOSActionSheetTitleDialog dialog = create();
            dialog.show();
        }

        /**
         * 创建dialog
         *
         * @return
         */
        private IOSActionSheetTitleDialog create() {
            final IOSActionSheetTitleDialog dialog = new IOSActionSheetTitleDialog(context, R.style.UikitActionSheetDialogStyle);
            // 获取Dialog布局
            View view = LayoutInflater.from(context).inflate(R.layout.uikit_layout_actionsheet_title, null);
            // 获取自定义Dialog布局中的控件
            ScrollView sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
            LinearLayout lLayout_content = (LinearLayout) view.findViewById(R.id.lLayout_content);
            //初始化title
            if (strTitle != null) {
                TextView txt_title = view.findViewById(R.id.txt_title);
                txt_title.setVisibility(View.VISIBLE);
                txt_title.setText(strTitle);
                if (titleTextColor == 0) {
                    titleTextColor = context.getResources().getColor(R.color.iosActionSheet_blue);
                }
                txt_title.setTextColor(titleTextColor);
            }
            //初始化取消
            TextView txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (cancelTextColor == 0) {
                cancelTextColor = context.getResources().getColor(R.color.iosActionSheet_blue);
            }
            txtCancel.setTextColor(cancelTextColor);
            // 定义Dialog布局和参数
            dialog.setContentView(view);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
            //获取设备的宽度
            WindowManager windowManager = dialogWindow.getWindowManager();
            mDisplay = windowManager.getDefaultDisplay();
            // 设置Dialog最小宽度为屏幕宽度
            view.setMinimumWidth(mDisplay.getWidth());
            //初始化SheetItems
            setSheetItems(dialog, sLayout_content, lLayout_content);

            dialog.setCancelable(flagCancelable);
            dialog.setCanceledOnTouchOutside(flagCanceledOnTouchOutside);
            return dialog;
        }

        /**
         * 设置条目布局
         */
        private void setSheetItems(final Dialog dialog, ScrollView sLayout_content, LinearLayout lLayout_content) {
            if (IOSActionSheetItemList == null || IOSActionSheetItemList.size() <= 0) {
                return;
            }

            final int size = IOSActionSheetItemList.size();

            //高度控制，非最佳解决办法
            // 添加条目过多的时候控制高度
            if (size >= 7) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sLayout_content
                        .getLayoutParams();
                params.height = mDisplay.getHeight() / 2;
                sLayout_content.setLayoutParams(params);
            }

            // 循环添加条目
            for (int i = 1; i <= size; i++) {
                final int index = i;
                final IOSActionSheetItem IOSActionSheetItem = IOSActionSheetItemList.get(i - 1);
                String strItem = IOSActionSheetItem.name;
                IOSActionSheetItem.SheetItemColor color = IOSActionSheetItem.itemColor;
                IOSActionSheetItem.SheetItemColor checkColor = IOSActionSheetItem.checkColor;
                int resIconID = IOSActionSheetItem.itemIcon;
                final OnClickListener listener = IOSActionSheetItem.itemClickListener;

                View itemView = LayoutInflater.from(context).inflate(R.layout.uikit_layout_ios_actionsheet_item, null, false);
                ImageView itemIcon = (ImageView) itemView.findViewById(R.id.itemIcon);
                TextView itemText = (TextView) itemView.findViewById(R.id.itemText);
                IOSActionSheetItem.smoothCheckBox = (SmoothCheckBox) itemView.findViewById(R.id.itemCheck);

                if (color == null) {
                    color = com.hm.iou.uikit.dialog.IOSActionSheetItem.SheetItemColor.Blue;
                }
                if (checkColor == null) {
                    checkColor = com.hm.iou.uikit.dialog.IOSActionSheetItem.SheetItemColor.Blue;
                }
                //初始化条目TextView
                itemText.setText(strItem);
                itemText.setTextColor(Color.parseColor(color.getName()));
                //初始化icon
                if (resIconID != 0) {
                    itemIcon.setImageResource(resIconID);
                    itemIcon.setVisibility(View.VISIBLE);
                } else {
                    itemIcon.setVisibility(View.GONE);
                }
                //checkBox点击监听者
                if (IOSActionSheetItem.OnCheckedChangeListener != null) {
                    IOSActionSheetItem.smoothCheckBox.setCheckedColor(Color.parseColor(checkColor.getName()));
                    IOSActionSheetItem.smoothCheckBox.setVisibility(View.VISIBLE);
                    IOSActionSheetItem.smoothCheckBox.setOnCheckedChangeListener(IOSActionSheetItem.OnCheckedChangeListener);
                }
                // 点击事件
                if (listener != null)
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            listener.onClick(dialog, index - 1);
                        }
                    });
                // 背景图片
                if (size == 1) {
                    if (showTitle) {
                        itemView.setBackgroundResource(R.drawable.uikit_actionsheet_bottom_selector);
                    } else {
                        itemView.setBackgroundResource(R.drawable.uikit_actionsheet_single_selector);
                    }
                } else {
                    if (showTitle) {
                        if (i >= 1 && i < size) {
                            itemView.setBackgroundResource(R.drawable.uikit_actionsheet_middle_selector);
                        } else {
                            itemView.setBackgroundResource(R.drawable.uikit_actionsheet_bottom_selector);
                        }
                    } else {
                        if (i == 1) {
                            itemView.setBackgroundResource(R.drawable.uikit_actionsheet_top_selector);
                        } else if (i < size) {
                            itemView.setBackgroundResource(R.drawable.uikit_actionsheet_middle_selector);
                        } else {
                            itemView.setBackgroundResource(R.drawable.uikit_actionsheet_bottom_selector);
                        }
                    }
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        DensityUtil.dp2px(60)
                        );
                lLayout_content.addView(itemView, params);
            }
        }

    }
}
