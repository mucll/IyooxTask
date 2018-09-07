package com.huisu.iyoox.activity.student;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.videoplayer.ALiYunGxYsVideoPlayActivity;
import com.huisu.iyoox.activity.videoplayer.ALiYunVideoPlayActivity;
import com.huisu.iyoox.adapter.StudentCollectAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.CollectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseCollectModel;
import com.huisu.iyoox.entity.base.BaseGuoxueYishuVodModel;
import com.huisu.iyoox.entity.base.BaseMoreCollectModel;
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
 * @author: dl
 * @function: 学习记录
 * @date: 18/6/28
 */
public class StudentLearningHistoryActivity extends BaseActivity {


    private ArrayList<CollectModel> today = new ArrayList<>();
    private ArrayList<CollectModel> yesterday = new ArrayList<>();
    private ArrayList<CollectModel> earlier = new ArrayList<>();
    private View emptyView;
    private User user;
    private Loading loading;
    private RecyclerView mJTRecyclerView;
    private StudentCollectAdapter mJTAdapter;

    private RecyclerView mZTRecyclerView;
    private StudentCollectAdapter mZTAdapter;

    private RecyclerView mGZRecyclerView;
    private StudentCollectAdapter mGZAdapter;
    private TextView jtView;
    private TextView ztView;
    private TextView gzView;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();
        emptyView = findViewById(R.id.student_learning_history_empty_view);

        jtView = findViewById(R.id.jt_text_view);
        ztView = findViewById(R.id.zt_text_view);
        gzView = findViewById(R.id.gz_text_view);

        mJTRecyclerView = findViewById(R.id.jt_student_learning_history_list_view);
        setRecyclerView(mJTRecyclerView);
        mJTAdapter = new StudentCollectAdapter(context, today);
        mJTRecyclerView.setAdapter(mJTAdapter);

        mZTRecyclerView = findViewById(R.id.zt_student_learning_history_list_view);
        setRecyclerView(mZTRecyclerView);
        mZTAdapter = new StudentCollectAdapter(context, yesterday);
        mZTRecyclerView.setAdapter(mZTAdapter);

        mGZRecyclerView = findViewById(R.id.gz_student_learning_history_list_view);
        setRecyclerView(mGZRecyclerView);
        mGZAdapter = new StudentCollectAdapter(context, earlier);
        mGZRecyclerView.setAdapter(mGZAdapter);
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        //解决数据加载不完的问题
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        recyclerView.setFocusable(false);
    }

    @Override
    protected void initData() {
        setTitle("学习记录");
        postStudentData();
    }

    private void postStudentData() {
        if (user == null) return;
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.get_xuexi_record(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                today.clear();
                yesterday.clear();
                earlier.clear();
                BaseMoreCollectModel baseModel = (BaseMoreCollectModel) responseObj;
                if (baseModel.data != null) {
                    emptyView.setVisibility(View.GONE);
                    if (baseModel.data.getJt_list() != null && baseModel.data.getJt_list().size() > 0) {
                        today.addAll(baseModel.data.getJt_list());
                        jtView.setVisibility(View.VISIBLE);
                    } else {
                        jtView.setVisibility(View.GONE);
                    }

                    if (baseModel.data.getZt_list() != null && baseModel.data.getZt_list().size() > 0) {
                        yesterday.addAll(baseModel.data.getZt_list());
                        ztView.setVisibility(View.VISIBLE);
                    } else {
                        ztView.setVisibility(View.GONE);
                    }

                    if (baseModel.data.getGz_list() != null && baseModel.data.getGz_list().size() > 0) {
                        earlier.addAll(baseModel.data.getGz_list());
                        gzView.setVisibility(View.VISIBLE);
                    } else {
                        gzView.setVisibility(View.GONE);
                    }

                    if (today.size() == 0 && yesterday.size() == 0 && earlier.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    }

                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }

                mJTAdapter.notifyDataSetChanged();
                mZTAdapter.notifyDataSetChanged();
                mGZAdapter.notifyDataSetChanged();
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
        mJTAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                startVideoActivity(today.get(position));
            }
        });
        mZTAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                startVideoActivity(yesterday.get(position));
            }
        });
        mGZAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                startVideoActivity(earlier.get(position));
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_learning_history;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, StudentLearningHistoryActivity.class);
        context.startActivity(intent);
    }


    private void startVideoActivity(CollectModel model) {
        if (model.getShipin_type() == Constant.VIDEO_ordinary_TYPE) {
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
        } else if (model.getShipin_type() == Constant.VIDEO_XSC_TYPE) {
            postXscDetailsHttp(model.getVedio_id() + "", "小升初专题");
        } else {
            postVodHttp(model.getVedio_id() + "", model.getShipin_type() + "", model.getGrade_name());
        }
    }

    private void postXscDetailsHttp(String id, final String name) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.get_xiaoshengchu_vedio(user.getUserId(), id, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseGuoxueYishuVodModel baseModel = (BaseGuoxueYishuVodModel) responseObj;
                if (baseModel.data != null) {
                    Intent intent = new Intent(context, ALiYunGxYsVideoPlayActivity.class);
                    intent.putExtra("model", baseModel.data);
                    intent.putExtra("zhangjieName", name);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    private void postVodHttp(String id, String type, final String name) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.get_guoxue_yishu_vedio(user.getUserId(), id, type, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseGuoxueYishuVodModel baseModel = (BaseGuoxueYishuVodModel) responseObj;
                if (baseModel.data != null) {
                    Intent intent = new Intent(context, ALiYunGxYsVideoPlayActivity.class);
                    intent.putExtra("model", baseModel.data);
                    intent.putExtra("zhangjieName", name);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }
}
