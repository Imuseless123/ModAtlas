package com.example.modatlas.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
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
    @GET("search")
    Call<ModrinthResponse> searchFacetMod(
            @Query("query") String query,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("facets") String facets,  // Pass facets as a query parameter
            @Query("loader") String loader
    );
    @GET("project/{slug}/version")
    Call<List<ModVersion>> getModVersions(@Path("slug") String slug);
    @GET("version/{id}")
    Call<ModVersion> getVersionDetails(@Path("id") String id);

    @GET("projects")
    Call<List<Mod>> getModsByProjectIds(@Query("ids") String ids);
    @GET("versions")
    Call<List<ModVersion>> getModVersionsById(@Query("ids") String ids);
    @GET("tag/game_version")
    Call<List<GameVersion>> getGameVersion();
    @GET("tag/loader")
    Call<List<Loader>> getLoader();
    @GET("tag/category")
    Call<List<Category>> getCategory();
    @GET("project/{id}")
    Call<Project> getProjectById(@Path("id") String id);
    @GET("project/{id}/version")
    Call<List<ProjectVersion>> getProjectVersionById(@Path("id") String id);
}