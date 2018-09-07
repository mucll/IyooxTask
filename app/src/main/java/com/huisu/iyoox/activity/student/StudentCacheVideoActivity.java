package com.huisu.iyoox.activity.student;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.aliyun.vodplayer.downloader.AliyunDownloadConfig;
import com.aliyun.vodplayer.downloader.AliyunDownloadInfoListener;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.downloader.AliyunRefreshPlayAuthCallback;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.activity.videoplayer.ALiYunVideoPlayActivity;
import com.huisu.iyoox.alivideo.constants.PlayParameter;
import com.huisu.iyoox.alivideo.download.AlivcDialog;
import com.huisu.iyoox.alivideo.download.AlivcDownloadMediaInfo;
import com.huisu.iyoox.alivideo.download.DownloadBlackView;
import com.huisu.iyoox.alivideo.download.DownloadDataProvider;
import com.huisu.iyoox.alivideo.download.DownloadView;
import com.huisu.iyoox.alivideo.utils.Commen;
import com.huisu.iyoox.alivideo.utils.PlayAuthUtil;
import com.huisu.iyoox.constant.Constant;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 学生缓存视频界面
 */
public class StudentCacheVideoActivity extends BaseActivity {

    private Commen commenUtils;
    private AliyunDownloadConfig config;
    private AliyunDownloadManager downloadManager;
    private DownloadDataProvider downloadDataProvider;
    private DownloadBlackView downloadView;
    private PlayerHandler playerHandler;

    List<AliyunDownloadMediaInfo> aliyunDownloadMediaInfoList;

    private static final String DOWNLOAD_ERROR_KEY = "error_key";
    private static final int DOWNLOAD_ERROR = 1;


    @Override
    protected void initView() {
        downloadView = findViewById(R.id.cache_download_view);
    }

    @Override
    protected void initData() {
        setTitle("缓存");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_cache;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, StudentCacheVideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void copyAssets() {
        commenUtils = Commen.getInstance(getApplicationContext()).copyAssetsToSD("encrypt", "aliyun");
        commenUtils.setFileOperateCallback(

                new Commen.FileOperateCallback() {
                    @Override
                    public void onSuccess() {
                        config = new AliyunDownloadConfig();
                        config.setSecretImagePath(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/aliyun/encryptedApp.dat");
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/iyoox_save/");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        config.setDownloadDir(file.getAbsolutePath());
                        //设置同时下载个数
                        config.setMaxNums(2);
                        // 获取AliyunDownloadManager对象
                        downloadManager = AliyunDownloadManager.getInstance(getApplicationContext());
                        downloadManager.setDownloadConfig(config);

                        downloadDataProvider = DownloadDataProvider.getSingleton(getApplicationContext());
                        // 更新sts回调
                        downloadManager.setRefreshAuthCallBack(new MyRefreshPlayAuthCallback());
                        // 视频下载的回调
                        downloadManager.setDownloadInfoListener(new MyDownloadInfoListener(downloadView));
                        //
                        downloadViewSetting(downloadView);
                    }

                    @Override
                    public void onFailed(String error) {
                    }
                });
    }

    private static class MyRefreshPlayAuthCallback implements AliyunRefreshPlayAuthCallback {

        @Override
        public AliyunPlayAuth refreshPlayAuth(String vid, String quality, String format, String title, boolean encript) {
            //NOTE: 注意：这个不能启动线程去请求。因为这个方法已经在线程中调用了。
            String playauth = PlayAuthUtil.getPlayAuth(vid);
            if (playauth == null) {
                return null;
            }
            AliyunPlayAuth.AliyunPlayAuthBuilder authBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
            authBuilder.setPlayAuth(playauth);
            authBuilder.setVid(vid);
            authBuilder.setTitle(title);
            authBuilder.setQuality(quality);
            authBuilder.setFormat(format);
            authBuilder.setIsEncripted(encript ? 1 : 0);
            return authBuilder.build();
        }
    }

    private class MyDownloadInfoListener implements AliyunDownloadInfoListener {

        private DownloadBlackView downloadView;

        public MyDownloadInfoListener(DownloadBlackView downloadView) {
            this.downloadView = downloadView;
        }


        @Override
        public void onPrepared(List<AliyunDownloadMediaInfo> infos) {
            Collections.sort(infos, new Comparator<AliyunDownloadMediaInfo>() {
                @Override
                public int compare(AliyunDownloadMediaInfo mediaInfo1, AliyunDownloadMediaInfo mediaInfo2) {
                    if (mediaInfo1.getSize() > mediaInfo2.getSize()) {
                        return 1;
                    }
                    if (mediaInfo1.getSize() < mediaInfo2.getSize()) {
                        return -1;
                    }

                    if (mediaInfo1.getSize() == mediaInfo2.getSize()) {
                        return 0;
                    }
                    return 0;
                }
            });
            onDownloadPrepared(infos);
        }

        @Override
        public void onStart(AliyunDownloadMediaInfo info) {
            Toast.makeText(StudentCacheVideoActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
            //downloadView.addDownloadMediaInfo(info);
            //dbHelper.insert(info, DownloadDBHelper.DownloadState.STATE_DOWNLOADING);
            if (!downloadDataProvider.hasAdded(info)) {
                downloadDataProvider.addDownloadMediaInfo(info);
            }

        }

        @Override
        public void onProgress(AliyunDownloadMediaInfo info, int percent) {
            downloadView.updateInfo(info);
            if (downloadView != null) {
                downloadView.updateInfo(info);
            }
        }

        @Override
        public void onStop(AliyunDownloadMediaInfo info) {
            Log.d("yds100", "onStop");
            downloadView.updateInfo(info);
            //dbHelper.update(info, DownloadDBHelper.DownloadState.STATE_PAUSE);
        }

        @Override
        public void onCompletion(AliyunDownloadMediaInfo info) {
            Log.d("yds100", "onCompletion");
            downloadView.updateInfoByComplete(info);
            downloadDataProvider.addDownloadMediaInfo(info);
            //aliyunDownloadMediaInfoList.remove(info);
        }

        @Override
        public void onError(AliyunDownloadMediaInfo info, int code, String msg, String requestId) {
            Log.d("yds100", "onError" + msg);
            downloadView.updateInfoByError(info);
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(DOWNLOAD_ERROR_KEY, msg);
            message.setData(bundle);
            message.what = DOWNLOAD_ERROR;
            playerHandler = new StudentCacheVideoActivity.PlayerHandler(StudentCacheVideoActivity.this);
            playerHandler.sendMessage(message);
        }

        @Override
        public void onWait(AliyunDownloadMediaInfo outMediaInfo) {
            Log.d("yds100", "onWait");
        }

        @Override
        public void onM3u8IndexUpdate(AliyunDownloadMediaInfo outMediaInfo, int index) {
            Log.d("yds100", "onM3u8IndexUpdate");
        }

    }

    /**
     * downloadView的配置 里面配置了需要下载的视频的信息, 事件监听等 抽取该方法的主要目的是, 横屏下download dialog的离线视频列表中也用到了downloadView, 而两者显示内容和数据是同步的,
     * 所以在此进行抽取 StudentCacheVideoActivity.class#showAddDownloadView(DownloadVie view)中使用
     *
     * @param downloadView
     */
    private void downloadViewSetting(final DownloadBlackView downloadView) {
        downloadView.addAllDownloadMediaInfo(downloadDataProvider.getAllDownloadMediaInfo());

        downloadView.setOnDownloadViewListener(new DownloadBlackView.OnDownloadViewListener() {
            @Override
            public void onStop(AliyunDownloadMediaInfo downloadMediaInfo) {
                downloadManager.stopDownloadMedia(downloadMediaInfo);
            }

            @Override
            public void onStart(AliyunDownloadMediaInfo downloadMediaInfo) {
                downloadManager.startDownloadMedia(downloadMediaInfo);
            }

            @Override
            public void onDeleteDownloadInfo(final ArrayList<AlivcDownloadMediaInfo> alivcDownloadMediaInfos) {
                // 视频删除的dialog
                final AlivcDialog alivcDialog = new AlivcDialog(StudentCacheVideoActivity.this);
                alivcDialog.setDialogIcon(R.drawable.icon_delete_tips);
                alivcDialog.setMessage(getResources().getString(R.string.alivc_delete_confirm));
                alivcDialog.setOnConfirmclickListener(getResources().getString(R.string.alivc_dialog_sure),
                        new AlivcDialog.onConfirmClickListener() {
                            @Override
                            public void onConfirm() {
                                alivcDialog.dismiss();
                                if (alivcDownloadMediaInfos != null && alivcDownloadMediaInfos.size() > 0) {
                                    downloadView.deleteDownloadInfo();
                                    downloadDataProvider.deleteAllDownloadInfo(alivcDownloadMediaInfos);
                                } else {
                                    Toast.makeText(StudentCacheVideoActivity.this, "没有删除的视频选项...", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
                alivcDialog.setOnCancelOnclickListener(getResources().getString(R.string.alivc_dialog_cancle),
                        new AlivcDialog.onCancelOnclickListener() {
                            @Override
                            public void onCancel() {
                                alivcDialog.dismiss();
                            }
                        });
                alivcDialog.show();
            }
        });
        //下载完成后点击事件
        downloadView.setOnDownloadedItemClickListener(new DownloadBlackView.OnDownloadItemClickListener() {
            @Override
            public void onDownloadedItemClick(int positin) {

                ArrayList<AliyunDownloadMediaInfo> downloadedList = downloadDataProvider.getAllDownloadMediaInfo();
                //// 存入顺序和显示顺序相反,  所以进行倒序

                ArrayList<AliyunDownloadMediaInfo> tempList = new ArrayList<>();
                int size = downloadedList.size();
                for (int i = 0; i < size; i++) {
                    if (downloadedList.get(i).getProgress() == 100) {
                        tempList.add(downloadedList.get(i));
                    }
                }

                Collections.reverse(tempList);
                tempList.add(downloadedList.get(downloadedList.size() - 1));
                for (int i = 0; i < size; i++) {
                    AliyunDownloadMediaInfo downloadMediaInfo = downloadedList.get(i);
                    if (!tempList.contains(downloadMediaInfo)) {
                        tempList.add(downloadMediaInfo);
                    }
                }

                if (positin < 0) {
                    Toast.makeText(StudentCacheVideoActivity.this, "视频资源不存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO 如果点击列表中的视频, 需要将类型改为vid
                AliyunDownloadMediaInfo aliyunDownloadMediaInfo = tempList.get(positin);
                PlayParameter.PLAY_PARAM_TYPE = "localSource";
                if (aliyunDownloadMediaInfo != null) {
                    Intent intent = new Intent(context, ALiYunVideoPlayActivity.class);
                    intent.putExtra("type", Constant.PLAY_PARAM_URL_TYPE);
                    intent.putExtra("url", aliyunDownloadMediaInfo.getSavePath());
                    intent.putExtra("title", aliyunDownloadMediaInfo.getTitle());
                    startActivity(intent);
                }

            }

            @Override
            public void onDownloadingItemClick(ArrayList<AlivcDownloadMediaInfo> infos, int position) {
                AlivcDownloadMediaInfo alivcInfo = infos.get(position);
                AliyunDownloadMediaInfo aliyunDownloadInfo = alivcInfo.getAliyunDownloadMediaInfo();
                AliyunDownloadMediaInfo.Status status = aliyunDownloadInfo.getStatus();
                if (status == AliyunDownloadMediaInfo.Status.Error || status == AliyunDownloadMediaInfo.Status.Wait) {
                    //downloadManager.removeDownloadMedia(aliyunDownloadInfo);
                    downloadManager.startDownloadMedia(aliyunDownloadInfo);
                }
            }

        });
    }

    private void onDownloadPrepared(List<AliyunDownloadMediaInfo> infos) {
        aliyunDownloadMediaInfoList = new ArrayList<>();
        aliyunDownloadMediaInfoList.addAll(infos);
    }

    private static class PlayerHandler extends Handler {
        //持有弱引用StudentCacheVideoActivity,GC回收时会被回收掉.
        private final WeakReference<StudentCacheVideoActivity> mActivty;

        public PlayerHandler(StudentCacheVideoActivity activity) {
            mActivty = new WeakReference<StudentCacheVideoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            StudentCacheVideoActivity activity = mActivty.get();
            super.handleMessage(msg);
            if (activity != null) {
                switch (msg.what) {
                    case DOWNLOAD_ERROR:
                        //Toast.makeText(mActivty.get(), msg.getData().getString(DOWNLOAD_ERROR_KEY), Toast.LENGTH_LONG)
                        //    .show();
                        Log.d("donwload", msg.getData().getString(DOWNLOAD_ERROR_KEY));
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
