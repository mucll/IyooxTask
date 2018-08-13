package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/8/13
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class VersionBean {
    /**
     * versionMixno : 1
     * versionNo : 1
     * downloadUrl : www.aaa.com
     * versionRemark : 颂大知行1.0上线拉
     * versionName : 颂大知行
     */

    private int versionMixno;
    private int versionNo;
    private String downloadUrl;
    private String versionRemark;
    private String versionName;

    public int getVersionMixno() {
        return versionMixno;
    }

    public void setVersionMixno(int versionMixno) {
        this.versionMixno = versionMixno;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionRemark() {
        return versionRemark;
    }

    public void setVersionRemark(String versionRemark) {
        this.versionRemark = versionRemark;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
