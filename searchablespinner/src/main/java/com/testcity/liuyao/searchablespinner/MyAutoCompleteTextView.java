package com.testcity.liuyao.searchablespinner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;

import java.util.Calendar;


public class MyAutoCompleteTextView extends AutoCompleteTextView {

    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;

    public MyAutoCompleteTextView(Context context) {
        super(context);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



    public void performFiltering(CharSequence text){
        super.performFiltering(text, 0);
    }

    //覆盖父类方法，使得可以0输入的时候也产生下拉框选择，默认至少需要1个字符的输入
    @Override
    public boolean enoughToFilter() {
        if(getText().length() == 0){
            return true;
        }else{
            return super.enoughToFilter();
        }
    }

    //增加下拉框显示的机会。
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result =  super.onTouchEvent(event);
        if (!isEnabled() || getAdapter() == null)
            return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                     if(!isPopupShowing()){
                         performFiltering(getText());
                         showDropDown();
                     }
                }
            }
        }

        return result;
    }


}
