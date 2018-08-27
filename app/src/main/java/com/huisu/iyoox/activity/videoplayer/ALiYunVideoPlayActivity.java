package com.huisu.iyoox.activity.videoplayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.VcPlayerLog;
import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.huisu.iyoox.activity.VipBuyActivity;
import com.huisu.iyoox.adapter.VideoPlayerListAdapter;
import com.huisu.iyoox.alivideo.AliyunScreenMode;
import com.huisu.iyoox.alivideo.AliyunVodPlayerView;
import com.huisu.iyoox.alivideo.control.ControlView;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 播放器和播放列表界面 Created by Mulberry on 2018/4/9.
 */
public class ALiYunVideoPlayActivity extends AppCompatActivity implements View.OnClickListener, MyOnItemClickListener {

    private PlayerHandler playerHandler;

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

    public static boolean init = false;
    private boolean wifi = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        if (!StringUtils.isWifi(context)) {
            wifi = false;
            video_hint_layout.setVisibility(View.GONE);
        } else {
            wifi = true;
        }
    }

    private void initData() {
        user = UserManager.getInstance().getUser();
        selectModel = (VideoTitleModel) getIntent().getSerializableExtra("selectModel");
        zhishidianName = getIntent().getStringExtra("zhangjieName");
        List<VideoTitleModel> videoTitleModels = (List<VideoTitleModel>) getIntent().getSerializableExtra("models");
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
        } else {
            zhangjieContent.setVisibility(View.GONE);
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
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
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
                            if (wifi) {
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
        RequestCenter.collectZhishidian(user.getUserId(), selectModel.getZhishidian_id() + "", new DisposeDataListener() {
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

    private static class MyPrepareListener implements IAliyunVodPlayer.OnPreparedListener {

        private WeakReference<ALiYunVideoPlayActivity> activityWeakReference;

        public MyPrepareListener(ALiYunVideoPlayActivity skinActivity) {
            activityWeakReference = new WeakReference<>(skinActivity);
        }

        @Override
        public void onPrepared() {
            ALiYunVideoPlayActivity activity = activityWeakReference.get();
        }
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
            init = true;
            video_hint_layout.setVisibility(View.GONE);
            ALiYunVideoPlayActivity activity = activityWeakReference.get();
            if (activity != null) {
            }
        }
    }


    private class MyPlayViewClickListener implements AliyunVodPlayerView.OnPlayerViewClickListener {
        @Override
        public void onClick(AliyunScreenMode screenMode, AliyunVodPlayerView.PlayViewType viewType) {
            // 如果当前的Type是Download, 就显示Download对话框
            if (viewType == AliyunVodPlayerView.PlayViewType.Download) {
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

    private void setPlaySource(String vod, String auth) {
        AliyunPlayAuth.AliyunPlayAuthBuilder aliyunPlayAuthBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        aliyunPlayAuthBuilder.setVid(vod);
        aliyunPlayAuthBuilder.setPlayAuth(auth);
        aliyunPlayAuthBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_STAND);
        AliyunPlayAuth mPlayAuth = aliyunPlayAuthBuilder.build();
        mAliyunVodPlayerView.setAuthInfo(mPlayAuth);
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
                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
        init = false;
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
                        Toast.makeText(mActivty.get(), msg.getData().getString(DOWNLOAD_ERROR_KEY), Toast.LENGTH_LONG)
                                .show();
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
            if (!StringUtils.isWifi(context)) {
                wifi = false;
                video_hint_layout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNetUnConnected() {
            ALiYunVideoPlayActivity activity = weakReference.get();
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
            if (!init) {
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
}