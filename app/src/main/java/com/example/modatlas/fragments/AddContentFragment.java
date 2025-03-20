package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.modatlas.R;
import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.ModrinthResponse;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.views.AddContentEntryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContentFragment extends Fragment {
    private static final String ARG_LOADER = "param1";
    private static final String ARG_VERSION = "param2";
    private String loader;
    private String version;

    private EditText searchInput;
    private Button searchButton;
    private RecyclerView recyclerView;
    private AddContentEntryAdapter adapter;
    private List<Mod> modList = new ArrayList<>();
    private boolean isLoading = false;
    private int currentPage = 0;

    public AddContentFragment() {
    }

    public static AddContentFragment newInstance(String loader, String version) {
        AddContentFragment fragment = new AddContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOADER, loader);
        args.putString(ARG_VERSION, version);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loader = getArguments().getString(ARG_LOADER);
            version = getArguments().getString(ARG_VERSION);
            Log.v("AddContent", "Loader: " + loader + ", Version: " + version);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_content, container, false);
        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new AddContentEntryAdapter(modList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {
            modList.clear();
            adapter.notifyDataSetChanged();
            currentPage = 0;
            searchModrinth(searchInput.getText().toString(), true);
        });

        // Implement infinite scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { // Check if scrolling down
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        searchModrinth(searchInput.getText().toString(), false);
                    }
                }
            }
        });

        return view;
    }

    private void searchModrinth(String query, boolean reset) {
        if (query.isEmpty()) {
            return;
        }

        if (reset) {
            modList.clear();
            currentPage = 0;
            isLoading = false;
        }

        isLoading = true;
        String loaderCategory = loader.equals("fabric-loader") ? "fabric" :
                loader.equals("quilt-loader") ? "quilt" : loader;

        String facets = "[[\"categories:" + loaderCategory + "\"], [\"versions:" + version + "\"], [\"project_type:mod\"]]";

        ModrinthApi api = RetrofitClient.getApi();
        Call<ModrinthResponse> call = api.searchMods(query, 20, currentPage * 20, "relevance", facets);

        call.enqueue(new Callback<ModrinthResponse>() {
            @Override
            public void onResponse(Call<ModrinthResponse> call, Response<ModrinthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mod> mods = response.body().getHits();
                    modList.addAll(mods);
                    adapter.notifyDataSetChanged();
                    currentPage++;
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<ModrinthResponse> call, Throwable t) {
                isLoading = false;
            }
        });
    }
}

