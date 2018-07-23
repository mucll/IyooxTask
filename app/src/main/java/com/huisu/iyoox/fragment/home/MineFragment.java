package com.huisu.iyoox.fragment.home;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.ContactWayActivity;
import com.huisu.iyoox.activity.LoginActivity;
import com.huisu.iyoox.activity.PersonalDataActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.student.StudentInterestActivity;
import com.huisu.iyoox.activity.student.StudentLearningRemindingActivity;
import com.huisu.iyoox.activity.student.StudentPurchaseRecordActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.manager.ActivityStackManager;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.views.ChangeHeaderImgDialog;
import com.huisu.iyoox.views.HeadView;

import org.litepal.LitePal;

import java.io.File;

/**
 * @author: dl
 * @function: 我的fragment
 * @date: 18/6/28
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private HeadView mHeadView;
    private ChangeHeaderImgDialog mHeaderImgDialog;
    private RelativeLayout topLayout;
    private LinearLayout personalLayout, interestLayout, remindLayout, recordLayout, contactWayLayout, configLayout;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        initView();
        initData();
        setEvent();
        return view;
    }

    private void initView() {
        mHeadView = view.findViewById(R.id.user_icon);
        topLayout = view.findViewById(R.id.mine_fragment_top_layout);
        personalLayout = view.findViewById(R.id.mine_personal_layout);
        interestLayout = view.findViewById(R.id.mine_interest_layout);
        recordLayout = view.findViewById(R.id.mine_record_layout);
        remindLayout = view.findViewById(R.id.mine_remind_layout);
        contactWayLayout = view.findViewById(R.id.mine_contact_way_layout);
        configLayout = view.findViewById(R.id.mine_setting_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topLayout.setPadding(0, (int) (24 * BaseActivity.getScreenScale(getActivity())), 0, 0);
        }
    }

    private void initData() {
        user = UserManager.getInstance().getUser();
        mHeadView.setHead(user.getUserId(), "iyoox", "");
    }

    private void setEvent() {
        mHeadView.setOnClickListener(this);
        personalLayout.setOnClickListener(this);
        interestLayout.setOnClickListener(this);
        remindLayout.setOnClickListener(this);
        recordLayout.setOnClickListener(this);
        contactWayLayout.setOnClickListener(this);
        configLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_icon:
                configHeard();
                break;
            case R.id.mine_personal_layout:
                //个人资料
                PersonalDataActivity.start(getContext());
                break;
            case R.id.mine_interest_layout:
                //学习兴趣
                StudentInterestActivity.start(getContext());
                break;
            case R.id.mine_record_layout:
                //购买记录
                StudentPurchaseRecordActivity.start(getContext());
                break;
            case R.id.mine_remind_layout:
                //学习提醒
                StudentLearningRemindingActivity.start(getContext());
                break;
            case R.id.mine_contact_way_layout:
                //联系我们
                ContactWayActivity.start(getContext());
                break;
            case R.id.mine_setting_layout:
                //设置
                LitePal.deleteAll(User.class);
                ActivityStackManager.getActivityStackManager().popAllActivity();
                UserManager.getInstance().removeUser();
                LoginActivity.start(getContext());
//                ConfigMainActivity.start(getContext());
                break;
            default:
                break;
        }
    }

    private void configHeard() {
        mHeaderImgDialog = new ChangeHeaderImgDialog(getActivity(),
                mHeadView.head, this) {
            @Override
            public void getResult(final File file) {
                super.getResult(file);
                String path = file.getAbsolutePath();
                mHeadView.setHead(user.getUserId() + "", "iyoox", path);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mHeaderImgDialog != null) {
            mHeaderImgDialog.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void doOpenCamera() {
        mHeaderImgDialog.doOpenCamera();
    }

    @Override
    public void doWriteSDCard() {
        mHeaderImgDialog.doWriteSDCard();
    }
}
