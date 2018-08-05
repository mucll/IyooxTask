package com.huisu.iyoox.entity;

import java.io.Serializable;

/**
 * Function:
 * Date: 2018/8/5
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TaskTeacherLookWorkModel implements Serializable {
    private String name;
    private int zhunshilv;
    private int zhengqulv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getZhunshilv() {
        return zhunshilv;
    }

    public void setZhunshilv(int zhunshilv) {
        this.zhunshilv = zhunshilv;
    }

    public int getZhengqulv() {
        return zhengqulv;
    }

    public void setZhengqulv(int zhengqulv) {
        this.zhengqulv = zhengqulv;
    }
}
