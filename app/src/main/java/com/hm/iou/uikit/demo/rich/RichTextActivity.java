package com.hm.iou.uikit.demo.rich;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hm.iou.uikit.demo.R;
import com.hm.iou.uikit.richedittext.HMRichTextView;
import com.hm.iou.uikit.richedittext.itemview.RichItemData;

import java.util.List;

public class RichTextActivity extends AppCompatActivity {

    HMRichTextView richTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_text);
        richTextView = findViewById(R.id.richTextView);
        findViewById(R.id.btn_insertTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RichItemData data = new RichItemData();
                data.setText("测试文本");
                richTextView.insertTextView(data);
            }
        });
        findViewById(R.id.btn_insertImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RichItemData data01 = new RichItemData();
                data01.setSrc("http://t2.hddhhn.com/uploads/tu/201707/521/84st.png");
                richTextView.insertImageView(data01);

                RichItemData data02 = new RichItemData();
                data02.setSrc("http://t2.hddhhn.com/uploads/tu/201707/521/84st.png");
                data02.setWidth(19);
                data02.setHeight(14);
                richTextView.insertImageView(data02);
            }
        });
        findViewById(R.id.btn_insertList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RichItemData> list = richTextView.getAllRichItemData();
                richTextView.insertView(list);
            }
        });
        findViewById(R.id.btn_getContent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RichItemData> list = richTextView.getAllRichItemData();
                for (RichItemData data : list) {
                    Log.d("RichItemData", "========" + data.toString());
                }
            }
        });
    }

}
