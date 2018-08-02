package com.huisu.iyoox.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Function:
 * Date: 2018/8/2
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class HomeWorkResultModel implements Serializable {

    private int work_id;
    private String work_name;
    private int classroom_id;
    private int zhishidian_id;
    private String zhishidian_name;
    private int xueke_id;
    private String xueke_name;
    private int timu_count;
    private String times;
    private int status;
    private int zhengquelv;
    private int zhengquelv_avg;
    private List<ExercisesResultModel> answer_detail;
    private String dianping;
    private String start_time;
    private String end_time;

    public int getWork_id() {
        return work_id;
    }

    public void setWork_id(int work_id) {
        this.work_id = work_id;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

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

    public int getXueke_id() {
        return xueke_id;
    }

    public void setXueke_id(int xueke_id) {
        this.xueke_id = xueke_id;
    }

    public String getXueke_name() {
        return xueke_name;
    }

    public void setXueke_name(String xueke_name) {
        this.xueke_name = xueke_name;
    }

    public int getTimu_count() {
        return timu_count;
    }

    public void setTimu_count(int timu_count) {
        this.timu_count = timu_count;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getZhengquelv() {
        return zhengquelv;
    }

    public void setZhengquelv(int zhengquelv) {
        this.zhengquelv = zhengquelv;
    }

    public int getZhengquelv_avg() {
        return zhengquelv_avg;
    }

    public void setZhengquelv_avg(int zhengquelv_avg) {
        this.zhengquelv_avg = zhengquelv_avg;
    }

    public List<ExercisesResultModel> getAnswer_detail() {
        return answer_detail;
    }

    public void setAnswer_detail(List<ExercisesResultModel> answer_detail) {
        this.answer_detail = answer_detail;
    }

    public String getDianping() {
        return dianping;
    }

    public void setDianping(String dianping) {
        this.dianping = dianping;
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
}
