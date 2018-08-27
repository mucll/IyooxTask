package com.huisu.iyoox.activity.student;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.videoplayer.ALiYunVideoPlayActivity;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerNewActivity;
import com.huisu.iyoox.adapter.StudentCollectAdapter;
import com.huisu.iyoox.entity.CollectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseCollectModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DialogUtil;
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

    @Override
    protected void initView() {
        emptyView = findViewById(R.id.student_collect_empty_view);
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
    public void onItemClick(final int position, View view) {
        if (!isConfig) {
            startVideoActivity(position);
        } else {
            DialogUtil.show("提示", "是否删除收藏?", "确认", "取消", this,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectDeleteData(position);
                        }
                    }, null);
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
        Intent intent = new Intent(context, ALiYunVideoPlayActivity.class);
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
                }
                mAdapter.setConfig(isConfig);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void selectDeleteData(int position) {
        CollectModel model = models.get(position);
        List<String> strings = new ArrayList<>();
        strings.add(model.getZhishidian_id() + "");
        if (strings.size() > 0) {
            deleteCollect(JsonUtils.jsonFromObject(strings),position);
        } else {
            TabToast.showMiddleToast(context, "请勾选要删除的视频");
        }
    }

    private void deleteCollect(String jsons,final int position) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.deleteCollect(user.getUserId(), jsons, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                models.remove(position);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

}
