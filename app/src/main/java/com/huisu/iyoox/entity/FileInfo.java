package com.huisu.iyoox.entity;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by dl on 2017/3/22.
 */
public class FileInfo extends LitePalSupport implements Serializable {
    public static final int LOADING = 1;
    public static final int PAUSE = 2;
    public static final int FINISHED = 3;
    public static final int ERROR = 4;
    public static final int PREPARE = 5;
    private String resId;
    private String fileName;//资源显示的名字
    private String realName;//真实下载后的文件名，下载地址得后缀名
    private String url;
    private long length;
    private int progress;
    private int speed;
    private String subjectName;//科目名称
    private String dirName;//章节名称
    private int finishedCode;
    private boolean isCheck;
    private String finishedTime;
    private String fileType = "";
    private boolean isAddScroe;

    public boolean isAddScroe() {
        return isAddScroe;
    }

    public void setAddScroe(boolean addScroe) {
        isAddScroe = addScroe;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public int getFinishedCode() {
        return finishedCode;
    }

    public void setFinishedCode(int finishedCode) {
        this.finishedCode = finishedCode;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
