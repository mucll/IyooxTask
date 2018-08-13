package com.huisu.iyoox.service;

import android.content.Context;
import android.content.Intent;


import com.huisu.iyoox.entity.FileInfo;
import com.huisu.iyoox.entity.ThreadInfo;
import com.huisu.iyoox.util.DateUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dl on 2017/3/22.
 */

public class DownLoadTask {
    private Context mContext;
    private FileInfo mFileInfo;
    private long mFinished = 0;
    public boolean isPause = false;
    private int threadCount = 1;
    private List<TaskThread> threads = null;
    public static ExecutorService mExecutorService = Executors.newCachedThreadPool();

    public DownLoadTask(Context mContext, FileInfo mFileInfo, int threadCount) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        this.threadCount = threadCount;
    }

    /**
     * 开启下载线程
     */
    public void startTask() {
        this.isPause = false;
        //从数据读取上次下载信息
        List<ThreadInfo> infos = DataSupport.where("url = ?", mFileInfo.getUrl()).find(ThreadInfo.class);
        if (infos.size() == 0) {
            //每个线程要下载的长度
            long length = mFileInfo.getLength() / threadCount;
            for (int i = 0; i < threadCount; i++) {
                ThreadInfo info = new ThreadInfo(i, mFileInfo.getUrl(), length * i, length * (i + 1) - 1, 0);
                //每个线程的结尾是多一个下载的长度-1
                //最后一个有可能除不尽
                if (i == threadCount - 1) {
                    info.setEnd(mFileInfo.getLength());
                }
                infos.add(info);
                //将下载信息插入数据库
                info.save();
            }
        }
        threads = new ArrayList<>();
        //开启线程
        for (ThreadInfo info : infos) {
            mFinished += info.getFinished();
            TaskThread thread = new TaskThread(info);
            DownLoadTask.mExecutorService.execute(thread);
            threads.add(thread);
        }
    }

    private synchronized void checkAllThreadsFinished() {
        boolean allFinished = true;
        //判断所有线程是否都下载完成
        for (TaskThread thread : threads) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        if (allFinished) {
            //下载完成删除数据信息
            DownLoadService.maps.remove(mFileInfo.getResId());
            DataSupport.deleteAll(ThreadInfo.class, "url = ?", mFileInfo.getUrl());
            mFileInfo.setFinishedCode(FileInfo.FINISHED);
            mFileInfo.setProgress(100);
            mFileInfo.setFinishedTime(DateUtils.getPresentDate());
            mFileInfo.updateAll("resId = ? and url = ?", mFileInfo.getResId(), mFileInfo.getUrl());
            Intent intent = new Intent(DownLoadService.ACTION_FINISH);
            intent.putExtra("mFileInfo", mFileInfo);
            mContext.sendBroadcast(intent);
        }
    }

    public class TaskThread extends Thread {
        private ThreadInfo mThreadInfo;
        public boolean isFinished = false;

        public TaskThread(ThreadInfo mDownLoadInfo) {
            this.mThreadInfo = mDownLoadInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            InputStream is = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn .setRequestProperty("Accept-Encoding", "identity");
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                long start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                conn.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());
                //设置文件写入位置
                File file = new File(DownLoadService.DOWNLOAD_URL, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);

                Intent intent = new Intent(DownLoadService.ACTION_UPDATE);
                //开始下载
                if (conn.getResponseCode() == 206) {
                    //读取数据
                    is = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buffer)) != -1) {
                        //暂停
                        if (isPause) {
                            //更新数据库
                            mThreadInfo.updateAll("url = ? and id = ? ", mThreadInfo.getUrl(), mThreadInfo.getId() + "");
                            //保存下载进度
                            mFileInfo.setProgress((int)((mFinished * 100) / mFileInfo.getLength()));
                            //保存下载状态
                            mFileInfo.setFinishedCode(FileInfo.PAUSE);
                            mFileInfo.updateAll("resId = ? and url = ?", mFileInfo.getResId(), mFileInfo.getUrl());
                            return;
                        }
                        //写入文件
                        raf.write(buffer, 0, len);
                        mFinished += len;
                        mThreadInfo.setFinished(mThreadInfo.getFinished() + len);
                        //间隔一秒发送一次减轻UI更新
                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();
                            //把下载进度发送给activity
                            intent.putExtra("finished", (int)((mFinished * 100) / mFileInfo.getLength()));
                            intent.putExtra("id", mFileInfo.getResId());
                            mContext.sendBroadcast(intent);
                        }
                    }
                    isFinished = true;
                    intent.putExtra("finished",(int)((mFinished * 100) / mFileInfo.getLength()));
                    intent.putExtra("id", mFileInfo.getResId());
                    mContext.sendBroadcast(intent);
                    //检查下载任务是否完毕
                    checkAllThreadsFinished();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    raf.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
