package com.huisu.iyoox.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.alivc.player.AliVcMediaPlayer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePalApplication;


public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();

    public static String CACHEPATH = "";
    public static String DOWNLOAD_ROOT_DIR = "download";
    public static String DOWNLOAD_URL = "";

    private static MyApplication mApplication = null;

    public static MyApplication getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        LitePalApplication.initialize(this);
        initUMeng();
        try {//必须加上/否则剪切照片可能会出错
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                builder.detectFileUriExposure();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        //初始化播放器
        AliVcMediaPlayer.init(getApplicationContext());


    }



    public void initUMeng() {
//        UMConfigure.init(this, "5b83d4f5f29d9863b1000113"
//                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
//        UMConfigure.setLogEnabled(BuildConfig.LOGSHOW);
//        PlatformConfig.setWeixin("wx4a498229ebec148f", "b62f77ec061042af3925c87a8ff8bdaa");
    }

    @Override
    protected void attachBaseContext(Context base) {//方法数超过了64K
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}