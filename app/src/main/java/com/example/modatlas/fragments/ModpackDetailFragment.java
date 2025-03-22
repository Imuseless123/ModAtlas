package com.example.modatlas.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModpackDetailFragment extends Fragment {
    private ActivityResultLauncher<Intent> fileExportLauncher;
    private ModpackViewModel modpackViewModel;
    private ModFileAdapter modFileAdapter;
    private static final String ARG_MODPACK_NAME = "modpack_name";
    private String modpackName;
    private RecyclerView recyclerView;
    private AddContentEntryAdapter adapter;
    private List<Mod> modList = new ArrayList<>();

    public static ModpackDetailFragment newInstance(String modpackName) {
        ModpackDetailFragment fragment = new ModpackDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MODPACK_NAME, modpackName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modpack_detail, container, false);
        modpackViewModel = new ViewModelProvider(requireActivity()).get(ModpackViewModel.class);
        Button btnScanDependency = view.findViewById(R.id.btnScanDependency);
        TextView textView = view.findViewById(R.id.textModpackName);
        Button btnAddContent = view.findViewById(R.id.btnAddContent);
        TextView textJsonContent = view.findViewById(R.id.textJsonContent);
        Button btnDelete = view.findViewById(R.id.btnDeleteModpack);
        RecyclerView recyclerModFiles = view.findViewById(R.id.recyclerModFiles);
        Button btnExportModpack = view.findViewById(R.id.btnExportModpack);
        btnExportModpack.setOnClickListener(v -> {
            modpackViewModel.exportModpack(modpackName, requireActivity(), fileExportLauncher);
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AddContentEntryAdapter(modList, mod -> {
            // Handle mod button click event
            onModButtonClick(mod);
        });
        recyclerView.setAdapter(adapter);

        btnScanDependency.setOnClickListener(v -> {
            modpackViewModel.fetchRequiredDependencies();
        });

        modpackViewModel.getDependencyModsLiveData().observe(getViewLifecycleOwner(), mods -> {
            modList.clear();
            modList.addAll(mods);
            adapter.notifyDataSetChanged();
        });
        // Set up RecyclerView
        recyclerModFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        modFileAdapter = new ModFileAdapter(new ArrayList<>(), modFile -> modpackViewModel.removeModFile(modFile));
        recyclerModFiles.setAdapter(modFileAdapter);

        if (getArguments() != null) {
            modpackName = getArguments().getString(ARG_MODPACK_NAME);
            textView.setText(modpackName);

            modpackViewModel.loadModpack(modpackName);
            // Observe parsed Modpack data
            modpackViewModel.getModpack().observe(getViewLifecycleOwner(), modpack -> {
                if (modpack != null) {
                    textView.setText(modpack.getName());
                    modFileAdapter.updateList(modpack.getFiles());
                }
            });

            // Observe raw JSON text
            modpackViewModel.getRawJson().observe(getViewLifecycleOwner(), textJsonContent::setText);


        }

        btnAddContent.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, AddContentFragment.newInstance(modpackViewModel.getModpack().getValue().getLoader(),modpackViewModel.getModpack().getValue().getMinecraftVersion())) // Replace with the new fragment
                    .addToBackStack(null) // Add to back stack to allow back navigation
                    .commit();
        });
        // Delete modpack button click
        btnDelete.setOnClickListener(v -> deleteModpack());
        btnScanDependency.setOnClickListener(v -> modpackViewModel.fetchRequiredDependencies());

        fileExportLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    modpackViewModel.handleExportResult(uri, requireContext(), new File(requireContext().getFilesDir(), "modpacks/" + modpackName + ".mrpack"));
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        modpackViewModel.clearState(); // Call a function to reset ViewModel state
    }
    private void deleteModpack() {
        File modpackDir = new File(requireContext().getFilesDir(), "modpacks/" + modpackName);

        if (modpackDir.exists()) {
            deleteRecursive(modpackDir); // Delete folder and contents
            Toast.makeText(getContext(), "Deleted: " + modpackName, Toast.LENGTH_SHORT).show();

            // Notify MainActivity to refresh the list
            if (getActivity() instanceof ModpackActivity) {
                ((ModpackActivity) getActivity()).refreshModpacks();
            }

            getParentFragmentManager().popBackStack(); // Go back
        }
    }

    // Recursive function to delete a folder and its contents
    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
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
