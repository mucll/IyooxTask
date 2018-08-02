package com.huisu.iyoox.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class VideoTitleModel extends LitePalSupport implements Serializable {
    private int shipin_id;
    private String shipin_name;
    private int zhishidian_id;

    public int getShipin_id() {
        return shipin_id;
    }

    public void setShipin_id(int shipin_id) {
        this.shipin_id = shipin_id;
    }

    public String getShipin_name() {
        return shipin_name;
    }

    public void setShipin_name(String shipin_name) {
        this.shipin_name = shipin_name;
    }

    public int getZhishidian_id() {
        return zhishidian_id;
    }

    public void setZhishidian_id(int zhishidian_id) {
        this.zhishidian_id = zhishidian_id;
    }
}
