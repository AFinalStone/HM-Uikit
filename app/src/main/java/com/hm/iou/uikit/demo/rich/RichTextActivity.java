package com.hm.iou.uikit.demo.rich;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hm.iou.uikit.demo.R;
import com.hm.iou.uikit.richedittext.HMRichTextView;

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
                richTextView.insertTextView("测试文本");
            }
        });
        findViewById(R.id.btn_insertImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richTextView.insertImageView("http://t2.hddhhn.com/uploads/tu/201707/521/84st.png", 19, 14);
                richTextView.insertImageView("http://t2.hddhhn.com/uploads/tu/201707/521/84st.png");
            }
        });
    }

}
