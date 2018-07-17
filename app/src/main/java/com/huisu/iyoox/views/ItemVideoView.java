package com.huisu.iyoox.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerActivity;
import com.huisu.iyoox.adapter.ItemVideoListAdapter;
import com.huisu.iyoox.entity.VideoTitleModel;

import java.util.ArrayList;
import java.util.List;

public class ItemVideoView extends FrameLayout implements AdapterView.OnItemClickListener {
    private Context context;
    private View view;
    private NoScrollListView noScrollListView;
    private List<VideoTitleModel> videoTitleModels = new ArrayList<>();
    private ItemVideoListAdapter mAdapter;


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

    public void setData(List<VideoTitleModel> titleModel) {
        videoTitleModels.clear();
        videoTitleModels.addAll(titleModel);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoTitleModel titleModel = videoTitleModels.get(position);
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("videoId", titleModel.getShipin_id()+"");
        context.startActivity(intent);
    }
}
