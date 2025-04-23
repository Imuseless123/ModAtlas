package com.example.modatlas.models;

public class FilterTag extends FilterItem{
    public FilterTag(String itemName, String header) {
        super(itemName);
        this.setType(true);
        isSelected = false;
        this.header = header;
    }
}
