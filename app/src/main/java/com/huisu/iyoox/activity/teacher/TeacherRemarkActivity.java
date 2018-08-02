package com.huisu.iyoox.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

public class TeacherRemarkActivity extends BaseActivity implements View.OnClickListener {

    private EditText msgEt;
    private TextView submitTv;

    @Override
    protected void initView() {
        msgEt = findViewById(R.id.teacher_send_msg_et);
        submitTv = findViewById(R.id.submit_tv);
    }

    @Override
    protected void initData() {
        setTitle("教师点评");
    }

    @Override
    protected void setEvent() {
        setBack();
        submitTv.setOnClickListener(this);
        msgEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    submitTv.setEnabled(true);
                } else {
                    submitTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_remark;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_tv:
                TeacherRemarkResultActivity.start(this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TeacherRemarkActivity.class);
        ((Activity) context).startActivityForResult(intent, 1);
    }
}
