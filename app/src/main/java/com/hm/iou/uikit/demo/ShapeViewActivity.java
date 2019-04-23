package com.hm.iou.uikit.demo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hm.iou.uikit.ShapedImageView;


public class ShapeViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_view);

        ShapedImageView siv = findViewById(R.id.siv);
        siv.setText("V");
        siv.setTextSize(40);
        siv.setTextStyle(Typeface.NORMAL);
        siv.setBorderWidth(2);
        siv.setTextColor(Color.WHITE);
        siv.setBorderColor(Color.WHITE);
        siv.setBackgroundColor(Color.BLACK);
    }

}
