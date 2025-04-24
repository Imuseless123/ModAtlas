package com.example.modatlas.models;

import java.util.ArrayList;
import java.util.List;

public class ModPackFilter extends FilterManager{
    private List<Category> mainCategory = new ArrayList<>();
    private List<Loader> mainLoader = new ArrayList<>();
    public ModPackFilter() {
        super();
        name = "modpack";
    }

    private void setMainCategory(){
        for (Category c:this.categoryList) {
            if(c.getHeader().equals("categories") && c.getProjectType().equals("modpack")){
                mainCategory.add(c);
            }
        }
    }

    private void setMainLoader(){
        for (Loader l:loader) {
            if(l.getSupportedProjectTypes().contains("modpack")){
                mainLoader.add(l);
            }
        }
    }

    @Override
    protected void filterCategory(){
        setMainCategory();
    }

    @Override
    protected void filterLoader(){
        setMainLoader();
    }

    public List<Category> getMainCategory() {
        return mainCategory;
    }

    public List<Loader> getMainLoader() {
        return mainLoader;
    }
}
