package com.huisu.iyoox.entity;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:QustionsAnswers
 * Function:
 * Date: 2017/2/16
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class QustionsAnswers implements Serializable {
    private String qustionId;
    private String questionType;
    private String chooseAnswer;//选择题答案
    private String choiceAnswer = "";//判断题答案
    private String[] blankAnswers;//客观填空题答案
    private List<String> subjectiveAnswerPath = new ArrayList<>();//主观题答案
    private int usedTime;

    public String getChoiceAnswer() {
        return choiceAnswer;
    }

    public void setChoiceAnswer(String choiceAnswer) {
        this.choiceAnswer = choiceAnswer;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }

    public List<String> getSubjectiveAnswerPath() {
        return subjectiveAnswerPath;
    }

    public void setSubjectiveAnswerPath(List<String> subjectiveAnswerPath) {
        this.subjectiveAnswerPath = subjectiveAnswerPath;
    }

    public String[] getBlankAnswers() {
        return blankAnswers;
    }

    public void setBlankAnswers(String[] blankAnswers) {
        this.blankAnswers = blankAnswers;
    }

    public String getQustionId() {
        return qustionId;
    }

    public void setQustionId(String qustionId) {
        this.qustionId = qustionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getChooseAnswer() {
        return chooseAnswer;
    }

    public void setChooseAnswer(String chooseAnswer) {
        this.chooseAnswer = chooseAnswer;
    }

    //是否一个空都没做
    public boolean blanksIsEmpty() {
        if (blankAnswers == null || blankAnswers.length == 0) {
            return true;
        } else {
            for (int i = 0; i < blankAnswers.length; i++) {
                if (!TextUtils.isEmpty(blankAnswers[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 是否有任意空没做
     *
     * @return
     */
    public boolean hasBlankNoDoing() {
        if (blankAnswers == null || blankAnswers.length == 0) {
            return true;
        } else {
            for (int i = 0; i < blankAnswers.length; i++) {
                if (TextUtils.isEmpty(blankAnswers[i])) {
                    return true;
                }
            }
            return false;
        }
    }
}
