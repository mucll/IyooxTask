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
import com.huisu.iyoox.entity.CollectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseCollectModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.Loading;

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

    private ArrayList<CollectModel> models = new ArrayList<>();
    private View emptyView;
    private User user;
    private Loading loading;

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
        user = UserManager.getInstance().getUser();
        postCollectDataHttp();
    }

    private void postCollectDataHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getCollectList(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                models.clear();
                BaseCollectModel baseModel = (BaseCollectModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    models.addAll(baseModel.data);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
                emptyView.setVisibility(View.VISIBLE);
            }
        });
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
        CollectModel model = models.get(position);
        VideoTitleModel titleModel = new VideoTitleModel();
        titleModel.setShipin_id(model.getVedio_id());
        titleModel.setShipin_name(model.getVedio_name());
        titleModel.setZhishidian_id(model.getZhishidian_id());
        List<VideoTitleModel> videoTitleModels = new ArrayList<>();
        videoTitleModels.add(titleModel);
        Intent intent = new Intent(context, VideoPlayerNewActivity.class);
        intent.putExtra("selectModel", titleModel);
        intent.putExtra("zhangjieName", model.getZhishidian_name());
        intent.putExtra("models", (Serializable) videoTitleModels);
        startActivity(intent);
    }
}
