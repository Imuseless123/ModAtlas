package com.example.modatlas.models;

public class FilterTag extends FilterItem{
    private boolean isSelected;
    public FilterTag(String itemName) {
        super(itemName);
        this.setType(true);
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
