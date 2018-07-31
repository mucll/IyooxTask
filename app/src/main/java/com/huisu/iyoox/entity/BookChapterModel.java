package com.huisu.iyoox.entity;

import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class BookChapterModel {
    private int zhangjie_id;
    private String name;
    private List<String> zhishidianArr;
    private boolean choiced;

    public int getZhangjie_id() {
        return zhangjie_id;
    }

    public void setZhangjie_id(int zhangjie_id) {
        this.zhangjie_id = zhangjie_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getZhishidianArr() {
        return zhishidianArr;
    }

    public void setZhishidianArr(List<String> zhishidianArr) {
        this.zhishidianArr = zhishidianArr;
    }

    public boolean isChoiced() {
        return choiced;
    }

    public void setChoiced(boolean choiced) {
        this.choiced = choiced;
    }
}
