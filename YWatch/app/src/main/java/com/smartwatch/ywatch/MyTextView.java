package com.smartwatch.ywatch;
import android.widget.*;
import android.util.*;
import android.content.*;
public class MyTextView extends TextView {
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    /**
     * 初始化字体
     * @param context
     */
    private void init(Context context) {
        //设置字体样式
        setTypeface(FontCustom.setFont(context));
    }
}
