package com.huisu.iyoox.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Function:
 * Date: 2018/8/13
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ArtBookModel implements Serializable {
    private int grade_id;
    private String grade_name;
    private List<ArtBookZhangJieModel> zhangjie;

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public List<ArtBookZhangJieModel> getZhangjie() {
        return zhangjie;
    }

    public void setZhangjie(List<ArtBookZhangJieModel> zhangjie) {
        this.zhangjie = zhangjie;
    }
}
