package com.example.modatlas.models;

public class FilterHeader extends FilterItem{
    public FilterHeader(String itemName) {
        super(itemName);
        this.setType(false);
        isSelected = false;
        header = itemName;
    }

}
