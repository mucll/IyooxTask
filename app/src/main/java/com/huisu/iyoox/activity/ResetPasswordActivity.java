package com.huisu.iyoox.activity;


import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseCheckMsgCode;
import com.huisu.iyoox.entity.base.BaseSendMsgCodeModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.ActivityStackManager;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.TimeCount;

import org.litepal.LitePal;

/**
 * 重置密码
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView tabSubmiteView;
    private TextView sendMsgTv;
    private EditText codePasswordEt;
    private EditText newPasswordEt;
    private TimeCount time;
    private User user;
    private Loading loading;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();
        //tab提交按钮
        tabSubmiteView = findViewById(R.id.tv_submit);
        tabSubmiteView.setText("完成");
        tabSubmiteView.setEnabled(false);
        tabSubmiteView.setVisibility(View.VISIBLE);
        sendMsgTv = findViewById(R.id.register_send_code_tv);
        time = new TimeCount(60000, 1000, sendMsgTv);
        //验证码
        codePasswordEt = findViewById(R.id.user_codel_password_et);
        //新密码
        newPasswordEt = findViewById(R.id.user_new_password_et);
    }

    @Override
    protected void initData() {
        setTitle("修改密码");
    }

    @Override
    protected void setEvent() {
        setBack();
        tabSubmiteView.setOnClickListener(this);
        sendMsgTv.setOnClickListener(this);
        codePasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 4) {
                    setSubmitEnable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 4) {
                    setSubmitEnable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setSubmitEnable() {
        if (!TextUtils.isEmpty(codePasswordEt.getText().toString().trim()) && !TextUtils.isEmpty(newPasswordEt.getText().toString().trim())) {
            tabSubmiteView.setEnabled(true);
        } else {
            tabSubmiteView.setEnabled(false);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_reset_password;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                judgeMsgCode();
                break;
            case R.id.register_send_code_tv:
                setMsg();
                break;
            default:
                break;
        }
    }

    private void setMsg() {
        if (TextUtils.isEmpty(user.getPhone())) {
            return;
        }
        RequestCenter.sendMsgCode(user.getPhone(), Constant.MSG_CODE_FORGET, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseSendMsgCodeModel baseSendMsgCodeModel = (BaseSendMsgCodeModel) responseObj;
                if (baseSendMsgCodeModel.data != null) {
                    //发送验证码
                    time.start();
                    TabToast.showMiddleToast(context, baseSendMsgCodeModel.data.getMessage());
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 判断验证码是否正确
     */
    private void judgeMsgCode() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.checkMsgCode(user.getPhone(), codePasswordEt.getText().toString(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseCheckMsgCode baseCheckMsgCode = (BaseCheckMsgCode) responseObj;
                if (baseCheckMsgCode.data != null) {
                    if (baseCheckMsgCode.data.isIs_Check()) {
                        postResetPassword();
                    } else {
                        TabToast.showMiddleToast(context, getString(R.string.msg_code_error_text));
                    }
                } else {
                    TabToast.showMiddleToast(context, baseCheckMsgCode.msg);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    /**
     * 重置密码接口
     */
    private void postResetPassword() {
        RequestCenter.ModifyPassword(user.getPhone(), newPasswordEt.getText().toString(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                TabToast.showMiddleToast(context, getString(R.string.modify_password_success_text));
                LitePal.deleteAll(User.class);
                ActivityStackManager.getActivityStackManager().popAllActivity();
                UserManager.getInstance().removeUser();
                LoginActivity.start(context);
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        });
    }

}
