package com.hm.iou.uikit.richedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hm.iou.uikit.R;
import com.hm.iou.uikit.richedittext.itemview.DataEditText;
import com.hm.iou.uikit.richedittext.itemview.DataImageView;
import com.hm.iou.uikit.richedittext.itemview.RichItemData;
import com.hm.iou.uikit.richedittext.listener.OnRtImageListener;
import com.hm.iou.uikit.richedittext.listener.OnRtItemDataListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * @author syl
 * @time 2018/8/28 下午6:20
 */
public class HMRichEditText extends ScrollView {
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp

    private LinearLayout mParentView; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private TextWatcher mTextWatcher; // 编辑框编辑状态监听
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private DataEditText mCurrentFocusEdit; // 最近被聚焦的EditText
    private String mCurrentFocusEditBeforeTextChangedValue; // 最近被聚焦的EditText的未被编辑之前的内容
    private int editNormalPadding = 0; //
    private int disappearingImageIndex = 0;
    //private Bitmap bmp;
    private int mTextMaxLength;//最大长度

    //文字相关属性，初始提示信息，文字大小和颜色
    private String rtTextInitHint = "请输入内容";
    private double rtTextSize;
    private int rtTextColor;
    private int rtTextHintColor;
    private int mRtImageHeight;
    private int mRtImageBottom;
    /**
     * 监听对象
     */
    private OnRtItemDataListener mOnRtItemDataListener;
    private OnRtImageListener mOnRtImageListener;

    public HMRichEditText(Context context) {
        super(context);
        initView(null);
    }

    public HMRichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public HMRichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public void initView(AttributeSet attrs) {
        //获取自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.HMRichEditText);
        mRtImageHeight = ta.getInteger(R.styleable.HMRichEditText_rt_editor_image_height, 500);
        mRtImageBottom = ta.getInteger(R.styleable.HMRichEditText_rt_editor_image_bottom, 10);
        mTextMaxLength = ta.getInteger(R.styleable.HMRichEditText_rt_editor_text_max_length, 2000);
        rtTextSize = ta.getDimension(R.styleable.HMRichEditText_rt_editor_text_size, 16);
        rtTextColor = ta.getColor(R.styleable.HMRichEditText_rt_editor_text_color, getResources().getColor(R.color.uikit_text_common_color));
        rtTextHintColor = ta.getColor(R.styleable.HMRichEditText_rt_editor_text_hint_color, getResources().getColor(R.color.uikit_text_hint_common_color));
        rtTextInitHint = ta.getString(R.styleable.HMRichEditText_rt_editor_text_init_hint);

        ta.recycle();

        // 1. 初始化mParentView
        mParentView = new LinearLayout(getContext());
        mParentView.setOrientation(LinearLayout.VERTICAL);
        //mParentView.setBackgroundColor(Color.WHITE);
        //setupLayoutTransitions();//禁止载入动画
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mParentView.setPadding(50, 15, 50, 15);//设置间距，防止生成图片时文字太靠边，不能用margin，否则有黑边
        addView(mParentView, layoutParams);

        // 2. 初始化键盘退格监听
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };


        focusListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mCurrentFocusEdit = (DataEditText) v;
                }
            }
        };

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", String.valueOf(s));
                mCurrentFocusEditBeforeTextChangedValue = String.valueOf(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", String.valueOf(s));
                Log.d("getEditTextCount", String.valueOf(getEditTextValue().length()));
                if (getEditTextValue().length() > mTextMaxLength) {
                    mCurrentFocusEdit.setText(mCurrentFocusEditBeforeTextChangedValue);
                    mCurrentFocusEdit.setSelection(mCurrentFocusEdit.length());
                    return;
                }
                if (mOnRtItemDataListener != null) {
                    mOnRtItemDataListener.onRtDataEditChangeListener();
                }
            }
        };
        insertFirstEdit();
    }

    /**
     * 插入第一个EditText控件
     */
    private void insertFirstEdit() {
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        DataEditText firstEdit = createEditText(rtTextInitHint, dip2px(getContext(), EDIT_PADDING));
        mParentView.addView(firstEdit, firstEditParam);
        mCurrentFocusEdit = firstEdit;
        firstEdit.requestFocus();
    }


    private int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = mParentView.indexOfChild(editTxt);
            View preView = mParentView.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof DataEditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    DataEditText preEdit = (DataEditText) preView;
                    String str2 = preEdit.getText().toString();

                    mParentView.removeView(editTxt);

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    mCurrentFocusEdit = preEdit;
                } else {
                    mParentView.removeView(editTxt);
                }
            } else {
                if (editTxt.length() == 0 && mParentView.getChildCount() != 1) {
                    mParentView.removeView(editTxt);
                }
            }
        }
    }

    /**
     * 生成文本输入框
     */
    private DataEditText createEditText(String hint, int paddingTop) {
        DataEditText editText = (DataEditText) LayoutInflater.from(getContext()).inflate(R.layout.uikit_rich_edittext, mParentView, false);
        editText.setOnKeyListener(keyListener);
        editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
        editText.setHint(hint);
        if (rtTextSize != 0) {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) rtTextSize);
        }
        editText.setTextColor(rtTextColor);
        editText.setHintTextColor(rtTextHintColor);
        editText.setOnFocusChangeListener(focusListener);
        editText.addTextChangedListener(mTextWatcher);
        return editText;
    }

    /**
     * 生成图片View
     */
    private LinearLayout createImageLayout(boolean isShowCurse) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.uikit_rich_edit_imageview, mParentView, false);

        //光标被点击
        ImageView ivCursor = layout.findViewById(R.id.iv_cursor);
        ivCursor.setTag(isShowCurse);
        ivCursor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) v.getTag()) {
                    Log.d("光标", "被隐藏");
                    ((ImageView) v).setImageResource(R.color.white);
                    v.setTag(false);

                    LinearLayout parent = (LinearLayout) v.getParent();
                    int ivCursorIndex = mParentView.indexOfChild(parent);//当前image所在的位置索引
                    View nextView = mParentView.getChildAt(ivCursorIndex + 1);
                    if (!(nextView instanceof EditText)) {
                        addEditTextAtIndex(ivCursorIndex + 1, "");
                    }
                } else {
                    Log.d("光标", "被显示");
                    v.setTag(true);
                    ((ImageView) v).setImageResource(R.mipmap.uikit_icon_rich_edit_cursor);
                }
            }
        });
        //图片被点击
        DataImageView imageView = layout.findViewById(R.id.edit_imageView);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("图片", "被点击");
                if (mOnRtImageListener != null) {
                    mOnRtImageListener.onRtImageClick(((DataImageView) v));
                }
            }
        });
        return layout;
    }


    public void setOnRtValueListener(OnRtItemDataListener onRtItemDataListener) {
        this.mOnRtItemDataListener = onRtItemDataListener;
    }

    public void setOnRtImageListener(OnRtImageListener onRtImageListener) {
        this.mOnRtImageListener = onRtImageListener;
    }

    /**
     * 插入一张图片
     */
    public void insertImage(@NonNull RichItemData data) {
        if (mCurrentFocusEdit == null) {
            return;
        }
        hideKeyBoard();
        //mCurrentFocusEdit获取焦点的EditText
        String lastEditStr = mCurrentFocusEdit.getText().toString();
        int cursorIndex = mCurrentFocusEdit.getSelectionStart();//获取光标所在位置
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();//获取光标前面的字符串
        String editStr2 = lastEditStr.substring(cursorIndex).trim();//获取光标后的字符串
        int lastEditIndex = mParentView.indexOfChild(mCurrentFocusEdit);//获取焦点的EditText所在位置

        if (lastEditStr.length() == 0) {
            //如果当前获取焦点的EditText为空，直接在EditText下方插入图片，并且插入空的EditText
//            mParentView.removeView(mCurrentFocusEdit);
//            int editIndex = mParentView.indexOfChild(editTxt);
            addImageViewAtIndex(lastEditIndex + 1, data, true);
            View previewView = mParentView.getChildAt(lastEditIndex);
            if (previewView instanceof EditText) {
                mParentView.removeView(previewView);
                mCurrentFocusEdit = null;
            }
        } else if (editStr1.length() == 0) {
            //如果光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            addImageViewAtIndex(lastEditIndex, data, true);
            //同时插入一个空的EditText，防止插入多张图片无法写文字
//            addEditTextAtIndex(lastEditIndex + 1, "");
        } else if (editStr2.length() == 0) {
            // 如果光标已经顶在了editText的最末端，则需要添加新的imageView和EditText
//            addEditTextAtIndex(lastEditIndex + 1, "");
            addImageViewAtIndex(lastEditIndex + 1, data, true);
        } else {
            //如果光标已经顶在了editText的最中间，则需要分割字符串，分割成两个EditText，并在两个EditText中间插入图片
            //把光标前面的字符串保留，设置给当前获得焦点的EditText（此为分割出来的第一个EditText）
            mCurrentFocusEdit.setText(editStr1);
            //把光标后面的字符串放在新创建的EditText中（此为分割出来的第二个EditText）
            addEditTextAtIndex(lastEditIndex + 1, editStr2);
            //在第二个EditText的位置插入一个空的EditText，以便连续插入多张图片时，有空间写文字，第二个EditText下移
            addEditTextAtIndex(lastEditIndex + 1, "");
            //在空的EditText的位置插入图片布局，空的EditText下移
            addImageViewAtIndex(lastEditIndex + 1, data, true);
        }
        if (mCurrentFocusEdit != null) {
            mCurrentFocusEdit.clearFocus();
            mCurrentFocusEdit = null;
        }
    }

    /**
     * 插入一张图片
     */
    public void insertImage(@NonNull List<RichItemData> listData) {
        if (mCurrentFocusEdit == null) {
            return;
        }
        hideKeyBoard();
        //mCurrentFocusEdit获取焦点的EditText
        String lastEditStr = mCurrentFocusEdit.getText().toString();
        int cursorIndex = mCurrentFocusEdit.getSelectionStart();//获取光标所在位置
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();//获取光标前面的字符串
        String editStr2 = lastEditStr.substring(cursorIndex).trim();//获取光标后的字符串
        int lastEditIndex = mParentView.indexOfChild(mCurrentFocusEdit);//获取焦点的EditText所在位置

        if (lastEditStr.length() == 0) {
            //如果当前获取焦点的EditText为空，直接在EditText下方插入图片，并且插入空的EditText
//            mParentView.removeView(mCurrentFocusEdit);
//            int editIndex = mParentView.indexOfChild(editTxt);
            for (int i = 0; i < listData.size(); i++) {
                RichItemData data = listData.get(i);
                if (i == listData.size() - 1) {
                    addImageViewAtIndex(lastEditIndex + 1 + i, data, true);
                } else {
                    addImageViewAtIndex(lastEditIndex + 1 + i, data, false);
                }
            }
            View previewView = mParentView.getChildAt(lastEditIndex);
            if (previewView instanceof EditText) {
                mParentView.removeView(previewView);
                mCurrentFocusEdit = null;
            }
        } else if (editStr1.length() == 0) {
            //如果光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            for (int i = 0; i < listData.size(); i++) {
                RichItemData data = listData.get(i);
                if (i == listData.size() - 1) {
                    addImageViewAtIndex(lastEditIndex + i, data, true);
                } else {
                    addImageViewAtIndex(lastEditIndex + i, data, false);
                }
            }
            //同时插入一个空的EditText，防止插入多张图片无法写文字
//            addEditTextAtIndex(lastEditIndex + 1, "");
        } else if (editStr2.length() == 0) {
            // 如果光标已经顶在了editText的最末端，则需要添加新的imageView和EditText
//            addEditTextAtIndex(lastEditIndex + 1, "");
            for (int i = 0; i < listData.size(); i++) {
                RichItemData data = listData.get(i);
                if (i == listData.size() - 1) {
                    addImageViewAtIndex(lastEditIndex + 1 + i, data, true);
                } else {
                    addImageViewAtIndex(lastEditIndex + 1 + i, data, false);
                }
            }
        } else {
            //如果光标已经顶在了editText的最中间，则需要分割字符串，分割成两个EditText，并在两个EditText中间插入图片
            //把光标前面的字符串保留，设置给当前获得焦点的EditText（此为分割出来的第一个EditText）
            mCurrentFocusEdit.setText(editStr1);
            //把光标后面的字符串放在新创建的EditText中（此为分割出来的第二个EditText）
            addEditTextAtIndex(lastEditIndex + 1, editStr2);
            //在第二个EditText的位置插入一个空的EditText，以便连续插入多张图片时，有空间写文字，第二个EditText下移
            addEditTextAtIndex(lastEditIndex + 1, "");
            //在空的EditText的位置插入图片布局，空的EditText下移
            for (int i = 0; i < listData.size(); i++) {
                RichItemData data = listData.get(i);
                if (i == listData.size() - 1) {
                    addImageViewAtIndex(lastEditIndex + 1 + i, data, true);
                } else {
                    addImageViewAtIndex(lastEditIndex + 1 + i, data, false);
                }
            }
        }
        if (mCurrentFocusEdit != null) {
            mCurrentFocusEdit.clearFocus();
            mCurrentFocusEdit = null;
        }
    }


    /**
     * 更新ImageView的图片内容
     *
     * @param dataImageView
     * @param imageData
     */
    public void updateImage(DataImageView dataImageView, RichItemData imageData) {
        if (imageData == null) {
            return;
        }
        Picasso.get()
                .load(imageData.getSrc())
                .placeholder(R.drawable.uikit_bg_pic_loading_place)
                .error(R.drawable.uikit_bg_pic_loading_error).into(dataImageView);
        dataImageView.setRichItemData(imageData);
        //更新文件夹里的图片
//        List<RichItemData> dataList = buildEditData();
//        if (disappearingImageIndex < 0 || disappearingImageIndex >= dataList.size()) {
//            return;
//        }
//        RichItemData richItemData = dataList.get(disappearingImageIndex);
//        if (richItemData.getImageData() != null) {
//            richItemData.setImageData(imageData);
//        }
    }

    /**
     * 删除图片
     */
    public void deleteImage(DataImageView dataImageView) {
        View imageParent = (View) dataImageView.getParent();
        disappearingImageIndex = mParentView.indexOfChild(imageParent);
        mParentView.removeView(imageParent);
        if (mOnRtItemDataListener != null) {
            mOnRtItemDataListener.onRtDataImageChangeListener();
        }
        if (mParentView.getChildCount() == 0) {
            insertFirstEdit();
            openKeyboard();
            return;
        }
        //合并上下EditText内容
        mergeEditText();
    }

    /**
     * 图片删除的时候，如果上下方都是EditText，则合并处理
     */
    private void mergeEditText() {
        View preView = mParentView.getChildAt(disappearingImageIndex - 1);
        View nextView = mParentView.getChildAt(disappearingImageIndex);

        if (preView != null && preView instanceof DataEditText && null != nextView
                && nextView instanceof DataEditText) {
            Log.d("LeiTest", "合并EditText");
            DataEditText preEdit = (DataEditText) preView;
            DataEditText nextEdit = (DataEditText) nextView;
            String str1 = preEdit.getText().toString();
            String str2 = nextEdit.getText().toString();
            String mergeText = "";
            if (str2.length() > 0) {
                mergeText = str1 + "\n" + str2;
            } else {
                mergeText = str1;
            }

            mParentView.setLayoutTransition(null);
            mParentView.removeView(nextEdit);
            preEdit.setText(mergeText);
            preEdit.requestFocus();
            preEdit.setSelection(str1.length(), str1.length());
        }
        if (null != preView && preView instanceof DataEditText) {
            preView.requestFocus();
            mCurrentFocusEdit = (DataEditText) preView;
        }
    }

    /**
     * 打开软键盘
     */
    private void openKeyboard() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
    }

    /**
     * 隐藏小键盘
     */
    private boolean hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return false;
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    private void addEditTextAtIndex(final int index, CharSequence editStr) {
        DataEditText editText = createEditText("", EDIT_PADDING);
        //判断插入的字符串是否为空，如果没有内容则显示hint提示信息
        if (editStr != null && editStr.length() > 0) {
            editText.setText(editStr);
        }
        editText.setOnFocusChangeListener(focusListener);

        // 请注意此处，EditText添加、或删除不触动Transition动画
        mParentView.setLayoutTransition(null);
        mParentView.addView(editText, index);
        //插入新的EditText之后，修改mCurrentFocusEdit的指向
        mCurrentFocusEdit = editText;
        mCurrentFocusEdit.requestFocus();
        mCurrentFocusEdit.setSelection(editStr.length(), editStr.length());
        openKeyboard();
    }

    /**
     * 在特定位置添加ImageView
     */
    private void addImageViewAtIndex(int index, RichItemData data, boolean isShowCurse) {
        LinearLayout imageLayout = createImageLayout(isShowCurse);
        DataImageView imageView = imageLayout.findViewById(R.id.edit_imageView);
        ImageView ivCursor = imageLayout.findViewById(R.id.iv_cursor);
        if (isShowCurse) {
            ivCursor.setImageResource(R.mipmap.uikit_icon_rich_edit_cursor);
        } else {
            ivCursor.setImageResource(R.color.white);
        }
        imageView.setRichItemData(data);
        Picasso.get().load(data.getSrc()).placeholder(R.drawable.uikit_bg_pic_loading_place).error(R.drawable.uikit_bg_pic_loading_error).into(imageView);
        mParentView.addView(imageLayout, index);
        if (mOnRtItemDataListener != null) {
            mOnRtItemDataListener.onRtDataImageChangeListener();
        }
    }


    /**
     * 获取当前全部的数据列表
     */
    public List<RichItemData> getAllRichItemData() {
        List<RichItemData> dataList = new ArrayList<RichItemData>();
        int num = mParentView.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = mParentView.getChildAt(index);
            if (itemView instanceof DataEditText) {
                DataEditText item = (DataEditText) itemView;
                RichItemData data = new RichItemData();
                data.setText(item.getText().toString());
                dataList.add(data);
            } else if (itemView instanceof LinearLayout) {
                DataImageView item = itemView.findViewById(R.id.edit_imageView);
                RichItemData data = item.getRichItemData();
                dataList.add(data);
            }
        }
        return dataList;
    }

    /**
     * 获取当前输入的全部字符内容
     */
    public String getEditTextValue() {
        String value = "";
        int num = mParentView.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = mParentView.getChildAt(index);
            if (itemView instanceof DataEditText) {
                DataEditText item = (DataEditText) itemView;
                value += item.getText().toString();
            }
        }
        return value;
    }


    /**
     * 获取插入的图片地址列表
     */
    public List<RichItemData> getImagePathList() {
        List<RichItemData> listPath = new ArrayList<>();
        int num = mParentView.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = mParentView.getChildAt(index);
            if (itemView instanceof LinearLayout) {
                DataImageView item = itemView.findViewById(R.id.edit_imageView);
                listPath.add(item.getRichItemData());
            }
        }
        return listPath;
    }

}
