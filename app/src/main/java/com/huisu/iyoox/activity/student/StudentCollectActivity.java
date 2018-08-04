package com.huisu.iyoox.activity.student;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerActivity;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerNewActivity;
import com.huisu.iyoox.adapter.StudentCollectAdapter;
import com.huisu.iyoox.entity.VideoTitleModel;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生收藏界面
 */
public class StudentCollectActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    private GridView gridView;
    private StudentCollectAdapter mAdapter;

    private ArrayList<VideoTitleModel> models = new ArrayList<>();
    private View emptyView;

    @Override
    protected void initView() {
        emptyView = findViewById(R.id.student_collect_empty_view);
        gridView = findViewById(R.id.student_collect_list_view);
        mAdapter = new StudentCollectAdapter(context, models);
        gridView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        setTitle("我的收藏");
        List<VideoTitleModel> titleModels = LitePal.findAll(VideoTitleModel.class);
        if (titleModels != null && titleModels.size() > 0) {
            models.clear();
            models.addAll(titleModels);
            mAdapter.notifyDataSetChanged();
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setEvent() {
        setBack();
        gridView.setOnItemClickListener(this);
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, StudentCollectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_collect;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoTitleModel model = models.get(position);
        Intent intent = new Intent(context, VideoPlayerNewActivity.class);
        intent.putExtra("selectModel", model);
        startActivity(intent);
    }
}
