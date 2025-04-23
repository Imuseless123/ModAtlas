package com.example.modatlas.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ModFilter extends FilterManager{
    private List<Category> mainCategory = new ArrayList<>();
    private List<Loader> mainLoader = new ArrayList<>();
    public ModFilter() {
        super();
        name = "mod";
    }

    private void setMainCategory(){
        for (Category c:this.categoryList) {
            if(c.getHeader().equals("categories") && c.getProjectType().equals("mod")){
//                Log.i("test",c.getName());
                mainCategory.add(c);
            }
        }
    }

    private void setMainLoader(){
        for (Loader l:loader) {
            if(l.getSupportedProjectTypes().contains("mod")){
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

    public String getName() {
        return name;
    }
}
