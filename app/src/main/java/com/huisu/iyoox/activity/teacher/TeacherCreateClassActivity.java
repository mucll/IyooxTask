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
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseClassRoomModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.Loading;

public class TeacherCreateClassActivity extends BaseActivity implements View.OnClickListener {


    private TextView gradeNameTv;
    private EditText classNameEt;
    private User user;
    private TextView tabSubmitTv;
    private Loading loading;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();
        tabSubmitTv = findViewById(R.id.tv_submit);
        tabSubmitTv.setText("完成");
        tabSubmitTv.setVisibility(View.VISIBLE);
        tabSubmitTv.setEnabled(false);
        gradeNameTv = findViewById(R.id.teacher_grade_name_tv);
        gradeNameTv.setText(user.getGradeName());
        classNameEt = findViewById(R.id.teacher_create_class_name);
    }

    @Override
    protected void initData() {
        setTitle("创建班级");
    }

    @Override
    protected void setEvent() {
        setBack();
        tabSubmitTv.setOnClickListener(this);
        classNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    tabSubmitTv.setEnabled(true);
                } else {
                    tabSubmitTv.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_teacher_create_class;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                postCreateClassRoomHttp();
                break;
        }
    }

    private void postCreateClassRoomHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.teacherCreateClassroom(user.getUserId(), user.getGrade() + "", classNameEt.getText().toString().trim(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseClassRoomModel baseClassRoomModel = (BaseClassRoomModel) responseObj;
                if (baseClassRoomModel.data != null) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TeacherCreateClassActivity.class);
        ((Activity) context).startActivityForResult(intent, 1);
    }
}
