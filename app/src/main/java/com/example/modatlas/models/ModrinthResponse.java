package com.example.modatlas.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ModrinthResponse {
    @SerializedName("hits")
    private List<Mod> hits;

    public List<Mod> getHits() { return hits; }
}

