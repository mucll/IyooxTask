package com.huisu.iyoox.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Function:
 * Date: 2018/9/5
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class GuoxueYishuVodModel implements Serializable {
    private int id;
    private String name;
    private String url;
    private int type;
    private int vip_status;
    private List<GuoxueYishuVodModel> related_vedios;
    private boolean is_shipin_collected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public List<GuoxueYishuVodModel> getRelated_vedios() {
        return related_vedios;
    }

    public void setRelated_vedios(List<GuoxueYishuVodModel> related_vedios) {
        this.related_vedios = related_vedios;
    }

    public boolean isIs_shipin_collected() {
        return is_shipin_collected;
    }

    public void setIs_shipin_collected(boolean is_shipin_collected) {
        this.is_shipin_collected = is_shipin_collected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVip_status() {
        return vip_status;
    }

    public void setVip_status(int vip_status) {
        this.vip_status = vip_status;
    }
}
