package com.huisu.iyoox.entity;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/10
 */
public class PhoneModel {

    private boolean IsExis;
    private String Message;

    public boolean isExis() {
        return IsExis;
    }

    public void setExis(boolean exis) {
        IsExis = exis;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }
}
