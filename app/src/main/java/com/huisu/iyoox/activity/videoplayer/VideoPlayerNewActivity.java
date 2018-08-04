package com.huisu.iyoox.activity.videoplayer;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easefun.polyvsdk.video.PolyvVideoView;
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
public class VideoPlayerNewActivity extends BaseActivity implements MyOnItemClickListener {

    private MyJZVideoPlayerStandard mJZVideoPlayerStandard;
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
    /**
     * 播放器的parentView
     */
    private RelativeLayout viewLayout = null;
    /**
     * 播放主视频播放器
     */
    private PolyvVideoView polyvVideoView;
    /**
     * 视频控制栏
     */
//    private PolyvPlayerMediaController mediaController = null;

    @Override
    protected void initView() {
//        mJZVideoPlayerStandard = findViewById(R.id.ac_video_player_view);
        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        polyvVideoView = findViewById(R.id.polyv_video_view);
        zhangjieContent = findViewById(R.id.video_zhangjia_content_layout);
        zhangjieName = findViewById(R.id.video_zhangjie_name_tv);
        zhangjieCount = findViewById(R.id.video_zhangjie_count_tv);
        mRecyclerView = findViewById(R.id.video_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new VideoPlayerListAdapter(context, models);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        selectModel = (VideoTitleModel) getIntent().getSerializableExtra("selectModel");
        zhishidianName = getIntent().getStringExtra("zhangjieName");
        List<VideoTitleModel> videoTitleModels = (List<VideoTitleModel>) getIntent().getSerializableExtra("models");
        if (selectModel != null) {
//            postVideoUrlData(selectModel.getShipin_id());
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
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_video_player_new;
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


    @Override
    public void onItemClick(int position, View view) {
        VideoTitleModel model = models.get(position);
        if (model.getZhishidian_id() != selectModel.getZhishidian_id()) {
            this.selectModel = model;
//            postVideoUrlData(model.getShipin_id());
        }
    }
}
