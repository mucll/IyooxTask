package com.huisu.iyoox.entity;

import java.util.List;

/**
 * Function:
 * Date: 2018/9/6
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class MoreCollectModel {
    private List<CollectModel> jt_list;
    private List<CollectModel> zt_list;
    private List<CollectModel> gz_list;

    public List<CollectModel> getJt_list() {
        return jt_list;
    }

    public void setJt_list(List<CollectModel> jt_list) {
        this.jt_list = jt_list;
    }

    public List<CollectModel> getZt_list() {
        return zt_list;
    }

    public void setZt_list(List<CollectModel> zt_list) {
        this.zt_list = zt_list;
    }

    public List<CollectModel> getGz_list() {
        return gz_list;
    }

    public void setGz_list(List<CollectModel> gz_list) {
        this.gz_list = gz_list;
    }
}
