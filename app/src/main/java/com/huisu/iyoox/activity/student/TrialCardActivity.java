package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.register.RegisterPersonDetailsActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.TrialCardModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseGradeListModel;
import com.huisu.iyoox.entity.base.BaseTrialCardModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.exception.OkHttpException;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.SelectGradeDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 试用卡号兑换
 */
public class TrialCardActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditText;
    private EditText cardCodeTv;
    private EditText selectGrade;
    private Button submitBt;
    private boolean init = false;
    private User user;
    private TrialCardModel trialCardModel;
    List<GradeListModel> gradeModels;
    private int gradeCode = 0;
    private SelectGradeDialog gradeDialog;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();

        mEditText = findViewById(R.id.trial_card_et);
        cardCodeTv = findViewById(R.id.trial_card_code_tv);
//        selectGrade = findViewById(R.id.trial_card_select_grade_tv);
        cardCodeTv.setEnabled(false);
        cardCodeTv.setFocusable(false);
        cardCodeTv.setFocusableInTouchMode(false);
        cardCodeTv.setLongClickable(false);
        cardCodeTv.setTextIsSelectable(false);

//        selectGrade.setFocusable(false);
//        selectGrade.setFocusableInTouchMode(false);
//        selectGrade.setLongClickable(false);
//        selectGrade.setTextIsSelectable(false);

        submitBt = findViewById(R.id.card_submit_bt);
    }

    @Override
    protected void initData() {
        setTitle("激活卡兑换");
        postGrade();
    }

    @Override
    protected void setEvent() {
        setBack();
        submitBt.setOnClickListener(this);
//        selectGrade.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 11 && !init) {
                    postCardCodeHttp();
                    setViewEnable();
                    init = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 获取激活码
     */
    private void postCardCodeHttp() {
        RequestCenter.getJiHuoCode(user.getUserId(), mEditText.getText().toString().trim(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                init = false;
                BaseTrialCardModel baseTrialCardModel = (BaseTrialCardModel) responseObj;
                if (baseTrialCardModel.data != null) {
                    trialCardModel = baseTrialCardModel.data;
                    if (!TextUtils.isEmpty(trialCardModel.getNo())) {
                        cardCodeTv.setText(trialCardModel.getNo());
                        setViewEnable();
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                init = false;
            }
        });
    }

    /**
     * 获取年级列表
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
    protected int getContentView() {
        return R.layout.activity_trial_card;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.trial_card_select_grade_tv:
//                if (gradeModels == null) {
//                    postGrade();
//                    return;
//                }
//                gradeDialog = new SelectGradeDialog(this, gradeModels, gradeCode) {
//                    @Override
//                    public void getGradeType(GradeListModel gradeModel, int gradeCode) {
//                        selectGrade.setText(gradeModel.getName());
//                        TrialCardActivity.this.gradeCode = gradeCode;
//                        setViewEnable();
//                    }
//                };
//                break;
            case R.id.card_submit_bt:
                TabToast.showMiddleToast(context, "激活成功");
                finish();
//                postStudentCardHttp();
                break;
        }
    }

    /**
     * 绑定
     */
    private void postStudentCardHttp() {
        RequestCenter.getStudentBindCard(trialCardModel.getId() + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if (code == Constant.POST_SUCCESS_CODE) {
                        TabToast.showMiddleToast(context, "成功激活卡号");
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

    /**
     * 设置提交按钮状态
     */
    private void setViewEnable() {
        if (!TextUtils.isEmpty(mEditText.getText().toString()) &&
                !TextUtils.isEmpty(cardCodeTv.getText().toString())) {
//             &&
//            !TextUtils.isEmpty(selectGrade.getText().toString())
            submitBt.setEnabled(true);
        } else {
            submitBt.setEnabled(false);
        }
    }

    /**
     * 跳转到TrialCardActivity
     *
     * @param context 上下文
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, TrialCardActivity.class);
        context.startActivity(intent);
    }
}
