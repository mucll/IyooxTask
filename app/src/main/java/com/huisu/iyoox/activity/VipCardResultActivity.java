package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alivc.player.VcPlayerLog;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.entity.VipCardModel;
import com.huisu.iyoox.util.StringUtils;

public class VipCardResultActivity extends BaseActivity {

    private ImageView card_result_iv;
    private ImageView cardView;
    private TextView endTimeTv;

    @Override
    protected void initView() {
        setWindow();
        card_result_iv = findViewById(R.id.card_result_iv);
        endTimeTv = findViewById(R.id.result_card_end_time_tv);
        cardView = findViewById(R.id.result_card_bg_iv);
    }

    @Override
    protected void initData() {
        VipCardModel model = (VipCardModel) getIntent().getSerializableExtra("cardmodel");
        if (model != null) {
            cardView.setImageResource(getCardResultResId(model.getId()));
            endTimeTv.setText("有效期:" + StringUtils.getTimeStringYMD(model.getEnddate()));
            endTimeTv.setVisibility(View.GONE);
        }
    }

    /**
     * VIP图片类型
     *
     * @param id VIP类别Id
     */
    private int getCardResultResId(int id) {
        switch (id) {
            case 2://月卡
                return R.drawable.icon_vip_buy_result_sannian_img;
            case 3://半年
                return R.drawable.icon_vip_buy_result_bannian_img;
            case 8://年卡
                return R.drawable.icon_vip_buy_result_yinian_img;
            case 9://3年卡
                return R.drawable.icon_vip_buy_result_sannian_img;
            default:
                return R.drawable.icon_vip_buy_result_sannian_img;
        }
    }

    @Override
    protected void setEvent() {
        card_result_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_vip_card_result;
    }

    public static void start(Context context, VipCardModel model) {
        Intent intent = new Intent(context, VipCardResultActivity.class);
        intent.putExtra("cardmodel", model);
        context.startActivity(intent);
    }

    private void setWindow() {
        if (!isStrangePhone()) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }


}
