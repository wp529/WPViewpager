package com.pzhu.wpviewpager_lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HandleTouchViewPager extends ViewPager{
    private int firstX;
    private int secondX;
    private OnViewPagerClickListener listener;
    private int count;

    public HandleTouchViewPager(Context context) {
        this(context,null);
    }

    public HandleTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                firstX = (int) event.getX();//按下的时候开始的x的位置
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:

            case MotionEvent.ACTION_UP:
                secondX = (int)event.getX();//up的时候x的位置
                int distance = secondX - firstX;
                if (distance == 0) {
                    if(listener != null){
                        listener.onViewPagerClick(getCurrentItem() % count + 1);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    interface OnViewPagerClickListener{
       void onViewPagerClick(int index);
    }
    public void setOnPageClickListener(OnViewPagerClickListener listener){
        this.listener = listener;
    }

    public void setPageCount(int count){
        this.count = count;
    }
}
