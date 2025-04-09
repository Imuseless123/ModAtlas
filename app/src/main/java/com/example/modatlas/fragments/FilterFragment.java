package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.modatlas.R;
import com.example.modatlas.models.FilterHeader;
import com.example.modatlas.models.FilterItem;
import com.example.modatlas.viewmodels.FilterTable;
import com.example.modatlas.models.FilterTag;
import com.example.modatlas.models.GameVersion;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.RecyclerViewInterface;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.views.FilterAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterFragment extends Fragment implements RecyclerViewInterface {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILTER_ID = "param1";
    private String filterId;
    private RecyclerView recyclerView;
    private FilterAdapter gameVersionAdapter, loaderAdapter;
    private ModrinthApi api;
    private List<GameVersion> versions = new ArrayList<>();
    private List<FilterItem> tes = new ArrayList<>();
    private FilterTable filterTable;

    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance(String filterid) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILTER_ID, filterid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterTable = new ViewModelProvider(requireActivity()).get(FilterTable.class);
        if (getArguments() != null) {
            filterId = getArguments().getString(ARG_FILTER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        recyclerView = view.findViewById(R.id.modFilterTable);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        GameVersionFilter gameVersionFilter = new GameVersionFilter(gameVersionAdapter, recyclerView,getContext(),FilterFragment.this);


        this.api = RetrofitClient.getApi();
        this.api.getGameVersion().enqueue(new Callback<List<GameVersion>>() {
            @Override
            public void onResponse(Call<List<GameVersion>> call, Response<List<GameVersion>> response) {
                if(response.body() != null){
                    versions = response.body();

                    tes.clear(); // Clear old data just in case

                    Log.i("test","version: "+response.body());
                    int a = 0;
                    tes.add(new FilterHeader("game version"));

                    for (GameVersion v: versions) {
                        a++;
                        tes.add(new FilterTag(v.getVersion(),"versions"));
//                        Log.i("test", "version: " + ((FilterTag) tes.get(a)).getItemName());
                    }

//                    for (GameVersion v: versions) {
//                        filterTable.addVersion(v.getVersion());
//                    }

                    gameVersionAdapter = new FilterAdapter(getContext(),tes, FilterFragment.this);
                    recyclerView.setAdapter(gameVersionAdapter);
                } else {
                    Log.e("test", "Response body is null");
                }
            }

            @Override
            public void onFailure(Call<List<GameVersion>> call, Throwable t) {
                Log.e("test", "API call failed: " + t.getMessage());
            }
        });

//        gameVersionAdapter = new FilterAdapter(getContext(),a.getItemList(), FilterFragment.this);
//        recyclerView.setAdapter(gameVersionAdapter);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        String selectedTag = tes.get(position).getItemName();
        String header = "";
        if (tes.get(position).isTag()){
            header = ((FilterTag) tes.get(position)).getHeader();
        }
        Log.i( "test",selectedTag);

        if (!filterTable.haveVersion(header+":"+selectedTag)){
            filterTable.addVersion(header+":"+selectedTag);
        } else {
            filterTable.removeVersion(header+":"+selectedTag);
        }


        List<String> versions = filterTable.getSelectedVersions().getValue();

        if (versions != null) {
            Log.i("test", "Selected versions: " + versions.toString());
        } else {
            Log.i("test", "Selected versions: null");
        }
    }
}