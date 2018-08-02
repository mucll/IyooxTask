package com.huisu.iyoox.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerActivity;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.TabToast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * 这里可以监听到视频播放的生命周期和播放状态
 * 所有关于视频的逻辑都应该写在这里
 * Created by Nathen on 2017/7/2.
 */
public class MyJZVideoPlayerStandard extends JZVideoPlayerStandard {

    private ImageView downLoadIv;
    private ImageView collectIv;
    private boolean isCollect;

    public MyJZVideoPlayerStandard(Context context) {
        super(context);
    }

    public MyJZVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        downLoadIv = findViewById(R.id.video_download_icon_iv);
        collectIv = findViewById(R.id.video_collect_iv);
        collectIv.setOnClickListener(this);
    }

    @Override
    public void setUp(Object[] dataSourceObjects, int defaultUrlMapIndex, int screen, Object... objects) {
        super.setUp(dataSourceObjects, defaultUrlMapIndex, screen, objects);
        backButton.setVisibility(View.VISIBLE);
        List<VideoTitleModel> models = LitePal.where("zhishidian_id = ?", String.valueOf(VideoPlayerActivity.selectModel.getZhishidian_id())).find(VideoTitleModel.class);
        if (models != null && models.size() > 0) {
            collectIv.setImageResource(R.drawable.video_icon_collected);
            isCollect = true;
        } else {
            isCollect = false;
            collectIv.setImageResource(R.drawable.video_icon_collect);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.video_collect_iv:
                if (!isCollect) {
                    boolean success = false;
                    if (VideoPlayerActivity.selectModel != null) {
                        success = VideoPlayerActivity.selectModel.save();
                    }
                    if (success) {
                        isCollect = true;
                        collectIv.setImageResource(R.drawable.video_icon_collected);
                        TabToast.showMiddleToast(getContext(), "收藏成功");
                    }
                }
                break;
            case cn.jzvd.R.id.fullscreen:
                if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                    //click quit fullscreen
                } else {
                    //click goto fullscreen
                }
                break;
            case cn.jzvd.R.id.back:
                if (listener != null) {
                    listener.backPressed();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void clearFloatScreen() {
        super.clearFloatScreen();
        if (listener != null) {
            listener.setWindow();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_standard;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        batteryTimeLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if (listener != null) {
            listener.onWatchOut();
        }
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();

    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
    }

    @Override
    public void startWindowTiny() {
        super.startWindowTiny();

    }

    /**
     * 回调接口
     */
    public interface OnWatchOutListener {
        /**
         * 播放完毕
         */
        public void onWatchOut();

        /**
         * 隐藏状态栏
         */
        public void setWindow();

        /**
         * 返回键
         */
        public void backPressed();

    }

    private OnWatchOutListener listener;

    public void setOnWatchOutListener(OnWatchOutListener listener) {
        this.listener = listener;
    }
}
