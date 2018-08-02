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
import android.widget.ScrollView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherLookStudentTaskDetailActivity;
import com.huisu.iyoox.adapter.TeacherLookTaskDetailAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.views.ExercisesNumberView;

/**
 * 老师查看班级作业详情Fragment
 */
public class TeacherLookTaskDetailFragment extends BaseFragment implements MyOnItemClickListener {

    private View view;
    private RecyclerView mRecyclerView;
    private TeacherLookTaskDetailAdapter mAdapter;
    private int type;
    private View finishedView;
    private View unfinishView;
    private ScrollView scrollView;

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
        view = inflater.inflate(R.layout.fragment_teacher_look_task_detail, container, false);
        finishedView = view.findViewById(R.id.teacher_look_task_student_finished_ll);
        unfinishView = view.findViewById(R.id.teacher_look_task_student_unfinish_ll);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        scrollView = view.findViewById(R.id.teacher_look_number_sl);
        if (Constant.TASK_EXERCISES_NUMBER == type) {
            initNumberView();
        } else {
            initView();
            setEvent();
        }
        return view;
    }

    private void initView() {
        scrollView.setVisibility(View.GONE);
        if (Constant.TASK_STUDENT_FINISHED == type) {
            finishedView.setVisibility(View.VISIBLE);
            unfinishView.setVisibility(View.GONE);
        } else if (Constant.TASK_STUDENT_UNFINISH == type) {
            finishedView.setVisibility(View.GONE);
            unfinishView.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherLookTaskDetailAdapter(getContext(), type);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initNumberView() {
        scrollView.setVisibility(View.VISIBLE);
        finishedView.setVisibility(View.GONE);
        unfinishView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }


    private void setEvent() {
        if (Constant.TASK_STUDENT_FINISHED == type) {
            mAdapter.setOnItemClickListener(this);
        }

    }

    @Override
    public void onItemClick(int position, View view) {
        TeacherLookStudentTaskDetailActivity.start(getContext());
    }
}
