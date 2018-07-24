package com.huisu.iyoox.fragment.student;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.fragment.base.BaseFragment;

/**
 * 学生缓存Fragment
 */
public class StudentCacheVideoListFragment extends BaseFragment {

    private View view;

    private String type = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_cache_list, container, false);
        initView();
        return view;
    }

    private void initView() {
    }

}
