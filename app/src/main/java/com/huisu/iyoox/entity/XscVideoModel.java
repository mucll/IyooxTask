package com.huisu.iyoox.entity;

import java.util.List;

/**
 * Function:
 * Date: 2018/9/6
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class XscVideoModel {
    private String module_name;
    private List<VideoTitleModel> shipin_list;

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public List<VideoTitleModel> getShipin_list() {
        return shipin_list;
    }

    public void setShipin_list(List<VideoTitleModel> shipin_list) {
        this.shipin_list = shipin_list;
    }
}
