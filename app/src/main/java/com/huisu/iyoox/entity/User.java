package com.huisu.iyoox.entity;


import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private int user_id;
    private String name;
    private String phone;
    private String email;
    private int sex;
    private int isvalid;
    private int grade;
    private int classroom_id;
    private String classroom_name;
    private int xueke_id;
    private int jiaocai_id;
    private int grade_detail_id;
    private String jiaocai_name;
    private int type;

    public String getUserId() {
        return user_id + "";
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(int isvalid) {
        this.isvalid = isvalid;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

    public String getClassroom_name() {
        return classroom_name;
    }

    public void setClassroom_name(String classroom_name) {
        this.classroom_name = classroom_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getXueke_id() {
        return xueke_id;
    }

    public void setXueke_id(int xueke_id) {
        this.xueke_id = xueke_id;
    }

    public int getJiaocai_id() {
        return jiaocai_id;
    }

    public void setJiaocai_id(int jiaocai_id) {
        this.jiaocai_id = jiaocai_id;
    }

    public int getGrade_detail_id() {
        return grade_detail_id;
    }

    public void setGrade_detail_id(int grade_detail_id) {
        this.grade_detail_id = grade_detail_id;
    }

    public String getJiaocai_name() {
        return jiaocai_name;
    }

    public void setJiaocai_name(String jiaocai_name) {
        this.jiaocai_name = jiaocai_name;
    }

    public String getGradeName() {
        switch (grade) {
            case 1:
                return "一年级";
            case 2:
                return "二年级";
            case 3:
                return "三年级";
            case 4:
                return "四年级";
            case 5:
                return "五年级";
            case 6:
                return "六年级";
            case 7:
                return "七年级";
            case 8:
                return "八年级";
            case 9:
                return "九年级";
            default:
                return "一年级";
        }
    }

    public String getVersionName() {
        switch (grade_detail_id) {
            case 0:
                return jiaocai_name;
            case 1:
                return jiaocai_name + "上册";
            case 2:
                return jiaocai_name + "下册";
        }
        return "";
    }
}
