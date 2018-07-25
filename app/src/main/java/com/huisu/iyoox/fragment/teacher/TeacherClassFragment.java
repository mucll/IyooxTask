package com.huisu.iyoox.fragment.teacher;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherCreateClassActivity;
import com.huisu.iyoox.adapter.TeacherClassFragmentListAdapter;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseClassRoomModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;

import java.util.ArrayList;

/**
 * 老师端 首页 班级Fragment
 */
public class TeacherClassFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private TextView titleTv;
    private View addClassView;
    private RecyclerView recyclerView;
    private View emptyView;
    private TeacherClassFragmentListAdapter mAdapter;
    private ArrayList<ClassRoomModel> roomModels = new ArrayList<>();
    private User user;

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
        addClassView = view.findViewById(R.id.teacher_add_class_ic);
        recyclerView = view.findViewById(R.id.teacher_class_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherClassFragmentListAdapter(getContext(), roomModels);
        recyclerView.setAdapter(mAdapter);
        emptyView = view.findViewById(R.id.empty_view_layout);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void setEvent() {
        addClassView.setOnClickListener(this);
    }


    private void postClassRoomListHttp() {
        RequestCenter.teacherClassroomList(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
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

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_add_class_ic:
                TeacherCreateClassActivity.start(getContext());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
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
}
