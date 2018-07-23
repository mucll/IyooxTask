package com.huisu.iyoox.activity.student;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.fragment.student.StudentCacheVideoListFragment;

import java.util.ArrayList;

/**
 * 学生缓存视频界面
 */
public class StudentCacheVideoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRadioGroup;
    private FragmentManager manager;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();


    @Override
    protected void initView() {
        mRadioGroup = findViewById(R.id.radio_group_title);
        manager = getSupportFragmentManager();
    }

    @Override
    protected void initData() {
        fragments.clear();
        fragments.add(getFragment(TaskStatus.FINISH));
        fragments.add(getFragment(TaskStatus.UNFINISH));
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    protected void setEvent() {
        setBack();
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_cache;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int indexOf = group.indexOfChild(group.findViewById(checkedId));
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.student_cache_content, fragments.get(indexOf));
        ft.commit();
    }


    private StudentCacheVideoListFragment getFragment(String type) {
        StudentCacheVideoListFragment fragment = new StudentCacheVideoListFragment();
        Bundle b = new Bundle();
        b.putString("type", type);
        fragment.setArguments(b);
        return fragment;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, StudentCacheVideoActivity.class);
        context.startActivity(intent);
    }
}
