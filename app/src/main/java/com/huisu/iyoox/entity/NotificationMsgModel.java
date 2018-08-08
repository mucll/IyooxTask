package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/8/8
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class NotificationMsgModel {
    //    		"id": 62,
//                    "sender_id": 0,
//                    "message_type": 1,
//                    "receiver_id": 0,
//                    "body": null,
//                    "data": null,
//                    "createdate": "2018-08-07T18:23:28.6",
//                    "handledate": null,
//                    "status": 1
    private int id;
    private int sender_id;
    private int message_type;
    private int receiver_id;
    private String body;
    private String data;
    private String createdate;
    private int status;
    private String handledate;
    private int message_business_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHandledate() {
        return handledate;
    }

    public void setHandledate(String handledate) {
        this.handledate = handledate;
    }

    public int getMessage_action() {
        return message_business_type;
    }

    public void setMessage_action(int message_action) {
        this.message_business_type = message_action;
    }
}
