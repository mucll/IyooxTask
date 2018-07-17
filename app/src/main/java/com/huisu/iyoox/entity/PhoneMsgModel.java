package com.huisu.iyoox.entity;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/10
 */
public class PhoneMsgModel {
    private boolean Is_Send;
    private String SMS_Code;
    private String Message;

    public boolean isIs_Send() {
        return Is_Send;
    }

    public void setIs_Send(boolean is_Send) {
        Is_Send = is_Send;
    }

    public String getSMS_Code() {
        return SMS_Code;
    }

    public void setSMS_Code(String SMS_Code) {
        this.SMS_Code = SMS_Code;
    }

    public String getMessMessage() {
        return Message;
    }

    public void setMessMessage(String messMessage) {
        this.Message = messMessage;
    }
}
