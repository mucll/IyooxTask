package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.fragment.StudentMsgFragment;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.views.MyFragmentLayout_line;

import java.util.ArrayList;

/**
 * 学生消息通知界面
 */
public class StudentMsgActivity extends BaseActivity {

    private MyFragmentLayout_line mFragmentLayout;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void initView() {
        mFragmentLayout = findViewById(R.id.student_msg_content_layout);
        initFragment();
    }

    private void initFragment() {
        fragments.clear();
        fragments.add(getFragment(Constant.MSG_NOTIFICATION));
        fragments.add(getFragment(Constant.MSG_SYSTEM));
        mFragmentLayout.setScorllToNext(true);
        mFragmentLayout.setScorll(true);
        mFragmentLayout.setWhereTab(1);
        mFragmentLayout.setTabHeight(6, getResources().getColor(R.color.main_text_color), false);
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
        mFragmentLayout.setAdapter(fragments, R.layout.tablayout_student_msg, 0x213);
    }

    private StudentMsgFragment getFragment(int type) {
        StudentMsgFragment fragment = new StudentMsgFragment();
        Bundle b = new Bundle();
        b.putInt("type", type);
        fragment.setArguments(b);
        return fragment;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, StudentMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        setTitle("消息");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_msg;
    }

}
