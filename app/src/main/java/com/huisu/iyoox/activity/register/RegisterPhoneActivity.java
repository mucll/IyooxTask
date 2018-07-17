package com.huisu.iyoox.activity.register;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.views.PhoneEditText;

/**
 * @author: dl
 * @function: 注册手机号
 * @date: 18/6/28
 */
public class RegisterPhoneActivity extends BaseActivity implements View.OnClickListener, PhoneEditText.onPhoneTextChangedListener {

    private PhoneEditText mPhoneEditText;
    private Button nextBt;
    private final int edit_max_length = 13;
    private final int START_CODE = 0x101;

    @Override
    protected void initView() {
        StatusBarUtil.transparencyBar(this);
        mPhoneEditText = (PhoneEditText) findViewById(R.id.register_phone_edit_text);
        nextBt = (Button) findViewById(R.id.register_phone_next_bt);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setEvent() {
        setTransBack();
        mPhoneEditText.setOnPhoneTextChanged(this);
        nextBt.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register_phone;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterPhoneActivity.class);
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
            case R.id.register_phone_next_bt:
                if (StringUtils.isChinaPhoneLegal(mPhoneEditText.getPhoneText())) {
                    startCodeActivity();
                } else {
                    Toast.makeText(context, R.string.phone_error_hint_text, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void startCodeActivity() {
        Intent intent = new Intent(context, RegisterPhoneVerificationCodeActivity.class);
        intent.putExtra("phone", mPhoneEditText.getPhoneText());
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
