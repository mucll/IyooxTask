package com.huisu.iyoox.activity.videoplayer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvBitRate;
import com.easefun.polyvsdk.video.PolyvPlayErrorReason;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureClickListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureLeftUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightDownListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureRightUpListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeLeftListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnGestureSwipeRightListener;
import com.easefun.polyvsdk.video.listener.IPolyvOnPreparedListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoPlayErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoStatusListener;
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
import com.huisu.iyoox.util.PolyvScreenUtils;
import com.huisu.iyoox.util.TabToast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: dl
 * @function: 视频播放界面
 * @date: 18/6/28
 */
public class VideoPlayerNewActivity extends BaseActivity implements MyOnItemClickListener, View.OnClickListener {

    private ArrayList<VideoTitleModel> models = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private VideoPlayerListAdapter mAdapter;
    public VideoTitleModel selectModel;
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
    private PolyvVideoView videoView;

    /**
     * 视频控制栏
     */
    private PolyvPlayerMediaController mediaController;
    /**
     * 视频加载缓冲视图
     */
    private ProgressBar loadingProgress;
    /**
     * 手势出现的进度界面
     */
    private PolyvPlayerProgressView progressView = null;
    /**
     * 手势出现的亮度界面
     */
    private PolyvPlayerLightView lightView = null;
    /**
     * 手势出现的音量界面
     */
    private PolyvPlayerVolumeView volumeView = null;
    /**
     * 缩略图界面
     */
    private PolyvPlayerPreviewView firstStartView = null;

    private LinearLayout videoErrorLayout;
    private TextView videoErrorContent, videoErrorRetry;

    private String vid;
    private int bitrate;
    private int fastForwardPos = 0;
    private boolean isMustFromLocal;
    private boolean isPlay = false;
    private View shareView;
    private View collectView;
    private TextView collectTv;
    private boolean isCollect;

    @Override
    protected void initView() {
        bitrate = getIntent().getIntExtra("bitrate", PolyvBitRate.ziDong.getNum());
        isMustFromLocal = getIntent().getBooleanExtra("isMustFromLocal", false);

        shareView = findViewById(R.id.video_share_rl);
        collectView = findViewById(R.id.video_collect_rl);
        collectTv = findViewById(R.id.video_collect_tv);
        zhangjieContent = findViewById(R.id.video_zhangjia_content_layout);
        zhangjieName = findViewById(R.id.video_zhangjie_name_tv);
        zhangjieCount = findViewById(R.id.video_zhangjie_count_tv);
        mRecyclerView = findViewById(R.id.video_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new VideoPlayerListAdapter(context, models);
        mRecyclerView.setAdapter(mAdapter);

        viewLayout = findViewById(R.id.view_layout);
        videoView = findViewById(R.id.polyv_video_view);
        mediaController = findViewById(R.id.polyv_player_media_controller);
        progressView = findViewById(R.id.polyv_player_progress_view);
        lightView = findViewById(R.id.polyv_player_light_view);
        volumeView = findViewById(R.id.polyv_player_volume_view);
        firstStartView = findViewById(R.id.polyv_player_first_start_view);

        videoErrorLayout = findViewById(R.id.video_error_layout);
        videoErrorContent = findViewById(R.id.video_error_content);
        videoErrorRetry = findViewById(R.id.video_error_retry);

        loadingProgress = findViewById(R.id.loading_progress);
        videoView.setMediaController(mediaController);

        mediaController.initConfig(viewLayout);
        videoView.setOpenAd(true);
        videoView.setOpenTeaser(true);
        videoView.setOpenQuestion(true);
        videoView.setOpenSRT(true);
        videoView.setOpenPreload(true, 2);
        videoView.setOpenMarquee(true);
        videoView.setAutoContinue(true);
        videoView.setNeedGestureDetector(true);

        PolyvScreenUtils.generateHeight16_9(this);
        int playModeCode = getIntent().getIntExtra("playMode", PlayMode.portrait.getCode());
        PlayMode playMode = PlayMode.getPlayMode(playModeCode);
        if (playMode == null) {
            playMode = PlayMode.portrait;
        }
        switch (playMode) {
            case landScape:
                mediaController.changeToLandscape();
                break;
            case portrait:
                mediaController.changeToPortrait();
                break;
        }

    }

    /**
     * 播放视频
     *
     * @param vid             视频id
     * @param bitrate         码率（清晰度）
     * @param startNow        是否现在开始播放视频
     * @param isMustFromLocal 是否必须从本地（本地缓存的视频）播放
     */
    public void play(final String vid, final int bitrate, boolean startNow, final boolean isMustFromLocal) {
        if (TextUtils.isEmpty(vid)) return;
//        if (iv_vlms_cover != null && iv_vlms_cover.getVisibility() == View.VISIBLE){
//            iv_vlms_cover.setVisibility(View.GONE);
//        }

        videoView.release();
        mediaController.hide();
        loadingProgress.setVisibility(View.GONE);
        progressView.resetMaxValue();

        if (startNow) {
            //调用setVid方法视频会自动播放
            videoView.setVid(vid, bitrate, isMustFromLocal);
        } else {
            //视频不播放，先显示一张缩略图
            firstStartView.setCallback(new PolyvPlayerPreviewView.Callback() {

                @Override
                public void onClickStart() {
                    /**
                     * 调用setVid方法视频会自动播放
                     * 如果是有学员登陆的播放，可以在登陆的时候通过
                     * {@link com.easefun.polyvsdk.PolyvSDKClient.getinstance().setViewerId()}设置学员id
                     * 或者调用{@link videoView.setVidWithStudentId}传入学员id进行播放
                     */

                    videoView.setVidWithStudentId(vid, bitrate, isMustFromLocal, "123");
                }
            });
            firstStartView.show(vid);
        }
    }

    @Override
    protected void initData() {
        selectModel = (VideoTitleModel) getIntent().getSerializableExtra("selectModel");
        zhishidianName = getIntent().getStringExtra("zhangjieName");
        List<VideoTitleModel> videoTitleModels = (List<VideoTitleModel>) getIntent().getSerializableExtra("models");
        if (selectModel != null) {
            List<VideoTitleModel> models = LitePal.where("zhishidian_id = ?", String.valueOf(selectModel.getZhishidian_id())).find(VideoTitleModel.class);
            if (models != null && models.size() > 0) {
                collectTv.setSelected(true);
                isCollect = true;
            } else {
                isCollect = false;
                collectTv.setSelected(false);
            }
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
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE) {
                    if (baseVideoUrlModel.data != null) {
                        VideoTimuModel urlModel = baseVideoUrlModel.data;
                        mAdapter.setSelectId(selectModel.getZhishidian_id());
                        mAdapter.notifyDataSetChanged();
                        vid = urlModel.getShipin_url();
                        play(vid, bitrate, false, isMustFromLocal);
                    } else {
                        TabToast.showMiddleToast(context, "暂无视频");
                    }
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
                setCollect();
                break;
            case R.id.video_share_rl:
                break;
            default:
                break;
        }
    }

    private void setCollect() {
        if (isCollect) return;
        boolean success = false;
        if (selectModel != null) {
            success = selectModel.save();
        }
        if (success) {
            isCollect = true;
            collectTv.setSelected(true);
            TabToast.showMiddleToast(this, "收藏成功");
        } else {
            collectTv.setSelected(false);
            TabToast.showMiddleToast(this, "收藏成功失败");
        }
    }

    @Override
    protected void setEvent() {
        shareView.setOnClickListener(this);
        collectView.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);

        videoView.setOnPreparedListener(new IPolyvOnPreparedListener2() {
            @Override
            public void onPrepared() {
                mediaController.preparedView();
                progressView.setViewMaxValue(videoView.getDuration());
                // 没开预加载在这里开始弹幕
                // danmuFragment.start();
            }
        });

        videoView.setOnVideoStatusListener(new IPolyvOnVideoStatusListener() {
            @Override
            public void onStatus(int status) {
                if (status < 60) {
                    Toast.makeText(VideoPlayerNewActivity.this, "状态错误 " + status, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, String.format("状态正常 %d", status));
                }
            }
        });

        videoView.setOnVideoPlayErrorListener(new IPolyvOnVideoPlayErrorListener2() {
            @Override
            public boolean onVideoPlayError(@PolyvPlayErrorReason.PlayErrorReason int playErrorReason) {
                String message = PolyvErrorMessageUtils.getPlayErrorMessage(playErrorReason);
                message += "(error code " + playErrorReason + ")";

                showErrorView(message);
                return true;
            }
        });

        videoView.setOnErrorListener(new IPolyvOnErrorListener2() {
            @Override
            public boolean onError() {
                String message = "当前视频无法播放，请尝试切换网络重新播放或者向管理员反馈(error code " + PolyvPlayErrorReason.VIDEO_ERROR + ")";
                showErrorView(message);
                Toast.makeText(VideoPlayerNewActivity.this, message, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        videoView.setOnGestureLeftUpListener(new IPolyvOnGestureLeftUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, videoView.getBrightness(VideoPlayerNewActivity.this)));
                int brightness = videoView.getBrightness(VideoPlayerNewActivity.this) + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                videoView.setBrightness(VideoPlayerNewActivity.this, brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        videoView.setOnGestureLeftDownListener(new IPolyvOnGestureLeftDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, videoView.getBrightness(VideoPlayerNewActivity.this)));
                int brightness = videoView.getBrightness(VideoPlayerNewActivity.this) - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                videoView.setBrightness(VideoPlayerNewActivity.this, brightness);
                lightView.setViewLightValue(brightness, end);
            }
        });

        videoView.setOnGestureRightUpListener(new IPolyvOnGestureRightUpListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                videoView.setVolume(volume);
                volumeView.setViewVolumeValue(volume, end);
            }
        });

        videoView.setOnGestureRightDownListener(new IPolyvOnGestureRightDownListener() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                videoView.setVolume(volume);
                volumeView.setViewVolumeValue(volume, end);
            }
        });

        videoView.setOnGestureSwipeLeftListener(new IPolyvOnGestureSwipeLeftListener() {

            @Override
            public void callback(boolean start, boolean end) {
                // 左滑事件
                Log.d(TAG, String.format("SwipeLeft %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos < 0)
                        fastForwardPos = 0;
                    videoView.seekTo(fastForwardPos);
                    if (videoView.isCompletedState()) {
                        videoView.start();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos -= 10000;
                    if (fastForwardPos <= 0)
                        fastForwardPos = -1;
                }
                progressView.setViewProgressValue(fastForwardPos, videoView.getDuration(), end, false);
            }
        });

        videoView.setOnGestureSwipeRightListener(new IPolyvOnGestureSwipeRightListener() {

            @Override
            public void callback(boolean start, boolean end) {
                // 右滑事件
                Log.d(TAG, String.format("SwipeRight %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos > videoView.getDuration())
                        fastForwardPos = videoView.getDuration();
                    if (!videoView.isCompletedState()) {
                        videoView.seekTo(fastForwardPos);
                    } else if (videoView.isCompletedState() && fastForwardPos != videoView.getDuration()) {
                        videoView.seekTo(fastForwardPos);
                        videoView.start();
                    }
                    fastForwardPos = 0;
                } else {
                    fastForwardPos += 10000;
                    if (fastForwardPos > videoView.getDuration())
                        fastForwardPos = videoView.getDuration();
                }
                progressView.setViewProgressValue(fastForwardPos, videoView.getDuration(), end, true);
            }
        });

        videoView.setOnGestureClickListener(new IPolyvOnGestureClickListener() {
            @Override
            public void callback(boolean start, boolean end) {
                if (videoView.isInPlaybackState() || videoView.isExceptionCompleted() && mediaController != null)
                    if (mediaController.isShowing())
                        mediaController.hide();
                    else
                        mediaController.show();
            }
        });

        videoErrorRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoErrorLayout.setVisibility(View.GONE);
                //调用setVid方法视频会自动播放
                play(vid, bitrate, true, isMustFromLocal);
            }
        });
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_video_player_new;
    }


    @Override
    public void onItemClick(int position, View view) {
        VideoTitleModel model = models.get(position);
        if (model.getZhishidian_id() != selectModel.getZhishidian_id()) {
            this.selectModel = model;
            List<VideoTitleModel> models = LitePal.where("zhishidian_id = ?", String.valueOf(selectModel.getZhishidian_id())).find(VideoTitleModel.class);
            if (models != null && models.size() > 0) {
                collectTv.setSelected(true);
                isCollect = true;
            } else {
                isCollect = false;
                collectTv.setSelected(false);
            }
            postVideoUrlData(model.getShipin_id());
        }
    }

    public void showErrorView(String message) {
        videoErrorLayout.setVisibility(View.VISIBLE);
        videoErrorContent.setText(message);
    }

    private void clearGestureInfo() {
        videoView.clearGestureInfo();
        progressView.hide();
        volumeView.hide();
        lightView.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //回来后继续播放
        if (isPlay) {
            videoView.onActivityResume();
        }
        mediaController.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearGestureInfo();
        mediaController.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //弹出去暂停
        isPlay = videoView.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.destroy();
        firstStartView.hide();
        mediaController.disable();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (PolyvScreenUtils.isLandscape(this) && mediaController != null) {
                mediaController.changeToPortrait();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 播放模式
     *
     * @author TanQu
     */
    public enum PlayMode {
        /**
         * 横屏
         */
        landScape(3),
        /**
         * 竖屏
         */
        portrait(4);

        private final int code;

        private PlayMode(int code) {
            this.code = code;
        }

        /**
         * 取得类型对应的code
         *
         * @return
         */
        public int getCode() {
            return code;
        }

        public static PlayMode getPlayMode(int code) {
            switch (code) {
                case 3:
                    return landScape;
                case 4:
                    return portrait;
            }

            return null;
        }
    }
}
