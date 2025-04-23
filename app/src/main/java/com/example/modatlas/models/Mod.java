package com.example.modatlas.models;

import com.example.modatlas.views.LoadState;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Mod {
    @SerializedName("project_id")
    private String projectId;

    @SerializedName("slug")
    private String slug;
    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("versions")
    private List<String> versions;

    @SerializedName("downloads")
    private int downloads;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("client_side")
    private String clientSide;

    @SerializedName("server_side")
    private String serverSide;

    private LoadState importState = LoadState.READY; // Default state

    // Add a getter and setter for importState
    public LoadState getImportState() {
        return importState;
    }

    public void setImportState(LoadState importState) {
        this.importState = importState;
    }
    // Getters
    public String getProjectId() { return projectId; }
    public String getSlug() { return slug; }
    public String getAuthor() { return author;}
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getCategories() { return categories; }
    public List<String> getVersions() { return versions; }
    public int getDownloads() { return downloads; }
    public String getIconUrl() { return iconUrl; }
    public String getClientSide() { return clientSide; }
    public String getServerSide() { return serverSide; }
}
