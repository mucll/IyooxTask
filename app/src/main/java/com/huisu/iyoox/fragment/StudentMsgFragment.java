package com.huisu.iyoox.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.fragment.base.BaseFragment;

/**
 * 学生消息界面
 */
public class StudentMsgFragment extends BaseFragment {

    private View view;
    private View emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_msg, container, false);
        initView();
        return view;
    }

    private void initView() {
        RecyclerView mRecyclerView = view.findViewById(R.id.student_msg_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        emptyView = view.findViewById(R.id.student_msg_empty_view);
    }

}
