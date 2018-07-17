package com.huisu.iyoox.entity;

import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class BookDetailsModel {
    private int jiaocai_id;
    private int grade_detail_id;
    private String cover_url;
    private String name;
    private List<BookChapterModel> zhangjielist;

    public int getJiaocai_id() {
        return jiaocai_id;
    }

    public void setJiaocai_id(int jiaocai_id) {
        this.jiaocai_id = jiaocai_id;
    }

    public int getGrade_detail_id() {
        return grade_detail_id;
    }

    public void setGrade_detail_id(int grade_detail_id) {
        this.grade_detail_id = grade_detail_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public List<BookChapterModel> getZhangjielist() {
        return zhangjielist;
    }

    public void setZhangjielist(List<BookChapterModel> zhangjielist) {
        this.zhangjielist = zhangjielist;
    }
}
