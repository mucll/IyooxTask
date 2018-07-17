package com.huisu.iyoox.entity;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/10
 */
public class CheckMsgCode {
    private boolean Is_Check;
    private String Message;

    public boolean isIs_Check() {
        return Is_Check;
    }

    public void setIs_Check(boolean is_Check) {
        Is_Check = is_Check;
    }

    public String getMessMessage() {
        return Message;
    }

    public void setMessMessage(String messMessage) {
        this.Message = messMessage;
    }
}
