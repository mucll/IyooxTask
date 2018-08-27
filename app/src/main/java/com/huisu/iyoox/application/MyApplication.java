package com.huisu.iyoox.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.alivc.player.AliVcMediaPlayer;
import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.huisu.iyoox.BuildConfig;
import com.huisu.iyoox.util.PolyvStorageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import org.litepal.LitePalApplication;

import java.io.File;
import java.util.ArrayList;


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
        initPolyvCilent();
        //初始化播放器
        AliVcMediaPlayer.init(getApplicationContext());


    }

    //加密秘钥和加密向量，在后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93

    public void initPolyvCilent() {
        //网络方式取得SDK加密串，（推荐）
        //网络获取到的SDK加密串可以保存在本地SharedPreference中，下次先从本地获取
//		new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        //使用SDK加密串来配置
        client.setConfig("4M8bz4R7szj26ldbt4SD6pBUcTtKyS3NRjc9aP5BGqMAl+gR574y2NWmTzxyxpfST15EqPTDhfukzvONDaWl8sy5APM83cAhPGjAhNT7VB1TjzkypifXMRRfCOBVuy0hdniLl0nIT4HmPUw/rFalIg==");
        //初始化SDK设置
        client.initSetting(getApplicationContext());
        //启动Bugly
        client.initCrashReport(getApplicationContext());
        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
        // 设置下载队列总数，多少个视频能同时下载。(默认是1，设置负数和0是没有限制)
        PolyvDownloaderManager.setDownloadQueueCount(1);
    }


    public void initUMeng() {
//        UMConfigure.init(this, "5b39bfeff43e4806d20000da"
//                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
//        UMConfigure.setLogEnabled(BuildConfig.LOGSHOW);
//        PlatformConfig.setWeixin("wx1ab6fdc63d403355", "b62f77ec061042af3925c87a8ff8bdaa");
    }

    @Override
    protected void attachBaseContext(Context base) {//方法数超过了64K
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}