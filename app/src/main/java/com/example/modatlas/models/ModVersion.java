package com.example.modatlas.models;

import java.util.List;

public class ModVersion {
    private List<String> game_versions;
    private List<String> loaders;
    private String version_number;
    private List<ModFile> files;

    public String getVersionNumber() { return version_number; }
    public List<ModFile> getFiles() { return files; }
    public List<String> getLoaders() { return loaders; }
    public List<String> getGameVersions() { return game_versions; }
}

