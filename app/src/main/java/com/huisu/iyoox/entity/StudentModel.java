package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/7/26
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class StudentModel {
    private int student_id;
    private String student_name;
    private String student_avatar;

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

    public String getStudent_avatar() {
        return student_avatar;
    }

    public void setStudent_avatar(String student_avatar) {
        this.student_avatar = student_avatar;
    }
}
