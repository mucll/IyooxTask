package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

/**
 * @author: dl
 * @function: 个人资料
 * @date: 18/6/28
 */
public class PersonalDataActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("个人资料");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_data;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PersonalDataActivity.class);
        context.startActivity(intent);
    }

}
