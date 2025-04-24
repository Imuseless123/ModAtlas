package com.example.modatlas.models;

import java.util.ArrayList;
import java.util.List;

public class PluginFilter extends FilterManager{
    private List<Category> mainCategory = new ArrayList<>();
    private List<Loader> mainLoader = new ArrayList<>();
    public PluginFilter() {
        super();
        name = "plugin";
    }

    private void setMainCategory(){
        for (Category c:this.categoryList) {
            if(c.getHeader().equals("categories") && c.getProjectType().equals("mod")){
                mainCategory.add(c);
            }
        }
    }

    private void setMainLoader(){
        for (Loader l:loader) {
            if(l.getSupportedProjectTypes().contains("plugin")){
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
