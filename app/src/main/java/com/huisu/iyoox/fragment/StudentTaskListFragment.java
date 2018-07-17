package com.huisu.iyoox.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.adapter.StudentTaskListAdapter;
import com.huisu.iyoox.fragment.base.BaseFragment;

/**
 * @author: dl
 * @function: 学生作业列表fragment
 * @date: 18/6/28
 */
public class StudentTaskListFragment extends BaseFragment {

    private View view;
    private StudentTaskListAdapter mAdapter;
    private ListView mListView;
    private String taskType;

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
        return view;
    }

    private void initView() {
        mListView = view.findViewById(R.id.student_task_list_view);
        mAdapter = new StudentTaskListAdapter(getContext(), taskType);
        mListView.setAdapter(mAdapter);
    }

}
