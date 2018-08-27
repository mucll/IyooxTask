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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.ConfigMainActivity;
import com.huisu.iyoox.activity.PatriarchActivity;
import com.huisu.iyoox.activity.PersonalDataActivity;
import com.huisu.iyoox.activity.VipBuyActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.student.StudentCacheVideoActivity;
import com.huisu.iyoox.activity.student.StudentCollectActivity;
import com.huisu.iyoox.activity.student.StudentLearningCardActivity;
import com.huisu.iyoox.activity.student.StudentLearningHistoryActivity;
import com.huisu.iyoox.activity.student.TrialCardActivity;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.UserContentModel;
import com.huisu.iyoox.entity.base.BaseUserContentModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DateUtils;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.util.JsonUtils;
import com.huisu.iyoox.util.SaveDate;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.views.HeadView;

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
    private TextView shipinCount;
    private TextView timuCount;
    private TextView workCount;
    private TextView activeDateCount;
    private TextView vipEndTimeTv;
    private TextView user_no_vip_tv;
    private ImageView headbg;
    private ImageView huangGuanIv;
    private ImageView vipTypeIv;
    private View shiyongLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        user = UserManager.getInstance().getUser();
        initTab();
        initView();
        initUserContent();
        initData();
        setEvent();
        return view;
    }

    private void initView() {
        headbg = view.findViewById(R.id.headbg);
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

        shipinCount = view.findViewById(R.id.look_shipin_count);
        timuCount = view.findViewById(R.id.look_timu_count);
        workCount = view.findViewById(R.id.look_work_count);
        activeDateCount = view.findViewById(R.id.active_date_count);
        vipEndTimeTv = view.findViewById(R.id.user_vip_end_time_tv);
        huangGuanIv = view.findViewById(R.id.huang_guan_iv);
        vipTypeIv = view.findViewById(R.id.user_vip_type_iv);
        shiyongLayout = view.findViewById(R.id.shiyong_layout);
        user_no_vip_tv = view.findViewById(R.id.user_no_vip_tv);
    }

    private void initData() {
        mHeadView.setHead(user.getUserId(), user.getName(), TextUtils.isEmpty(user.getAvatar()) ? "" : user.getAvatar(), user.getType());
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
                //VIP会员
                VipBuyActivity.start(getContext());
                break;
            case R.id.mine_trial_card_ll:
                //激活
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


    @Override
    public void onShow() {
        super.onShow();
        postUserDetailHttp();
    }

    /**
     * 获取学生VIP 做题数 完成作业次数
     */
    private void postUserDetailHttp() {
        RequestCenter.getUserCenterInfo(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseUserContentModel baseModel = (BaseUserContentModel) responseObj;
                if (baseModel.data != null) {
                    String json = JsonUtils.jsonFromObject(baseModel.data);
                    String cache = SaveDate.getInstence(getContext()).getUserContent(user.getUserId());
                    if (TextUtils.isEmpty(cache)) {
                        SaveDate.getInstence(getContext()).
                                setUserContent(user.getUserId(), json);
                        setUserContent(baseModel.data);
                    } else {
                        if (!json.equals(cache)) {
                            SaveDate.getInstence(getContext()).
                                    setUserContent(user.getUserId(), json);
                            setUserContent(baseModel.data);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    private void setUserContent(UserContentModel model) {
        shipinCount.setText(model.getView_shipin_count() + "");
        timuCount.setText(model.getZuoti_count() + "");
        workCount.setText(model.getFinished_zuoye_count() + "");
        activeDateCount.setText("已高效学习" + model.getStudy_days() + "天");
        if (!TextUtils.isEmpty(model.getVip_info().getOut_date())) {
            vipEndTimeTv.setText("有效期:" + StringUtils.getTimeStringYMD(model.getVip_info().getOut_date()));
            headbg.setImageResource(R.drawable.icon_mine_vip_bg);
            huangGuanIv.setVisibility(View.VISIBLE);
            if (model.getVip_info().getName().contains("试用")) {
                shiyongLayout.setVisibility(View.VISIBLE);
                user_no_vip_tv.setText("试用账号");
            } else {
                vipTypeIv.setVisibility(View.VISIBLE);
            }
        } else {
            shiyongLayout.setVisibility(View.VISIBLE);
            user_no_vip_tv.setText("游客");
            headbg.setImageResource(R.drawable.icon_mine_no_vip_bg);
            huangGuanIv.setVisibility(View.INVISIBLE);
        }

    }


    private void initUserContent() {
        String cache = SaveDate.getInstence(getContext()).getUserContent(user.getUserId());
        if (!TextUtils.isEmpty(cache)) {
            UserContentModel model = JsonUtils.objectFromJson(cache, UserContentModel.class);
            setUserContent(model);
        }
    }
}
