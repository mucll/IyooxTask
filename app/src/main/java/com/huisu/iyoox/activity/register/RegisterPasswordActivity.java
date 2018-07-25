package com.huisu.iyoox.activity.register;

import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.LoginActivity;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseUser;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.SoftKeyBoardListener;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 输入密码
 */
public class RegisterPasswordActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private EditText passwordEditText;
    private Button nextBt;
    private final int START_CODE = 0x103;
    private String phone;
    private View contentView;
    private final int password_min_length = 4;
    private int startType;
    private TextView titleTv;
    private TextView resetPasswordTV;
    private Loading loading;

    @Override
    protected void initView() {
        StatusBarUtil.transparencyBar(this);
        passwordEditText = (EditText) findViewById(R.id.register_password_edit_text);
        titleTv = findViewById(R.id.passwrod_title_tv);
        nextBt = (Button) findViewById(R.id.register_password_next_bt);
        contentView = findViewById(R.id.register_password_content_layout);
        resetPasswordTV = findViewById(R.id.reset_password_tv);
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra("phone");
        startType = getIntent().getIntExtra("start_type", Constant.ERROR_CODE);
        setViewData();
    }

    /**
     * 根据startTyep来显示界面数据
     */
    private void setViewData() {
        switch (startType) {
            //输入密码
            case Constant.PASSWORD_LOGIN:
                titleTv.setText(getString(R.string.edit_password_title_text));
                passwordEditText.setHint(getString(R.string.edit_password_title_text));
                nextBt.setText(getString(R.string.longin_title_text));
                resetPasswordTV.setVisibility(View.VISIBLE);
                break;
            //设置密码
            case Constant.PASSWORD_REGISTER:
                titleTv.setText(getString(R.string.set_password_title_text));
                passwordEditText.setHint(getString(R.string.set_password_hint_text));
                nextBt.setText(getString(R.string.next_bt_text));
                resetPasswordTV.setVisibility(View.INVISIBLE);
                break;
            //重置密码
            case Constant.PASSWORD_RESET:
                titleTv.setText(getString(R.string.reset_password_title_text));
                passwordEditText.setHint(getString(R.string.set_password_hint_text));
                nextBt.setText(getString(R.string.reset_passwrod_next_bt_text));
                resetPasswordTV.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void setEvent() {
        setTransBack();
        passwordEditText.addTextChangedListener(this);
        nextBt.setOnClickListener(this);
        SoftKeyBoardListener.setPaddingListener(this, contentView);
        resetPasswordTV.setOnClickListener(this);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                int v = StringUtils.dp2px(context, 180);
                contentView.setPadding(0, 0, 0, v);
            }

            @Override
            public void keyBoardHide(int height) {
                contentView.setPadding(0, 0, 0, 0);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register_password;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //下一步或登陆
            case R.id.register_password_next_bt:
                judgeStartTtpe();
                break;
            //忘记密码
            case R.id.reset_password_tv:
                forgetPassword();
                break;
            default:
                break;
        }
    }

    /**
     * 忘记密码
     */
    private void forgetPassword() {
        Intent intent = new Intent(context, RegisterPhoneVerificationCodeActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("code_type", Constant.MSG_CODE_FORGET);
        startActivityForResult(intent, START_CODE);
    }

    /**
     * 判断界面跳转
     */
    private void judgeStartTtpe() {
        if (startType == Constant.PASSWORD_LOGIN) {
            //登陆接口
            postLogin();
        } else if (startType == Constant.PASSWORD_REGISTER) {
            //设置身份界面
            startIdentityActivity();
        } else if (startType == Constant.PASSWORD_RESET) {
            //重置密码
            postResetPassword();
        }
    }

    /**
     * 请求登陆接口
     */
    private void postLogin() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.login(phone, passwordEditText.getText().toString(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseUser baseUser = (BaseUser) responseObj;
                if (baseUser.data != null) {
                    judgeUserInfo(baseUser.data);
                } else {
                    TabToast.showMiddleToast(context, baseUser.msg);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                JSONObject object = (JSONObject) reasonObj;
                try {
                    TabToast.showMiddleToast(context, object.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading.dismiss();
            }
        });
    }

    private void judgeUserInfo(User user) {
        if (TextUtils.isEmpty(user.getName())) {
            if (user.getType() == Constant.STUDENT_TYPE) {
                Intent intent = new Intent(context, RegisterPersonDetailsActivity.class);
                intent.putExtra("userId", user.getUserId());
                startActivityForResult(intent, START_CODE);
            } else if (user.getType() == Constant.TEACHER_TYPE) {
                Intent intent = new Intent(context, RegisterTeacherSubjectActivity.class);
                intent.putExtra("userId", user.getUserId());
                startActivityForResult(intent, START_CODE);
            }
        } else {
            UserManager.getInstance().setUser(user);
            user.clearSavedState();
            boolean isSave = user.save();
            MainActivity.start(context);
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 跳转到选择身份 界面
     */
    private void startIdentityActivity() {
        Intent intent = new Intent(context, RegisterIdentityActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("password", passwordEditText.getText().toString());
        startActivityForResult(intent, START_CODE);
    }

    /**
     * 重置密码接口
     */
    private void postResetPassword() {
        RequestCenter.ModifyPassword(phone, passwordEditText.getText().toString(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                TabToast.showMiddleToast(context, getString(R.string.modify_password_success_text));
                LoginActivity.start(context);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {
                TabToast.showMiddleToast(context, getString(R.string.modify_password_error_text));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() >= password_min_length) {
            nextBt.setEnabled(true);
        } else {
            nextBt.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
