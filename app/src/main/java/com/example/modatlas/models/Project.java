package com.example.modatlas.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Project {
    @SerializedName("client_side")
    private String clientSide;

    @SerializedName("server_side")
    private String serverSide;

    @SerializedName("game_versions")
    private List<String> gameVersions;

    @SerializedName("id")
    private String id;

    @SerializedName("slug")
    private String slug;

    @SerializedName("project_type")
    private String projectType;

    @SerializedName("team")
    private String team;

    @SerializedName("organization")
    private String organization;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("body")
    private String body;

    @SerializedName("body_url")
    private String bodyUrl;

    @SerializedName("published")
    private String published;

    @SerializedName("updated")
    private String updated;

    @SerializedName("approved")
    private String approved;

    @SerializedName("queued")
    private String queued;

    @SerializedName("status")
    private String status;

    @SerializedName("requested_status")
    private String requestedStatus;

    @SerializedName("moderator_message")
    private String moderatorMessage;

    @SerializedName("license")
    private License license;

    @SerializedName("downloads")
    private int downloads;

    @SerializedName("followers")
    private int followers;

    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("additional_categories")
    private List<String> additionalCategories;

    @SerializedName("loaders")
    private List<String> loaders;

    @SerializedName("versions")
    private List<String> versions;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("issues_url")
    private String issuesUrl;

    @SerializedName("source_url")
    private String sourceUrl;

    @SerializedName("wiki_url")
    private String wikiUrl;

    @SerializedName("discord_url")
    private String discordUrl;

    @SerializedName("donation_urls")
    private List<donationUrls> donationUrls;  // You can replace Object with a specific class if you know the structure

    @SerializedName("gallery")
    private List<gallery> gallery;  // Same, replace Object if you know structure

    @SerializedName("color")
    private int color;

    @SerializedName("thread_id")
    private String threadId;

    @SerializedName("monetization_status")
    private String monetizationStatus;

    public String getClientSide() {
        return clientSide;
    }

    public String getServerSide() {
        return serverSide;
    }

    public List<String> getGameVersions() {
        return gameVersions;
    }

    public String getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getTeam() {
        return team;
    }

    public String getOrganization() {
        return organization;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public String getBodyUrl() {
        return bodyUrl;
    }

    public String getPublished() {
        return published;
    }

    public String getUpdated() {
        return updated;
    }

    public String getApproved() {
        return approved;
    }

    public String getQueued() {
        return queued;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestedStatus() {
        return requestedStatus;
    }

    public String getModeratorMessage() {
        return moderatorMessage;
    }

    public License getLicense() {
        return license;
    }

    public int getDownloads() {
        return downloads;
    }

    public int getFollowers() {
        return followers;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getAdditionalCategories() {
        return additionalCategories;
    }

    public List<String> getLoaders() {
        return loaders;
    }

    public List<String> getVersions() {
        return versions;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getIssuesUrl() {
        return issuesUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public String getDiscordUrl() {
        return discordUrl;
    }

    public List<donationUrls> getDonationUrls() {
        return donationUrls;
    }

    public List<gallery> getGallery() {
        return gallery;
    }

    public int getColor() {
        return color;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getMonetizationStatus() {
        return monetizationStatus;
    }

    // --- Getters and Setters (optional) ---
    public class License {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("url")
        private String url;

        // --- Getters and Setters (optional) ---

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }

    public class donationUrls {
        @SerializedName("id")
        private String id;

        @SerializedName("platform")
        private String platform;

        @SerializedName("url")
        private String url;

        public String getId() {
            return id;
        }

        public String getPlatform() {
            return platform;
        }

        public String getUrl() {
            return url;
        }
    }

    public class gallery {
        @SerializedName("url")
        private String url;

        @SerializedName("raw_url")
        private String rawUrl;

        @SerializedName("featured")
        private boolean featured;

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("created")
        private String created;

        @SerializedName("ordering")
        private Integer ordering;

        public String getUrl() {
            return url;
        }

        public String getRawUrl() {
            return rawUrl;
        }

        public boolean isFeatured() {
            return featured;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getCreated() {
            return created;
        }

        public Integer getOrdering() {
            return ordering;
        }
    }
}
