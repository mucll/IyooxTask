package com.huisu.iyoox.activity.register;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.entity.BookEditionModel;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.base.BaseGradeListModel;
import com.huisu.iyoox.entity.base.BaseUser;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.exception.OkHttpException;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.SelectGradeDialog;

import java.util.List;

/**
 * 老师注册设置授课信息
 */
public class RegisterTeacherSubjectActivity extends BaseActivity implements View.OnClickListener {

    private EditText nameEt;
    private TextView gradeTv;
    private TextView subjectTv;
    private TextView versionTv;
    private View gradeView;
    private View subjectView;
    private View versionView;
    private List<GradeListModel> gradeModels;
    private SelectGradeDialog gradeDialog;
    private int gradeCode = 0;

    private int subjectId = 0;
    private SubjectModel subjectModel;
    private int versionId = 0;
    private int versionDetailId = 0;
    private BookEditionModel versionModel;
    private Button stubmit;
    private String userId;
    private Loading loading;

    @Override
    protected void initView() {
        nameEt = findViewById(R.id.register_name_edit_text);
        gradeView = findViewById(R.id.register_grade_ll);
        gradeTv = findViewById(R.id.register_grade_tv);
        subjectView = findViewById(R.id.register_select_subject_ll);
        subjectTv = findViewById(R.id.register_select_subject_tv);
        versionView = findViewById(R.id.register_version_ll);
        versionTv = findViewById(R.id.register_version_tv);
        stubmit = findViewById(R.id.register_teacher_submit_bt);
    }

    @Override
    protected void initData() {
        userId = getIntent().getStringExtra("userId");
        postGrade();
    }

    @Override
    protected void setEvent() {
        setBack();
        gradeView.setOnClickListener(this);
        subjectView.setOnClickListener(this);
        versionView.setOnClickListener(this);
        stubmit.setOnClickListener(this);
        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setViewEnable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register_teacher_subject;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_grade_ll:
                if (gradeModels == null) {
                    postGrade();
                    return;
                }
                gradeDialog = new SelectGradeDialog(this, gradeModels, gradeCode) {
                    @Override
                    public void getGradeType(GradeListModel gradeModel, int gradeCode) {
                        gradeTv.setText(gradeModel.getName());
                        RegisterTeacherSubjectActivity.this.gradeCode = gradeCode + 1;
                        setSubjectEmpty();
                    }
                };
                break;
            case R.id.register_select_subject_ll:
                if (gradeCode == 0) {
                    return;
                }
                TeacherSelectSubjectActivity.start(this, gradeCode, subjectId);
                break;
            case R.id.register_version_ll:
                if (subjectModel != null) {
                    TeacherSelectSubjectVersionActivity.start(this, gradeCode,
                            subjectModel.getKemu_id(), versionId, versionDetailId);
                }
                break;
            case R.id.register_teacher_submit_bt:
                postSetUserInfo();
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
        RequestCenter.setTeacherUserInfo(userId, nameEt.getText().toString(), gradeCode + "",
                subjectModel.getKemu_id() + "", versionModel.getJiaocai_id() + ""
                , versionModel.getGrade_detail_id() + "",
                new DisposeDataListener() {
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

    /**
     * 修改按钮状态
     */
    private void setViewEnable() {
        if (!TextUtils.isEmpty(nameEt.getText().toString().trim())
                && gradeCode != 0 && subjectModel != null && versionModel != null) {
            stubmit.setEnabled(true);
        } else {
            stubmit.setEnabled(false);
        }
    }

    /**
     * 请求年级列表
     */
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            subjectModel = (SubjectModel) data.getSerializableExtra("model");
            subjectId = data.getIntExtra("subjectId", 0);
            subjectTv.setText(subjectModel.getName());
            setVersionEmpty();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            versionModel = (BookEditionModel) data.getSerializableExtra("versionModel");
            versionId = data.getIntExtra("versionId", 0);
            versionDetailId = data.getIntExtra("versionDetailId", 0);
            versionTv.setText(versionModel.getVersionName());
            setViewEnable();
        }
    }

    private void setSubjectEmpty() {
        subjectModel = null;
        subjectId = 0;
        subjectTv.setText("");
        versionModel = null;
        versionId = 0;
        versionTv.setText("");
    }

    private void setVersionEmpty() {
        versionModel = null;
        versionId = 0;
        versionTv.setText("");
    }
}
