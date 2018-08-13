package com.huisu.iyoox.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.huisu.iyoox.BuildConfig;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.util.PolyvStorageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import org.litepal.LitePalApplication;

import java.io.File;
import java.util.ArrayList;


public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();


    public static String CACHEPATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/iyoox/";
    public static String DOWNLOAD_ROOT_DIR = "download";
    public static String DOWNLOAD_URL = CACHEPATH + "/" + DOWNLOAD_ROOT_DIR + "/";

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
        initCache();
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
    }


    private void initCache() {
        File file = new File(CACHEPATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    //加密秘钥和加密向量，在后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93
    /**
     * 加密秘钥
     */
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    /**
     * 加密向量
     */
    private String iv = "2u9gDPKdX6GyQJKU";

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
        setDownloadDir();
        // 设置下载队列总数，多少个视频能同时下载。(默认是1，设置负数和0是没有限制)
        PolyvDownloaderManager.setDownloadQueueCount(1);
    }

    /**
     * 设置下载视频目录
     */
    private void setDownloadDir() {
        String rootDownloadDirName = "polyvdownload";
        ArrayList<File> externalFilesDirs = PolyvStorageUtils.getExternalFilesDirs(getApplicationContext());
        if (externalFilesDirs.size() == 0) {
            // TODO 没有可用的存储设备,后续不能使用视频缓存功能
            Log.e(TAG, "没有可用的存储设备,后续不能使用视频缓存功能");
            return;
        }

        //SD卡会有SD卡接触不良，SD卡坏了，SD卡的状态错误的问题。
        //我们在开发中也遇到了SD卡没有权限写入的问题，但是我们确定APP是有赋予android.permission.WRITE_EXTERNAL_STORAGE权限的。
        //有些是系统问题，有些是SD卡本身的问题，这些问题需要通过重新拔插SD卡来解决。所以如果想要保存下载视频至SD卡请了解这些情况。
        File downloadDir = new File(externalFilesDirs.get(0), rootDownloadDirName);
        PolyvSDKClient.getInstance().setDownloadDir(downloadDir);

        //兼容旧下载视频目录，如果新接入SDK，无需使用以下代码
        //获取SD卡信息
        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {

            @Override
            public void callback() {
                //是否有可移除的存储介质（例如 SD 卡）或内部（不可移除）存储可供使用。
                if (!PolyvDevMountInfo.getInstance().isSDCardAvaiable()) {
                    return;
                }

                //可移除的存储介质（例如 SD 卡），需要写入特定目录/storage/sdcard1/Android/data/包名/。
                //现在PolyvDevMountInfo.getInstance().getExternalSDCardPath()默认返回的目录路径就是/storage/sdcard1/Android/data/包名/。
                //跟PolyvDevMountInfo.getInstance().init(Context context, final OnLoadCallback callback)接口有区别，请保持同步修改。
                ArrayList<File> subDirList = new ArrayList<>();
                String externalSDCardPath = PolyvDevMountInfo.getInstance().getExternalSDCardPath();
                if (!TextUtils.isEmpty(externalSDCardPath)) {
                    StringBuilder dirPath = new StringBuilder();
                    dirPath.append(externalSDCardPath).append(File.separator).append("polyvdownload");
                    File saveDir = new File(dirPath.toString());
                    if (!saveDir.exists()) {
                        saveDir.mkdirs();//创建下载目录
                    }

                    //设置下载存储目录
//					PolyvSDKClient.getInstance().setDownloadDir(saveDir);
//					return;
                    subDirList.add(saveDir);
                }

                //如果没有可移除的存储介质（例如 SD 卡），那么一定有内部（不可移除）存储介质可用，都不可用的情况在前面判断过了。
                File saveDir = new File(PolyvDevMountInfo.getInstance().getInternalSDCardPath() + File.separator + "polyvdownload");
                if (!saveDir.exists()) {
                    saveDir.mkdirs();//创建下载目录
                }

                //设置下载存储目录
//				PolyvSDKClient.getInstance().setDownloadDir(saveDir);
                subDirList.add(saveDir);
                PolyvSDKClient.getInstance().setSubDirList(subDirList);
            }
        }, true);
    }

    public void initUMeng() {
        UMConfigure.init(this, "5b39bfeff43e4806d20000da"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(BuildConfig.LOGSHOW);
        PlatformConfig.setWeixin("wx1ab6fdc63d403355", "b62f77ec061042af3925c87a8ff8bdaa");
    }

    @Override
    protected void attachBaseContext(Context base) {//方法数超过了64K
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}