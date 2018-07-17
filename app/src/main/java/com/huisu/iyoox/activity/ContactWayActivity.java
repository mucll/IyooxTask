package com.huisu.iyoox.activity;


import android.content.Context;
import android.content.Intent;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.student.StudentLearningRemindingActivity;

/**
 * @author: dl
 * @function: 联系我们
 * @date: 18/6/28
 */
public class ContactWayActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("联系我们");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_contact_way;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ContactWayActivity.class);
        context.startActivity(intent);
    }
}
