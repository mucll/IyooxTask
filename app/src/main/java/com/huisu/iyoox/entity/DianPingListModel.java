package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/8/5
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class DianPingListModel {
    private int work_id;
    private int zhengquelv;
    private int tijiaolv;
    private String end_time;
    private String name;

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }

    public int getZhengquelv() {
        return zhengquelv;
    }

    public void setZhengquelv(int zhengquelv) {
        this.zhengquelv = zhengquelv;
    }

    public int getTijiaolv() {
        return tijiaolv;
    }

    public void setTijiaolv(int tijiaolv) {
        this.tijiaolv = tijiaolv;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
