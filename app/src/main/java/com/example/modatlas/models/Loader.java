package com.example.modatlas.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Loader {
    @SerializedName("icon")
    private String icon;

    @SerializedName("name")
    private String name;

    @SerializedName("supported_project_types")
    private List<String> supportedProjectTypes;

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public List<String> getSupportedProjectTypes() {
        return supportedProjectTypes;
    }
}
