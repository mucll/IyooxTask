package com.huisu.iyoox.views;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerActivity;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerNewActivity;
import com.huisu.iyoox.adapter.ItemVideoListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.VideoTimuModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

public class ItemVideoView extends FrameLayout implements AdapterView.OnItemClickListener {
    private Context context;
    private View view;
    private NoScrollListView noScrollListView;
    private List<VideoTitleModel> videoTitleModels = new ArrayList<>();
    private ItemVideoListAdapter mAdapter;
    private String zhangjieName;

    public ItemVideoView(Context context) {
        this(context, null);
    }

    public ItemVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        view = View.inflate(context, R.layout.item_video_view_content_layout, this);
        noScrollListView = view.findViewById(R.id.item_video_list_view);
        mAdapter = new ItemVideoListAdapter(context, videoTitleModels);
        noScrollListView.setAdapter(mAdapter);
        noScrollListView.setOnItemClickListener(this);
    }

    public void setData(List<VideoTitleModel> titleModel, String zhangjieName) {
        this.zhangjieName = zhangjieName;
        videoTitleModels.clear();
        videoTitleModels.addAll(titleModel);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoTitleModel titleModel = videoTitleModels.get(position);
        Intent intent = new Intent(context, VideoPlayerNewActivity.class);
        intent.putExtra("selectModel", titleModel);
        intent.putExtra("zhangjieName", zhangjieName);
        intent.putExtra("models", (Serializable) videoTitleModels);
        context.startActivity(intent);
//        postVideoUrlData(titleModel.getShipin_id() + "");
    }

    private String VIDEO_URL = "http://flv2.bn.netease.com/videolib3/1611/28/GbgsL3639/HD/movie_index.m3u8";
    private String TITLE = "视频标题";

//    private void postVideoUrlData(String videoId) {
//        RequestCenter.getVideoTimu(videoId, new DisposeDataListener() {
//            @Override
//            public void onSuccess(Object responseObj) {
//                BaseVideoTimuModel baseVideoUrlModel = (BaseVideoTimuModel) responseObj;
//                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE && baseVideoUrlModel.data != null) {
//                    VideoTimuModel urlModel = baseVideoUrlModel.data;
//                    if (!TextUtils.isEmpty(urlModel.getShipin_url())) {
//                        VIDEO_URL = urlModel.getShipin_url();
//                        TITLE = urlModel.getShipin_name();
//                        setVideoPlayerUrl(VIDEO_URL, TITLE);
//                    } else {
//                        setVideoPlayerUrl(VIDEO_URL, TITLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Object reasonObj) {
//
//            }
//        });
//    }

    /**
     * 设置视频播放地址等参数
     *
     * @param uri   视频地址
     * @param title 视频标题
     */
    private void setVideoPlayerUrl(String uri, String title) {
        MyJZVideoPlayerStandard.releaseAllVideos();
        MyJZVideoPlayerStandard.startFullscreen(context, MyJZVideoPlayerStandard.class, uri
                , title);
    }
}
