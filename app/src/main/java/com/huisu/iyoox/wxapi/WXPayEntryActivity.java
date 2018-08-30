package com.huisu.iyoox.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.huisu.iyoox.activity.VipCardResultActivity;
import com.huisu.iyoox.activity.student.StudentLearningCardActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.constant.EventBusMsg;
import com.huisu.iyoox.util.TabToast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
      /*
          0 支付成功
         -1 发生错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
         -2 用户取消 发生场景：用户不支付了，点击取消，返回APP。
         */

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // 根据返回码
            int code = resp.errCode;
            switch (code) {
                case 0:
                    // 去本地确认支付结果
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    EventBus.getDefault().post(new EventBusMsg.finishActivity());
                    finish();
                    break;
                case -2:
                    TabToast.showMiddleToast(this, "支付已取消");
                    finish();
                    break;
                default:
                    TabToast.showMiddleToast(this, "支付失败");
                    finish();
                    break;
            }
        }
    }
}
