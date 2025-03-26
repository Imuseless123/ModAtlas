package com.example.modatlas.models;

import java.util.List;

public class ModVersionDetail {
    private List<String> game_versions;
    private List<String> loaders;
    private String id;
    private String project_id;
    private List<Dependency> dependencies;

    public List<Dependency> getDependencies() {
        return dependencies;
    }
}
