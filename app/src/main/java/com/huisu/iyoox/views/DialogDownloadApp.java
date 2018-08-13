package com.huisu.iyoox.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.service.DownloadApkService;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.TabToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogDownloadApp extends Dialog {


    private final int SUCCESS_DOWN_LOAD = 12;
    private final int CANCEL_DOWNLOAD = 11;
    private final int DOWNLOAD_ERROR = 14;
    private final int SET_PROGRESS = 13;
    private final int URL_ERROR = 15;

    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_desc)
    public TextView tvDesc;
    private Context context;
    private String apkUrl;
    private static final String savePath = DownloadApkService.APK_SAVEPATH;
    private static final String saveFileName = DownloadApkService.APK_SAVEFILENAME;
    private int progress;
    private boolean canceled;
    private int lastRate = 0;
    private Thread downLoadThread;


    public DialogDownloadApp(Context paramContext) {
        super(paramContext);
    }

    public DialogDownloadApp(Context paramContext, int paramInt, String apkUrl) {
        super(paramContext, paramInt);
        this.context = paramContext;
        this.apkUrl = apkUrl;
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.dialog_download_app_view);
        ButterKnife.bind(this);
        downloadApk();
    }

    public void setProgressbarText(int progressbarText) {
        tvDesc.setText("已经下载" + progressbarText + "%");
    }

    public void setProgress(int progress) {
        progressbar.setProgress(progress);
    }


    public void show() {
        super.show();
        Display display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        WindowManager.LayoutParams localLayoutParams = getWindow()
                .getAttributes();
        localLayoutParams.width = width;
        getWindow().setAttributes(localLayoutParams);
    }


    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnableGO);
        downLoadThread.start();
    }

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


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case URL_ERROR:
                    errorDownload();
                    break;
                case SUCCESS_DOWN_LOAD:
                    finishDownload();
                    break;
                case CANCEL_DOWNLOAD:

                    break;
                case DOWNLOAD_ERROR:
                    errorDownload();
                    break;
                case SET_PROGRESS:
                    int rate = msg.arg1;
                    if (rate <= 100) {
                        setProgressbarText(progress);
                        setProgress(progress);
                    }
                    break;
            }
        }
    };

    private void errorDownload() {
        TabToast.makeText(
                context.getResources().getString(R.string.update_version_error),
                context);
        tvDesc.setText(R.string.update_version_error_download);
        tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e(apkUrl);
                downLoadThread = new Thread(mdownApkRunnableGO);
                downLoadThread.start();
            }
        });
    }


    private void finishDownload() {
        tvDesc.setText(R.string.complete_download);
        tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installApk();
            }
        });
        installApk();
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
        context.startActivity(i);
    }
}

