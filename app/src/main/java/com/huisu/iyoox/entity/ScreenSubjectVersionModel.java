package com.huisu.iyoox.entity;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/17
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ScreenSubjectVersionModel {
    private int jiaocai_id;
    private String jiaocai_name;
    private List<ScreenZhiShiDianModel> zhishidian_list;

    public int getJiaocai_id() {
        return jiaocai_id;
    }

    public void setJiaocai_id(int jiaocai_id) {
        this.jiaocai_id = jiaocai_id;
    }

    public String getJiaocai_name() {
        return jiaocai_name;
    }

    public void setJiaocai_name(String jiaocai_name) {
        this.jiaocai_name = jiaocai_name;
    }

    public List<ScreenZhiShiDianModel> getZhishidian_list() {
        return zhishidian_list;
    }

    public void setZhishidian_list(List<ScreenZhiShiDianModel> zhishidian_list) {
        this.zhishidian_list = zhishidian_list;
    }
}
