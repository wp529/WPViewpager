package com.pzhu.wpviewpager_lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 一键设置图片的viewpager 可以轮询，可加载本地或者网络图片
 */
public class WPViewPager extends FrameLayout{
    private long time; //viewpager轮询时间
    private LinearLayout pointGroup;
    private View point;
    private Context ctx;
    private HandleTouchViewPager vp;


    public WPViewPager(Context context) {
        this(context, null);
    }

    public WPViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.layout_images, this);
        vp = (HandleTouchViewPager) findViewById(R.id.vp_images);
        pointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        point = findViewById(R.id.view_point);
    }

    /**
     * 一行代码设置viewpager的网络图片资源
     *
     * @param resource      网络图片资源url
     * @param pointSelectId 选中时的导航小点
     * @param pointNormalId 正常时的图片小点
     * @param looper        是否需要轮询
     */
    public void setImageResourceByInternet(String[] resource, int pointSelectId, int pointNormalId, boolean looper) {
        vp.setPageCount(resource.length);
        if (pointSelectId == 0) {
            point.setBackgroundResource(R.drawable.page_guide_check);
        } else {
            point.setBackgroundResource(pointSelectId);
        }
        Utils.controlPointMove(ctx, resource.length, pointGroup, point, vp, pointNormalId);
        if (time == 0) {
            time = 5000;
        }
        ImageInternetAdapter adapter = new ImageInternetAdapter(ctx, vp, resource, looper, time);
        vp.setAdapter(adapter);
        vp.setCurrentItem(resource.length * 1000);
    }

    /**
     * 一行代码设置viewpager的本地图片资源
     *
     * @param resource      本地图片资源id
     * @param pointSelectId 选中时的导航小点
     * @param pointNormalId 正常时的图片小点
     * @param looper        是否需要轮询
     */
    public void setImageResourceByLocal(int[] resource, int pointSelectId, int pointNormalId, boolean looper) {
        vp.setPageCount(resource.length);
        if (pointSelectId == 0) {
            point.setBackgroundResource(R.drawable.page_guide_check);
        } else {
            point.setBackgroundResource(pointSelectId);
        }

        Utils.controlPointMove(ctx, resource.length, pointGroup, point, vp, pointNormalId);
        if (time == 0) {
            time = 5000;
        }
        ImageLocalAdapter adapter = new ImageLocalAdapter(ctx, vp, resource, looper, time);
        vp.setAdapter(adapter);
        vp.setCurrentItem(resource.length * 1000);
    }

    public void setLooperTime(long time) {
        this.time = time;
    }

    public void setOnPageClickListener(final OnPageClickListener listener){
        vp.setOnPageClickListener(new HandleTouchViewPager.OnViewPagerClickListener() {
            @Override
            public void onViewPagerClick(int index) {
                listener.onPageClick(index);
            }
        });
    }

}
