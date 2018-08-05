package com.huisu.iyoox.entity;

import java.io.Serializable;

/**
 * Function:
 * Date: 2018/8/5
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TaskTeacherLookStudentModel implements Serializable {
    private int paiming;
    private int student_id;
    private String student_name;
    private int zhengquelv;
    private String avatar;

    public int getPaiming() {
        return paiming;
    }

    public void setPaiming(int paiming) {
        this.paiming = paiming;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public int getZhengquelv() {
        return zhengquelv;
    }

    public void setZhengquelv(int zhengquelv) {
        this.zhengquelv = zhengquelv;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
