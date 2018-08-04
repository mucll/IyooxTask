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
import com.huisu.iyoox.constant.Constant;
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
 * 注册选择身份
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
        nextBt = findViewById(R.id.register_identity_next_bt);
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

    private int identity = 0;

    private void showHintDialog() {
        String identityString = studentCheck.isChecked() ? "学生" : "老师";
        identity = studentCheck.isChecked() ? Constant.STUDENT_TYPE : Constant.TEACHER_TYPE;
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
    private void postRegister(final int type) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.register(phone, password, type + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseRegisterResultModel baseRegisterResultModel = (BaseRegisterResultModel) responseObj;
                if (baseRegisterResultModel.data != null) {
                    TabToast.showMiddleToast(context, getString(R.string.register_success_text));
                    if (Constant.STUDENT_TYPE == type) {
                        //跳转个人信息设置界面
                        startDetailsActivity(baseRegisterResultModel.data.getUserId());
                    } else {
                        startTeaDetailsActivity(baseRegisterResultModel.data.getUserId());
                    }

                    //关闭先前界面
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

    /**
     * 跳转个人信息设置界面
     */
    private void startDetailsActivity(String userId) {
        Intent intent = new Intent(context, RegisterPersonDetailsActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, START_CODE);
    }

    private void startTeaDetailsActivity(String userId) {
        Intent intent = new Intent(context, RegisterTeacherSubjectActivity.class);
        intent.putExtra("userId", userId);
        startActivityForResult(intent, START_CODE);
    }
}
