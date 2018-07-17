package com.huisu.iyoox.activity.videoplayer;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.VideoTimuModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.MyJZVideoPlayerStandard;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * @author: dl
 * @function: 视频播放界面
 * @date: 18/6/28
 */
public class VideoPlayerActivity extends BaseActivity implements MyJZVideoPlayerStandard.OnWatchOutListener {


    private MyJZVideoPlayerStandard mJZVideoPlayerStandard;
    private JZVideoPlayer.JZAutoFullscreenListener mSensorEventListener;
    private SensorManager mSensorManager;

    private String VIDEO_URL = "http://flv2.bn.netease.com/videolib3/1611/28/GbgsL3639/HD/movie_index.m3u8";
    private String TITLE = "视频标题";
    private String IMAGE_URL = "http://vimg2.ws.126.net/image/snapshot/2016/11/I/M/VC62HMUIM.jpg";
    private ImageButton menuBack;

    @Override
    protected void initView() {
        mJZVideoPlayerStandard = findViewById(R.id.ac_video_player_view);
        menuBack = findViewById(R.id.menu_back);
        //横屏全屏的实现
        //横向
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        //纵向
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JZVideoPlayer.JZAutoFullscreenListener();
    }

    @Override
    protected void initData() {
        setTitle("视频界面");
        String videoId = getIntent().getStringExtra("videoId");
        setVideoPlayerUrl(VIDEO_URL, TITLE, IMAGE_URL);
//        if (!TextUtils.isEmpty(videoId)) {
//            postVideoUrlData(videoId);
//        }
    }

    private void postVideoUrlData(String videoId) {
        RequestCenter.getVideoTimu(videoId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseVideoTimuModel baseVideoUrlModel = (BaseVideoTimuModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE && baseVideoUrlModel.data != null) {
                    VideoTimuModel urlModel = baseVideoUrlModel.data;
                    if (!TextUtils.isEmpty(urlModel.getShipin_url())) {
                        VIDEO_URL = urlModel.getShipin_url();
                        TITLE = urlModel.getShipin_name();
                        setVideoPlayerUrl(VIDEO_URL, TITLE, "");
                    } else {
                        setVideoPlayerUrl(VIDEO_URL, TITLE, IMAGE_URL);
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    protected void setEvent() {
        menuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mJZVideoPlayerStandard.setOnWatchOutListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_video_player;
    }

    /**
     * 设置视频播放地址等参数
     *
     * @param uri      视频地址
     * @param title    视频标题
     * @param imageUrl 视频封面地址
     */
    private void setVideoPlayerUrl(String uri, String title, String imageUrl) {
        mJZVideoPlayerStandard.clearFocus();
        mJZVideoPlayerStandard.setUp(uri
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, title);
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(this).load(imageUrl).into(mJZVideoPlayerStandard.thumbImageView);
        }
    }

    /**
     * 视频播放完后回调
     */
    @Override
    public void onWatchOut() {
        TabToast.makeText("完成播放,进行习题跳转", context);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //home back
        JZVideoPlayer.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JZVideoPlayer.clearSavedProgress(this, null);
        //home back
        JZVideoPlayer.goOnPlayOnPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
