package com.huisu.iyoox.activity.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.fragment.teacher.TeacherLookTaskListFragment;
import com.huisu.iyoox.views.MyFragmentLayout_line;

import java.util.ArrayList;

/**
 * 老师查看班级作业列表Activity
 */
public class TeacherLookTaskListActivity extends BaseActivity {

    private MyFragmentLayout_line mFragmentLayout;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private ClassRoomModel classModel;

    @Override
    protected void initView() {
        classModel = (ClassRoomModel) getIntent().getSerializableExtra("model");
        mFragmentLayout = findViewById(R.id.teacher_look_task_list_layout);
        initFragment();
    }

    @Override
    protected void initData() {
        if (classModel != null) {
            setTitle(classModel.getName());
        }
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_look_task_list;
    }

    private void initFragment() {
        fragments.clear();
        fragments.add(getFragment(Constant.TASK_SEND_CODE));
        fragments.add(getFragment(Constant.TASK_UNSEND_CODE));
        mFragmentLayout.setScorllToNext(true);
        mFragmentLayout.setScorll(true);
        mFragmentLayout.setWhereTab(1);
        mFragmentLayout.setTabHeight(4, getResources().getColor(R.color.maincolor), false);
        mFragmentLayout.setOnChangeFragmentListener(new MyFragmentLayout_line.ChangeFragmentListener() {
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
        mFragmentLayout.setAdapter(fragments, R.layout.tablayout_teacher_look_task_list, 0x243);
    }

    private TeacherLookTaskListFragment getFragment(int type) {
        TeacherLookTaskListFragment fragment = new TeacherLookTaskListFragment();
        Bundle b = new Bundle();
        b.putInt("type", type);
        b.putInt("classId", classModel.getClassroom_id());
        fragment.setArguments(b);
        return fragment;
    }

    public static void start(Context context, ClassRoomModel model) {
        Intent intent = new Intent(context, TeacherLookTaskListActivity.class);
        intent.putExtra("model", model);
        context.startActivity(intent);
    }

}
