package com.example.modatlas.models;

import java.util.List;

public class Modpack {
    private final String name;
    private final String minecraftVersion;
    private final String loader;
    private final String loaderVersion;
    private final List<ModFile> files;

    public Modpack(String name, String minecraftVersion, String loader, String loaderVersion, List<ModFile> files) {
        this.name = name;
        this.minecraftVersion = minecraftVersion;
        this.loader = loader;
        this.loaderVersion = loaderVersion;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public String getLoader() {
        return loader;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }

    public List<ModFile> getFiles() {
        return files;
    }
}
