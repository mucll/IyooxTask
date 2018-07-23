package com.huisu.iyoox.activity.student;


import android.content.Context;
import android.content.Intent;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

/**
 * 学生收藏界面
 */
public class StudentCollectActivity extends BaseActivity {


    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        setTitle("我的收藏");
    }

    @Override
    protected void setEvent() {
        setBack();
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, StudentCollectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_collect;
    }

}
