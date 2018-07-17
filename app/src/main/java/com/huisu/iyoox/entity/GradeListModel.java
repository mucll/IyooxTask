package com.huisu.iyoox.entity;

import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/11
 */
public class GradeListModel {
    private int grade_id;
    private String name;
    private List<SubjectModel> kemuArr;
    private boolean isSelect;

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubjectModel> getKemuArr() {
        return kemuArr;
    }

    public void setKemuArr(List<SubjectModel> kemuArr) {
        this.kemuArr = kemuArr;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
