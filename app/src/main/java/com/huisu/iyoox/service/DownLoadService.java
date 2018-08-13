package com.huisu.iyoox.service;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.huisu.iyoox.application.MyApplication;
import com.huisu.iyoox.entity.FileInfo;
import com.huisu.iyoox.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dl on 2017/3/22.
 */
public class DownLoadService extends Service {
    //Service状态
    public static final String START_CODE = "START_CODE";//开始
    public static final String STOP_CODE = "STOP_CODE";//暂停
    //广播action
    public static final String ACTION_UPDATE = "ACTION_UPDATE";//更新
    public static final String ACTION_FINISH = "ACTION_FINISH";//完成
    public static final String ACTION_ERROR = "ACTION_ERROR";//错误
    //下载文件路径
    public static final String DOWNLOAD_URL = MyApplication.DOWNLOAD_URL;
    //handler  what
    private static final int MSG_INIT = 0;
    private static final int MSG_ERROR = 1;
    //线程的数量
    private static final int THREAD_COUNT = 3;
    //下载任务的缓存
    public static Map<String, DownLoadTask> maps = new LinkedHashMap<>();//key:resId
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_INIT:
                    //开启线程进行下载
                    FileInfo mFileInfo = (FileInfo) msg.obj;
                    LogUtil.e("test", mFileInfo.isSaved() ? "存过" : "没存过");
                    //litepal这里判断mFileInfo 不存在数据库中 所以只能手动取一下数据再做更新
                    List<FileInfo> sqlDatas = DataSupport.where("url = ?", mFileInfo.getUrl()).find(FileInfo.class);
                    if (sqlDatas != null && sqlDatas.size() > 0) {
                        FileInfo sqlData = sqlDatas.get(0);
                        sqlData.setLength(mFileInfo.getLength());
                        sqlData.setFinishedCode(FileInfo.LOADING);
                        sqlData.updateAll("resId = ? and url = ?", mFileInfo.getResId(), mFileInfo.getUrl());
                    }
                    DownLoadTask mTask = new DownLoadTask(DownLoadService.this, mFileInfo, THREAD_COUNT);
                    mTask.startTask();
                    //把下载任务添加到集合当中
                    maps.put(mFileInfo.getResId(), mTask);
                    break;
                case MSG_ERROR://数据出错的情况
                    Toast.makeText(DownLoadService.this, "该文件下载失败，请与老师联系进行查看", Toast.LENGTH_SHORT).show();
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    fileInfo.setFinishedCode(FileInfo.ERROR);
                    LogUtil.e("test", fileInfo.isSaved() ? "存过" : "没存过");
                    //litepal这里判断mFileInfo 不存在数据库中 所以只能手动取一下数据再做更新
                    List<FileInfo> infos = DataSupport.where("url = ?", fileInfo.getUrl()).find(FileInfo.class);
                    if (infos != null && infos.size() > 0) {
                        FileInfo sqlData = infos.get(0);
                        sqlData.setFinishedCode(FileInfo.ERROR);
                        sqlData.updateAll("resId = ? and url = ?", sqlData.getResId(), sqlData.getUrl());
                    }
                    break;
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        if (START_CODE.equals(intent.getAction())) {
            FileInfo mFileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            //map中存在的话就不再次开启
            if (!maps.containsKey(mFileInfo.getResId())) {
                LogUtil.e("test", "开启线程");
                new MyThread(mFileInfo).start();
            }
        }
        if (STOP_CODE.equals(intent.getAction())) {
            //暂停更新数据库操作在线程中完成。DownloadTask.class
            FileInfo mFileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            //从集合中取出下载任务
            DownLoadTask task = maps.get(mFileInfo.getResId());
            //有可能不存在
            if (task != null) {
                task.isPause = true;
                maps.remove(mFileInfo.getResId());
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        maps = null;
    }
    public class MyThread extends Thread {
        private FileInfo mFileInfo;

        public MyThread(FileInfo mFileInfo) {
            this.mFileInfo = mFileInfo;
        }

        @Override
        public void run() {
            super.run();
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn .setRequestProperty("Accept-Encoding", "identity");
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                int length = -1;
                //获取文件大小
                if (conn.getResponseCode() == 200) {
                    length = conn.getContentLength();
                }
                if (length <= 0) {
                    handler.obtainMessage(MSG_ERROR, mFileInfo).sendToTarget();
                    Intent intent = new Intent(DownLoadService.ACTION_ERROR);
                    intent.putExtra("mFileInfo", mFileInfo);
                    sendBroadcast(intent);
                    return;
                }
                //文件夹不存在的话自己手动创建
                File dir = new File(DOWNLOAD_URL);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                //创建文件并设置文件大小
                File file = new File(dir, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(length);
                mFileInfo.setLength(length);
                handler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (raf != null) {
                        raf.close();
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
