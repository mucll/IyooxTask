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
    private String classroom_name;
    private int correct_rate_avg;
    private int submit_rate;
    private String work_name;
    private String type_name;

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

    public String getClassroom_name() {
        return classroom_name;
    }

    public void setClassroom_name(String classroom_name) {
        this.classroom_name = classroom_name;
    }

    public int getCorrect_rate_avg() {
        return correct_rate_avg;
    }

    public void setCorrect_rate_avg(int correct_rate_avg) {
        this.correct_rate_avg = correct_rate_avg;
    }

    public int getSubmit_rate() {
        return submit_rate;
    }

    public void setSubmit_rate(int submit_rate) {
        this.submit_rate = submit_rate;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
