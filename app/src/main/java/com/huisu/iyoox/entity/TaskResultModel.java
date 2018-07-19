package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/7/18
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TaskResultModel {
    private int student_id;
    private String times;
    private String zhishidian_name;
    private int correct_rate;
    private int nanyi_level;
    private String zhangwo_level;
    private String suggest;
    private String sort_rate;
    private int timu_count;

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getZhishidian_name() {
        return zhishidian_name;
    }

    public void setZhishidian_name(String zhishidian_name) {
        this.zhishidian_name = zhishidian_name;
    }

    public int getCorrect_rate() {
        return correct_rate;
    }

    public void setCorrect_rate(int correct_rate) {
        this.correct_rate = correct_rate;
    }

    public int getNanyi_level() {
        return nanyi_level;
    }

    public void setNanyi_level(int nanyi_level) {
        this.nanyi_level = nanyi_level;
    }

    public String getZhangwo_level() {
        return zhangwo_level;
    }

    public void setZhangwo_level(String zhangwo_level) {
        this.zhangwo_level = zhangwo_level;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getSort_rate() {
        return sort_rate;
    }

    public void setSort_rate(String sort_rate) {
        this.sort_rate = sort_rate;
    }

    public int getTimu_count() {
        return timu_count;
    }

    public void setTimu_count(int timu_count) {
        this.timu_count = timu_count;
    }
}
