package com.huisu.iyoox.fragment.teacher;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.ConfigMainActivity;
import com.huisu.iyoox.activity.LoginActivity;
import com.huisu.iyoox.activity.PersonalDataActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.manager.ActivityStackManager;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.views.HeadView;

import org.litepal.LitePal;

/**
 * 老师端-个人
 */
public class TeacherMineFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private TextView userName;
    private HeadView headView;
    private User user;
    private View configLayout;
    private View servicePhoneLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_mine, container, false);
        initTab();
        initView();
        initData();
        setEvent();
        return view;
    }

    private void initView() {
        headView = view.findViewById(R.id.user_icon);
        userName = view.findViewById(R.id.user_name);
        //设置
        configLayout = view.findViewById(R.id.mine_setting_layout);
        //客服
        servicePhoneLayout = view.findViewById(R.id.mine_service_phone_layout);
    }

    private void initData() {
        user = UserManager.getInstance().getUser();
        headView.setHead(user.getUserId(), user.getName(),  TextUtils.isEmpty(user.getAvatar()) ? "" : user.getAvatar(),user.getType());
        userName.setText(user.getName());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_icon:
                //个人资料
                PersonalDataActivity.start(getContext());
                break;
            case R.id.mine_service_phone_layout:
                showDialog();
                break;
            case R.id.mine_setting_layout:
                //设置
                ConfigMainActivity.start(getContext());
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            initData();
        }
    }

    private void showDialog() {
        DialogUtil.show("", "呼叫【400-700-9987】", "确认", "取消", (Activity) getContext(),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //客服电话
                        Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:4007009987"));//跳转到拨号界面，同时传递电话号码
                        startActivity(Intent);
                    }
                }, null);
    }

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }

    private void setEvent() {
        headView.setOnClickListener(this);
        configLayout.setOnClickListener(this);
        servicePhoneLayout.setOnClickListener(this);
    }

}
