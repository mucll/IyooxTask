package com.huisu.iyoox.activity.register;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.entity.RegisterResultModel;
import com.huisu.iyoox.entity.base.BaseRegisterResultModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/10
 */
public class RegisterIdentityActivity extends BaseActivity implements View.OnClickListener {

    private Button nextBt;
    private final int START_CODE = 0x103;
    private String phone;
    private String password;
    private Loading loading;
    private CheckBox teacherCheck;
    private CheckBox studentCheck;
    private View selectStudent;
    private View selectTeacher;

    @Override
    protected void initView() {
        StatusBarUtil.transparencyBar(this);
        nextBt = (Button) findViewById(R.id.register_identity_next_bt);
        teacherCheck = findViewById(R.id.identity_select_teahcer_cb);
        studentCheck = findViewById(R.id.identity_select_student_cb);
        studentCheck.setChecked(true);
        selectStudent = findViewById(R.id.select_student_layout);
        selectTeacher = findViewById(R.id.select_teacher_layout);
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");
    }

    @Override
    protected void setEvent() {
        setTransBack();
        nextBt.setOnClickListener(this);
        selectTeacher.setOnClickListener(this);
        selectStudent.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register_identity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_identity_next_bt:
                showHintDialog();
                break;
            case R.id.select_teacher_layout:
                setAllUnSelect();
                teacherCheck.setChecked(true);
                break;
            case R.id.select_student_layout:
                setAllUnSelect();
                studentCheck.setChecked(true);
                break;
            default:
                break;
        }
    }

    /**
     * 跳转个人信息设置界面
     */
    private void startDetailsActivity(String userId) {
        Intent intent = new Intent(context, RegisterPersonDetailsActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, START_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 提示身份注册后不可更改
     */
    private void showHintDialog() {
        String identityString = studentCheck.isChecked() ? "学生" : "老师";
        final String identity = studentCheck.isChecked() ? "1" : "2";
        DialogUtil.show("确认注册为" + identityString + "?", "注册后身份将不可更改!", "确认", "取消", this,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postRegister(identity);
                    }
                }, null);
    }

    private void setAllUnSelect() {
        teacherCheck.setChecked(false);
        studentCheck.setChecked(false);
    }

    /**
     * 请求注册接口
     *
     * @param type
     */
    private void postRegister(String type) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.register(phone, password, type, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseRegisterResultModel baseRegisterResultModel = (BaseRegisterResultModel) responseObj;
                if (baseRegisterResultModel.data != null) {
                    TabToast.showMiddleToast(context, getString(R.string.register_success_text));
                    //跳转个人信息设置界面
                    startDetailsActivity(baseRegisterResultModel.data.getUserId());
                    //关闭先前界面
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                TabToast.showMiddleToast(context, getString(R.string.register_error_text));
                loading.dismiss();
            }
        });
    }
}
