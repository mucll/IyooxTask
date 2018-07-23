package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseUser;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;

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
        postPersonData();
    }

    private void postPersonData() {
        User user = UserManager.getInstance().getUser();
        RequestCenter.userInfo(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseUser baseUser = (BaseUser) responseObj;
                if (baseUser.data != null) {

                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
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
