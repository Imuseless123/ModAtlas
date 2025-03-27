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
import android.widget.Button;

import com.example.modatlas.R;
import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModFile;
import com.example.modatlas.models.ModVersion;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.viewmodels.ModpackViewModel;
import com.example.modatlas.views.AddContentEntryAdapter;
import com.example.modatlas.views.LoadState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DependencyFragment extends Fragment {
    private RecyclerView recyclerView;
    private AddContentEntryAdapter adapter;
    private List<Mod> modList = new ArrayList<>();
    private ModpackViewModel modpackViewModel;

    public DependencyFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dependency, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDependency);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AddContentEntryAdapter(modList, null, this::onModButtonClick);
        recyclerView.setAdapter(adapter);

        // Get ViewModel
        modpackViewModel = new ViewModelProvider(requireActivity()).get(ModpackViewModel.class);

        // Observe dependency changes
        modpackViewModel.getDependencyModsLiveData().observe(getViewLifecycleOwner(), mods -> {
            modList.clear();
            modList.addAll(mods);
            adapter.notifyDataSetChanged();
        });

        modpackViewModel.fetchRequiredDependencies();
        // Close button
        Button btnClose = view.findViewById(R.id.btnCloseFragment);
        btnClose.setOnClickListener(v -> closeFragment());

        return view;
    }

    private void closeFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void onModButtonClick(Mod mod) {
        String loader = modpackViewModel.getModpack().getValue().getLoader();
        String version = modpackViewModel.getModpack().getValue().getMinecraftVersion();
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
                                mod.setImportState(LoadState.DONE);
                                adapter.notifyItemChanged(modList.indexOf(mod));
                                return; // Stop searching after finding the first valid file
                            }
                        }
                    }
                    Log.e("AddContentEntryView", "No matching file found for loader: " + loader + " and version: " + version);
                } else {
                    Log.e("AddContentEntryView", "Failed to fetch versions: " + response.message());
                    mod.setImportState(LoadState.READY);
                    adapter.notifyItemChanged(modList.indexOf(mod));
                }
            }

            @Override
            public void onFailure(Call<List<ModVersion>> call, Throwable t) {
                Log.e("AddContentEntryView", "Error fetching versions: " + t.getMessage());
                mod.setImportState(LoadState.READY);
                adapter.notifyItemChanged(modList.indexOf(mod));
            }
        });
    }
}
