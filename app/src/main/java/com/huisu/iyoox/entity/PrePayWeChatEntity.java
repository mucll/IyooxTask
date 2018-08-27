package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/8/24
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class PrePayWeChatEntity {

    private String appid;
    private String partnerid;
    private String prepayid;
    private String pkgstr;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPkgstr() {
        return pkgstr;
    }

    public void setPkgstr(String pkgstr) {
        this.pkgstr = pkgstr;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
