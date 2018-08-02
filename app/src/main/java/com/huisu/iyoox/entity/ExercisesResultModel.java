package com.huisu.iyoox.entity;

import java.io.Serializable;

/**
 * Function:
 * Date: 2018/7/18
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ExercisesResultModel implements Serializable {
    private int timu_id;
    private int is_correct;
    private String chooseanswer;

    public int getTimu_id() {
        return timu_id;
    }

    public void setTimu_id(int timu_id) {
        this.timu_id = timu_id;
    }

    public int getIs_correct() {
        return is_correct;
    }

    public void setIs_correct(int is_correct) {
        this.is_correct = is_correct;
    }

    public String getChooseanswer() {
        return chooseanswer;
    }

    public void setChooseanswer(String chooseanswer) {
        this.chooseanswer = chooseanswer;
    }
}
