package com.huisu.iyoox.fragment.teacher;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.TeacherLookTaskListAdapter;
import com.huisu.iyoox.entity.TaskTeacherListModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.swipetoloadlayout.OnLoadMoreListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;

/**
 * 老师端-点评Fragment
 */
public class TeacherRemarkFragment extends BaseFragment implements MyOnItemClickListener, OnLoadMoreListener, OnRefreshListener {

    private View view;
    private TextView titleTv;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_correct, container, false);
        initTab();
        initView();
        setEvent();
        initData();
        return view;
    }

    private void initView() {
        titleTv = view.findViewById(R.id.title_bar_tv);
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        emptyView = view.findViewById(R.id.task_list_teacher_empty_view);
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
        titleTv.setText("点评");
    }

    private void postTaskListHttp() {
        closeLoading();
    }

    @Override
    public void onItemClick(int position, View view) {

    }

    private void closeLoading() {
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setLoadingMore(false);
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        postTaskListHttp();
    }

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

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }

}
