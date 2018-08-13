package com.huisu.iyoox.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dl on 2017/5/2.
 */

public class DownLoadVersionDialog extends Dialog {
    @Bind(R.id.version_message_tv)
    public TextView message;
    @Bind(R.id.left_btn)
    public TextView left_btn;
    @Bind(R.id.right_btn)
    public TextView right_btn;

    private Context context;

    public DownLoadVersionDialog(Context context) {
        super(context, R.style.MyDialogStyleTop);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_down_load_version_view);
        ButterKnife.bind(this);
    }


    public void setLeft_btn(String paramString) {
        this.left_btn.setText(paramString);
    }

    public void setMessage(String paramString) {
        this.message.setText(paramString);
    }

    public void setRight_btn(String paramString) {
        this.right_btn.setText(paramString);
    }

    public void show() {
        super.show();
        if (BaseActivity.getScreenWidth(((Activity) context)) < BaseActivity.getScreenHeigth(((Activity)
                context))) {
            WindowManager.LayoutParams params =
                    this.getWindow().getAttributes();
            params.width = (int) (MainActivity.getScreenWidth(((Activity) context)) * 5.0f / 7.0f);
            this.getWindow().setAttributes(params);
        } else {
            WindowManager.LayoutParams params =
                    this.getWindow().getAttributes();
            params.width = (int) (MainActivity.getScreenWidth(((Activity) context)) * 1f / 2f);
            this.getWindow().setAttributes(params);
        }
    }
}
