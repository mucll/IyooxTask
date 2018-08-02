package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/7/19
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TaskStudentListModel {
    private int zhishidian_id;
    private String zhishidain_name;
    private String start_time;
    private String end_time;
    private String work_name;
    private int work_id;
    private String xueke_name;

    public int getZhishidian_id() {
        return zhishidian_id;
    }

    public void setZhishidian_id(int zhishidian_id) {
        this.zhishidian_id = zhishidian_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getXueke_name() {
        return xueke_name;
    }

    public void setXueke_name(String xueke_name) {
        this.xueke_name = xueke_name;
    }

    public String getZhishidain_name() {
        return zhishidain_name;
    }

    public void setZhishidain_name(String zhishidain_name) {
        this.zhishidain_name = zhishidain_name;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }
}
