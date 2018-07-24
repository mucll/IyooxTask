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

/**
 * 重置密码
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView tabSubmiteView;
    private EditText oldPasswordEt;
    private EditText newPasswordEt;

    @Override
    protected void initView() {
        //tab提交按钮
        tabSubmiteView = findViewById(R.id.tv_submit);
        tabSubmiteView.setText("完成");
        tabSubmiteView.setEnabled(false);
        tabSubmiteView.setVisibility(View.VISIBLE);
        //旧密码
        oldPasswordEt = findViewById(R.id.user_old_password_et);
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
        oldPasswordEt.addTextChangedListener(new TextWatcher() {
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
        if (!TextUtils.isEmpty(oldPasswordEt.getText().toString().trim()) && !TextUtils.isEmpty(newPasswordEt.getText().toString().trim())) {
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

                break;
            default:
                break;
        }
    }


}
