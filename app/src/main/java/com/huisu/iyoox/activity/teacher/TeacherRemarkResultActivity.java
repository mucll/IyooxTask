package com.huisu.iyoox.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class TeacherRemarkResultActivity extends BaseActivity implements View.OnClickListener {

    private View backView;
    private Button button;
    private int studentId;
    private int workId;
    private String msg;

    @Override
    protected void initView() {
        backView = findViewById(R.id.menu_back);
        button = findViewById(R.id.remark_student_bt);
    }

    @Override
    protected void initData() {
        studentId = getIntent().getIntExtra("studentId", Constant.ERROR_CODE);
        workId = getIntent().getIntExtra("workId", Constant.ERROR_CODE);
        msg = getIntent().getStringExtra("msg");
    }

    @Override
    protected void setEvent() {
        button.setOnClickListener(this);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_remark_result;
    }

    public static void start(Context context, int studentId, int workId, String msg) {
        Intent intent = new Intent(context, TeacherRemarkResultActivity.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra("workId", workId);
        intent.putExtra("msg", msg);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    @Override
    public void onClick(View v) {
        postRemindHomeHttp();
    }

    private void postRemindHomeHttp() {
        if (TextUtils.isEmpty(msg)) return;
        if (studentId == Constant.ERROR_CODE) return;
        List<Integer> ints = new ArrayList<>();
        ints.add(studentId);
        List<Integer> workIds = new ArrayList<>();
        workIds.add(workId);
        RequestCenter.notifyParents(JsonUtils.jsonFromObject(ints), msg,
                Constant.MSG_NOTIFICATION + "",
                Constant.NOTIFICATION_DP + "",
                JsonUtils.jsonFromObject(workIds), new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });
    }
}
