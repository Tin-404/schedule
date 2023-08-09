package com.neu.edu.schedule.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

//让跑马灯处于选中状态
public class marqueeTextView extends androidx.appcompat.widget.AppCompatTextView {
    public marqueeTextView(Context context) {
        super(context);
    }

    public marqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public marqueeTextView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
    }

    //一直返回true，假装这个控件一直获取着焦点
    @Override
    public boolean isFocused() {
        return true;
    }
}