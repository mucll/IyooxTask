package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/7/21
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class StudentScoreModel {
    private int work_id;
    private int fenshu;
    private String submit_datetime;

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }

    public int getFenshu() {
        return fenshu;
    }

    public void setFenshu(int fenshu) {
        this.fenshu = fenshu;
    }

    public String getSubmit_datetime() {
        return submit_datetime;
    }

    public void setSubmit_datetime(String submit_datetime) {
        this.submit_datetime = submit_datetime;
    }
}
