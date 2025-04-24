package com.example.modatlas.models;

import java.util.ArrayList;
import java.util.List;

public class ResourcePackFilter extends FilterManager{
    private List<Category> mainCategory = new ArrayList<>();
    private List<Category> feature = new ArrayList<>();
    private List<Category> resolution = new ArrayList<>();

    public ResourcePackFilter(){
        super();
        name = "resourcepack";
    }

    private void setMainCategory(){
        for (Category c:categoryList) {
            if (c.getProjectType().equals("resourcepack")){
                if (c.getHeader().equals("categories")){
                    mainCategory.add(c);
                } else if (c.getHeader().equals("features")){
                    feature.add(c);
                } else if (c.getHeader().equals("resolutions")) {
                    resolution.add(c);
                }
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

    public List<Category> getFeature() {
        return feature;
    }

    public List<Category> getResolution() {
        return resolution;
    }
}
