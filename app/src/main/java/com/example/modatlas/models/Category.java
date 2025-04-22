package com.example.modatlas.models;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("icon")
    private String icon;

    @SerializedName("name")
    private String name;

    @SerializedName("project_type")
    private String projectType;

    @SerializedName("header")
    private boolean header;

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getProjectType() {
        return projectType;
    }

    public boolean isHeader() {
        return header;
    }
}
