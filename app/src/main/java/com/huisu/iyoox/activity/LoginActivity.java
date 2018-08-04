package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.register.RegisterPasswordActivity;
import com.huisu.iyoox.activity.register.RegisterPhoneVerificationCodeActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.PhoneModel;
import com.huisu.iyoox.entity.base.BasePhoneModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.SoftKeyBoardListener;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;
import com.huisu.iyoox.views.PhoneEditText;

/**
 * @author: dl
 * @function: 登陆界面
 * @date: 18/6/28
 */
public class LoginActivity extends BaseActivity implements PhoneEditText.onPhoneTextChangedListener, View.OnClickListener {
    private PhoneEditText mPhoneEditText;
    private Button nextBt;
    private final int edit_max_length = 13;
    private final int START_CODE = 0x101;
    private View editContetView;
    private Loading loading;

    @Override
    protected void initView() {
        mPhoneEditText = (PhoneEditText) findViewById(R.id.login_phone_edit_text);
        nextBt = (Button) findViewById(R.id.login_phone_next_bt);
        editContetView = findViewById(R.id.login_edit_content);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setEvent() {
        setTransBack();
        mPhoneEditText.setOnPhoneTextChanged(this);
        nextBt.setOnClickListener(this);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                int v = StringUtils.dp2px(context, 160);
                editContetView.setPadding(0, 0, 0, v);
            }

            @Override
            public void keyBoardHide(int height) {
                editContetView.setPadding(0, 0, 0, 0);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setOnPhoneTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == edit_max_length) {
            nextBt.setEnabled(true);
        } else {
            nextBt.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_phone_next_bt:
                if (StringUtils.isChinaPhoneLegal(mPhoneEditText.getPhoneText())) {
                    judgePhone();
                } else {
                    TabToast.showMiddleToast(context, getString(R.string.phone_error_hint_text));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断输入手机号 是否注册
     */
    private void judgePhone() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.isExistphone(mPhoneEditText.getPhoneText(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BasePhoneModel basePhoneModel = (BasePhoneModel) responseObj;
                if (basePhoneModel.data != null) {
                    configSuccessData(basePhoneModel.data);
                } else {
                    TabToast.showMiddleToast(context, basePhoneModel.msg);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    /**
     * 根据请求数据判断跳转界面
     *
     * @param responseObj
     */
    private void configSuccessData(PhoneModel responseObj) {
        if (responseObj.isExis()) {
            startPasswordActivity();
        } else {
            startRegisterCodeActivity();
        }
    }

    /**
     * 手机号未注册 跳转到发送验证码界面
     */
    private void startRegisterCodeActivity() {
        Intent intent = new Intent(context, RegisterPhoneVerificationCodeActivity.class);
        intent.putExtra("phone", mPhoneEditText.getPhoneText());
        intent.putExtra("code_type", Constant.MSG_CODE_REGISTER);
        startActivityForResult(intent, START_CODE);
    }

    /**
     * 手机号 已注册 输入密码登陆
     */
    private void startPasswordActivity() {
        Intent intent = new Intent(context, RegisterPasswordActivity.class);
        intent.putExtra("phone", mPhoneEditText.getPhoneText());
        intent.putExtra("start_type", Constant.PASSWORD_LOGIN);
        startActivityForResult(intent, START_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }
}
