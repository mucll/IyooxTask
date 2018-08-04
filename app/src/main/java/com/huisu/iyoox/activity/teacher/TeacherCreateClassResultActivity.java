package com.huisu.iyoox.activity.teacher;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.base.BaseSendTaskResultModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;

/**
 * 老师成功创建班级后结果界面
 */
public class TeacherCreateClassResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView classIdTv;
    private String classNo;
    private BaseSendTaskResultModel resultModel;
    private View classView;
    private View taskView;
    private Button taskBt;
    private Button inviteBt;

    @Override
    protected void initView() {
        classIdTv = findViewById(R.id.teacher_create_class_class_id_tv);
        classView = findViewById(R.id.teacher_create_class_success_ll);
        taskView = findViewById(R.id.teacher_send_task_success_rl);
        inviteBt = findViewById(R.id.teacher_invite_bt);
        taskBt = findViewById(R.id.task_submit_home_bt);
    }

    @Override
    protected void initData() {
        int type = getIntent().getIntExtra("type", Constant.ERROR_CODE);
        if (Constant.CREATE_CLASS_RESULT == type) {
            classNo = getIntent().getStringExtra("classNo");
            if (!TextUtils.isEmpty(classNo)) {
                classIdTv.setText(classNo);
            }
            classView.setVisibility(View.VISIBLE);
        } else if (Constant.CREATE_TASK_RESULT == type) {
            resultModel = (BaseSendTaskResultModel) getIntent().getSerializableExtra("model");
            taskView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_submit_home_bt:
                if (resultModel.data != null && resultModel.data.size() > 0) {
                    for (int i = 0; i < resultModel.data.size(); i++) {
                        postRemindHomeHttp(resultModel.data.get(i));
                    }
                }
                taskBt.setEnabled(false);
                break;
            case R.id.teacher_invite_bt:
                finish();
                break;
            default:
                break;
        }
    }

    private void postRemindHomeHttp(int taskId) {
        RequestCenter.notifyParents(taskId + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    protected void setEvent() {
        setBack();
        taskBt.setOnClickListener(this);
        inviteBt.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_create_class_result;
    }

    public static void start(Context context, int type, String classNo, BaseSendTaskResultModel model) {
        Intent intent = new Intent(context, TeacherCreateClassResultActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("classNo", classNo);
        intent.putExtra("model", model);
        context.startActivity(intent);
    }

}
