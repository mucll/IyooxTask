package com.huisu.iyoox.entity;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/10
 */
public class SendMsgCodeModel {
    private boolean Is_Send;
    private String Message;

    public boolean isIs_Send() {
        return Is_Send;
    }

    public void setIs_Send(boolean is_Send) {
        Is_Send = is_Send;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
