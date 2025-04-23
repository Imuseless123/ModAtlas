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
    private RecyclerView recyclerViewDependency;
    private RecyclerView recyclerModFiles;
    private ModFileAdapter modFileAdapter;
    private AddContentEntryAdapter addContentEntryAdapter;
    private Button btnScanDependency;
    private List<Mod> modList = new ArrayList<>();
    private ModpackViewModel modpackViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modpack_detail_tab_mods, container, false);

        modpackViewModel = new ViewModelProvider(requireActivity()).get(ModpackViewModel.class);
        btnScanDependency = view.findViewById(R.id.btnScanDependency);
        btnScanDependency.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.fragmentContainer, new DependencyFragment())
                    .addToBackStack(null)
                    .commit();
        });


//        recyclerViewDependency = view.findViewById(R.id.recyclerViewDependency);
//        recyclerViewDependency.setLayoutManager(new LinearLayoutManager(getContext()));
//        addContentEntryAdapter = new AddContentEntryAdapter(modList, null, this::onModButtonClick);
//        recyclerViewDependency.setAdapter(addContentEntryAdapter);
//        modpackViewModel.getDependencyModsLiveData().observe(getViewLifecycleOwner(), mods -> {
//            modList.clear();
//            modList.addAll(mods);
//            addContentEntryAdapter.notifyDataSetChanged();
//        });

        recyclerModFiles = view.findViewById(R.id.recyclerModFiles);
        recyclerModFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        modFileAdapter = new ModFileAdapter(new ArrayList<>(), modFile -> modpackViewModel.removeModFile(modFile));
        recyclerModFiles.setAdapter(modFileAdapter);



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


}