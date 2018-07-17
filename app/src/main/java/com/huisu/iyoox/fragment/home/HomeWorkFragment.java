package com.huisu.iyoox.fragment.home;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.student.TaskStudentHomeWorkActivity;
import com.huisu.iyoox.adapter.HomeWorkFragmentAdapter;
import com.huisu.iyoox.entity.BookBean;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.fragment.StudentTaskListFragment;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.views.MyFragmentLayout_line;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 作业fragment
 * @date: 18/6/28
 */
public class HomeWorkFragment extends BaseFragment {
    private View view;
    private ListView mListView;
    private MyFragmentLayout_line myFragmentLayout;
    private ArrayList<BaseFragment> fragments = new ArrayList();
    private String taskType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            taskType = args.getString("grade_id");
        }
    }

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

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }
}
