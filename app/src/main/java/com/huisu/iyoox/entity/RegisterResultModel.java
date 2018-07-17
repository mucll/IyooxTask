package com.huisu.iyoox.entity;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/10
 */
public class RegisterResultModel {
    private boolean IsRegister;
    private String Message;
    private String UserId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public boolean isRegister() {
        return IsRegister;
    }

    public void setRegister(boolean register) {
        IsRegister = register;
    }

    public String getMessMessage() {
        return Message;
    }

    public void setMessMessage(String messMessage) {
        this.Message = messMessage;
    }
}
