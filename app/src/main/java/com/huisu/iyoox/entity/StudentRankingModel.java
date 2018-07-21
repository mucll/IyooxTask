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
    private int zhishidian_id;
    private String zhishidian_name;
    private List<UserBaseModel> student_list;

    public int getZhishidian_id() {
        return zhishidian_id;
    }

    public void setZhishidian_id(int zhishidian_id) {
        this.zhishidian_id = zhishidian_id;
    }

    public String getZhishidian_name() {
        return zhishidian_name;
    }

    public void setZhishidian_name(String zhishidian_name) {
        this.zhishidian_name = zhishidian_name;
    }

    public List<UserBaseModel> getStudent_list() {
        return student_list;
    }

    public void setStudent_list(List<UserBaseModel> student_list) {
        this.student_list = student_list;
    }
}
