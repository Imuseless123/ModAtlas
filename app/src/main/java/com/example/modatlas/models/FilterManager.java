package com.example.modatlas.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterManager {
    private ModrinthApi api;
    protected List<Category> categoryList = new ArrayList<>();
    protected List<GameVersion> gameVersionList = new ArrayList<>();
    protected List<Loader> loader = new ArrayList<>();
    protected String name;
    public FilterManager(){
        this.categoryList = categoryList;
        api = RetrofitClient.getApi();
    }

    public void setCategoryList(CategoryCallback callback){
        api.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();                      // clear old data if needed
                    categoryList.addAll(response.body());     // update data
                    filterCategory();
                    callback.onCategoriesLoaded(categoryList); // ✅ use callback
                } else {
                    callback.onError(new Exception("API returned empty or error"));
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("test", "Failed to load categories: " + t.getMessage());
            }
        });
    }

    public void setGameVersionList(GameVersionCallback callback){
        api.getGameVersion().enqueue(new Callback<List<GameVersion>>() {
            @Override
            public void onResponse(Call<List<GameVersion>> call, Response<List<GameVersion>> response) {
                if (response.isSuccessful() && response.body() != null){
                    gameVersionList.clear();
                    for (GameVersion v: response.body()) {
                        if (v.getVersionType().equals("release")){
                            gameVersionList.add(v);
                        }
                    }
                    callback.onGameVersionsLoaded(gameVersionList);
                } else {
                    callback.onError(new Exception("API returned empty or error"));
                }
            }

            @Override
            public void onFailure(Call<List<GameVersion>> call, Throwable t) {
                Log.e("test", "Failed to load game version: " + t.getMessage());
            }
        });
    }

    public void setLoader(LoaderCallback callback){
        api.getLoader().enqueue(new Callback<List<Loader>>() {
            @Override
            public void onResponse(Call<List<Loader>> call, Response<List<Loader>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    loader.clear();
                    loader.addAll(response.body());
                    filterLoader();
                    callback.onLoadersLoaded(loader);
                } else {
                    callback.onError(new Exception("API returned empty or error"));
                }
            }

            @Override
            public void onFailure(Call<List<Loader>> call, Throwable t) {
                Log.e("test","Failed to load loader: " + t.getMessage());
            }
        });
    }

    public List<Category> getCategoryList(){
        return categoryList;
    }

    public List<GameVersion> getGameVersionList(){
        return gameVersionList;
    }

    public List<Loader> getLoader(){
        return loader;
    }

    public String getName() {
        return name;
    }

    protected void filterCategory(){}
    protected void filterLoader(){}
}
