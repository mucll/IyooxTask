package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.VipBuyActivity;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.constant.EventBusMsg;
import com.huisu.iyoox.entity.TrialCardModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseTrialCardModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 激活码卡号兑换
 */
public class TrialCardActivity extends BaseActivity implements View.OnClickListener {

    private User user;
    private Loading loading;
    private boolean init = false;

    private EditText mEditText;
    private EditText cardCodeTv;
    private TextView cardSubmitBt;
    private TextView phoneSubmitBt;
    private TextView startVipView;

    @Override
    protected void initView() {
        user = UserManager.getInstance().getUser();
        mEditText = findViewById(R.id.trial_card_et);
        mEditText.clearFocus();
        cardCodeTv = findViewById(R.id.trial_card_code_tv);
        cardCodeTv.clearFocus();
        cardSubmitBt = findViewById(R.id.card_submit_bt);
        phoneSubmitBt = findViewById(R.id.phone_submit_bt);

        startVipView = findViewById(R.id.start_vip_buy_tv);
        startVipView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initData() {
        setTitle("激活");
    }

    @Override
    protected void setEvent() {
        setBack();
        cardSubmitBt.setOnClickListener(this);
        phoneSubmitBt.setOnClickListener(this);
        startVipView.setOnClickListener(this);
    }

    /**
     * 获取激活码
     */
    private void postCardCodeHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getJiHuoCode(user.getUserId(), mEditText.getText().toString().trim(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                init = false;
                BaseTrialCardModel baseTrialCardModel = (BaseTrialCardModel) responseObj;
                if (baseTrialCardModel.data != null) {
                    postStudentCardHttp(baseTrialCardModel.data.getNo());
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                init = false;
                loading.dismiss();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_trial_card;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_submit_bt:
                String phone = mEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    postCardCodeHttp();
                } else {
                    TabToast.showMiddleToast(context, "手机号不能为空");
                }
                break;
            case R.id.card_submit_bt:
                String card = cardCodeTv.getText().toString().trim();
                if (!TextUtils.isEmpty(card)) {
                    postStudentCardHttp(card);
                } else {
                    TabToast.showMiddleToast(context, "激活码不能为空");
                }
                break;
            case R.id.start_vip_buy_tv:
                VipBuyActivity.start(context);
                break;
        }
    }

    /**
     * 绑定
     */
    private void postStudentCardHttp(String card) {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getStudentBindCard(user.getUserId(), card, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int code = jsonObject.getInt("code");
                    if (code == Constant.POST_SUCCESS_CODE) {
                        TabToast.showMiddleToast(context, "成功激活卡号");
                        StudentLearningCardActivity.start(context);
                        EventBus.getDefault().post(new EventBusMsg.finishMsg());
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }

    /**
     * 跳转到TrialCardActivity
     *
     * @param context 上下文
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, TrialCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


}
