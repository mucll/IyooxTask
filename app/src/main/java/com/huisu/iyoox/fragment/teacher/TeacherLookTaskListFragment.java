package com.huisu.iyoox.fragment.teacher;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherLookTaskDetailsActivity;
import com.huisu.iyoox.adapter.TeacherLookTaskListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.TaskTeacherListModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseTaskTeacherListModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnLoadMoreListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;
import com.huisu.iyoox.util.TabToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 老师看班级作业列表Fragment
 */
public class TeacherLookTaskListFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener, MyOnItemClickListener {

    private View view;
    private int type;
    private RecyclerView mRecyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private TeacherLookTaskListAdapter mAdapter;
    private int page = 1;
    private boolean init = false;
    private User user;
    private int classId;
    private ArrayList<TaskTeacherListModel> models = new ArrayList<>();
    private View emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", Constant.ERROR_CODE);
            classId = bundle.getInt("classId", Constant.ERROR_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_look_task_list, container, false);
        user = UserManager.getInstance().getUser();
        initView();
        setEvent();
        initData();
        return view;
    }

    private void postTaskListHttp() {
        RequestCenter.teacherTaskList(user.getUserId(), classId + "", type + "", page + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                closeLoading();
                if (page == 1) {
                    models.clear();
                }
                BaseTaskTeacherListModel baseTaskTeacherListModel = (BaseTaskTeacherListModel) responseObj;
                if (baseTaskTeacherListModel.data != null && baseTaskTeacherListModel.data.size() > 0) {
                    models.addAll(baseTaskTeacherListModel.data);
                    emptyView.setVisibility(View.GONE);
                } else {
                    if (page != 1) {
                        TabToast.showMiddleToast(getContext(), "暂无更多数据");
                        page--;
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                closeLoading();
            }
        });
    }

    private void closeLoading() {
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setLoadingMore(false);
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    private void initView() {
        emptyView = view.findViewById(R.id.task_list_teacher_empty_view);
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherLookTaskListAdapter(getContext(), models, type);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setEvent() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
    }

    private void initData() {
        if (Constant.TASK_SEND_CODE == type) {
            if (!init) {
                swipeToLoadLayout.setRefreshing(true);
                init = true;
            }
        }
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        page++;
        postTaskListHttp();
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        postTaskListHttp();
    }

    @Override
    public void onShow() {
        if (!init) {
            swipeToLoadLayout.setRefreshing(true);
            init = true;
        }
    }

    @Override
    public void onItemClick(int position, View view) {
        TaskTeacherListModel model = models.get(position);
        if (Constant.TASK_SEND_CODE == type) {
            TeacherLookTaskDetailsActivity.start(getContext(), model.getId());
        }
    }
}
