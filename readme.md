#   WPViewpager
 一行代码快速实现ViewPager加载图片 网络和本地图片 可设置轮询 轮询间隔时间 触摸时取消轮询 抬起继续轮询 导航小点跟随页面滑动 可自行设置小点样式 便捷实现导航页面开发 顶部广告开发
#   效果图
     ![image](https://github.com/wp529/WPViewpager/blob/master/pic1/demo1.png?raw=true)
#   步骤
#   引用
   将本类库下载下来作为moudle导入到工作控件 添加依赖 compile project(':wpviewpager-lib')
#    使用详解
##  一、布局
      <com.pzhu.wpviewpager_lib.WPViewPager
        android:id="@+id/vp"
        android:layout_width="match_parent"
        android:layout_height="300dp"/>
## 二、代码
      private int[] guideImages = {
            R.drawable.speech_bubble,
            R.drawable.speech_bubble,
            R.drawable.speech_bubble,
            R.drawable.speech_bubble
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WPViewPager vp = (WPViewPager) findViewById(R.id.vp);
        vp.setImageResourceByLocal(guideImages,R.drawable.aaa,R.drawable.dont,false);
    }
## 三、主要使用方法介绍
    /**
     * 一行代码设置viewpager的网络图片资源
     *
     * @param resource      网络图片资源url
     * @param pointSelectId 选中时的导航小点
     * @param pointNormalId 正常时的图片小点
     * @param looper        是否需要轮询
     */
    public void setImageResourceByInternet(String[] resource, 
    int pointSelectId, int pointNormalId, boolean looper);
    /**
     * 一行代码设置viewpager的本地图片资源
     *
     * @param resource      本地图片资源id
     * @param pointSelectId 选中时的导航小点
     * @param pointNormalId 正常时的图片小点
     * @param looper        是否需要轮询
     */
    public void setImageResourceByLocal(int[] resource, 
    int pointSelectId, int pointNormalId, boolean looper);
    /**
     * 设置点击页面监听回调 回调参数为当前点击第几页
     */
    public void setOnPageClickListener(OnPageClickListener listener);
    /**
     * 设置轮询时间间隔 默认为5000ms
     * 注意：如果需要设置轮询时间间隔 需要在设置数据前调用此方法
     */
    public void setLooperTime(long time);
## 四、注意事项
    如果需要加载网络图片需要初始化imageloader，参考初始化代码(一般在application中初始化):
        
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.parseColor("#f0f0f0")))
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int memCacheSize = 1024 * 1024 * memClass / 8;
        File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/jiecao/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(memCacheSize))
                .memoryCacheSize(memCacheSize)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
        
        如果使用本地图片资源不用初始化
## 五、作者总结
      这个封装是为了简化大家的开发，并且提供了ViewPager不能像ListView那样设置条目点击事件，这是我第一次封装发布，肯定会有不足之处，希望各位多多包       涵并指出，共同进步。
