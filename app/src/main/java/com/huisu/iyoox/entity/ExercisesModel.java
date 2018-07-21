package com.huisu.iyoox.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/13
 */
public class ExercisesModel implements Serializable {

    private String daan;
    private String fenxi;
    private String jiexi;
    private String kaodian;
    private String tigan;
    private String timu_id;
    private int type;
    private String chooseanswer;
    private int is_correct;
    private ExercisesChooseModel option_One;
    private StudentAnswersModel answersModel;

    public String getDaan() {
        return daan.trim();
    }

    public void setDaan(String daan) {
        this.daan = daan;
    }

    public String getFenxi() {
        return fenxi;
    }

    public void setFenxi(String fenxi) {
        this.fenxi = fenxi;
    }

    public String getJiexi() {
        return jiexi;
    }

    public void setJiexi(String jiexi) {
        this.jiexi = jiexi;
    }

    public String getKaodian() {
        return kaodian;
    }

    public void setKaodian(String kaodian) {
        this.kaodian = kaodian;
    }

    public String getTigan() {
        return tigan;
    }

    public void setTigan(String tigan) {
        this.tigan = tigan;
    }

    public String getTimu_id() {
        return timu_id;
    }

    public void setTimu_id(String timu_id) {
        this.timu_id = timu_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ExercisesChooseModel getOption_One() {
        return option_One;
    }

    public void setOption_One(ExercisesChooseModel option_One) {
        this.option_One = option_One;
    }

    public StudentAnswersModel getAnswersModel() {
        return answersModel;
    }

    public void setAnswersModel(StudentAnswersModel answersModel) {
        this.answersModel = answersModel;
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
