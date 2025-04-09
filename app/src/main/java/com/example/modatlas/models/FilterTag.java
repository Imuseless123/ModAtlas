package com.example.modatlas.models;

public class FilterTag extends FilterItem{
    private boolean isSelected;
    private String header;
    public FilterTag(String itemName, String header) {
        super(itemName);
        this.setType(true);
        isSelected = false;
        this.header = header;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getHeader() {
        return header;
    }
}
