package com.example.modatlas.models;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.fragments.FilterFragment;
import com.example.modatlas.views.FilterAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameVersionFilter extends FilterManager<GameVersion, FilterItem>{
//    FilterAdapter filterAdapter;
    public GameVersionFilter(FilterAdapter filterAdapter, RecyclerView recyclerView, Context context, RecyclerViewInterface recyclerViewInterface){
        super();
//        this.filterAdapter = filterAdapter;
        this.api.getGameVersion().enqueue(new Callback<List<GameVersion>>() {
            @Override
            public void onResponse(Call<List<GameVersion>> call, Response<List<GameVersion>> response) {
                if(response.body() != null){
                    GameVersionFilter.this.apiItem = response.body();

                    GameVersionFilter.this.itemList.clear(); // Clear old data just in case

                    Log.i("test","version: "+response.body());
                    int a = 0;
                    GameVersionFilter.this.itemList.add(new FilterHeader("game version"));

                    for (GameVersion v: GameVersionFilter.this.apiItem) {
                        a++;
                        GameVersionFilter.this.itemList.add(new FilterTag(v.getVersion(),"versions"));
//                        Log.i("test", "version: " + ((FilterTag) tes.get(a)).getItemName());
                    }

                    FilterAdapter filterAdapter = new FilterAdapter(context,GameVersionFilter.this.getItemList(), recyclerViewInterface);
                    recyclerView.setAdapter(filterAdapter );
                } else {
                    Log.e("test", "Response body is null");
                }
            }

            @Override
            public void onFailure(Call<List<GameVersion>> call, Throwable t) {
                Log.e("test",call+"");
            }
        });

    }
}
