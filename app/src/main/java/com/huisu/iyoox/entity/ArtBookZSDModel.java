package com.huisu.iyoox.entity;

import java.io.Serializable;

/**
 * Function:
 * Date: 2018/8/13
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ArtBookZSDModel implements Serializable {
    private String name;
    private String url;
    private int zhishidian_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getZhishidian_id() {
        return zhishidian_id;
    }

    public void setZhishidian_id(int zhishidian_id) {
        this.zhishidian_id = zhishidian_id;
    }
}
