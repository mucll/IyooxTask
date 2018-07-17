package com.huisu.iyoox.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/11
 */
public class BookEditionModel implements Serializable {
    private int jiaocai_id;
    private String name;
    private String grade_detail_id;
    private boolean isSelect;

    public int getJiaocai_id() {
        return jiaocai_id;
    }

    public void setJiaocai_id(int jiaocai_id) {
        this.jiaocai_id = jiaocai_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade_detail_id() {
        return TextUtils.isEmpty(grade_detail_id) ? "" : grade_detail_id;
    }

    public void setGrade_detail_id(String grade_detail_id) {
        this.grade_detail_id = grade_detail_id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
