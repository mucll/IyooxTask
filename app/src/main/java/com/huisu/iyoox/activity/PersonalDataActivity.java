package com.huisu.iyoox.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.register.RegisterTeacherSubjectActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseGradeListModel;
import com.huisu.iyoox.entity.base.BaseUser;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.ChangeHeaderImgDialog;
import com.huisu.iyoox.views.EditTextDialog;
import com.huisu.iyoox.views.HeadView;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.SelectGradeDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dl
 * @function: 个人资料
 * @date: 18/6/28
 */
public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {

    private View headLayout, accountLayout, userNameLayout, sexLayout, gradeLayout, classLayout;
    private TextView accountTv, userNameTv, sexTv, gradeTv, classTv;
    private HeadView headView;
    private User user;
    private ChangeHeaderImgDialog mHeaderImgDialog;
    private ImageButton backBtn;
    private boolean setResult = false;
    private Loading loading;
    private View studentContentView;
    private View teacherContentView;
    private View teacherUserNameView;
    private TextView teacherUserNameTv;
    private View teacherGradeView;
    private TextView teacherGradeTv;
    private List<GradeListModel> gradeModels;
    private SelectGradeDialog gradeDialog;
    private EditTextDialog nameDialog;
    private boolean init = false;

    @Override
    protected void initView() {
        backBtn = findViewById(R.id.menu_back);
        headLayout = findViewById(R.id.head_layout);
        accountLayout = findViewById(R.id.person_account_ll);
        userNameLayout = findViewById(R.id.person_user_name_ll);
        sexLayout = findViewById(R.id.person_sex_ll);
        gradeLayout = findViewById(R.id.person_grade_ll);
        classLayout = findViewById(R.id.person_class_ll);

        headView = findViewById(R.id.person_iv);
        accountTv = findViewById(R.id.person_account_tv);
        userNameTv = findViewById(R.id.person_user_name_tv);
        sexTv = findViewById(R.id.person_sex_tv);
        gradeTv = findViewById(R.id.person_grade_tv);
        classTv = findViewById(R.id.person_class_tv);

        studentContentView = findViewById(R.id.student_content_ll);
        teacherContentView = findViewById(R.id.teacher_content_ll);

        teacherUserNameView = findViewById(R.id.teacher_person_user_name_ll);
        teacherUserNameTv = findViewById(R.id.teacher_person_user_name_tv);
        teacherGradeView = findViewById(R.id.teacher_person_grade_ll);
        teacherGradeTv = findViewById(R.id.teacher_person_grade_tv);
    }

    @Override
    protected void initData() {
        setTitle("编辑资料");
        setPersonData();
    }

    private void setPersonData() {
        user = UserManager.getInstance().getUser();
        postUserData(user);
    }

    private void postUserData(User user) {
        headView.setHead(user.getUserId(), user.getName(), TextUtils.isEmpty(user.getAvatar()) ? "" : user.getAvatar(), user.getType());
        accountTv.setText(!TextUtils.isEmpty(user.getPhone()) ? user.getPhone() : "");

        if (Constant.TEACHER_TYPE == user.getType()) {
            studentContentView.setVisibility(View.GONE);
            teacherContentView.setVisibility(View.VISIBLE);
            teacherUserNameTv.setText(!TextUtils.isEmpty(user.getName()) ? user.getName() : "");
            teacherGradeTv.setText(user.getGradeName());
            postGrade();
        } else if (Constant.STUDENT_TYPE == user.getType()) {
            studentContentView.setVisibility(View.VISIBLE);
            teacherContentView.setVisibility(View.GONE);
            userNameTv.setText(!TextUtils.isEmpty(user.getName()) ? user.getName() : "");
            gradeTv.setText(user.getGradeName());
            sexTv.setText(user.getSex() == 0 ? "女" : "男");
            classTv.setText(TextUtils.isEmpty(user.getClassroom_name()) ? "" : user.getClassroom_name());
        }
    }

    @Override
    protected void setEvent() {
        backBtn.setOnClickListener(this);
        headLayout.setOnClickListener(this);
        teacherUserNameView.setOnClickListener(this);
        teacherGradeView.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_back:
                if (setResult) {
                    setResult(RESULT_OK);
                }
                finish();
                break;
            case R.id.head_layout:
                configHeard();
                break;
            case R.id.teacher_person_user_name_ll:
                nameDialog = new EditTextDialog(context, "姓名", "请输入要修改姓名",
                        EditTextDialog.TYPE_TEXT) {
                    @Override
                    public void result(final String result) {
                        postConfigNameHttp(result);
                    }
                };
                nameDialog.isChina(true);
                nameDialog.show();
                break;
            case R.id.teacher_person_grade_ll:
                if (init) {
                    return;
                }
                if (gradeModels == null) {
                    postGrade();
                    return;
                }
                gradeDialog = new SelectGradeDialog(this, gradeModels, user.getGrade()) {
                    @Override
                    public void getGradeType(GradeListModel gradeModel, int gradeCode) {
                        postConfigGradeHttp(gradeCode);
                    }
                };
                break;
            default:
                break;
        }
    }

    /**
     * 修改名字
     */
    private void postConfigNameHttp(final String name) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.modifyTeacherInfo(user.getUserId(), name, "", "", "", ""
                , new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        loading.dismiss();
                        user.setName(name);
                        user.updateAll();
                        UserManager.getInstance().setUser(user);
                        teacherUserNameTv.setText(name);
                        TabToast.showMiddleToast(context, "姓名修改成功");
                        setResult = true;
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        loading.dismiss();
                    }
                });
    }

    /**
     * 修改年级
     */
    private void postConfigGradeHttp(final int grade) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.modifyTeacherInfo(user.getUserId(), "",
                user.getXueke_id() + "", "", "", grade + ""
                , new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        loading.dismiss();
                        user.setGrade(grade);
                        user.updateAll();
                        UserManager.getInstance().setUser(user);
                        teacherGradeTv.setText(user.getGradeName());
                        TabToast.showMiddleToast(context, "年级修改成功");
                        setResult = true;
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        loading.dismiss();
                    }
                });
    }

    /**
     * 选择图片
     */
    private void configHeard() {
        mHeaderImgDialog = new ChangeHeaderImgDialog(this,
                headView.head) {
            @Override
            public void getResult(final File file) {
                super.getResult(file);
                setHead(file);
            }
        };
    }

    /**
     * 上传头像
     */
    private void setHead(final File head) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.updateAvatar(user.getUserId(), head, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if (code == Constant.POST_SUCCESS_CODE) {
                        TabToast.showMiddleToast(context, "头像修改成功");
                        String path = head.getAbsolutePath();
                        headView.setHead(user.getUserId() + "", user.getName(), path, user.getType());
                        String data = jsonObject.getString("data");
                        user.setAvatar(data);
                        user.updateAll();
                        UserManager.getInstance().setUser(user);
                        setResult = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (setResult) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHeaderImgDialog.onActivityResult(requestCode, resultCode, data);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PersonalDataActivity.class);
        ((Activity) context).startActivityForResult(intent, 100);
    }

    /**
     * 请求年级列表
     */
    private void postGrade() {
        init = true;
        RequestCenter.getGrades(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                init = false;
                BaseGradeListModel baseGradeModel = (BaseGradeListModel) responseObj;
                if (baseGradeModel.data != null && baseGradeModel.data.size() > 0) {
                    setGradeModels(baseGradeModel.data);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                init = false;
            }
        });
    }


    public void setGradeModels(List<GradeListModel> gradeModels) {
        if (gradeModels != null) {
            this.gradeModels = gradeModels;
        }
    }

}
