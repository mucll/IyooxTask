package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.StudentLearningCardAdapter;
import com.huisu.iyoox.entity.LearningCardModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseLearningCardModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.Loading;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生 学习卡
 */
public class StudentLearningCardActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private User user;
    private ArrayList<LearningCardModel> models = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private StudentLearningCardAdapter mAdapter;
    private View emptyView;
    private SwipeRefreshLayout refreshView;

    @Override
    protected void initView() {
        emptyView = findViewById(R.id.student_learning_empty_view);

        refreshView = findViewById(R.id.student_learning_refresh_ll);
        refreshView.setColorSchemeResources(R.color.maincolor);//设置颜色
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                getResources().getDisplayMetrics());
        refreshView.setProgressViewOffset(false, 0, height);
        refreshView.setOnRefreshListener(this);//SwipeRefreshLayout监听

        mRecyclerView = findViewById(R.id.student_learning_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new StudentLearningCardAdapter(this, models);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        user = UserManager.getInstance().getUser();
        setTitle("学习卡");
        refreshView.post(new Runnable() {
            @Override
            public void run() {
                refreshView.setRefreshing(true);
                onRefresh();
            }
        });
    }

    private void postHttpData() {
        RequestCenter.getLearningCardList(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                closeRefresh();
                models.clear();
                BaseLearningCardModel baseModel = (BaseLearningCardModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    models.addAll(baseModel.data);
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                closeRefresh();
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_learning_card;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, StudentLearningCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postHttpData();
            }
        }, 500);
    }

    /**
     * 关闭刷新控件
     */
    private void closeRefresh() {
        if (refreshView != null && refreshView.isRefreshing()) {
            refreshView.setRefreshing(false);
        }
    }
}
