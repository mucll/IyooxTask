package com.huisu.iyoox.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.MsgAdapter;
import com.huisu.iyoox.adapter.TeacherLookTaskListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.NotificationMsgModel;
import com.huisu.iyoox.entity.TaskTeacherListModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseNotificationMsgModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnLoadMoreListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;
import com.huisu.iyoox.util.TabToast;

import java.util.ArrayList;

/**
 * 学生消息界面
 */
public class StudentMsgFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    private View view;
    private View emptyView;
    private RecyclerView mRecyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private boolean init = false;
    private int type;
    private User user;
    private int page = 1;
    private ArrayList<NotificationMsgModel> models = new ArrayList<>();
    private MsgAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", Constant.ERROR_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_msg, container, false);
        user = UserManager.getInstance().getUser();
        initView();
        setEvent();
        initData();
        return view;
    }

    private void initData() {
        if (Constant.MSG_NOTIFICATION == type) {
            if (!init) {
                swipeToLoadLayout.setRefreshing(true);
                init = true;
            }
        }
    }

    private void postMsgListDataHttp() {
        if (user == null) return;
        RequestCenter.getMsgList(user.getUserId(), type + "", page + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                closeLoading();
                if (page == 1) {
                    models.clear();
                }
                BaseNotificationMsgModel baseModel = (BaseNotificationMsgModel) responseObj;
                if (baseModel.data != null && baseModel.data.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    models.addAll(baseModel.data);
                } else {
                    if (page != 1) {
                        page--;
                        TabToast.showMiddleToast(getContext(), "暂无更多数据");
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


    @Override
    public void onShow() {
        super.onShow();
        if (!init) {
            swipeToLoadLayout.setRefreshing(true);
            init = true;
        }
    }

    private void initView() {
        emptyView = view.findViewById(R.id.student_msg_empty_view);
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new MsgAdapter(getContext(), models);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void setEvent() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
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
        postMsgListDataHttp();
    }

    @Override
    public void onRefresh() {
        page = 1;
        postMsgListDataHttp();
    }
}
