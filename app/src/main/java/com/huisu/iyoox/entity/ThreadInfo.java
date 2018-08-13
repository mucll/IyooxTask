package com.huisu.iyoox.entity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by dl on 2017/3/23.
 * 下载实体类
 */

public class ThreadInfo extends LitePalSupport {
    private int id;
    private String url;
    private long end;
    private long start;
    private long finished;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String url, long start, long end, long finished) {
        this.id = id;
        this.url = url;
        this.end = end;
        this.start = start;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }
}
