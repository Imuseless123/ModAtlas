package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.modatlas.R;
import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModFile;
import com.example.modatlas.models.ModVersion;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.ModrinthResponse;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.viewmodels.ModpackViewModel;
import com.example.modatlas.views.AddContentEntryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContentFragment extends Fragment {
    private ModpackViewModel modpackViewModel;
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
            loader = loader.equals("fabric-loader") ? "fabric" :
                    loader.equals("quilt-loader") ? "quilt" : loader;
            version = getArguments().getString(ARG_VERSION);
            Log.v("AddContent", "Loader: " + loader + ", Version: " + version);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_content, container, false);
        modpackViewModel = new ViewModelProvider(requireActivity()).get(ModpackViewModel.class);
        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new AddContentEntryAdapter(modList,loader,this::onModButtonClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {
            modList.clear();
            adapter.notifyDataSetChanged();
            currentPage = 0;
            searchModrinth(searchInput.getText().toString(), true);
        });
        // Listen for Enter key on EditText
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                performSearch();
                return true;
            }
            return false;
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
    // Helper method to perform search
    private void performSearch() {
        modList.clear();
        adapter.notifyDataSetChanged();
        currentPage = 0;
        searchModrinth(searchInput.getText().toString(), true);
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
        String forkLoader = loader;
        if(Objects.equals(loader, "quilt")){forkLoader = "fabric";}
        if(Objects.equals(loader, "neoforge")){forkLoader = "forge";}
        Log.v("Loader", forkLoader + "fork of"+ forkLoader);
        String facets = "[[\"categories:" + forkLoader + "\"], [\"versions:" + version + "\"], [\"project_type:mod\"]]";

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
    private void onModButtonClick(Mod mod) {
        // Make an API call when the button is clicked
        Log.v("Import","Import: "+ mod.getTitle());
                if (mod.getSlug() == null) {
            Log.e("AddContentEntryView", "Slug is null, cannot fetch versions.");
            return;
        }

        ModrinthApi api = RetrofitClient.getApi();
        Call<List<ModVersion>> call = api.getModVersions(mod.getSlug());
        Log.v("Import","call: "+ mod.getSlug());

        call.enqueue(new Callback<List<ModVersion>>() {
            @Override
            public void onResponse(Call<List<ModVersion>> call, Response<List<ModVersion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String normalizedLoader = loader.replace("-loader", ""); // Normalize loader names

                    for (ModVersion modVersion : response.body()) {
                        if (modVersion.getGameVersions().contains(version) && modVersion.getLoaders().contains(normalizedLoader)) {
                            if (!modVersion.getFiles().isEmpty()) {
                                ModFile file = modVersion.getFiles().get(0); // Take the first file

                                Log.d("Selected ModFile", "Filename: " + file.getFilename());
                                Log.d("Selected ModFile", "SHA1: " + file.getHashes().getSha1());
                                Log.d("Selected ModFile", "URL: " + file.getUrl());
                                Log.d("Selected ModFile", "Size: " + file.getSize());

                                // Use this file (e.g., download or store its URL)
//                                String downloadUrl = file.getUrl();
                                modpackViewModel.addModFile(file);
                                return; // Stop searching after finding the first valid file
                            }
                        }
                    }
                    Log.e("AddContentEntryView", "No matching file found for loader: " + loader + " and version: " + version);
                } else {
                    Log.e("AddContentEntryView", "Failed to fetch versions: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ModVersion>> call, Throwable t) {
                Log.e("AddContentEntryView", "Error fetching versions: " + t.getMessage());
            }
        });
    }

}

