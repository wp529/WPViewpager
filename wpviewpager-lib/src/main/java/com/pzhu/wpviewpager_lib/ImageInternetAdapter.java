package com.pzhu.wpviewpager_lib;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 所有需要轮询和处理touch事件的viewpager的adapter,
 */
public class ImageInternetAdapter extends PagerAdapter {
    private String[] images;
    private int count;
    private Handler handler = new Handler();
    private Runnable run;
    private long time;
    private Context ctx;

    public ImageInternetAdapter(Context ctx, final ViewPager vp, String[] images, boolean looper, long time) {
        this.images = images;
        this.time = time;
        this.count = images.length;
        this.ctx = ctx;
        //设置viewpager的各项属性
        if(count > 1 && looper){
            setViewPager(vp);
        }
    }

    @Override
    public int getCount() {
        if(count == 1){
            return 1;
        }else{
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View viewLayout = View.inflate(ctx,R.layout.guide_viewpager_item,null);
        ImageView imageView = (ImageView) viewLayout
                .findViewById(R.id.image);
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(images[position % count], imageView);
        container.addView(viewLayout, 0); // 将图片增加到ViewPager
        return viewLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 为viewPager设置好轮询和touch时不轮询
     * @param  viewPager 需要设置轮询的viewpager
     */
    private void setViewPager(final ViewPager viewPager) {
        viewPager.setCurrentItem(50000 - 50000 % images.length);
        run = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                currentItem++;
                viewPager.setCurrentItem(currentItem);
                handler.postDelayed(this, time);
            }
        };
        //设置轮询
        handler.postDelayed(run, time);

        //设置顶端图片的触摸监听 触摸时不轮询
        viewPager.setOnTouchListener(new TopViewPagerTouchListener());
    }

    /**
     * 顶部轮询viewpager的触摸监听
     */
    class TopViewPagerTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacksAndMessages(null);// 删除Handler中的所有消息
                    break;
                case MotionEvent.ACTION_CANCEL:
                    handler.postDelayed(run, time);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.postDelayed(run, time);
                    break;
            }
            return false;
        }

    }
}

