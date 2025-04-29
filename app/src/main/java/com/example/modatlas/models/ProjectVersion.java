package com.example.modatlas.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProjectVersion {
    @SerializedName("game_versions")
    private List<String> gameVersions;

    @SerializedName("loaders")
    private List<String> loaders;

    @SerializedName("id")
    private String id;

    @SerializedName("project_id")
    private String projectId;

    @SerializedName("author_id")
    private String authorId;

    @SerializedName("featured")
    private boolean featured;

    @SerializedName("name")
    private String name;

    @SerializedName("version_number")
    private String versionNumber;

    @SerializedName("changelog")
    private String changelog;

    @SerializedName("changelog_url")
    private String changelogUrl;

    @SerializedName("date_published")
    private String datePublished; // or LocalDateTime if you want better typing

    @SerializedName("downloads")
    private int downloads;

    @SerializedName("version_type")
    private String versionType;

    @SerializedName("status")
    private String status;

    @SerializedName("requested_status")
    private String requestedStatus;

    public List<String> getGameVersions() {
        return gameVersions;
    }

    public List<String> getLoaders() {
        return loaders;
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getName() {
        return name;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getChangelog() {
        return changelog;
    }

    public String getChangelogUrl() {
        return changelogUrl;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public int getDownloads() {
        return downloads;
    }

    public String getVersionType() {
        return versionType;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestedStatus() {
        return requestedStatus;
    }
}
