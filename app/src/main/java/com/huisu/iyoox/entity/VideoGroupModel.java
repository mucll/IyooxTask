package com.huisu.iyoox.entity;

import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class VideoGroupModel {
    private int zhangjie_id;
    private String zhangjie_name;
    private List<VideoModel> zhishidian;

    public int getZhangjie_id() {
        return zhangjie_id;
    }

    public void setZhangjie_id(int zhangjie_id) {
        this.zhangjie_id = zhangjie_id;
    }

    public String getZhangjie_name() {
        return zhangjie_name;
    }

    public void setZhangjie_name(String zhangjie_name) {
        this.zhangjie_name = zhangjie_name;
    }

    public List<VideoModel> getZhishidian() {
        return zhishidian;
    }

    public void setZhishidian(List<VideoModel> zhishidian) {
        this.zhishidian = zhishidian;
    }
}
