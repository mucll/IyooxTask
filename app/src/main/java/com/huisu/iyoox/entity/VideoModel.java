package com.huisu.iyoox.entity;


import android.text.TextUtils;

import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class VideoModel {
    private String zhishidian_name;
    private List<VideoTitleModel> shipinlist;
    private boolean isSelect;
    private String page_count;
    private int zsd_count;


    public String getZhishidian_name() {
        return zhishidian_name;
    }

    public void setZhishidian_name(String zhishidian_name) {
        this.zhishidian_name = zhishidian_name;
    }

    public List<VideoTitleModel> getShipinlist() {
        return shipinlist;
    }

    public void setShipinlist(List<VideoTitleModel> shipinlist) {
        this.shipinlist = shipinlist;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPage_count() {
        return TextUtils.isEmpty(page_count) ? "" : page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public int getZsd_count() {
        return zsd_count;
    }

    public void setZsd_count(int zsd_count) {
        this.zsd_count = zsd_count;
    }
}
