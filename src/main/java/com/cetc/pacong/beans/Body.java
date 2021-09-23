package com.cetc.pacong.beans;

public class Body {

    private String item;
    private String savePath;
    private Boolean withChild;

    public String getItem() {
        return item == null ? null : item.trim();
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public Boolean getWithChild() {
        return withChild == null ? true : withChild;
    }

    public void setWithChild(Boolean withChild) {
        this.withChild = withChild;
    }
}