package com.huisu.iyoox.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

public class TeacherRemarkResultActivity extends BaseActivity {

    private View backView;

    @Override
    protected void initView() {
        backView = findViewById(R.id.menu_back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setEvent() {
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

    public static void start(Context context) {
        Intent intent = new Intent(context, TeacherRemarkResultActivity.class);
        ((Activity) context).startActivityForResult(intent, 1);
    }

}
