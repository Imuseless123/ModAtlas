package com.example.modatlas.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Map;

public interface ModrinthApi {
    @GET("search")
    Call<ModrinthResponse> searchMods(
            @Query("query") String query,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("index") String index,
            @Query("facets") String facets
    );
}