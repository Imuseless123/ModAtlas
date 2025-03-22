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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modatlas.ModpackActivity;
import com.example.modatlas.R;
import com.example.modatlas.viewmodels.ModpackViewModel;
import com.example.modatlas.views.ModFileAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ModpackDetailFragment extends Fragment {
    private ActivityResultLauncher<Intent> fileExportLauncher;

    private ModpackViewModel modpackViewModel;
    private ModFileAdapter modFileAdapter;
    private static final String ARG_MODPACK_NAME = "modpack_name";
    private String modpackName;

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
        TextView textView = view.findViewById(R.id.textModpackName);
        Button btnAddContent = view.findViewById(R.id.btnAddContent);
        TextView textJsonContent = view.findViewById(R.id.textJsonContent);
        Button btnDelete = view.findViewById(R.id.btnDeleteModpack);
        RecyclerView recyclerModFiles = view.findViewById(R.id.recyclerModFiles);
        Button btnExportModpack = view.findViewById(R.id.btnExportModpack);
        btnExportModpack.setOnClickListener(v -> {
            modpackViewModel.exportModpack(modpackName, requireActivity(), fileExportLauncher);
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
}
