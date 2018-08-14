package com.huisu.iyoox.activity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.manager.ActivityStackManager;
import com.huisu.iyoox.manager.UserManager;

import org.litepal.LitePal;

/**
 * 设置
 */
public class ConfigMainActivity extends BaseActivity implements View.OnClickListener {

    private TextView logoutTv;
    private View resetPasswordView;

    @Override
    protected void initView() {
        logoutTv = findViewById(R.id.logout_tv);
        resetPasswordView = findViewById(R.id.reset_password_ll);
    }

    @Override
    protected void initData() {
        setTitle("设置");
    }

    @Override
    protected void setEvent() {
        setBack();
        logoutTv.setOnClickListener(this);
        resetPasswordView.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_config_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_password_ll:
                ResetPasswordActivity.start(this);
                break;
            case R.id.logout_tv:
                logout();
                break;
            default:
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        ActivityStackManager.getActivityStackManager().popAllActivity();
        LitePal.deleteAll(User.class);
        UserManager.getInstance().removeUser();
        LoginActivity.start(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ConfigMainActivity.class);
        context.startActivity(intent);
    }
}
