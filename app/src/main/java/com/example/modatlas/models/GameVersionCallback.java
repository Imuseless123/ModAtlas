package com.example.modatlas.models;

import java.util.List;

public interface GameVersionCallback {
    void onGameVersionsLoaded(List<GameVersion> categories);
    void onError(Throwable t);
}
