package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/8/9
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class LearningCardModel {
    private int id;
    private String tag;
    private String type_name;
    private String end_date;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
