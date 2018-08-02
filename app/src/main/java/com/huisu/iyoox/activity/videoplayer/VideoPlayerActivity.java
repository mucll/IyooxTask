package com.huisu.iyoox.activity.videoplayer;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.VideoPlayerListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.VideoTimuModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.MyJZVideoPlayerStandard;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * @author: dl
 * @function: 视频播放界面
 * @date: 18/6/28
 */
public class VideoPlayerActivity extends BaseActivity implements MyJZVideoPlayerStandard.OnWatchOutListener, MyOnItemClickListener {

    private MyJZVideoPlayerStandard mJZVideoPlayerStandard;
    private JZVideoPlayer.JZAutoFullscreenListener mSensorEventListener;
    private SensorManager mSensorManager;
    private ArrayList<VideoTitleModel> models = new ArrayList<>();

    private String VIDEO_URL = "http://flv2.bn.netease.com/videolib3/1611/28/GbgsL3639/HD/movie_index.m3u8";
    private String IMAGE_URL = "http://vimg2.ws.126.net/image/snapshot/2016/11/I/M/VC62HMUIM.jpg";
    private RecyclerView mRecyclerView;
    private VideoPlayerListAdapter mAdapter;
    public static VideoTitleModel selectModel;
    private TextView zhangjieName;
    private TextView zhangjieCount;
    private View zhangjieContent;
    private String zhishidianName;

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mJZVideoPlayerStandard = findViewById(R.id.ac_video_player_view);
        zhangjieContent = findViewById(R.id.video_zhangjia_content_layout);
        zhangjieName = findViewById(R.id.video_zhangjie_name_tv);
        zhangjieCount = findViewById(R.id.video_zhangjie_count_tv);
        mRecyclerView = findViewById(R.id.video_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new VideoPlayerListAdapter(context, models);
        mRecyclerView.setAdapter(mAdapter);
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
        selectModel = (VideoTitleModel) getIntent().getSerializableExtra("selectModel");
        zhishidianName = getIntent().getStringExtra("zhangjieName");
        List<VideoTitleModel> videoTitleModels = (List<VideoTitleModel>) getIntent().getSerializableExtra("models");
        if (selectModel != null) {
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

    private void postVideoUrlData(final int videoId) {
        RequestCenter.getVideoData(videoId + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseVideoTimuModel baseVideoUrlModel = (BaseVideoTimuModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE && baseVideoUrlModel.data != null) {
                    VideoTimuModel urlModel = baseVideoUrlModel.data;
                    mAdapter.setSelectId(selectModel.getZhishidian_id());
                    mAdapter.notifyDataSetChanged();
                    if (!TextUtils.isEmpty(urlModel.getShipin_url())) {
//                        VIDEO_URL = urlModel.getShipin_url();
                        setVideoPlayerUrl(VIDEO_URL, "", IMAGE_URL);
                    } else {
                        setVideoPlayerUrl(VIDEO_URL, "", IMAGE_URL);
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
        mJZVideoPlayerStandard.setOnWatchOutListener(this);
        mAdapter.setOnItemClickListener(this);
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
        mJZVideoPlayerStandard.releaseAllVideos();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    }

    @Override
    public void setWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void backPressed() {
        onBackPressed();
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

    @Override
    public void onItemClick(int position, View view) {
        VideoTitleModel model = models.get(position);
        if (model.getZhishidian_id() != selectModel.getZhishidian_id()) {
            this.selectModel = model;
            postVideoUrlData(model.getShipin_id());
        }
    }
}
