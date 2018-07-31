package com.huisu.iyoox.fragment.teacher;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.activity.teacher.TeacherClassRoomConfigActivity;
import com.huisu.iyoox.activity.teacher.TeacherClassRoomDetailsActivity;
import com.huisu.iyoox.activity.teacher.TeacherCreateClassActivity;
import com.huisu.iyoox.adapter.TeacherClassFragmentListAdapter;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseClassRoomModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.ResMorePopWindow;

import java.util.ArrayList;

/**
 * 老师端 首页 班级Fragment
 */
public class TeacherClassFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, MyOnItemClickListener {

    private View view;
    private TextView titleTv;
    private View addClassView;
    private RecyclerView recyclerView;
    private View emptyView;
    private TeacherClassFragmentListAdapter mAdapter;
    private ArrayList<ClassRoomModel> roomModels = new ArrayList<>();
    private User user;
    private SwipeRefreshLayout refreshView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_class, container, false);
        initTab();
        initView();
        initData();
        setEvent();
        postClassRoomListHttp();
        return view;
    }

    private void initData() {
        titleTv.setText("我的班级");
    }

    private void initView() {
        user = UserManager.getInstance().getUser();
        titleTv = view.findViewById(R.id.title_bar_tv);
        addClassView = view.findViewById(R.id.teacher_add_class_ll);
        refreshView = view.findViewById(R.id.teacher_class_fragment_refresh_ll);
        refreshView.setOnRefreshListener(this);//SwipeRefreshLayout监听
        refreshView.setColorSchemeResources(R.color.maincolor);//设置颜色
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                getResources().getDisplayMetrics());
        refreshView.setProgressViewOffset(false, 0, height);
        refreshView.setRefreshing(true);
        recyclerView = view.findViewById(R.id.teacher_class_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherClassFragmentListAdapter(getContext(), roomModels);
        recyclerView.setAdapter(mAdapter);
        emptyView = view.findViewById(R.id.empty_view_layout);
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置点击事件
     */
    private void setEvent() {
        addClassView.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    private void initPopupWindow() {
        //弹出dialog
        Dialog mPop = new ResMorePopWindow(getContext());
        mPop.show();
        WindowManager.LayoutParams params =
                mPop.getWindow().getAttributes();
        params.width = (int) (MainActivity.getScreenWidth(getActivity()));
        params.height = (int) (MainActivity.getScreenHeigth(getActivity()));
        mPop.getWindow().setAttributes(params);
    }


    @Override
    public void onItemClick(int positions, View view) {
        ClassRoomModel model = roomModels.get(positions);
        TeacherClassRoomDetailsActivity.start(getContext(), model);
    }

    /**
     * 班級列表
     */
    private void postClassRoomListHttp() {
        RequestCenter.teacherClassroomList(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                closeRefresh();
                BaseClassRoomModel baseClassRoomModel = (BaseClassRoomModel) responseObj;
                roomModels.clear();
                if (baseClassRoomModel.data != null && baseClassRoomModel.data.size() > 0) {
                    roomModels.addAll(baseClassRoomModel.data);
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                closeRefresh();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_add_class_ll:
                initPopupWindow();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //创建班级后 返回刷新数据
        if (requestCode == TeacherCreateClassActivity.START_CODE && resultCode == Activity.RESULT_OK) {
            if (refreshView != null && !refreshView.isRefreshing()) {
                refreshView.setRefreshing(true);
            }
            postClassRoomListHttp();
        }
        if (requestCode == TeacherClassRoomConfigActivity.START_CODE && resultCode == Activity.RESULT_OK) {
            if (refreshView != null && !refreshView.isRefreshing()) {
                refreshView.setRefreshing(true);
            }
            postClassRoomListHttp();
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

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        postClassRoomListHttp();
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
