package com.hm.iou.uikit.demo.rich;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hm.iou.tools.ToastUtil;
import com.hm.iou.uikit.demo.R;
import com.hm.iou.uikit.richedittext.HMRichEditText;
import com.hm.iou.uikit.richedittext.RichDataUtil;
import com.hm.iou.uikit.richedittext.itemview.DataImageView;
import com.hm.iou.uikit.richedittext.itemview.RichItemData;
import com.hm.iou.uikit.richedittext.listener.OnRtImageListener;
import com.hm.iou.uikit.richedittext.listener.OnRtItemDataListener;

import java.util.List;

public class RichEditActivity extends AppCompatActivity {

    HMRichEditText richEditText;
    TextView tvPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_edit);
        tvPreview = findViewById(R.id.tv_preview);
        richEditText = findViewById(R.id.richEditText);
        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (richEditText.getImagePathList().size() < 4) {
//                richEditor.insertImage("http://t2.hddhhn.com/uploads/tu/201707/571/106st.png");
                    RichItemData data = new RichItemData();
                    data.setSrc("http://t2.hddhhn.com/uploads/tu/201707/521/84st.png");
                    data.setWidth("700");
                    data.setHeight("300");
                    richEditText.insertImage(data);
//                richEditText.insertImage("file:///android_res/mipmap/ic_launcher.png");
                } else {
                    ToastUtil.showMessage(RichEditActivity.this, "最多插入4张图片");
                }


            }
        });
        findViewById(R.id.btn_getContent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = RichDataUtil.getStringFromEditData(richEditText.buildEditData());
                tvPreview.setText(content);

                List<RichItemData> list = RichDataUtil.getEditDataFromString(content);
                for (RichItemData data : list) {
                    Log.d("RichItemData", "========" + data.toString());
                }
            }
        });
        findViewById(R.id.btn_showContent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RichEditActivity.this, RichTextActivity.class));
            }
        });
        richEditText.setOnRtImageListener(new OnRtImageListener() {
            @Override
            public void onRtImageClick(final DataImageView imageView) {
                new AlertDialog
                        .Builder(RichEditActivity.this)
                        .setTitle("是否删除图片")
                        .setMessage("图片链接" + imageView.getRichItemData().getSrc())
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                richEditText.deleteImage(imageView);
                            }
                        }).show();
            }
        });
        richEditText.setOnRtValueListener(new OnRtItemDataListener() {
            @Override
            public void onRtEditTextChangeListener(String editValue) {
                tvPreview.setText(editValue);
            }

            @Override
            public void onRtDataImageChangeListener(List<ImageData> list) {
                tvPreview.setText("图片数量" + list.size());
            }
        });

        richEditText.setOnRtValueListener(new OnRtItemDataListener() {
            @Override
            public void onRtEditTextChangeListener(String editValue) {

            }

            @Override
            public void onRtDataImageChangeListener(List<RichItemData> list) {

            }
        });
    }

}
