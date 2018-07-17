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
    private String type;
    private ExercisesChooseModel option_One;
    private StudentAnswersModel answersModel;

    public String getDaan() {
        return daan;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
}
