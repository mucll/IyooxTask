package com.huisu.iyoox.entity;

public class SexBean {
    private String sexCode;
    private boolean isSelect;

    public SexBean() {
    }

    public SexBean(String sexCode, boolean isSelect) {
        this.sexCode = sexCode;
        this.isSelect = isSelect;
    }

    public String getSexCode() {
        return sexCode;
    }

    public void setSexCode(String sexCode) {
        this.sexCode = sexCode;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
