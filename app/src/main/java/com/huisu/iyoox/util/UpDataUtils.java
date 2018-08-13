package com.huisu.iyoox.util;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.VersionBean;
import com.huisu.iyoox.manager.ActivityStackManager;
import com.huisu.iyoox.service.DownloadApkService;
import com.huisu.iyoox.service.ICallbackResult;
import com.huisu.iyoox.views.DialogDownloadApp;
import com.huisu.iyoox.views.DownLoadVersionDialog;

/**
 * Created by Mucll on 2016/10/9.
 */
public class UpDataUtils {
    public static final int FORCE_TYPE = 0x011;//强制
    public static final int SELECT_TYPE = 0x012;//选择
    Intent intentDownload;
    VersionBean bean;
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                ActivityStackManager.getActivityStackManager().popAllActivity();
                return true;
            } else {
                return false;
            }
        }
    };
    DialogInterface.OnKeyListener noKeylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                ActivityStackManager.getActivityStackManager().popAllActivity();
                return true;
            } else {
                return false;
            }
        }
    };
    private Context context;
    private DownLoadVersionDialog dialog;
    private String downLoad_url = "";
    private DownloadApkService.DownloadBinder binder;
    private boolean isBinded = false;
    private ICallbackResult callback = new ICallbackResult() {
        @Override
        public void OnBackResult(Object result) {
            if ("finish".equals(result)) {
                //finish();
                if (intentDownload != null) {
                    if (conn != null) {
                        context.getApplicationContext().unbindService(conn);
                    }
                }
                return;
            }
        }
    };
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloadApkService.DownloadBinder) service;
            LogUtil.e("服务启动!!!");
            // 开始下载
            binder = (DownloadApkService.DownloadBinder) service;
            // 开始下载
            isBinded = true;
            binder.addCallback(callback);
            binder.start();
        }
    };

    public UpDataUtils(Context context) {
        this.context = context;
    }

    public void isUpdata(int version, VersionBean bean) {
        this.bean = bean;
        downLoad_url = bean.getDownloadUrl();
        if (TextUtils.isEmpty(downLoad_url))
            return;
        if (!isValidContext(context))
            return;
        switch (version) {
            case FORCE_TYPE://强制
                showForceUpdateDialog();
                break;
            case SELECT_TYPE://选择
                showUpdateDialog();
                break;
        }
    }

    //判断Activity的状态
    private boolean isValidContext(Context c) {
        Activity a = (Activity) c;
        if (a.isFinishing()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 强制更新
     */
    public void showForceUpdateDialog() {
        dialog = new DownLoadVersionDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnKeyListener(keylistener);
        dialog.show();
        //        String tip[] = appVersion.getTips().split("#");
        //        String tipDesc = "版本号：" + appVersion.getVersion_name() + "\n";
        //        for (int i = 0; i < tip.length; i++) {
        //            tipDesc += (i + 1) + "、" + tip[i] + "\n";
        //        }
        //        dialog.message.setText("1231123\n1234123\n123123123\n12312312\n123123123123\n123123123123
        // \n123123123123\n123123123123\n123123123123\n123123123123\n123123123123\n123123123123\n123123123123
        // \n123123123123\n123123123123\n");
        //        dialog.message.setText("123123123123\n123123123123\n");
        dialog.message.setText(bean.getVersionRemark());
        dialog.message.setGravity(Gravity.LEFT);
        TextView tv = (TextView) dialog.findViewById(R.id.right_btn);
        tv.setText(R.string.update_now);
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                showForceDownloadDialog(downLoad_url);//url 链接
            }
        });
        tv = (TextView) dialog.findViewById(R.id.left_btn);
        tv.setText(R.string.exit_app);
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                ActivityStackManager.getActivityStackManager().popAllActivity();
                ((Activity) context).finish();
            }
        });
    }

    //选择更新
    public void showUpdateDialog() {
        final DownLoadVersionDialog dialog = new DownLoadVersionDialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //        String tip[] = appVersion.getTips().split("#");
        //        String tipDesc = "版本号：" + appVersion.getVersion_name() + "\n";
        //        for (int i = 0; i < tip.length; i++) {
        //            tipDesc += (i + 1) + "、" + tip[i] + "\n";
        //        }
        dialog.message.setText(bean.getVersionRemark());
        dialog.message.setGravity(Gravity.LEFT);
        TextView tv = (TextView) dialog.findViewById(R.id.right_btn);
        tv.setText(R.string.exp_now);
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //后台service更新弹通知
                //                dialog.dismiss();
                //                intentDownload = new Intent(context.getApplicationContext(), DownloadApkService
                //                        .class);
                //                //此处要保存url链接 service获取
                //                SaveDate.getInstence(context).setApkUrl(downLoad_url);
                //                context.getApplicationContext().bindService(intentDownload, conn,
                //                        Context.BIND_AUTO_CREATE);
                //activity内更新
                dialog.dismiss();
                showForceDownloadDialog(downLoad_url);//url 链接
            }
        });
        tv = (TextView) dialog.findViewById(R.id.left_btn);
        tv.setText(R.string.exp_after);
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void showForceDownloadDialog(String url) {
        final DialogDownloadApp dialog = new DialogDownloadApp(context, R.style.MyDialogStyleTop,
                url);//url
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(noKeylistener);
        dialog.show();
    }
}
