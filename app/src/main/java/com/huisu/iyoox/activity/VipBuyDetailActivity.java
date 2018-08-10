package com.huisu.iyoox.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.student.StudentLearningCardActivity;
import com.huisu.iyoox.alipay.PayResult;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VipCardModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 支付中心
 */
public class VipBuyDetailActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final int SDK_PAY_FLAG = 1;  //支付宝支付消息标识

    private View submitView;
    private TextView goodsNameTv;
    private TextView goodsPriceTv;
    private TextView tagTv;
    private TextView startTimeTv;
    private VipCardModel model;
    private Loading loading;
    private RadioGroup mRadioGroup;
    private User user;

    @Override
    protected void initView() {
        mRadioGroup = findViewById(R.id.radio_group_price);
        goodsNameTv = findViewById(R.id.goods_name_tv);
        tagTv = findViewById(R.id.vip_card_tag_tv);
        startTimeTv = findViewById(R.id.vip_card_start_time);
        goodsPriceTv = findViewById(R.id.goods_price_tv);
        submitView = findViewById(R.id.submit_tv);
    }

    @Override
    protected void initData() {
        setTitle("支付中心");
        user = UserManager.getInstance().getUser();
        model = (VipCardModel) getIntent().getSerializableExtra("model");
        if (model.getPrice() > 0) {
            String str1 = "合计: " + "<font color = '#F00202'>" + model.getPrice() + "</font>";
            goodsPriceTv.setText(Html.fromHtml(str1));
        }
        if (!TextUtils.isEmpty(model.getType_name())) {
            goodsNameTv.setText("商品名: " + model.getType_name());
        }
        tagTv.setText(TextUtils.isEmpty(model.getTag()) ? "" : model.getTag());
        startTimeTv.setText(TextUtils.isEmpty(model.getCreatedate()) ? "" : "起始时间: " + StringUtils.getTimeStringYMD(model.getCreatedate()));
    }

    @Override
    protected void setEvent() {
        setBack();
        submitView.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_vip_buy_detail;
    }

    public static void start(Context context, VipCardModel model) {
        Intent intent = new Intent(context, VipBuyDetailActivity.class);
        intent.putExtra("model", model);
        ((Activity) context).startActivityForResult(intent, 123);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_tv:
                int indexOf = mRadioGroup.indexOfChild(mRadioGroup.findViewById(mRadioGroup.getCheckedRadioButtonId()));
                if (indexOf == 0) {
                    postPayJsonHttp();
                } else {
                    TabToast.showMiddleToast(context, "微信");
                }
                break;
        }
    }

    private void postPayJsonHttp() {
        loading = Loading.show(null, context, getString(R.string.loading_one_hint_text));
        RequestCenter.getPayJson(user.getUserId(), model.getId() + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    String data = jsonObject.getString("data");
                    pay(data);
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

    private void pay(final String body) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(VipBuyDetailActivity.this);
                Map<String, String> result = alipay.payV2(body, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    // Handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    LogUtil.e("payresult:" + payResult.toString());
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    // 同步返回需要验证的信息
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                 /*
                    9000——订单支付成功
                    8000——正在处理中
                    4000——订单支付失败
                    5000——重复请求
                    6001——用户中途取消
                    6002——网络连接出错
                 */
                    // 判断resultStatus
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        TabToast.showMiddleToast(context, "支付成功");
                        // 跳到成功页
                        StudentLearningCardActivity.start(context);
                        setResult(RESULT_OK);
                        finish();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        TabToast.showMiddleToast(context, "支付取消");
                    } else {
                        LogUtil.e("错误码：" + resultStatus);
                        TabToast.showMiddleToast(context, "支付失败");
                    }
                    break;
                }
            }
        }

        ;
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
