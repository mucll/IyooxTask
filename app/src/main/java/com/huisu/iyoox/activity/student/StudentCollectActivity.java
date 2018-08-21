package com.huisu.iyoox.activity.student;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerNewActivity;
import com.huisu.iyoox.adapter.StudentCollectAdapter;
import com.huisu.iyoox.entity.CollectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseCollectModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生收藏界面
 */
public class StudentCollectActivity extends BaseActivity implements View.OnClickListener, MyOnItemClickListener {


    private StudentCollectAdapter mAdapter;

    private ArrayList<CollectModel> models = new ArrayList<>();
    private View emptyView;
    private User user;
    private Loading loading;
    private TextView configView;
    private boolean isConfig = false;
    private RecyclerView mRecyclerView;
    private TextView deleteTv;

    @Override
    protected void initView() {
        emptyView = findViewById(R.id.student_collect_empty_view);
        deleteTv = findViewById(R.id.delete_tv);
        configView = findViewById(R.id.tv_submit);
        configView.setVisibility(View.VISIBLE);
        mRecyclerView = findViewById(R.id.student_collect_list_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        mAdapter = new StudentCollectAdapter(context, models);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        setTitle("我的收藏");
        configView.setText("编辑");
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
                    configView.setVisibility(View.VISIBLE);
                } else {
                    configView.setVisibility(View.GONE);
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
        configView.setOnClickListener(this);
        deleteTv.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
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
    public void onItemClick(int position, View view) {
        if (!isConfig) {
            startVideoActivity(position);
        } else {
            CollectModel model = models.get(position);
            model.setDelete(!model.isDelete());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void startVideoActivity(int position) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                isConfig = !isConfig;
                if (isConfig) {
                    configView.setText("完成");
                } else {
                    configView.setText("编辑");
                    setListDataOriginalState();
                }
                deleteTv.setVisibility(isConfig ? View.VISIBLE : View.GONE);
                mAdapter.setConfig(isConfig);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.delete_tv:
                selectDeleteData();
                break;
            default:
                break;
        }
    }

    private void setListDataOriginalState() {
        for (CollectModel model : models) {
            model.setDelete(false);
        }
    }

    private void selectDeleteData() {
        List<String> strings = new ArrayList<>();
        for (CollectModel model : models) {
            if (model.isDelete()) {
                strings.add(model.getZhishidian_id() + "");
            }
        }
        if (strings.size() > 0) {
            deleteCollect(JsonUtils.jsonFromObject(strings));
        } else {
            TabToast.showMiddleToast(context, "请勾选要删除的视频");
        }
    }

    private void deleteCollect(String jsons) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.deleteCollect(user.getUserId(), jsons, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                isConfig = !isConfig;
                configView.setText("编辑");
                mAdapter.setConfig(isConfig);
                deleteTv.setVisibility(isConfig ? View.VISIBLE : View.GONE);
                postCollectDataHttp();
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

}
