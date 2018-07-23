package com.huisu.iyoox.activity.student;


import android.content.Context;
import android.content.Intent;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
/**
 * @author: dl
 * @function: 学习提醒
 * @date: 18/6/28
 */
public class StudentLearningHistoryActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("学习记录");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_learning_history;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, StudentLearningHistoryActivity.class);
        context.startActivity(intent);
    }

}
