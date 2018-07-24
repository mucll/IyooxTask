package com.huisu.iyoox.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    private TextView tabSubmitView;

    @Override
    protected void initView() {
        tabSubmitView = findViewById(R.id.tv_submit);
        tabSubmitView.setText("提交");
        tabSubmitView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        setTitle("意见反馈");
    }

    @Override
    protected void setEvent() {
        setBack();
        tabSubmitView.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                break;
            default:
                break;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        context.startActivity(intent);
    }
}
