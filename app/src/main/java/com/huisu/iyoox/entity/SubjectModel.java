package com.huisu.iyoox.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/11
 */
public class SubjectModel implements Serializable {
    private int kemu_id;
    private String name;
    private String kemu_name;
    private boolean isSelect;

    public int getKemu_id() {
        return kemu_id;
    }

    public void setKemu_id(int kemu_id) {
        this.kemu_id = kemu_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getKemu_name() {
        return kemu_name;
    }

    public void setKemu_name(String kemu_name) {
        this.kemu_name = kemu_name;
    }
}
