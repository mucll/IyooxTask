package com.huisu.iyoox.activity.teacher;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

/**
 * 老师查看学生作业详情
 */
public class TeacherLookStudentTaskDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView submitTv;

    @Override
    protected void initView() {
        submitTv = findViewById(R.id.submit_tv);
    }

    @Override
    protected void initData() {
        setTitle("学生作业详情");
    }

    @Override
    protected void setEvent() {
        setBack();
        submitTv.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_look_student_task_detail;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TeacherLookStudentTaskDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_tv:
                TeacherRemarkActivity.start(this);
                break;
            default:
                break;
        }
    }
}
