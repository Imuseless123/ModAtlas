package com.example.modatlas.models;

import java.util.List;

public interface LoaderCallback {
    void onLoadersLoaded(List<Loader> loaders);
    void onError(Throwable t);
}
