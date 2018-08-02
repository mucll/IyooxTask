package com.huisu.iyoox.entity;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/21
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ClassRankingModel {
    private String classroom_name;
    private int classroom_id;
    private List<TeacherModel> teacher_list;
    private List<StudentScoreModel> student_fenshu_list;
    private List<StudentRankingModel> zhishidian_list;

    public List<TeacherModel> getTeacher_list() {
        return teacher_list;
    }

    public void setTeacher_list(List<TeacherModel> teacher_list) {
        this.teacher_list = teacher_list;
    }

    public List<StudentScoreModel> getStudent_fenshu_list() {
        return student_fenshu_list;
    }

    public void setStudent_fenshu_list(List<StudentScoreModel> student_fenshu_list) {
        this.student_fenshu_list = student_fenshu_list;
    }

    public List<StudentRankingModel> getZhishidian_list() {
        return zhishidian_list;
    }

    public void setZhishidian_list(List<StudentRankingModel> zhishidian_list) {
        this.zhishidian_list = zhishidian_list;
    }

    public String getClassroom_name() {
        return classroom_name;
    }

    public void setClassroom_name(String classroom_name) {
        this.classroom_name = classroom_name;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }
}
