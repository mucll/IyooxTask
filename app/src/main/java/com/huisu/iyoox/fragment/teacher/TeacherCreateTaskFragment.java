package com.huisu.iyoox.fragment.teacher;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherCreateTaskActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.fragment.base.BaseFragment;

/**
 * 老师端 布置作业Fragment
 */
public class TeacherCreateTaskFragment extends BaseFragment {

    private View view;
    private TextView titleTv;
    private View createTaskView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_create_task, container, false);
        initTab();
        initView();
        initData();
        setEvent();
        return view;
    }

    private void initData() {
        titleTv.setText("布置作业");
    }

    private void initView() {
        titleTv = view.findViewById(R.id.title_bar_tv);
        createTaskView = view.findViewById(R.id.teacher_create_task_ll);
    }

    private void setEvent() {
        createTaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherCreateTaskActivity.start(getContext(), Constant.TASK_YIKE_TYPE);
            }
        });
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
