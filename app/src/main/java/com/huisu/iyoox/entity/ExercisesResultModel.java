package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/7/18
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ExercisesResultModel {
    private int timu_id;
    private int is_correct;

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
}
