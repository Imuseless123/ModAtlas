package com.example.modatlas.models;

public class FilterHeader extends FilterItem{
    private boolean isExpanded;
    public FilterHeader(String itemName) {
        super(itemName);
        this.setType(false);
        isExpanded = false;
        header = itemName;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expand) {
        isExpanded = expand;
    }
}
