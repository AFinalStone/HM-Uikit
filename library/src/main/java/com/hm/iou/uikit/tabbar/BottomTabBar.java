package com.hm.iou.uikit.tabbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.tabbar.custom.CustomFragmentTabHost;
import com.hm.iou.uikit.tabbar.util.TintUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.widget.RelativeLayout.CENTER_HORIZONTAL;

/**
 * Created by hjy on 18/5/1.<br>
 */
public class BottomTabBar extends LinearLayout {

    /*=====================定义所用组件Start==========================*/
    private Context context;
    //BottomTabBar整体布局，这里使用FragmentTabHost
    private CustomFragmentTabHost mTabHost;
    //根布局
    private LinearLayout mLayout;
    //分割线
    private View mDivider;
    //小红点集合
    private List<TextView> spots = new ArrayList<>();
    //tabId集合
    private List<String> tabIdList = new ArrayList<>();
    /*=====================定义所用组件 End===========================*/


    /*=====================监听设置Start============================*/
    private OnTabChangeListener listener;

    /**
     * Tab标签切换监听
     */
    public interface OnTabChangeListener {
        void onTabChange(int position, String name, View view);
    }

    public BottomTabBar setOnTabChangeListener(OnTabChangeListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
        return this;
    }
    /*=====================监听设置 End=============================*/


    /*=====================默认样式定义Start==========================*/
    //图片的宽高、文字尺寸
    private double imgWidth = 70, imgHeight = 70, fontSize = 14;
    //上边距、下边距、图文间距
    private double paddingTop = 5, paddingBottom = 0, fontImgPadding = 5;
    //选中、未选中的颜色
    private int selectColor = Color.parseColor("#F1453B"), unSelectColor = Color.parseColor("#626262");
    //是否显示分割线
    private boolean isShowDivider = true;
    //分割线背景
    private int dividerBackgroundColor = Color.parseColor("#CCCCCC");
    //BottomTabBar的整体背景
    private int tabBarBackgroundColor = Color.parseColor("#FFFFFF");
    //当前屏幕尺寸
    private double realWidth, realHeight;
    //标准屏幕尺寸
    private double designWidth, designHeight;
    /*=====================默认样式定义 End==========================*/


    /*=====================构造方法Start============================*/
    public BottomTabBar(Context context) {
        super(context);
        this.context = context;
    }

    public BottomTabBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.BottomTabBar);
        if (attributes != null) {
            //图片宽度
            imgWidth = attributes.getDimension(R.styleable.BottomTabBar_tab_img_width, 70);
            //图片高度
            imgHeight = attributes.getDimension(R.styleable.BottomTabBar_tab_img_height, 70);
            //文字尺寸
            fontSize = attributes.getDimension(R.styleable.BottomTabBar_tab_font_size, 14);
            //上边距
            paddingTop = attributes.getDimension(R.styleable.BottomTabBar_tab_padding_top, 5);
            //图片文字间隔
            fontImgPadding = attributes.getDimension(R.styleable.BottomTabBar_tab_img_font_padding, 0);
            //下边距
            paddingBottom = attributes.getDimension(R.styleable.BottomTabBar_tab_padding_bottom, 5);
            //选中的颜色
            selectColor = attributes.getColor(R.styleable.BottomTabBar_tab_selected_color, Color.parseColor("#F1453B"));
            //未选中的颜色
            unSelectColor = attributes.getColor(R.styleable.BottomTabBar_tab_unselected_color, Color.parseColor("#626262"));
            //是否显示分割线
            isShowDivider = attributes.getBoolean(R.styleable.BottomTabBar_tab_isshow_divider, true);
            //BottomTabBar的整体背景
            tabBarBackgroundColor = attributes.getColor(R.styleable.BottomTabBar_tab_bar_background, Color.parseColor("#FFFFFF"));
            //分割线背景
            dividerBackgroundColor = attributes.getColor(R.styleable.BottomTabBar_tab_divider_background, Color.parseColor("#CCCCCC"));
            attributes.recycle();
        }
    }
    /*=====================构造方法 End=============================*/


    /*=====================初始化设置Start==========================*/

    /**
     * 进行初始化
     *
     * @return BottomTabBar
     */
    public BottomTabBar init(FragmentManager manager, double designWidth, double designHeight) {
        this.designWidth = designWidth;
        this.designHeight = designHeight;
        try {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);

            if (Build.VERSION.SDK_INT >= 17) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (Build.VERSION.SDK_INT < 17
                    && Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //尺寸相关部分适配
        imgWidth = px2designWidthPx(imgWidth);
        imgHeight = px2designHeightPx(imgHeight);
        paddingTop = px2designHeightPx(paddingTop);
        paddingBottom = px2designHeightPx(paddingBottom);
        fontImgPadding = px2designHeightPx(fontImgPadding);

        mLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.uikit_bottom_tab_bar, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mLayout.setLayoutParams(layoutParams);
        addView(mLayout);

        mTabHost = mLayout.findViewById(android.R.id.tabhost);
        mTabHost.setup(context, manager, R.id.realtabcontent);
        mTabHost.setBackgroundColor(tabBarBackgroundColor);
        mTabHost.getTabWidget().setDividerDrawable(null);
        tabIdList.clear();
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (listener != null) {
                    listener.onTabChange(mTabHost.getCurrentTab(), tabId, mTabHost.getCurrentTabView());
                }
            }
        });

        mDivider = mLayout.findViewById(R.id.split);
        setDividerColor(dividerBackgroundColor);
        return this;
    }
    /*=====================初始化设置 End===========================*/


    /*=====================属性设置Start============================*/

    /**
     * 设置Icon的宽高尺寸
     * 此方法必须在addTabItem()之前调用
     *
     * @param width
     * @param height
     * @return
     */
    public BottomTabBar setImgSize(double width, double height) {
        this.imgWidth = px2designWidthPx(width);
        this.imgHeight = px2designHeightPx(height);
        return this;
    }

    /**
     * 设置文字的大小
     * 此方法必须在addTabItem()之前调用
     *
     * @param fontSize
     * @return
     */
    public BottomTabBar setFontSize(double fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 设置Tab的padding值
     * 此方法必须在addTabItem()之前调用
     *
     * @param top    上边距 px
     * @param middle 文字图片的距离 px
     * @param bottom 下边距 px
     * @return
     */
    public BottomTabBar setTabPadding(double top, double middle, double bottom) {
        this.paddingTop = px2designHeightPx(top);
        this.fontImgPadding = px2designHeightPx(middle);
        this.paddingBottom = px2designHeightPx(bottom);
        return this;
    }

    /**
     * 设置选中未选中的颜色
     * 此方法必须在addTabItem()之前调用
     *
     * @param selectColor   选中的颜色
     * @param unSelectColor 未选中的颜色
     * @return
     */
    public BottomTabBar setChangeColor(@ColorInt int selectColor, @ColorInt int unSelectColor) {
        this.selectColor = selectColor;
        this.unSelectColor = unSelectColor;
        return this;
    }

    /**
     * 是否显示分割线
     *
     * @param isShowDivider
     * @return
     */
    public BottomTabBar isShowDivider(boolean isShowDivider) {
        if (isShowDivider) {
            mDivider.setVisibility(VISIBLE);
        } else {
            mDivider.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 设置分割线的颜色
     *
     * @param color
     * @return
     */
    public BottomTabBar setDividerColor(@ColorInt int color) {
        mDivider.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置BottomTabBar的整体背景
     *
     * @param color 背景颜色
     * @return
     */
    public BottomTabBar setTabBarBackgroundColor(@ColorInt int color) {
        mTabHost.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置BottomTabBar的整体背景
     *
     * @param resid 背景图片id
     * @return
     */
    public BottomTabBar setTabBarBackgroundResource(@DrawableRes int resid) {
        mTabHost.setBackgroundResource(resid);
        return this;
    }

    /**
     * 设置BottomTabBar的整体背景
     *
     * @param drawable 背景图片
     * @return
     */
    public BottomTabBar setTabBarBackgroundResource(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTabHost.setBackground(drawable);
        } else {
            mTabHost.setBackgroundDrawable(drawable);
        }
        return this;
    }
    /*=====================属性设置 End=============================*/


    /*=====================核心功能Start============================*/

    /**
     * 添加TabItem
     *
     * @param name          文字
     * @param imgId         图片id
     * @param fragmentClass fragment
     * @return
     */
    public BottomTabBar addTabItem(String name, int imgId, Class fragmentClass) {
        return addTabItem(name, ContextCompat.getDrawable(context, imgId), fragmentClass);
    }

    public BottomTabBar addTabItem(String name, int imgIdSelect, int imgIdUnSelect, Class fragmentClass) {
        return addTabItem(name, ContextCompat.getDrawable(context, imgIdSelect), ContextCompat.getDrawable(context, imgIdUnSelect), fragmentClass);
    }

    /**
     * 添加TabItem
     *
     * @param name          文字
     * @param drawable      图片
     * @param fragmentClass fragment
     * @return
     */
    public BottomTabBar addTabItem(String name, Drawable drawable, Class fragmentClass) {
        tabIdList.add(TextUtils.isEmpty(name) ? fragmentClass.getName() : name);
        mTabHost.addTab(mTabHost.newTabSpec(TextUtils.isEmpty(name) ? fragmentClass.getName() : name)
                .setIndicator(getTabItemView(TextUtils.isEmpty(name) ? fragmentClass.getName() : name, drawable, null)), fragmentClass, null);
        return this;
    }

    public BottomTabBar addTabItem(String name, Drawable drawableSelect, Drawable drawableUnSelect, Class fragmentClass) {
        tabIdList.add(TextUtils.isEmpty(name) ? fragmentClass.getName() : name);
        mTabHost.addTab(mTabHost.newTabSpec(TextUtils.isEmpty(name) ? fragmentClass.getName() : name)
                .setIndicator(getTabItemView(TextUtils.isEmpty(name) ? fragmentClass.getName() : name, drawableSelect, drawableUnSelect)), fragmentClass, null);
        return this;
    }
    /*=====================核心功能 End=============================*/


    /*=====================功能设置Start============================*/

    /**
     * 设置当前Tab
     *
     * @param index
     * @return
     */
    public BottomTabBar setCurrentTab(int index) {
        mTabHost.setCurrentTab(index);
        return this;
    }

    /**
     * @param index
     * @return TextView
     */
    public BottomTabBar setSpot(int index, boolean isShow) {
        if (index > spots.size()) {
            Log.e("BottomTabBar", "setSpot()下标越界");
        } else {
            if (isShow) {
                spots.get(index).setVisibility(VISIBLE);
            } else {
                spots.get(index).setVisibility(GONE);
            }
        }
        return this;
    }
    /*=====================功能设置 End=============================*/


    /*=====================工具类Start==============================*/
    private View getTabItemView(String name, Drawable drawableSelect, Drawable drawableUnSelect) {
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.uikit_tab_item, null);
        view.setPadding(0, (int) paddingTop, 0, (int) paddingBottom);

        ImageView iv = view.findViewById(R.id.tab_item_iv);
        Log.i("hjm===", imgWidth + "   " + imgHeight);
        RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(imgWidth == 0 ?
                RelativeLayout.LayoutParams.WRAP_CONTENT : (int) imgWidth, imgHeight == 0 ?
                RelativeLayout.LayoutParams.WRAP_CONTENT : (int) imgHeight);
        ivParams.addRule(CENTER_HORIZONTAL);
        iv.setLayoutParams(ivParams);

        if (drawableUnSelect == null) {
            iv.setImageDrawable(TintUtil.setStateListTintColor(drawableSelect, unSelectColor, selectColor));
        } else {
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{android.R.attr.state_pressed},
                    drawableSelect);
            drawable.addState(new int[]{android.R.attr.state_selected},
                    drawableSelect);
            drawable.addState(new int[]{},
                    drawableUnSelect);
            iv.setImageDrawable(drawable);
        }

        TextView tv = view.findViewById(R.id.tab_item_tv);
        tv.setText(name);
        tv.setPadding(0, (int) fontImgPadding, 0, 0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) fontSize);
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{};
        int[] colors = new int[]{selectColor, selectColor, unSelectColor};//colorSelect跟states[0]对应，color跟states[2]对应，以此类推
        ColorStateList csl = new ColorStateList(states, colors);
        tv.setTextColor(csl);

        TextView spot = view.findViewById(R.id.little_red_spot);
        spots.add(spot);
        return view;
    }

    private double px2designWidthPx(double px) {
        return px * 1.0f / designWidth * realWidth;
    }

    private double px2designHeightPx(double px) {
        return px * 1.0f / designHeight * realHeight;
    }
    /*=====================工具类 End===============================*/
}
