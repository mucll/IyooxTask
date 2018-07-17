package com.huisu.iyoox.entity;

import java.io.Serializable;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/16
 */
public class StudentAnswersModel implements Serializable {
    private String tiMuId;
    private String chooseAnswer;
    private boolean isCorrect;

    public String getTiMuId() {
        return tiMuId;
    }

    public void setTiMuId(String tiMuId) {
        this.tiMuId = tiMuId;
    }

    public String getChooseAnswer() {
        return chooseAnswer;
    }

    public void setChooseAnswer(String chooseAnswer) {
        this.chooseAnswer = chooseAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
