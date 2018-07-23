package com.huisu.iyoox.activity;


import android.content.Context;
import android.content.Intent;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

/**
 * @author: dl
 * @function: 联系我们
 * @date: 18/6/28
 */
public class PatriarchActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("家长端");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_patriarch;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PatriarchActivity.class);
        context.startActivity(intent);
    }
}
