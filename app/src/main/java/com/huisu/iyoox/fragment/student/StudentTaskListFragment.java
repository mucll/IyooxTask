package com.huisu.iyoox.fragment.student;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.activity.TaskResultActivity;
import com.huisu.iyoox.activity.student.TaskStudentHomeWorkActivity;
import com.huisu.iyoox.adapter.StudentTaskListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.TaskStudentListModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseTaskStudentListModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnLoadMoreListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.TaskEmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * @author: dl
 * @function: 学生作业列表fragment
 * @date: 18/6/28
 */
public class StudentTaskListFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener, AdapterView.OnItemClickListener {

    private View view;
    private StudentTaskListAdapter mAdapter;
    private ListView mListView;
    private String taskType;
    private boolean init;
    private int page = 1;
    private SwipeToLoadLayout swipeToLoadLayout;
    private User user;
    private TaskEmptyView emptyView;
    private ArrayList<TaskStudentListModel> listModels = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskType = bundle.getString("task_type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_student_task_list, container, false);
        }
        initView();
        setEvent();
        EventBus.getDefault().register(this);
        return view;
    }

    private void setEvent() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        emptyView.setOnEmptyClick(new TaskEmptyView.onEmptyClickListener() {
            @Override
            public void onEmptyClick(View v) {
                if (((MainActivity) getActivity()).myFragmentLayout != null) {
                    ((MainActivity) getActivity()).myFragmentLayout.setCurrenItem(0);
                }
            }
        });
    }

    @Override
    public void onShow() {
        if (!init) {
            postTaskListData();
            init = true;
        }
    }

    /**
     * 获取学生作业列表
     */
    private void postTaskListData() {
        RequestCenter.getStudentTaskListData(user.getUserId(), taskType, page + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                closeLoading();
                BaseTaskStudentListModel baseTaskStudentListModel = (BaseTaskStudentListModel) responseObj;
                if (baseTaskStudentListModel.data != null && baseTaskStudentListModel.data.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    if (page == 1) {
                        listModels.clear();
                    }
                    listModels.addAll(baseTaskStudentListModel.data);
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (page != 1) {
                        page--;
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                    TabToast.showMiddleToast(getContext(), "没有更多数据");
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                closeLoading();
                emptyView.setVisibility(View.VISIBLE);
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
        user = UserManager.getInstance().getUser();
        mListView = view.findViewById(R.id.swipe_target);
        emptyView = view.findViewById(R.id.task_empty_view);
        mAdapter = new StudentTaskListAdapter(getContext(), listModels, taskType);
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 上拉更多
     */
    @Override
    public void onLoadMore() {
        page++;
        postTaskListData();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        postTaskListData();
    }

    /**
     * listview点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskStudentListModel model = listModels.get(position);
        //未完成
        if (TaskStatus.UNFINISH.equals(taskType)) {
            Intent intent = new Intent(getContext(), TaskStudentHomeWorkActivity.class);
            intent.putExtra("type", Constant.STUDENT_HOME_WORK);
            intent.putExtra("work_id", model.getRowid() + "");
            intent.putExtra("title", model.getName());
            getContext().startActivity(intent);
        } else {
            //已完成
            Intent intent = new Intent(getContext(), TaskResultActivity.class);
            intent.putExtra("type", Constant.STUDENT_TASK_FINISHED);
            intent.putExtra("workId", model.getRowid() + "");
            intent.putExtra("title", model.getName());
            getContext().startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String type) {
        page = 1;
        postTaskListData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
