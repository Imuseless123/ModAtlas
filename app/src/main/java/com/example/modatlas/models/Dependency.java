package com.example.modatlas.models;

public class Dependency {
    private String version_id;
    private String project_id;
    private String file_name;
    private String dependency_type;

    public String getProjectId() {
        return project_id;
    }

    public String getDependencyType() {
        return dependency_type;
    }
}
