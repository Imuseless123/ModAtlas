package com.example.modatlas.models;

import java.util.ArrayList;
import java.util.List;

public class ShaderFilter extends FilterManager{
    private List<Category> mainCategory = new ArrayList<>();
    private List<Category> feature = new ArrayList<>();
    private List<Category> performanceImpact = new ArrayList<>();
    private List<Loader> mainLoader = new ArrayList<>();

    public ShaderFilter(){
        super();
        name = "shader";
    }

    private void setMainCategory(){
        for (Category c:categoryList) {
            if (c.getProjectType().equals("shader")){
                if (c.getHeader().equals("categories")){
                    mainCategory.add(c);
                } else if (c.getHeader().equals("features")){
                    feature.add(c);
                } else if (c.getHeader().equals("performance impact")) {
                    performanceImpact.add(c);
                }
            }
        }
    }

    private void setMainLoader(){
        for (Loader l:loader) {
            if(l.getSupportedProjectTypes().contains("shader")){
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

    public List<Category> getFeature() {
        return feature;
    }

    public List<Category> getPerformanceImpact() {
        return performanceImpact;
    }

    public List<Loader> getMainLoader() {
        return mainLoader;
    }
}
