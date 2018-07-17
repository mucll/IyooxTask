package com.huisu.iyoox.entity;

import java.io.Serializable;

public class BookBean implements Serializable {
    private String bookName;
    private boolean isSelect;
    private String type;

    public BookBean() {
    }

    public BookBean(String bookName, boolean isSelect) {
        this.bookName = bookName;
        this.isSelect = isSelect;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
