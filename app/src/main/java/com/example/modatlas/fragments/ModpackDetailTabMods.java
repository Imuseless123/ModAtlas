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
import android.widget.Toast;

import com.example.modatlas.ModpackActivity;
import com.example.modatlas.R;
import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModFile;
import com.example.modatlas.models.ModVersion;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.viewmodels.ModpackViewModel;
import com.example.modatlas.views.AddContentEntryAdapter;
import com.example.modatlas.views.ModFileAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ModpackDetailTabMods extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView recyclerModFiles;
    private ModFileAdapter modFileAdapter;
    private AddContentEntryAdapter adapter;
    private List<Mod> modList = new ArrayList<>();
    private ModpackViewModel modpackViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modpack_detail_tab_mods, container, false);

        modpackViewModel = new ViewModelProvider(requireActivity()).get(ModpackViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AddContentEntryAdapter(modList, null, this::onModButtonClick);
        recyclerView.setAdapter(adapter);

        recyclerModFiles = view.findViewById(R.id.recyclerModFiles);
        recyclerModFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        modFileAdapter = new ModFileAdapter(new ArrayList<>(), modFile -> modpackViewModel.removeModFile(modFile));
        recyclerModFiles.setAdapter(modFileAdapter);

        Button btnScanDependency = view.findViewById(R.id.btnScanDependency);
        btnScanDependency.setOnClickListener(v -> modpackViewModel.fetchRequiredDependencies());

        Button btnAddContent = view.findViewById(R.id.btnAddContent);
        btnAddContent.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, AddContentFragment.newInstance(
                            modpackViewModel.getModpack().getValue().getLoader(),
                            modpackViewModel.getModpack().getValue().getMinecraftVersion()
                    ))
                    .addToBackStack(null)
                    .commit();
        });

        Button btnDelete = view.findViewById(R.id.btnDeleteModpack);
        btnDelete.setOnClickListener(v -> deleteModpack());

        modpackViewModel.getDependencyModsLiveData().observe(getViewLifecycleOwner(), mods -> {
            modList.clear();
            modList.addAll(mods);
            adapter.notifyDataSetChanged();
        });

        modpackViewModel.getModpack().observe(getViewLifecycleOwner(), modpack -> {
            if (modpack != null) {
                modFileAdapter.updateList(modpack.getFiles());
            }
        });

        return view;
    }

    private void deleteModpack() {
        File modpackDir = new File(requireContext().getFilesDir(), "modpacks/" + modpackViewModel.getModpack().getValue().getName());
        if (modpackDir.exists()) {
            for (File file : modpackDir.listFiles()) {
                file.delete();
            }
            modpackDir.delete();
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

            if (getActivity() instanceof ModpackActivity) {
                ((ModpackActivity) getActivity()).refreshModpacks();
            }
            modpackViewModel.clearState();
            getParentFragmentManager().popBackStack();
        }
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