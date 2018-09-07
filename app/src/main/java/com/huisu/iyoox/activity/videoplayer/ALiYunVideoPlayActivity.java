package com.huisu.iyoox.activity.videoplayer;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.downloader.AliyunDownloadConfig;
import com.aliyun.vodplayer.downloader.AliyunDownloadInfoListener;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.downloader.AliyunRefreshPlayAuthCallback;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.activity.VipBuyActivity;
import com.huisu.iyoox.adapter.VideoPlayerListAdapter;
import com.huisu.iyoox.alivideo.AliyunScreenMode;
import com.huisu.iyoox.alivideo.AliyunVodPlayerView;
import com.huisu.iyoox.alivideo.constants.PlayParameter;
import com.huisu.iyoox.alivideo.control.ControlView;
import com.huisu.iyoox.alivideo.download.AddDownloadView;
import com.huisu.iyoox.alivideo.download.AlivcDialog;
import com.huisu.iyoox.alivideo.download.AlivcDownloadMediaInfo;
import com.huisu.iyoox.alivideo.download.DownloadChoiceDialog;
import com.huisu.iyoox.alivideo.download.DownloadDataProvider;
import com.huisu.iyoox.alivideo.download.DownloadView;
import com.huisu.iyoox.alivideo.tipsview.ErrorInfo;
import com.huisu.iyoox.alivideo.utils.Commen;
import com.huisu.iyoox.alivideo.utils.PlayAuthUtil;
import com.huisu.iyoox.alivideo.utils.ScreenUtils;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoTimuModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.ShareDialog;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 播放器和播放列表界面 Created by Mulberry on 2018/4/9.
 */
public class ALiYunVideoPlayActivity extends AppCompatActivity implements View.OnClickListener, MyOnItemClickListener {

    private PlayerHandler playerHandler;
    private DownloadChoiceDialog downloadDialog;

    private DownloadDataProvider downloadDataProvider;
    private AliyunDownloadManager downloadManager;
    private DownloadView dialogDownloadView;
    private DownloadView downloadView;

    private boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }

    private AliyunVodPlayerView mAliyunVodPlayerView = null;

    /**
     * 开启设置界面的请求码
     */
    private static final int CODE_REQUEST_SETTING = 1000;
    /**
     * 设置界面返回的结果码, 100为vid类型, 200为url类型
     */
    private static final int CODE_RESULT_TYPE_VID = 100;
    private static final int CODE_RESULT_TYPE_URL = 200;

    private ArrayList<VideoTitleModel> models = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private VideoPlayerListAdapter mAdapter;
    private TextView zhangjieName;
    private TextView zhangjieCount;
    private TextView collectTv;
    private View zhangjieContent;
    private View shareView;
    private View collectView;
    private View bipBuyView;
    private View video_hint_layout;
    private TextView userHintView;

    private Context context;

    private User user;
    private String zhishidianName;
    private VideoTimuModel urlModel;
    private VideoTitleModel selectModel;
    private boolean isCollect;

    private String vod = "";

    private String auth = "";

    private boolean wifi = false;

    private Commen commenUtils;
    private AliyunDownloadConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        copyAssets();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_player_layout_skin);
        context = this;
        initView();
        setEvent();
        initData();
    }

    private void initView() {
        initAliyunPlayerView();
        bipBuyView = findViewById(R.id.vip_buy_rl);
        shareView = findViewById(R.id.video_share_rl);
        collectView = findViewById(R.id.video_collect_rl);
        collectTv = findViewById(R.id.video_collect_tv);
        zhangjieContent = findViewById(R.id.video_zhangjia_content_layout);
        zhangjieName = findViewById(R.id.video_zhangjie_name_tv);
        zhangjieCount = findViewById(R.id.video_zhangjie_count_tv);
        video_hint_layout = findViewById(R.id.video_hint_layout);
        userHintView = findViewById(R.id.user_hint_tv);

        mRecyclerView = findViewById(R.id.video_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new VideoPlayerListAdapter(context, models);
        mRecyclerView.setAdapter(mAdapter);
        downloadView = findViewById(R.id.download_view);
        if (!StringUtils.isWifi(context)) {
            wifi = false;
            video_hint_layout.setVisibility(View.GONE);
        } else {
            wifi = true;
        }
    }

    private void initData() {
        user = UserManager.getInstance().getUser();
        int type = getIntent().getIntExtra("type", Constant.ERROR_CODE);
        selectModel = (VideoTitleModel) getIntent().getSerializableExtra("selectModel");
        zhishidianName = getIntent().getStringExtra("zhangjieName");
        List<VideoTitleModel> videoTitleModels = (List<VideoTitleModel>) getIntent().getSerializableExtra("models");
        if (type == Constant.START_VOD_TYPE) {
            collectView.setEnabled(false);
            String vod = getIntent().getStringExtra("vod");
            if (!TextUtils.isEmpty(vod)) {
                this.vod = vod;
                postPlayAuthHttp(vod);
                video_hint_layout.setVisibility(View.GONE);
            }
        }
        if (type == Constant.PLAY_PARAM_URL_TYPE) {
            collectView.setEnabled(false);
            String vod = getIntent().getStringExtra("url");
            String title = getIntent().getStringExtra("title");
            if (!TextUtils.isEmpty(vod)) {
                this.vod = vod;
                changePlayLocalSource(vod, title);
                video_hint_layout.setVisibility(View.GONE);
                mAliyunVodPlayerView.hintDownLoad();
            }
        }
        if (selectModel != null) {
            userHintView.setText(selectModel.getShipin_name());
            postVideoUrlData(selectModel.getShipin_id());
        }
        if (videoTitleModels != null && videoTitleModels.size() > 0) {
            zhangjieName.setText(zhishidianName);
            zhangjieCount.setText("共" + videoTitleModels.size() + "讲");
            models.clear();
            models.addAll(videoTitleModels);
            mAdapter.notifyDataSetChanged();
            scrollToPosition();
        } else {
            zhangjieContent.setVisibility(View.GONE);
        }
    }

    private void scrollToPosition() {
        for (int i = 0; i < models.size(); i++) {
            VideoTitleModel model = models.get(i);
            if (selectModel != null && selectModel.getZhishidian_id() == model.getZhishidian_id()) {
                if (mRecyclerView != null) {
                    mRecyclerView.scrollToPosition(i);
                }
                return;
            }
        }
    }

    private void setEvent() {
        shareView.setOnClickListener(this);
        collectView.setOnClickListener(this);
        bipBuyView.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }


    private void initAliyunPlayerView() {
        mAliyunVodPlayerView = findViewById(R.id.video_view);
        //保持屏幕敞亮
        mAliyunVodPlayerView.setKeepScreenOn(true);
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/iyoox_save_cache";
        mAliyunVodPlayerView.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/);
        mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue);
        //mAliyunVodPlayerView.setCirclePlay(true);
        mAliyunVodPlayerView.setAutoPlay(true);

        mAliyunVodPlayerView.setOnPreparedListener(new MyPrepareListener(this));
        mAliyunVodPlayerView.setNetConnectedListener(new MyNetConnectedListener(this));
        mAliyunVodPlayerView.setOnCompletionListener(new MyCompletionListener(this));
        mAliyunVodPlayerView.setOnFirstFrameStartListener(new MyFrameInfoListener(this));
        mAliyunVodPlayerView.setOnChangeQualityListener(new MyChangeQualityListener(this));
        mAliyunVodPlayerView.setOnStoppedListener(new MyStoppedListener(this));
        mAliyunVodPlayerView.setmOnPlayerViewClickListener(new MyPlayViewClickListener());
        mAliyunVodPlayerView.setOrientationChangeListener(new MyOrientationChangeListener(this));
        mAliyunVodPlayerView.setOnUrlTimeExpiredListener(new MyOnUrlTimeExpiredListener(this));
        mAliyunVodPlayerView.setOnShowMoreClickListener(new MyShowMoreClickLisener(this));
        mAliyunVodPlayerView.setOnTipClickListener(new MyTipClickListener());
        mAliyunVodPlayerView.enableNativeLog();
    }

    /**
     * 获取PlayAuth
     */
    private void postPlayAuthHttp(String vodId) {
        RequestCenter.getplayAuth(vodId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    auth = jsonObject.getString("data");
                    if (!TextUtils.isEmpty(auth)) {
                        setPlaySource(vod, auth);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("getplayAuth ERROR");
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_collect_rl:
                postCollectHttp();
                break;
            case R.id.video_share_rl:
                ShareDialog.show(context, "分享", selectModel.getShipin_name(), "", "http://www.baidu.com", R.drawable.icon_app);
                break;
            case R.id.vip_buy_rl:
                VipBuyActivity.start(context);
                break;
            default:
                break;
        }
    }

    /**
     * 获取视频VOD
     */
    private void postVideoUrlData(final int videoId) {
        RequestCenter.getVideoData(user.getUserId(), selectModel.getZhishidian_id() + "", videoId + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                userHintView.setText(selectModel.getShipin_name());
                BaseVideoTimuModel baseVideoUrlModel = (BaseVideoTimuModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE) {
                    if (baseVideoUrlModel.data != null) {
                        urlModel = baseVideoUrlModel.data;
                        //是否收藏
                        if (urlModel.isIs_shipin_collected()) {
                            isCollect = true;
                            collectTv.setSelected(true);
                        } else {
                            isCollect = false;
                            collectTv.setSelected(false);
                        }
                        //高亮选中的
                        mAdapter.setSelectId(selectModel.getZhishidian_id());
                        mAdapter.notifyDataSetChanged();
                        //视频ID
                        vod = urlModel.getShipin_url();
                        if (!TextUtils.isEmpty(vod)) {
                            postPlayAuthHttp(vod);
                            video_hint_layout.setVisibility(View.GONE);
                        } else {
                            TabToast.showMiddleToast(context, "暂无视频");
                            if (wifi && !MainActivity.vod_init) {
                                video_hint_layout.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        collectTv.setSelected(false);
                        TabToast.showMiddleToast(context, "暂无视频");
                        if (wifi) {
                            video_hint_layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                video_hint_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 调用收藏接口
     */
    private void postCollectHttp() {
        if (urlModel == null) {
            TabToast.showMiddleToast(context, "暂无视频");
            return;
        }
        if (isCollect) {
            TabToast.showMiddleToast(context, "该视频已收藏");
            return;
        }
        RequestCenter.collectZhishidian(user.getUserId(), selectModel.getZhishidian_id() + "", "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                isCollect = true;
                collectTv.setSelected(true);
                TabToast.showMiddleToast(context, "收藏成功");
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    public void onItemClick(int position, View view) {
        VideoTitleModel model = models.get(position);
        if (model.getZhishidian_id() != selectModel.getZhishidian_id()) {
            this.selectModel = model;
            postVideoUrlData(model.getShipin_id());
        }
    }

    /**
     * 设置准备事件监听
     */
    private static class MyPrepareListener implements IAliyunVodPlayer.OnPreparedListener {

        private WeakReference<ALiYunVideoPlayActivity> activityWeakReference;

        public MyPrepareListener(ALiYunVideoPlayActivity skinActivity) {
            activityWeakReference = new WeakReference<>(skinActivity);
        }

        @Override
        public void onPrepared() {
            ALiYunVideoPlayActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onPrepared();
            }
        }
    }

    private void onPrepared() {
        Toast.makeText(ALiYunVideoPlayActivity.this.getApplicationContext(), R.string.toast_prepare_success,
                Toast.LENGTH_SHORT).show();
    }


    private static class MyCompletionListener implements IAliyunVodPlayer.OnCompletionListener {

        private WeakReference<ALiYunVideoPlayActivity> activityWeakReference;

        public MyCompletionListener(ALiYunVideoPlayActivity skinActivity) {
            activityWeakReference = new WeakReference<>(skinActivity);
        }

        @Override
        public void onCompletion() {

            ALiYunVideoPlayActivity activity = activityWeakReference.get();
        }
    }


    private class MyFrameInfoListener implements IAliyunVodPlayer.OnFirstFrameStartListener {

        private WeakReference<ALiYunVideoPlayActivity> activityWeakReference;

        public MyFrameInfoListener(ALiYunVideoPlayActivity skinActivity) {
            activityWeakReference = new WeakReference<>(skinActivity);
        }

        @Override
        public void onFirstFrameStart() {
            MainActivity.vod_init = true;
            video_hint_layout.setVisibility(View.GONE);
//            ALiYunVideoPlayActivity activity = activityWeakReference.get();
//            if (activity != null) {
//            }
            if (user != null && selectModel != null) {
                addShiPinCount(user.getUserId(), selectModel.getZhishidian_id() + "");
            }
        }
    }

    /**
     * 下载dialog
     */
    private class MyPlayViewClickListener implements AliyunVodPlayerView.OnPlayerViewClickListener {
        @Override
        public void onClick(AliyunScreenMode screenMode, AliyunVodPlayerView.PlayViewType viewType) {
            // 如果当前的Type是Download, 就显示Download对话框
            if (viewType == AliyunVodPlayerView.PlayViewType.Download) {
                showAddDownloadView(screenMode);
            }
        }
    }


    private static class MyChangeQualityListener implements IAliyunVodPlayer.OnChangeQualityListener {

        private WeakReference<ALiYunVideoPlayActivity> activityWeakReference;

        public MyChangeQualityListener(ALiYunVideoPlayActivity skinActivity) {
            activityWeakReference = new WeakReference<>(skinActivity);
        }

        @Override
        public void onChangeQualitySuccess(String finalQuality) {

            ALiYunVideoPlayActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onChangeQualitySuccess(finalQuality);
            }
        }

        @Override
        public void onChangeQualityFail(int code, String msg) {
            ALiYunVideoPlayActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onChangeQualityFail(code, msg);
            }
        }
    }

    private void onChangeQualitySuccess(String finalQuality) {
        Toast.makeText(ALiYunVideoPlayActivity.this.getApplicationContext(),
                getString(R.string.log_change_quality_success), Toast.LENGTH_SHORT).show();
    }

    void onChangeQualityFail(int code, String msg) {
        Toast.makeText(ALiYunVideoPlayActivity.this.getApplicationContext(),
                getString(R.string.log_change_quality_fail), Toast.LENGTH_SHORT).show();
    }

    private static class MyStoppedListener implements IAliyunVodPlayer.OnStoppedListener {

        private WeakReference<ALiYunVideoPlayActivity> activityWeakReference;

        public MyStoppedListener(ALiYunVideoPlayActivity skinActivity) {
            activityWeakReference = new WeakReference<>(skinActivity);
        }

        @Override
        public void onStopped() {
            ALiYunVideoPlayActivity activity = activityWeakReference.get();
        }
    }

    /**
     * 准备播放
     */
    private void setPlaySource(String vod, String auth) {
        AliyunPlayAuth.AliyunPlayAuthBuilder aliyunPlayAuthBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        aliyunPlayAuthBuilder.setVid(vod);
        aliyunPlayAuthBuilder.setPlayAuth(auth);
        aliyunPlayAuthBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_STAND);
        AliyunPlayAuth mPlayAuth = aliyunPlayAuthBuilder.build();
        mAliyunVodPlayerView.setAuthInfo(mPlayAuth);
        downloadManager.prepareDownloadMedia(mPlayAuth);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updatePlayerViewMode();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onStop();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
    }


    private void updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //转为竖屏了。
                //显示状态栏
                //                if (!isStrangePhone()) {
                //                    getSupportActionBar().show();
                //                }

                if (!isStrangePhone()) {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }

                //设置view的布局，宽高之类
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
                //                }

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //转到横屏了。
                //隐藏状态栏
                if (!isStrangePhone()) {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }

                //设置view的布局，宽高
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = 0;
                //                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mAliyunVodPlayerView != null) {

            mAliyunVodPlayerView.onDestroy();
            mAliyunVodPlayerView = null;
        }

        if (playerHandler != null) {
            playerHandler.removeMessages(DOWNLOAD_ERROR);
            playerHandler = null;
        }

        if (commenUtils != null) {
            commenUtils.onDestroy();
            commenUtils = null;
        }
        UMShareAPI.get(this).release();
        MainActivity.vod_init = false;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAliyunVodPlayerView != null) {
            boolean handler = mAliyunVodPlayerView.onKeyDown(keyCode, event);
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //解决某些手机上锁屏之后会出现标题栏的问题。
        updatePlayerViewMode();
    }

    private static final int DOWNLOAD_ERROR = 1;
    private static final String DOWNLOAD_ERROR_KEY = "error_key";

    private static class PlayerHandler extends Handler {
        //持有弱引用ALiYunVideoPlayActivity,GC回收时会被回收掉.
        private final WeakReference<ALiYunVideoPlayActivity> mActivty;

        public PlayerHandler(ALiYunVideoPlayActivity activity) {
            mActivty = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ALiYunVideoPlayActivity activity = mActivty.get();
            super.handleMessage(msg);
            if (activity != null) {
                switch (msg.what) {
                    case DOWNLOAD_ERROR:
                        Log.e("alidonwload", msg.getData().getString(DOWNLOAD_ERROR_KEY));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static class MyOrientationChangeListener implements AliyunVodPlayerView.OnOrientationChangeListener {

        private final WeakReference<ALiYunVideoPlayActivity> weakReference;

        public MyOrientationChangeListener(ALiYunVideoPlayActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void orientationChange(boolean from, AliyunScreenMode currentMode) {
            ALiYunVideoPlayActivity activity = weakReference.get();
        }
    }


    /**
     * 判断是否有网络的监听
     */
    private class MyNetConnectedListener implements AliyunVodPlayerView.NetConnectedListener {
        WeakReference<ALiYunVideoPlayActivity> weakReference;

        public MyNetConnectedListener(ALiYunVideoPlayActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onReNetConnected(boolean isReconnect) {
            ALiYunVideoPlayActivity activity = weakReference.get();
            if (activity != null) {
                activity.onReNetConnected(isReconnect);
            }
            if (!StringUtils.isWifi(context)) {
                wifi = false;
                video_hint_layout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNetUnConnected() {
            ALiYunVideoPlayActivity activity = weakReference.get();
            if (activity != null) {
                activity.onNetUnConnected();
            }
        }


    }

    private static class MyOnUrlTimeExpiredListener implements IAliyunVodPlayer.OnUrlTimeExpiredListener {
        WeakReference<ALiYunVideoPlayActivity> weakReference;

        public MyOnUrlTimeExpiredListener(ALiYunVideoPlayActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onUrlTimeExpired(String s, String s1) {
            ALiYunVideoPlayActivity activity = weakReference.get();
        }
    }

    private static class MyShowMoreClickLisener implements ControlView.OnShowMoreClickListener {
        WeakReference<ALiYunVideoPlayActivity> weakReference;

        MyShowMoreClickLisener(ALiYunVideoPlayActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void showMore() {
            ALiYunVideoPlayActivity activity = weakReference.get();
        }
    }

    private class MyTipClickListener implements AliyunVodPlayerView.OnTipClickListener {
        @Override
        public void onContinuePlay() {
            //点击继续播放按钮
            if (!MainActivity.vod_init) {
                video_hint_layout.setVisibility(View.VISIBLE);
                wifi = true;
            }
        }

        @Override
        public void onStopPlay() {

        }

        @Override
        public void onRetryPlay() {

        }

        @Override
        public void onReplay() {

        }
    }

    /**
     * 统计观看视频次数
     */
    private void addShiPinCount(String userId, String zhishiId) {
        RequestCenter.addShiPinCount(userId, zhishiId, "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    List<AliyunDownloadMediaInfo> aliyunDownloadMediaInfoList;

    private void onDownloadPrepared(List<AliyunDownloadMediaInfo> infos) {
        aliyunDownloadMediaInfoList = new ArrayList<>();
        aliyunDownloadMediaInfoList.addAll(infos);
    }

    /**
     * 下载显示download 对话框
     *
     * @param screenMode
     */
    private void showAddDownloadView(AliyunScreenMode screenMode) {
        downloadDialog = new DownloadChoiceDialog(this, screenMode);
        final AddDownloadView contentView = new AddDownloadView(this, screenMode);
        contentView.onPrepared(aliyunDownloadMediaInfoList);
        contentView.setOnViewClickListener(viewClickListener);
        final View inflate = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.alivc_dialog_download_video, null, false);
        dialogDownloadView = inflate.findViewById(R.id.download_view);
        downloadDialog.setContentView(contentView);
        downloadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
        downloadDialog.show();
        downloadDialog.setCanceledOnTouchOutside(true);

        if (screenMode == AliyunScreenMode.Full) {
            contentView.setOnShowVideoListLisener(new AddDownloadView.OnShowNativeVideoBtnClickListener() {
                @Override
                public void onShowVideo() {
                    downloadViewSetting(dialogDownloadView);
                    downloadDialog.setContentView(inflate);
                }
            });
        }
    }

    private AliyunDownloadMediaInfo aliyunDownloadMediaInfo;
    private long currentDownloadIndex = 0;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    /**
     * 开始下载的事件监听
     */
    private AddDownloadView.OnViewClickListener viewClickListener = new AddDownloadView.OnViewClickListener() {
        @Override
        public void onCancel() {
            if (downloadDialog != null) {
                downloadDialog.dismiss();
            }
        }

        @Override
        public void onDownload(AliyunDownloadMediaInfo info) {
            if (downloadDialog != null) {
                downloadDialog.dismiss();
            }

            aliyunDownloadMediaInfo = info;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                int permission = ContextCompat.checkSelfPermission(ALiYunVideoPlayActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ALiYunVideoPlayActivity.this, PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE);
                } else {
                    addNewInfo(info);
                }
            } else {
                addNewInfo(info);
            }

        }
    };

    /**
     * 添加下载
     *
     * @param info
     */
    private void addNewInfo(AliyunDownloadMediaInfo info) {
        if (downloadManager != null) {
            downloadManager.addDownloadMedia(info);
            downloadManager.startDownloadMedia(info);
        }
        if (downloadView != null) {
            downloadView.addDownloadMediaInfo(info);
        }

    }

    /**
     * downloadView的配置 里面配置了需要下载的视频的信息, 事件监听等 抽取该方法的主要目的是, 横屏下download dialog的离线视频列表中也用到了downloadView, 而两者显示内容和数据是同步的,
     * 所以在此进行抽取 AliyunPlayerSkinActivity.class#showAddDownloadView(DownloadVie view)中使用
     *
     * @param downloadView
     */
    private void downloadViewSetting(final DownloadView downloadView) {
        downloadView.addAllDownloadMediaInfo(downloadDataProvider.getAllDownloadMediaInfo());

        downloadView.setOnDownloadViewListener(new DownloadView.OnDownloadViewListener() {
            @Override
            public void onStop(AliyunDownloadMediaInfo downloadMediaInfo) {
                downloadManager.stopDownloadMedia(downloadMediaInfo);
            }

            @Override
            public void onStart(AliyunDownloadMediaInfo downloadMediaInfo) {
                downloadManager.startDownloadMedia(downloadMediaInfo);
            }

            @Override
            public void onDeleteDownloadInfo(final ArrayList<AlivcDownloadMediaInfo> alivcDownloadMediaInfos) {
                // 视频删除的dialog
                final AlivcDialog alivcDialog = new AlivcDialog(ALiYunVideoPlayActivity.this);
                alivcDialog.setDialogIcon(R.drawable.icon_delete_tips);
                alivcDialog.setMessage(getResources().getString(R.string.alivc_delete_confirm));
                alivcDialog.setOnConfirmclickListener(getResources().getString(R.string.alivc_dialog_sure),
                        new AlivcDialog.onConfirmClickListener() {
                            @Override
                            public void onConfirm() {
                                alivcDialog.dismiss();
                                if (alivcDownloadMediaInfos != null && alivcDownloadMediaInfos.size() > 0) {
                                    downloadView.deleteDownloadInfo();
                                    if (dialogDownloadView != null) {

                                        dialogDownloadView.deleteDownloadInfo();
                                    }
                                    downloadDataProvider.deleteAllDownloadInfo(alivcDownloadMediaInfos);
                                } else {
                                    Toast.makeText(ALiYunVideoPlayActivity.this, "没有删除的视频选项...", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
                alivcDialog.setOnCancelOnclickListener(getResources().getString(R.string.alivc_dialog_cancle),
                        new AlivcDialog.onCancelOnclickListener() {
                            @Override
                            public void onCancel() {
                                alivcDialog.dismiss();
                            }
                        });
                alivcDialog.show();
            }
        });
        //下载完成后点击事件
        downloadView.setOnDownloadedItemClickListener(new DownloadView.OnDownloadItemClickListener() {
            @Override
            public void onDownloadedItemClick(int positin) {

                ArrayList<AliyunDownloadMediaInfo> downloadedList = downloadDataProvider.getAllDownloadMediaInfo();
                //// 存入顺序和显示顺序相反,  所以进行倒序

                ArrayList<AliyunDownloadMediaInfo> tempList = new ArrayList<>();
                int size = downloadedList.size();
                for (int i = 0; i < size; i++) {
                    if (downloadedList.get(i).getProgress() == 100) {
                        tempList.add(downloadedList.get(i));
                    }
                }

                Collections.reverse(tempList);
                tempList.add(downloadedList.get(downloadedList.size() - 1));
                for (int i = 0; i < size; i++) {
                    AliyunDownloadMediaInfo downloadMediaInfo = downloadedList.get(i);
                    if (!tempList.contains(downloadMediaInfo)) {
                        tempList.add(downloadMediaInfo);
                    }
                }

                if (positin < 0) {
                    Toast.makeText(ALiYunVideoPlayActivity.this, "视频资源不存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 如果点击列表中的视频, 需要将类型改为vid
                AliyunDownloadMediaInfo aliyunDownloadMediaInfo = tempList.get(positin);
                PlayParameter.PLAY_PARAM_TYPE = "localSource";
                if (aliyunDownloadMediaInfo != null) {
                    PlayParameter.PLAY_PARAM_URL = aliyunDownloadMediaInfo.getSavePath();
                    mAliyunVodPlayerView.updateScreenShow();
                    changePlayLocalSource(PlayParameter.PLAY_PARAM_URL, aliyunDownloadMediaInfo.getTitle());
                }

            }

            @Override
            public void onDownloadingItemClick(ArrayList<AlivcDownloadMediaInfo> infos, int position) {
                AlivcDownloadMediaInfo alivcInfo = infos.get(position);
                AliyunDownloadMediaInfo aliyunDownloadInfo = alivcInfo.getAliyunDownloadMediaInfo();
                AliyunDownloadMediaInfo.Status status = aliyunDownloadInfo.getStatus();
                if (status == AliyunDownloadMediaInfo.Status.Error || status == AliyunDownloadMediaInfo.Status.Wait) {
                    //downloadManager.removeDownloadMedia(aliyunDownloadInfo);
                    downloadManager.startDownloadMedia(aliyunDownloadInfo);
                }
            }

        });
    }

    /**
     * 播放本地资源
     *
     * @param url
     * @param title
     */
    private void changePlayLocalSource(String url, String title) {
        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
        alsb.setSource(url);
        alsb.setTitle(title);
        AliyunLocalSource localSource = alsb.build();
        mAliyunVodPlayerView.setLocalSource(localSource);
    }

    private void copyAssets() {
        commenUtils = Commen.getInstance(getApplicationContext()).copyAssetsToSD("encrypt", "aliyun");
        commenUtils.setFileOperateCallback(

                new Commen.FileOperateCallback() {
                    @Override
                    public void onSuccess() {
                        config = new AliyunDownloadConfig();
                        config.setSecretImagePath(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/aliyun/encryptedApp.dat");
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/iyoox_save/");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        config.setDownloadDir(file.getAbsolutePath());
                        //设置同时下载个数
                        config.setMaxNums(2);
                        // 获取AliyunDownloadManager对象
                        downloadManager = AliyunDownloadManager.getInstance(getApplicationContext());
                        downloadManager.setDownloadConfig(config);

                        downloadDataProvider = DownloadDataProvider.getSingleton(getApplicationContext());
                        // 更新sts回调
                        downloadManager.setRefreshAuthCallBack(new MyRefreshPlayAuthCallback());
                        // 视频下载的回调
                        downloadManager.setDownloadInfoListener(new MyDownloadInfoListener(downloadView));
                        //
                        downloadViewSetting(downloadView);
                    }

                    @Override
                    public void onFailed(String error) {
                    }
                });
    }


    private static class MyRefreshPlayAuthCallback implements AliyunRefreshPlayAuthCallback {

        @Override
        public AliyunPlayAuth refreshPlayAuth(String vid, String quality, String format, String title, boolean encript) {
            //NOTE: 注意：这个不能启动线程去请求。因为这个方法已经在线程中调用了。
            String playauth = PlayAuthUtil.getPlayAuth(vid);
            if (playauth == null) {
                return null;
            }
            AliyunPlayAuth.AliyunPlayAuthBuilder authBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
            authBuilder.setPlayAuth(playauth);
            authBuilder.setVid(vid);
            authBuilder.setTitle(title);
            authBuilder.setQuality(quality);
            authBuilder.setFormat(format);
            authBuilder.setIsEncripted(encript ? 1 : 0);
            return authBuilder.build();
        }
    }

    private class MyDownloadInfoListener implements AliyunDownloadInfoListener {

        private DownloadView downloadView;

        public MyDownloadInfoListener(DownloadView downloadView) {
            this.downloadView = downloadView;
        }


        @Override
        public void onPrepared(List<AliyunDownloadMediaInfo> infos) {
            Collections.sort(infos, new Comparator<AliyunDownloadMediaInfo>() {
                @Override
                public int compare(AliyunDownloadMediaInfo mediaInfo1, AliyunDownloadMediaInfo mediaInfo2) {
                    if (mediaInfo1.getSize() > mediaInfo2.getSize()) {
                        return 1;
                    }
                    if (mediaInfo1.getSize() < mediaInfo2.getSize()) {
                        return -1;
                    }

                    if (mediaInfo1.getSize() == mediaInfo2.getSize()) {
                        return 0;
                    }
                    return 0;
                }
            });
            onDownloadPrepared(infos);
        }

        @Override
        public void onStart(AliyunDownloadMediaInfo info) {
            Toast.makeText(ALiYunVideoPlayActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
            //downloadView.addDownloadMediaInfo(info);
            //dbHelper.insert(info, DownloadDBHelper.DownloadState.STATE_DOWNLOADING);
            if (!downloadDataProvider.hasAdded(info)) {
                downloadDataProvider.addDownloadMediaInfo(info);
            }

        }

        @Override
        public void onProgress(AliyunDownloadMediaInfo info, int percent) {
            downloadView.updateInfo(info);
            if (dialogDownloadView != null) {
                dialogDownloadView.updateInfo(info);
            }
        }

        @Override
        public void onStop(AliyunDownloadMediaInfo info) {
            Log.d("yds100", "onStop");
            downloadView.updateInfo(info);
            if (dialogDownloadView != null) {
                dialogDownloadView.updateInfo(info);
            }
            //dbHelper.update(info, DownloadDBHelper.DownloadState.STATE_PAUSE);
        }

        @Override
        public void onCompletion(AliyunDownloadMediaInfo info) {
            Log.d("yds100", "onCompletion");
            downloadView.updateInfoByComplete(info);
            if (dialogDownloadView != null) {
                dialogDownloadView.updateInfoByComplete(info);
            }
            downloadDataProvider.addDownloadMediaInfo(info);
            //aliyunDownloadMediaInfoList.remove(info);
        }

        @Override
        public void onError(AliyunDownloadMediaInfo info, int code, String msg, String requestId) {
            Log.d("yds100", "onError" + msg);
            downloadView.updateInfoByError(info);
            if (dialogDownloadView != null) {
                dialogDownloadView.updateInfoByError(info);
            }
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(DOWNLOAD_ERROR_KEY, msg);
            message.setData(bundle);
            message.what = DOWNLOAD_ERROR;
            playerHandler = new PlayerHandler(ALiYunVideoPlayActivity.this);
            playerHandler.sendMessage(message);
        }

        @Override
        public void onWait(AliyunDownloadMediaInfo outMediaInfo) {
            Log.d("yds100", "onWait");
        }

        @Override
        public void onM3u8IndexUpdate(AliyunDownloadMediaInfo outMediaInfo, int index) {
            Log.d("yds100", "onM3u8IndexUpdate");
        }

    }

    private ErrorInfo currentError = ErrorInfo.Normal;

    private void onNetUnConnected() {
        currentError = ErrorInfo.UnConnectInternet;
        if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size() > 0) {
            downloadManager.stopDownloadMedias(aliyunDownloadMediaInfoList);
        }
    }

    private void onReNetConnected(boolean isReconnect) {
        currentError = ErrorInfo.Normal;
        if (isReconnect) {
            if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size() > 0) {
                int unCompleteDownload = 0;
                for (AliyunDownloadMediaInfo info : aliyunDownloadMediaInfoList) {
                    if (info.getStatus() == AliyunDownloadMediaInfo.Status.Stop) {
                        unCompleteDownload++;
                    }
                }

                if (unCompleteDownload > 0) {
                    Toast.makeText(this, "网络恢复, 请手动开启下载任务...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}