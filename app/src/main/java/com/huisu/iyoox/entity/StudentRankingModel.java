package com.huisu.iyoox.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Function:
 * Date: 2018/7/21
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class StudentRankingModel implements Serializable {
    private int id;
    private String name;
    private List<UserBaseModel> student_list;

    public int getId() {
        return id;
    }

    public void setZhishidian_id(int zhishidian_id) {
        this.id = zhishidian_id;
    }

    public String getName() {
        return name;
    }

    public void setZhishidian_name(String zhishidian_name) {
        this.name = zhishidian_name;
    }

    public List<UserBaseModel> getStudent_list() {
        return student_list;
    }

    public void setStudent_list(List<UserBaseModel> student_list) {
        this.student_list = student_list;
    }
}
