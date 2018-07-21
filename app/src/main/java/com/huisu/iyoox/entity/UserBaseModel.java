package com.huisu.iyoox.entity;

import java.io.Serializable;

/**
 * Function:
 * Date: 2018/7/21
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class UserBaseModel implements Serializable {
    private String student_name;
    private String avatar;
    private int fenshu;

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFenshu() {
        return fenshu;
    }

    public void setFenshu(int fenshu) {
        this.fenshu = fenshu;
    }
}
