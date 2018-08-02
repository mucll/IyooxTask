package com.huisu.iyoox.fragment.teacher;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.LoginActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.manager.ActivityStackManager;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.views.HeadView;

import org.litepal.LitePal;

/**
 * 老师端-个人
 */
public class TeacherMineFragment extends BaseFragment {

    private View view;
    private HeadView headView;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_mine, container, false);
        initTab();
        initView();
        return view;
    }

    private void initView() {
        user = UserManager.getInstance().getUser();
        TextView logout = view.findViewById(R.id.logout_tv);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        headView = view.findViewById(R.id.user_icon);
        headView.setHead(user.getUserId(), user.getName(), "");
    }

    /**
     * 退出登录
     */
    private void logout() {
        LitePal.deleteAll(User.class);
        ActivityStackManager.getActivityStackManager().popAllActivity();
        UserManager.getInstance().removeUser();
        LoginActivity.start(getContext());
    }

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }

}
