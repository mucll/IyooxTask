package com.huisu.iyoox.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Function:
 * Date: 2018/8/5
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TaskTeacherLookClassModel implements Serializable {
    private TaskTeacherLookWorkModel work;
    private List<TaskTeacherLookStudentModel> yiwancheng;
    private List<TaskTeacherLookStudentModel> weiwancheng;
    private List<TaskTeacherLookTimuModel> tongji;

    public List<TaskTeacherLookStudentModel> getYiwancheng() {
        return yiwancheng;
    }

    public void setYiwancheng(List<TaskTeacherLookStudentModel> yiwancheng) {
        this.yiwancheng = yiwancheng;
    }

    public List<TaskTeacherLookStudentModel> getWeiwancheng() {
        return weiwancheng;
    }

    public void setWeiwancheng(List<TaskTeacherLookStudentModel> weiwancheng) {
        this.weiwancheng = weiwancheng;
    }

    public TaskTeacherLookWorkModel getWork() {
        return work;
    }

    public void setWork(TaskTeacherLookWorkModel work) {
        this.work = work;
    }

    public List<TaskTeacherLookTimuModel> getTongji() {
        return tongji;
    }

    public void setTongji(List<TaskTeacherLookTimuModel> tongji) {
        this.tongji = tongji;
    }
}
