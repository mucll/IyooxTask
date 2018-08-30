package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/8/24
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class PrePayWeChatEntity {

    private String AppID;
    private String Package;
    private String NonceStr;
    private String Timestamp;
    private String Sign;
    private String PrepayId;
    private String MerchantID;

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public String getNonceStr() {
        return NonceStr;
    }

    public void setNonceStr(String nonceStr) {
        NonceStr = nonceStr;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getPrepayId() {
        return PrepayId;
    }

    public void setPrepayId(String prepayId) {
        PrepayId = prepayId;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }
}
