package com.example.modatlas.models;

import java.util.ArrayList;
import java.util.List;

public class DataPackFilter extends FilterManager{
    private List<Category> mainCategory = new ArrayList<>();
    public DataPackFilter() {
        super();
        name = "datapack";
    }

    private void setMainCategory(){
        for (Category c:this.categoryList) {
            if(c.getHeader().equals("categories") && c.getProjectType().equals("mod")){
                mainCategory.add(c);
            }
        }
    }

    @Override
    protected void filterCategory(){
        setMainCategory();
    }

    public List<Category> getMainCategory() {
        return mainCategory;
    }

}
