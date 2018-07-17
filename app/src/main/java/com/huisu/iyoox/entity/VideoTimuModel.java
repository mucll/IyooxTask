package com.huisu.iyoox.entity;

import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class VideoTimuModel {
    private int jiaocai_id;
    private int shipin_id;
    private String shipin_name;
    private String shipin_url;
    private List<ExercisesModel> timu_list;

    public int getJiaocai_id() {
        return jiaocai_id;
    }

    public void setJiaocai_id(int jiaocai_id) {
        this.jiaocai_id = jiaocai_id;
    }

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

    public String getShipin_url() {
        return shipin_url;
    }

    public void setShipin_url(String shipin_url) {
        this.shipin_url = shipin_url;
    }

    public List<ExercisesModel> getTimu_list() {
        return timu_list;
    }

    public void setTimu_list(List<ExercisesModel> timu_list) {
        this.timu_list = timu_list;
    }
}
