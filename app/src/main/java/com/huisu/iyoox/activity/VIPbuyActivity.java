package com.huisu.iyoox.activity;


import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

/**
 * 会员购买
 */
public class VIPbuyActivity extends BaseActivity {

    private TextView oneTv;
    private TextView twoTv;
    private TextView threeTv;
    private TextView fourTv;

    @Override
    protected void initView() {
        oneTv = findViewById(R.id.vip_jieshao_tv1);
        twoTv = findViewById(R.id.vip_jieshao_tv2);
        threeTv = findViewById(R.id.vip_jieshao_tv3);
        fourTv = findViewById(R.id.vip_jieshao_tv4);
    }

    @Override
    protected void initData() {
        setTitle("VIP会员");
        String str1 = "将" + "<font color = '#F00202'>" + "147位名师" + "</font>" + "随身携带";
        String str2 = "智能大数据诊断、" + "<font color = '#F00202'>" + "查漏补缺;" + "</font>";
        String str3 = "系统自动" + "<font color = '#F00202'>" + "记录学情" + "</font>"
                + "、生成属于你独一无二的" + "<font color = '#F00202'>" + "学习方案" + "</font>";
        String str4 = "“虚拟课堂”向你开户链接家长、学校、和学生" + "<font color = '#F00202'>" + "360全了解。" + "</font>";
        oneTv.setText(Html.fromHtml(str1));
        twoTv.setText(Html.fromHtml(str2));
        threeTv.setText(Html.fromHtml(str3));
        fourTv.setText(Html.fromHtml(str4));
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_vipbuy;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, VIPbuyActivity.class);
        context.startActivity(intent);
    }

}
