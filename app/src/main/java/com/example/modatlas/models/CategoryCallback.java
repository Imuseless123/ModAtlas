package com.example.modatlas.models;

import java.util.List;

public interface CategoryCallback {
    void onCategoriesLoaded(List<Category> categories);
    void onError(Throwable t);
}
