package com.huisu.iyoox.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.Button;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;

/**
 * Created by zhaojin on 2016/4/6.
 */
public class DialogUtil {
    public static AlertDialog show(String title, String msg, String button, Activity context) {
        AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        })
                .setMessage(msg)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        if (BaseActivity.getScreenWidth(context) < BaseActivity.getScreenHeigth(context)) {
            params.width = (int) (BaseActivity.getScreenWidth(context) * 6.0f / 7.0f);
        }
        dialog.getWindow().setAttributes(params);
        return dialog;
    }

    /**
     * @param title     标题
     * @param msg       消息
     * @param okStr     确认按钮文字
     * @param cancalStr 取消按钮文字
     * @param context
     * @param listener1 确认监听
     * @param listener2 取消监听
     * @return
     */
    public static AlertDialog show(String title, String msg, String okStr, String cancalStr,
                                   Activity context, DialogInterface.OnClickListener listener1,
                                   DialogInterface.OnClickListener listener2) {
        AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(okStr, listener1)
                .setNegativeButton(cancalStr, listener2)
                .setMessage(msg)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        Button button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (button != null) {
            button.setTextColor(context.getResources().getColor(R.color.color999));
        }
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        if (BaseActivity.getScreenWidth(context) < BaseActivity.getScreenHeigth(context)) {
            params.width = (int) (BaseActivity.getScreenWidth(context) * 6.0f / 7.0f);
        }
        dialog.getWindow().setAttributes(params);
        return dialog;
    }


    public interface CancelDialogListener {
        void cancel();
    }
}


