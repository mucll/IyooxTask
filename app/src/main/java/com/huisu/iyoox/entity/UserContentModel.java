package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/8/22
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class UserContentModel {
    private String name;
    private int study_days;
    private int view_shipin_count;
    private int zuoti_count;
    private int finished_zuoye_count;
    private UserVipModel vip_info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudy_days() {
        return study_days;
    }

    public void setStudy_days(int study_days) {
        this.study_days = study_days;
    }

    public int getView_shipin_count() {
        return view_shipin_count;
    }

    public void setView_shipin_count(int view_shipin_count) {
        this.view_shipin_count = view_shipin_count;
    }

    public int getZuoti_count() {
        return zuoti_count;
    }

    public void setZuoti_count(int zuoti_count) {
        this.zuoti_count = zuoti_count;
    }

    public int getFinished_zuoye_count() {
        return finished_zuoye_count;
    }

    public void setFinished_zuoye_count(int finished_zuoye_count) {
        this.finished_zuoye_count = finished_zuoye_count;
    }

    public UserVipModel getVip_info() {
        return vip_info;
    }

    public void setVip_info(UserVipModel vip_info) {
        this.vip_info = vip_info;
    }
}
