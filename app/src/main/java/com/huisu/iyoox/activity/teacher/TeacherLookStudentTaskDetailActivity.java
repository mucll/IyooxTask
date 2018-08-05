package com.huisu.iyoox.activity.teacher;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.TaskTeacherLookStudentModel;
import com.huisu.iyoox.entity.base.BaseTaskTeacherLookTimuModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.ExercisesNumberView;
import com.huisu.iyoox.views.Loading;

/**
 * 老师查看学生作业详情
 */
public class TeacherLookStudentTaskDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView submitTv;
    private TaskTeacherLookStudentModel studentModel;
    private int workId;
    private Loading loading;
    private ExercisesNumberView numberView;

    @Override
    protected void initView() {
        numberView = findViewById(R.id.detail_exercises_number_view);
        submitTv = findViewById(R.id.submit_tv);
    }

    @Override
    protected void initData() {
        setTitle("学生作业详情");
        workId = getIntent().getIntExtra("workId", Constant.ERROR_CODE);
        studentModel = (TaskTeacherLookStudentModel) getIntent().getSerializableExtra("studentModel");
        if (workId != Constant.ERROR_CODE && studentModel != null) {
            setTitle(studentModel.getStudent_name() + "的作业详情");
            postStudentDetailHttp();
        }
    }

    private void postStudentDetailHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.teacherLookStudentDetails(workId + "", studentModel.getStudent_id() + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseTaskTeacherLookTimuModel baseModel = (BaseTaskTeacherLookTimuModel) responseObj;
                if (baseModel.code == Constant.POST_SUCCESS_CODE && baseModel.data != null && baseModel.data.size() > 0) {
                    numberView.setData(baseModel.data, Constant.NUMBER_ANSWER);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
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

    public static void start(Context context, int workId, TaskTeacherLookStudentModel studentModel) {
        Intent intent = new Intent(context, TeacherLookStudentTaskDetailActivity.class);
        intent.putExtra("workId", workId);
        intent.putExtra("studentModel", studentModel);
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
                TeacherRemarkActivity.start(this, workId, studentModel.getStudent_id());
                break;
            default:
                break;
        }
    }
}
