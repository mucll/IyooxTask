package com.huisu.iyoox.fragment.home;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.ConfigMainActivity;
import com.huisu.iyoox.activity.PatriarchActivity;
import com.huisu.iyoox.activity.PersonalDataActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.student.StudentCacheVideoActivity;
import com.huisu.iyoox.activity.student.StudentCollectActivity;
import com.huisu.iyoox.activity.student.StudentLearningCardActivity;
import com.huisu.iyoox.activity.student.StudentLearningHistoryActivity;
import com.huisu.iyoox.activity.student.StudentPurchaseHistoryActivity;
import com.huisu.iyoox.activity.student.TrialCardActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.views.ChangeHeaderImgDialog;
import com.huisu.iyoox.views.HeadView;


import java.io.File;

/**
 * @author: dl
 * @function: 我的fragment
 * @date: 18/6/28
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private HeadView mHeadView;
    private TextView userName;
    private RelativeLayout topLayout;
    private LinearLayout learningCardLayout, learningHistoryLayout, patriarchLayout, configLayout, servicePhoneLayout;
    private View mineCacheView, mineCollectView, mineHistoryView, mineTrialCardView;

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        initTab();
        initView();
        initData();
        setEvent();
        return view;
    }

    private void initView() {
        //头像
        mHeadView = view.findViewById(R.id.user_icon);
        userName = view.findViewById(R.id.user_name);
        //缓存
        mineCacheView = view.findViewById(R.id.mine_cache_ll);
        //收藏
        mineCollectView = view.findViewById(R.id.mine_collect_ll);
        //购买记录
        mineHistoryView = view.findViewById(R.id.mine_purchase_history_ll);
        //试用卡
        mineTrialCardView = view.findViewById(R.id.mine_trial_card_ll);
        //学习卡
        learningCardLayout = view.findViewById(R.id.mine_learning_card_layout);
        //学习记录
        learningHistoryLayout = view.findViewById(R.id.mine_learning_history_layout);
        //家长
        patriarchLayout = view.findViewById(R.id.mine_patriarch_layout);
        //客服
        servicePhoneLayout = view.findViewById(R.id.mine_service_phone_layout);
        //设置
        configLayout = view.findViewById(R.id.mine_setting_layout);
        topLayout = view.findViewById(R.id.mine_fragment_top_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topLayout.setPadding(0, (int) (24 * BaseActivity.getScreenScale(getActivity())), 0, 0);
        }
    }

    private void initData() {
        user = UserManager.getInstance().getUser();
        mHeadView.setHead(user.getUserId(), user.getName(), TextUtils.isEmpty(user.getAvatar()) ? "" : user.getAvatar());
        userName.setText(user.getName());
    }

    private void setEvent() {
        mHeadView.setOnClickListener(this);
        mineCacheView.setOnClickListener(this);
        mineCollectView.setOnClickListener(this);
        mineHistoryView.setOnClickListener(this);
        mineTrialCardView.setOnClickListener(this);
        learningCardLayout.setOnClickListener(this);
        learningHistoryLayout.setOnClickListener(this);
        patriarchLayout.setOnClickListener(this);
        servicePhoneLayout.setOnClickListener(this);
        configLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_icon:
                //个人资料
                PersonalDataActivity.start(getContext());
                break;
            case R.id.mine_cache_ll:
                //缓存
                StudentCacheVideoActivity.start(getContext());
                break;
            case R.id.mine_collect_ll:
                //收藏
                StudentCollectActivity.start(getContext());
                break;
            case R.id.mine_purchase_history_ll:
                //购买记录
                StudentPurchaseHistoryActivity.start(getContext());
                break;
            case R.id.mine_trial_card_ll:
                //购买记录
                TrialCardActivity.start(getContext());
                break;
            case R.id.mine_learning_card_layout:
                //学习卡
                StudentLearningCardActivity.start(getContext());
                break;
            case R.id.mine_learning_history_layout:
                //学习记录
                StudentLearningHistoryActivity.start(getContext());
                break;
            case R.id.mine_patriarch_layout:
                //家长
                PatriarchActivity.start(getContext());
                break;
            case R.id.mine_service_phone_layout:
                DialogUtil.show("", "呼叫【400-700-9987】", "确认", "取消", (Activity) getContext(),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //客服电话
                                Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:4007009987"));//跳转到拨号界面，同时传递电话号码
                                startActivity(Intent);
                            }
                        }, null);
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


    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }
}
