package com.example.modatlas.models;

import java.util.ArrayList;
import java.util.List;

public class FilterManager<T , E> {
    protected ModrinthApi api;
    protected List<T> apiItem;
    protected List<E> itemList;

    public FilterManager(){
        api = RetrofitClient.getApi();
        this.itemList = new ArrayList<>();
        this.apiItem = new ArrayList<>();
    }

    public List<E> getItemList(){
        return itemList;
    }

    public List<T> getApiItem(){
        return apiItem;
    }
}
