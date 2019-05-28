package com.hm.iou.uikit;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class MobileInputEditText extends android.support.v7.widget.AppCompatEditText {

    public MobileInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        addTextChangedListener(watcher);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null || s.length() == 0) {
                return;
            }
            System.out.println(s.toString() + " start = " + start + ", before = " + before + ", count = " + count);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                    continue;
                } else {
                    sb.append(s.charAt(i));
                    if ((sb.length() == 4 || sb.length() == 9)
                            && sb.charAt(sb.length() - 1) != ' ') {
                        sb.insert(sb.length() - 1, ' ');
                    }
                }
            }
            if (!sb.toString().equals(s.toString())) {
                int index = start + 1;
                if (sb.charAt(start) == ' ') {
                    if (before == 0) {
                        index++;
                    } else {
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    } else {
                        index++;
                    }
                }
                index += count;
                if (index > sb.length())
                    index = sb.length();

                removeTextChangedListener(watcher);
                setText(sb.toString());
                if (index <= sb.length())
                    setSelection(index);
                addTextChangedListener(watcher);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public String getTextWithoutSpace() {
        return super.getText().toString().replace(" ", "");
    }

}