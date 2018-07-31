package com.huisu.iyoox.entity;

import java.io.Serializable;

/**
 * Function:
 * Date: 2018/7/25
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ClassRoomModel implements Serializable {
    private int classroom_id;
    private String classroom_no;
    private String name;
    private int student_num;
    private String create_date;
    private int islcok;
    private int isadmin;
    private boolean isSelect;

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

    public String getClassroom_no() {
        return classroom_no;
    }

    public void setClassroom_no(String classroom_no) {
        this.classroom_no = classroom_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudent_num() {
        return student_num;
    }

    public void setStudent_num(int student_num) {
        this.student_num = student_num;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getLock() {
        return islcok;
    }

    public void setLock(int lock) {
        this.islcok = lock;
    }

    public int getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(int isadmin) {
        this.isadmin = isadmin;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
