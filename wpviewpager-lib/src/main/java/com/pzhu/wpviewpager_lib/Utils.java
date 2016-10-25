package com.pzhu.wpviewpager_lib;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 工具类
 */
public class Utils {

    private static int mGuideWidth;

    /**
     * 配合viewpager控制小点的移动
     *
     * @param pointCount   小点的个数
     * @param linearLayout 小点的父容器
     * @param view         需要移动的小点
     * @param viewPager    与小点绑定的viewpager
     */
    public static void controlPointMove(Context ctx, int pointCount, final LinearLayout linearLayout, View view, ViewPager viewPager, int normal) {
        setPoint(ctx, pointCount, linearLayout, 20, normal);

        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    // 当layout执行结束后回调此方法
                    @Override
                    public void onGlobalLayout() {
                        linearLayout.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        mGuideWidth = linearLayout.getChildAt(1).getLeft()
                                - linearLayout.getChildAt(0).getLeft();
                    }
                });
        //轮询页面的滑动监听
        viewPager.setOnPageChangeListener(new GuidePageListener(view, pointCount));
    }

    //设置小点的属性
    private static void setPoint(Context ctx, int pointCount, LinearLayout linearLayout, int margin, int resourceID) {
        for (int i = 0; i < pointCount; i++) {
            View point = new View(ctx);
            if(resourceID == 0){
                point.setBackgroundResource(R.drawable.page_guide_normal);
            }else{
                point.setBackgroundResource(resourceID);// 设置引导页默认点
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dp2px(ctx, 20), dp2px(ctx, 20));
            if (i > 0) {
                params.leftMargin = margin;// 设置点间隔
            }

            point.setLayoutParams(params);// 设置点的大小

            linearLayout.addView(point);// 将点添加给线性布局
        }
    }

    static class GuidePageListener implements ViewPager.OnPageChangeListener {
        private View view;
        private int pointCount;

        public GuidePageListener(View view, int pointCount) {
            this.view = view;
            this.pointCount = pointCount;
        }

        // 滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (position % pointCount != pointCount - 1) {
                int len = (int) (mGuideWidth * positionOffset) + (position % pointCount)
                        * mGuideWidth;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();// 获取当前点的布局参数
                params.leftMargin = len;// 设置左边距
                view.setLayoutParams(params);// 重新给小点设置布局参数
            }
        }

        // 某个页面被选中
        @Override
        public void onPageSelected(int position) {
            if (position % pointCount == pointCount - 1 || position % pointCount == 0) {
                int len = (position % pointCount) * mGuideWidth;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();// 获取当前点的布局参数
                params.leftMargin = len;// 设置左边距
                view.setLayoutParams(params);// 重新给小点设置布局参数
            }
        }

        // 滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    /**
     * dp转px
     */
    public static int dp2px(Context ctx, float dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
