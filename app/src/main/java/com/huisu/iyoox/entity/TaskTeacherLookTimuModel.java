package com.huisu.iyoox.entity;

import java.io.Serializable;

/**
 * Function:
 * Date: 2018/8/5
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TaskTeacherLookTimuModel implements Serializable {
    private int timu_index;
    private String bili;
    private int timu_id;
    private String chooseanswer;
    private int is_correct;

    public int getTimu_index() {
        return timu_index;
    }

    public void setTimu_index(int timu_index) {
        this.timu_index = timu_index;
    }

    public String getBili() {
        return bili;
    }

    public void setBili(String bili) {
        this.bili = bili;
    }

    public int getTimu_id() {
        return timu_id;
    }

    public void setTimu_id(int timu_id) {
        this.timu_id = timu_id;
    }

    public String getChooseanswer() {
        return chooseanswer;
    }

    public void setChooseanswer(String chooseanswer) {
        this.chooseanswer = chooseanswer;
    }

    public int getIs_correct() {
        return is_correct;
    }

    public void setIs_correct(int is_correct) {
        this.is_correct = is_correct;
    }
}
