package com.huisu.iyoox.activity;


import android.content.Context;
import android.content.Intent;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
/**
 * @author: dl
 * @function: 设置
 * @date: 18/6/28
 */
public class ConfigMainActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("设置");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_config_main;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ConfigMainActivity.class);
        context.startActivity(intent);
    }

}
