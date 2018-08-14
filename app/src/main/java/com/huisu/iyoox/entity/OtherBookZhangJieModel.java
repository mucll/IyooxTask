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
public class OtherBookZhangJieModel implements Serializable {
    private String zhangjie_name;
    private List<OtherBookZSDModel> zhishidian;

    public String getZhangjie_name() {
        return zhangjie_name;
    }

    public void setZhangjie_name(String zhangjie_name) {
        this.zhangjie_name = zhangjie_name;
    }

    public List<OtherBookZSDModel> getZhishidian() {
        return zhishidian;
    }

    public void setZhishidian(List<OtherBookZSDModel> zhishidian) {
        this.zhishidian = zhishidian;
    }
}
