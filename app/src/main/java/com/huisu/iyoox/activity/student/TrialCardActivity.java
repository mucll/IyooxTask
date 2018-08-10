package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.TrialCardModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseTrialCardModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 激活码卡号兑换
 */
public class TrialCardActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditText;
    private EditText cardCodeTv;
    private Button submitBt;
    private boolean init = false;
    private User user;
    private TrialCardModel trialCardModel;
    private Loading loading;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();

        mEditText = findViewById(R.id.trial_card_et);
        cardCodeTv = findViewById(R.id.trial_card_code_tv);

        submitBt = findViewById(R.id.card_submit_bt);
    }

    @Override
    protected void initData() {
        setTitle("激活卡兑换");
    }

    @Override
    protected void setEvent() {
        setBack();
        submitBt.setOnClickListener(this);
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
        cardCodeTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 10 && !init) {
                    submitBt.setEnabled(true);
                } else {
                    submitBt.setEnabled(false);
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
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getJiHuoCode(user.getUserId(), mEditText.getText().toString().trim(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
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
                loading.dismiss();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_trial_card;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_submit_bt:
                postStudentCardHttp();
                break;
        }
    }

    /**
     * 绑定
     */
    private void postStudentCardHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getStudentBindCard(user.getUserId(), cardCodeTv.getText().toString().trim(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if (code == Constant.POST_SUCCESS_CODE) {
                        TabToast.showMiddleToast(context, "成功激活卡号");
                        StudentLearningCardActivity.start(context);
                        finish();
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

    /**
     * 设置提交按钮状态
     */
    private void setViewEnable() {
        if (!TextUtils.isEmpty(cardCodeTv.getText().toString())) {
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
