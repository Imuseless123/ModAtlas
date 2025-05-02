package com.example.modatlas.fragments;

import android.content.Intent;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

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
import com.example.modatlas.views.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
//    private ModFileAdapter modFileAdapter;
    private static final String ARG_MODPACK_NAME = "modpack_name";
    private String modpackName;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
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
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(requireActivity());
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Mods" : "Data")
        ).attach();

        TextView textView = view.findViewById(R.id.textModpackName);

//
        if (getArguments() != null) {
            modpackName = getArguments().getString(ARG_MODPACK_NAME);
            textView.setText(modpackName);

            modpackViewModel.loadModpack(modpackName);
            // Observe parsed Modpack data
            modpackViewModel.getModpack().observe(getViewLifecycleOwner(), modpack -> {
                if (modpack != null) {
                    textView.setText(modpack.getName());
//                    modFileAdapter.updateList(modpack.getFiles());
                }
            });

        }

        return view;
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
            modpackViewModel.clearState();
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
    public ModpackViewModel getModpackViewModel() {
        return modpackViewModel;
    }
}
