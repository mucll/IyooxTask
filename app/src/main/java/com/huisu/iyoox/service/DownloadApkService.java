package com.huisu.iyoox.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.MainActivity;
import com.huisu.iyoox.application.MyApplication;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.TabToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadApkService extends Service {
    private static final int NOTIFY_ID = 0;
    /**
     * 默认下载文件地址.
     */
    public static String APK_SAVEPATH = MyApplication.DOWNLOAD_URL;
    private static final String savePath = APK_SAVEPATH;
    public static String APK_SAVEFILENAME = APK_SAVEPATH + "/iyoox.apk";
    private static final String saveFileName = APK_SAVEFILENAME;
    private final int SUCCESS_DOWN_LOAD = 12;
    private final int CANCEL_DOWNLOAD = 11;
    private final int DOWNLOAD_ERROR = 14;
    private final int SET_PROGRESS = 13;
    private final int URL_ERROR = 15;
    Notification mNotification;
    NotificationCompat.Builder mBuilder;
    private int progress;
    private NotificationManager mNotificationManager;
    private boolean canceled;
    // 返回的安装包url
    private String resovleUrl = "";
    private String apkUrl = "";
    private String ACTION_CANCEL_DOWNLOAD_APK = "action_cancel_download_apk";
    private String ACTION_PAUSE_DOWNLOAD_APK = "action_pause_download_apk";
    private ICallbackResult callback;
    private DownloadBinder binder;
    // private MyApplication app;
    private boolean serviceIsDestroy = false;
    private Context mContext = this;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case URL_ERROR:
                    break;
                case SUCCESS_DOWN_LOAD:
                    // app.setDownload(false);
                    finishDownload();
                    installApk();
                    break;
                case CANCEL_DOWNLOAD:
                    // app.setDownload(false);
                    // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
                    // 取消通知
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
                case DOWNLOAD_ERROR:
                    TabToast.makeText(getResources().getString(R.string.update_version_error), mContext);
                    finishDownload();
                    callback.OnBackResult("finish");
                    break;
                case SET_PROGRESS:
                    int rate = msg.arg1;
                    Log.e("rate", "" + rate);
                    // app.setDownload(true);
                    if (rate < 100) {
                        //					RemoteViews contentview = mNotification.contentView;
                        //					contentview.setTextViewText(R.id.tv_progress, rate + "%");
                        //					contentview.setProgressBar(R.id.progressbar, 100, rate,
                        //							false);
                        mBuilder.setContentTitle("正在下载:" + progress + "%");
                        mBuilder.setProgress(100, progress, false);
                        mNotification = mBuilder.build();
                        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
                    } else {
                        finishDownload();
                    }
                    mNotificationManager.notify(NOTIFY_ID, mNotification);
                    break;
                default:
                    break;
            }
        }
    };
    BroadcastReceiver onclickCancelListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_CANCEL_DOWNLOAD_APK)) {
                // TODO;;
                // app.setDownload(false);
                // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
                // 取消通知
                mNotificationManager.cancel(NOTIFY_ID);
                binder.cancel();
                binder.cancelNotification();
                if (binder != null && binder.isCanceled()) {
                    stopSelf();
                }
                callback.OnBackResult("cancel");
            }
        }
    };
    /**
     * 下载apk
     */
    private Thread downLoadThread;
    private int lastRate = 0;
    private Runnable mdownApkRunnableGO = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setUseCaches(true);
                conn.setConnectTimeout(2 * 1000);
                conn.setRequestMethod("GET");
                long totalLenth = (long) conn.getContentLength();
                InputStream in = null;
                FileOutputStream fileOut = null;
                try {
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File fileApk = new File(saveFileName);
                    if (!fileApk.exists()) {
                        fileApk.createNewFile();
                    }
                    if (null != fileApk && fileApk.length() < totalLenth) {
                        fileApk.delete();
                        fileApk.createNewFile();
                    }
                    if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                        Message msgError = new Message();
                        msgError.what = DOWNLOAD_ERROR;
                        mHandler.sendMessage(msgError);
                        return;
                    }
                    in = conn.getInputStream();
                    fileOut = new FileOutputStream(fileApk);
                    byte[] buffer = new byte[1024];
                    int length = -1;
                    int currentCount = 0;
                    while ((length = in.read(buffer)) != -1 && !canceled) {
                        // int numread = in.read(buffer);
                        // currentCount += numread;
                        progress = (int) (((float) fileApk.length() / totalLenth) * 100);
                        // 更新进度
                        Message msg = mHandler.obtainMessage();
                        msg.what = SET_PROGRESS;
                        msg.arg1 = progress;
                        if (progress >= lastRate + 1) {
                            mHandler.sendMessage(msg);
                            lastRate = progress;
                            if (callback != null)
                                callback.OnBackResult(progress);
                        }
                        // fileOut.write(data, 0, numread);
                        fileOut.write(buffer, 0, length);
                    }
                    if (canceled) {
                        Message msgDownloadCancel = new Message();
                        msgDownloadCancel.what = CANCEL_DOWNLOAD;
                        mHandler.sendMessage(msgDownloadCancel);
                        return;
                    }
                    mHandler.sendEmptyMessage(SUCCESS_DOWN_LOAD);
                    // 下载完了，cancelled也要设置
                    canceled = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msgError = new Message();
                    msgError.what = DOWNLOAD_ERROR;
                    mHandler.sendMessage(msgError);
                } finally {
                    if (null != in) {
                        in.close();
                    }
                    if (null != conn) {
                        conn.disconnect();
                    }
                    if (null != fileOut) {
                        fileOut.flush();
                        fileOut.close();
                    }
                }
            } catch (Exception e) {
                Message msgError = new Message();
                msgError.what = DOWNLOAD_ERROR;
                mHandler.sendMessage(msgError);
            }
        }
    };

    private void finishDownload() {
        System.out.println("下载完毕!!!!!!!!!!!");
        // 下载完毕后变换通知形式
        mNotification = mBuilder.build();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotification.contentView = null;
        Intent intent = new Intent(mContext, MainActivity.class);
        // 告知已完成
        intent.putExtra("completed", "yes");
        // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
        PendingIntent contentIntent = PendingIntent.getActivity(
                mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //		mNotification.setLatestEventInfo(mContext, "下载完成",
        //				"文件已下载完毕", contentIntent);
        //
        serviceIsDestroy = true;
        stopSelf();// 停掉服务自身
        mNotificationManager.cancel(NOTIFY_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onclickCancelListener != null) {
            unregisterReceiver(onclickCancelListener);
        }
        // 假如被销毁了，无论如何都默认取消了。
        // app.setDownload(false);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        apkUrl = SaveDate.getInstence(mContext).getApkUrl();// TODO: 2016/10/9  apk URl
        apkUrl = "";// TODO: 2016/10/9  apk URl
        binder = new DownloadBinder();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CANCEL_DOWNLOAD_APK);
        registerReceiver(onclickCancelListener, filter);
    }
    //

    private void startDownloadGo() {
        Toast.makeText(mContext, "开始下载，请稍后。。。", Toast.LENGTH_SHORT).show();
        new Thread() {
            public void run() {
                canceled = false;
                setNotification();
                downloadApk();
            }

            ;
        }.start();
    }

    // 通知栏
    private void setNotification() {
        int icon = R.drawable.icon_app;
        Intent intent = new Intent(this, MainActivity.class);
        //        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        //                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 指定内容意图
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setSmallIcon(icon);
        mNotification = mBuilder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        //        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnableGO);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
        callback.OnBackResult("finish");
    }

    public class DownloadBinder extends Binder {
        public void start() {
            LogUtil.e(apkUrl);
            if (downLoadThread == null || !downLoadThread.isAlive()) {
                progress = 0;
                startDownloadGo();
            }
        }

        public void cancel() {
            canceled = true;
        }

        public int getProgress() {
            return progress;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public boolean serviceIsDestroy() {
            return serviceIsDestroy;
        }

        public void cancelNotification() {
            mHandler.sendEmptyMessage(CANCEL_DOWNLOAD);
        }

        public void addCallback(ICallbackResult callback) {
            DownloadApkService.this.callback = callback;
        }
    }
}
