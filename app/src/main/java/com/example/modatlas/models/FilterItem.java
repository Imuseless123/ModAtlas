package com.example.modatlas.models;

public class FilterItem {
    protected boolean isTag;
    protected String itemName;
    public FilterItem(String itemName){
        this.itemName = itemName;
    }

    public boolean isTag() {
        return isTag;
    }

    protected void setType(boolean item) {
        isTag = item;
    }

    public String getItemName() {
        return itemName;
    }
}
