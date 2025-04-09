package com.example.modatlas.models;

import com.google.gson.annotations.SerializedName;

public class GameVersion {
    @SerializedName("version")
    private String version;

    @SerializedName("version_type")
    private String versionType;

    @SerializedName("date")
    private String date;

    @SerializedName("major")
    private boolean major;

    public String getVersion() {
        return version;
    }

    public String getVersionType() {
        return versionType;
    }

    public String getDate() {
        return date;
    }

    public boolean getMajor() {
        return major;
    }
}
