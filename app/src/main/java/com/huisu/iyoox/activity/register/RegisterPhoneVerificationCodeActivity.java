package com.huisu.iyoox.activity.register;


import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.CheckMsgCode;
import com.huisu.iyoox.entity.SendMsgCodeModel;
import com.huisu.iyoox.entity.base.BaseCheckMsgCode;
import com.huisu.iyoox.entity.base.BaseSendMsgCodeModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.SoftKeyBoardListener;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.TimeCount;

/**
 * @author: dl
 * @function: 注册发送验证码 phone和code_type 必须要传
 * @date: 18/6/28
 */
public class RegisterPhoneVerificationCodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView phoneTextView, sendCodeView;
    private EditText mCodeEditText;
    private String phone;
    private TimeCount time;
    private Button nextBt;
    private int edit_max_length = 4;
    private final int START_CODE = 0x102;
    private Loading loading;
    private View editContetView;
    private String codeType;

    @Override
    protected void initView() {
        StatusBarUtil.transparencyBar(this);
        phoneTextView = findViewById(R.id.register_code_phone_tv);
        sendCodeView = findViewById(R.id.register_send_code_tv);
        sendCodeView.setSelected(true);
        mCodeEditText = findViewById(R.id.register_phone_code_edit_text);
        nextBt = findViewById(R.id.register_phone_code_next_bt);
        editContetView = findViewById(R.id.register_edit_content_layout);
        time = new TimeCount(60000, 1000, sendCodeView);
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra("phone");
        codeType = getIntent().getStringExtra("code_type");
        phoneTextView.setText(phone);
    }

    @Override
    protected void setEvent() {
        setTransBack();
        sendCodeView.setOnClickListener(this);
        nextBt.setOnClickListener(this);
        //监听输入框 判断输入框内个数
        mCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == edit_max_length) {
                    nextBt.setEnabled(true);
                } else {
                    nextBt.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SoftKeyBoardListener.setPaddingListener(this, editContetView);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                editContetView.setPadding(0, 0, 0, 0);
            }

            @Override
            public void keyBoardHide(int height) {
                int v = StringUtils.dp2px(context, 105);
                editContetView.setPadding(0, v, 0, 0);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register_phone_verification_code;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_send_code_tv:
                sendCodeView.setEnabled(false);
                sendMsgCode();
                break;
            case R.id.register_phone_code_next_bt:
                judgeMsgCode();
                break;
            default:
                break;
        }
    }

    /**
     * 判断验证码是否正确
     */
    private void judgeMsgCode() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.checkMsgCode(phone, mCodeEditText.getText().toString(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseCheckMsgCode baseCheckMsgCode = (BaseCheckMsgCode) responseObj;
                if (baseCheckMsgCode.data != null) {
                    if (baseCheckMsgCode.data.isIs_Check()) {
                        judgeStartActivity();
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

    private void judgeStartActivity() {
        int startType = Constant.ERROR_CODE;
        //是否注册发短信
        if (Constant.MSG_CODE_REGISTER.equals(codeType)) {
            startType = Constant.PASSWORD_REGISTER;
        } else if (Constant.MSG_CODE_FORGET.equals(codeType)) {
            startType = Constant.PASSWORD_RESET;
        }
        startPassWordActivity(startType);
    }

    /**
     * 发送验证吗
     */
    private void sendMsgCode() {
        if (TextUtils.isEmpty(phone)) {
            LogUtil.e("SendMsgCodeModel:phone error");
            return;
        }
        RequestCenter.sendMsgCode(phone, codeType, new DisposeDataListener() {
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
     * 跳转到设置密码界面
     */
    private void startPassWordActivity(int startType) {
        Intent intent = new Intent(context, RegisterPasswordActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("start_type", startType);
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
}
