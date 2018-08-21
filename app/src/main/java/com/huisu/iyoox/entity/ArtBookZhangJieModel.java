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
public class ArtBookZhangJieModel implements Serializable {
    private String zhangjie_name;
    private String cover_map;

    public String getZhangjie_name() {
        return zhangjie_name;
    }

    public void setZhangjie_name(String zhangjie_name) {
        this.zhangjie_name = zhangjie_name;
    }

    public String getCover_map() {
        return cover_map;
    }

    public void setCover_map(String cover_map) {
        this.cover_map = cover_map;
    }
}
