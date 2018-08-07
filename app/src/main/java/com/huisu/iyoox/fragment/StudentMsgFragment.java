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
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;

/**
 * 学生消息界面
 */
public class StudentMsgFragment extends BaseFragment {

    private View view;
    private View emptyView;
    private int type;
    private User user;
    private boolean init;

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
        return view;
    }

    private void initData() {
        if (user == null) return;
        RequestCenter.getMsgList(user.getUserId(), type + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    public void onShow() {
        super.onShow();
        if (!init) {
            initData();
            init = true;
        }
    }

    private void initView() {
        RecyclerView mRecyclerView = view.findViewById(R.id.student_msg_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        emptyView = view.findViewById(R.id.student_msg_empty_view);
    }

}
