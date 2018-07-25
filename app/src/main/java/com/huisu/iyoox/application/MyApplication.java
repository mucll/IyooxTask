package com.huisu.iyoox.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;


import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.huisu.iyoox.BuildConfig;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.okhttp.CommonOkHttpClient;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import org.litepal.LitePalApplication;

import java.io.File;

import okhttp3.OkHttpClient;


public class MyApplication extends Application {
    public static String CACHEPATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/iyoox/";

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
        if (hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION))
            initFresco();
        else {
            // 没有权限，暂时使用默认配置。
            Fresco.initialize(this);
        }
    }

    public void initUMeng() {
        UMConfigure.init(this, "5b39bfeff43e4806d20000da"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(BuildConfig.LOGSHOW);
        PlatformConfig.setWeixin("wx1ab6fdc63d403355", "b62f77ec061042af3925c87a8ff8bdaa");
    }

    /**
     * 高级初始话Fresco。
     */
    public void initFresco() {
        // 你的OkHttpClient根据你的设计来，建议是单例：
        OkHttpClient okHttpClient = CommonOkHttpClient.getOkHttpClient();
        Fresco.initialize(this, OkHttpImagePipelineConfigFactory.newBuilder(this, okHttpClient)
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(this)
                                .setBaseDirectoryPath(new File("SD卡的路径..."))
                                .build()
                )
                .build()
        );
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {
        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(this, permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context base) {//方法数超过了64K
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}