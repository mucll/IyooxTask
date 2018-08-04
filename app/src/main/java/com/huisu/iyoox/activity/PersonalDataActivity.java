package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseUser;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.views.ChangeHeaderImgDialog;
import com.huisu.iyoox.views.HeadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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

    @Override
    protected void initView() {
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
    }

    @Override
    protected void initData() {
        setTitle("编辑资料");
        postPersonData();
    }

    private void postPersonData() {
        user = UserManager.getInstance().getUser();
        RequestCenter.userInfo(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseUser baseUser = (BaseUser) responseObj;
                if (baseUser.data != null) {
                    postUserData(baseUser.data);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void postUserData(User data) {
        headView.setHead(data.getUserId(), data.getName(), "");
        accountTv.setText(!TextUtils.isEmpty(data.getPhone()) ? data.getPhone() : "");
        userNameTv.setText(!TextUtils.isEmpty(data.getName()) ? data.getName() : "");
        sexTv.setText(data.getSex() == 0 ? "女" : "男");
        gradeTv.setText(data.getGradeName());
        classTv.setText(TextUtils.isEmpty(data.getClassroom_name()) ? "" : data.getClassroom_name());
    }

    @Override
    protected void setEvent() {
        setBack();
        headLayout.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_layout:
                configHeard();
                break;
            case R.id.person_account_ll:
                break;
            case R.id.person_user_name_ll:
                break;
            case R.id.person_sex_ll:
                break;
            case R.id.person_grade_ll:
                break;
            case R.id.person_class_ll:
                break;
            default:
                break;
        }
    }


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

    private void setHead(final File head) {
        RequestCenter.updateAvatar(user.getUserId(), head, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if ( code== Constant.POST_SUCCESS_CODE){
                        String path = head.getAbsolutePath();
                        headView.setHead(user.getUserId() + "", user.getName(), path);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHeaderImgDialog.onActivityResult(requestCode, resultCode, data);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PersonalDataActivity.class);
        context.startActivity(intent);
    }

}
