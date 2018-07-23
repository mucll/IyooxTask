package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.Intent;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

public class StudentLearningCardActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("学习卡");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_learning_card;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, StudentLearningCardActivity.class);
        context.startActivity(intent);
    }

}
