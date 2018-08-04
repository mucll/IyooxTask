package com.huisu.iyoox.fragment.home;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.fragment.student.StudentTaskListFragment;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.views.MyFragmentLayout_line;

import java.util.ArrayList;

/**
 * tab作业界面
 */
public class HomeWorkFragment extends BaseFragment {
    private View view;
    private MyFragmentLayout_line myFragmentLayout;
    private ArrayList<BaseFragment> fragments = new ArrayList();
    private boolean init;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_work, container, false);
        }
        initTab();
        initView();
        initFragment();
        setEvent();
        return view;
    }

    private void setEvent() {
    }

    private void initView() {
        myFragmentLayout = view.findViewById(R.id.myFragmentLayout);
    }

    private void initFragment() {
        fragments.clear();
        fragments.add(getFragment(TaskStatus.UNFINISH));
        fragments.add(getFragment(TaskStatus.FINISH));
        fragments.add(getFragment(TaskStatus.YUQI));
        myFragmentLayout.setScorllToNext(true);
        myFragmentLayout.setScorll(true);
        myFragmentLayout.setWhereTab(1);
        myFragmentLayout.setTabHeight(6, getResources().getColor(R.color.main_text_color), false);
        myFragmentLayout
                .setOnChangeFragmentListener(new MyFragmentLayout_line.ChangeFragmentListener() {
                    @Override
                    public void change(int lastPosition, int positon,
                                       View lastTabView, View currentTabView) {
                        ((TextView) lastTabView.findViewById(R.id.tab_text))
                                .setTextColor(getResources().getColor(R.color.color333));
                        ((TextView) currentTabView.findViewById(R.id.tab_text))
                                .setTextColor(getResources().getColor(R.color.main_text_color));
                        fragments.get(positon).onShow();
                    }
                });
        myFragmentLayout.setAdapter(fragments, R.layout.tablayout_student_task, 0x203);
    }

    private StudentTaskListFragment getFragment(String taskType) {
        StudentTaskListFragment fragment = new StudentTaskListFragment();
        Bundle b = new Bundle();
        b.putString("task_type", taskType);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onShow() {
        if (!init) {
            fragments.get(myFragmentLayout.getCurrentPosition()).onShow();
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
