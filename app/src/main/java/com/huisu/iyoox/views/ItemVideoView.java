package com.huisu.iyoox.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.videoplayer.ALiYunVideoPlayActivity;
import com.huisu.iyoox.adapter.ItemVideoListAdapter;
import com.huisu.iyoox.entity.VideoTitleModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


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

    public void clearData() {
        videoTitleModels.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoTitleModel titleModel = videoTitleModels.get(position);
        Intent intent = new Intent(context, ALiYunVideoPlayActivity.class);
        intent.putExtra("selectModel", titleModel);
        intent.putExtra("zhangjieName", zhangjieName);
        intent.putExtra("models", (Serializable) videoTitleModels);
        context.startActivity(intent);
    }

}
