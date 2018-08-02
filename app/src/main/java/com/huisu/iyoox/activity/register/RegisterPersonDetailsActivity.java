package com.huisu.iyoox.activity.register;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.GradeModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseGradeListModel;
import com.huisu.iyoox.entity.base.BaseGradeModel;
import com.huisu.iyoox.entity.base.BaseUser;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.exception.OkHttpException;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.SoftKeyBoardListener;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.SelectGradeDialog;
import com.huisu.iyoox.views.SelectSexDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册学生设置个人信息
 */
public class RegisterPersonDetailsActivity extends BaseActivity implements View.OnClickListener {

    private EditText nameEditText, genderEditText, gradeEditText;
    private SelectSexDialog sexDialog;
    private String sexCode = SelectSexDialog.MAN_CODE;
    private int gradeCode = 0;
    private SelectGradeDialog gradeDialog;
    private Button nextBt;
    private String userId;
    private Loading loading;
    private View contentView;
    private List<GradeListModel> gradeModels;

    @Override
    protected void initView() {
        StatusBarUtil.transparencyBar(this);
        nextBt = (Button) findViewById(R.id.register_person_details_next_bt);
        //名字
        nameEditText = (EditText) findViewById(R.id.register_name_edit_text);
        //性别
        genderEditText = (EditText) findViewById(R.id.register_gender_text);
        genderEditText.setFocusable(false);
        //年级
        gradeEditText = (EditText) findViewById(R.id.register_select_grade_text);
        gradeEditText.setFocusable(false);
        contentView = findViewById(R.id.register_person_details_content);
    }

    @Override
    protected void initData() {
        userId = getIntent().getStringExtra("userId");
        postGrade();
    }

    private void postGrade() {
        RequestCenter.getGrades(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseGradeListModel baseGradeModel = (BaseGradeListModel) responseObj;
                if (baseGradeModel.data != null && baseGradeModel.data.size() > 0) {
                    setGradeModels(baseGradeModel.data);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        });
    }

    public void setGradeModels(List<GradeListModel> gradeModels) {
        if (gradeModels != null) {
            this.gradeModels = gradeModels;
        }
    }

    @Override
    protected void setEvent() {
        setTransBack();
        nextBt.setOnClickListener(this);
        genderEditText.setOnClickListener(this);
        gradeEditText.setOnClickListener(this);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setViewEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                contentView.setPadding(0, 0, 0, 0);
            }

            @Override
            public void keyBoardHide(int height) {
                int v = StringUtils.dp2px(context, 105);
                contentView.setPadding(0, v, 0, 0);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register_person_details;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_person_details_next_bt:
                postSetUserInfo();
                break;

            case R.id.register_gender_text:
                sexDialog = new SelectSexDialog(RegisterPersonDetailsActivity.this, sexCode) {
                    @Override
                    public void getSexString(String sexString, String sexCode) {
                        genderEditText.setText(sexString);
                        RegisterPersonDetailsActivity.this.sexCode = sexCode;
                        setViewEnable();
                    }
                };
                break;
            case R.id.register_select_grade_text:
                if (gradeModels == null) {
                    postGrade();
                    return;
                }
                gradeDialog = new SelectGradeDialog(RegisterPersonDetailsActivity.this, gradeModels, gradeCode) {
                    @Override
                    public void getGradeType(GradeListModel gradeModel, int gradeCode) {
                        gradeEditText.setText(gradeModel.getName());
                        RegisterPersonDetailsActivity.this.gradeCode = gradeCode;
                        setViewEnable();
                    }
                };
                break;
            default:
                break;
        }
    }

    /**
     * 个人信息
     */
    private void postSetUserInfo() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.setUserInfo(userId, nameEditText.getText().toString(), sexCode, gradeCode + 1 + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseUser baseUser = (BaseUser) responseObj;
                if (baseUser.data != null) {
                    UserManager.getInstance().setUser(baseUser.data);
                    baseUser.data.save();
                    MainActivity.start(context);
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

    private void setViewEnable() {
        if (!TextUtils.isEmpty(nameEditText.getText().toString()) &&
                !TextUtils.isEmpty(genderEditText.getText().toString()) &&
                !TextUtils.isEmpty(gradeEditText.getText().toString())) {
            nextBt.setEnabled(true);
        } else {
            nextBt.setEnabled(false);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterIdentityActivity.class);
        context.startActivity(intent);
    }

}
