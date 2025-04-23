package com.example.modatlas.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.modatlas.R;
import com.example.modatlas.viewmodels.ModpackViewModel;

import java.io.File;

public class ModpackDetailTabData extends Fragment {
    private ModpackViewModel modpackViewModel;
    private ActivityResultLauncher<Intent> fileExportLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modpack_detail_tab_data, container, false);

        modpackViewModel = new ViewModelProvider(requireActivity()).get(ModpackViewModel.class);

        TextView textJsonContent = view.findViewById(R.id.textJsonContent);
        Button btnExportModpack = view.findViewById(R.id.btnExportModpack);

        modpackViewModel.getRawJson().observe(getViewLifecycleOwner(), textJsonContent::setText);

        fileExportLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    modpackViewModel.handleExportResult(uri, requireContext(), new File(requireContext().getFilesDir(), "modpacks/" + modpackViewModel.getModpack().getValue().getName() + ".mrpack"));
                }
            }
        });

        btnExportModpack.setOnClickListener(v -> {
            modpackViewModel.exportModpack(modpackViewModel.getModpack().getValue().getName(), requireActivity(), fileExportLauncher);
        });

        return view;
    }
}