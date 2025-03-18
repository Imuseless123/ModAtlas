package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.modatlas.ModpackActivity;
import com.example.modatlas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateModpackFragment extends Fragment {
    private EditText editTextModpack;
    private Spinner spinnerMinecraftVersion, spinnerLoader, spinnerLoaderVersion;
    private Button btnSave;
    private String[] minecraftVersions = {"1.20.1", "1.21", "1.21.1"};
    private String[] loaders = {"forge", "neoforge", "fabric-loader", "quilt-loader"};
    private String[] loadersVersion = {"0.16.10", "0.17.10", "0.18.10", "0.19.10"};


    public CreateModpackFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_modpack, container, false);
        editTextModpack = view.findViewById(R.id.editTextModpack);
        spinnerLoaderVersion = view.findViewById(R.id.spinnerLoaderVersion);
        spinnerMinecraftVersion = view.findViewById(R.id.spinnerMinecraftVersion);
        spinnerLoader = view.findViewById(R.id.spinnerLoader);
        btnSave = view.findViewById(R.id.btnSave);

        // Set up Spinners
        ArrayAdapter<String> versionAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, minecraftVersions);
        versionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMinecraftVersion.setAdapter(versionAdapter);

        ArrayAdapter<String> loaderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, loaders);
        loaderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoader.setAdapter(loaderAdapter);

        ArrayAdapter<String> loaderVersionAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, loadersVersion);
        loaderVersionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaderVersion.setAdapter(loaderVersionAdapter);

        btnSave.setOnClickListener(v -> createModpack());

        return view;
    }

    private void createModpack() {
        String modpackName = editTextModpack.getText().toString().trim();
        String minecraftVersion = spinnerMinecraftVersion.getSelectedItem().toString();
        String loader = spinnerLoader.getSelectedItem().toString();
        String loaderVersion = spinnerLoaderVersion.getSelectedItem().toString();
        if (modpackName.isEmpty() || loaderVersion.isEmpty() || minecraftVersion.isEmpty()||loader.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        File modpackDir = new File(requireContext().getFilesDir(), "modpacks/" + modpackName);
        if (!modpackDir.exists() && modpackDir.mkdirs()) {
            File jsonFile = new File(modpackDir, "modrinth.index.json");

            try (FileWriter writer = new FileWriter(jsonFile)) {
                JSONObject modpackJson = new JSONObject();
                modpackJson.put("formatVersion", 1);
                modpackJson.put("game", "minecraft");
                modpackJson.put("versionId", minecraftVersion);
                modpackJson.put("name", modpackName);
                modpackJson.put("files", new JSONArray()); // Empty files array for now

                JSONObject dependencies = new JSONObject();
                dependencies.put(loader, loaderVersion);
                dependencies.put("minecraft", minecraftVersion);

                modpackJson.put("dependencies", dependencies);

                writer.write(modpackJson.toString(4)); // Pretty print JSON
                writer.flush();

                Toast.makeText(requireContext(), "Modpack Created!", Toast.LENGTH_SHORT).show();

                if (getActivity() instanceof ModpackActivity) {
                    ((ModpackActivity) getActivity()).refreshModpacks();
                }

                getParentFragmentManager().popBackStack(); // Return to previous screen

            } catch (IOException | JSONException e) {
                Log.e("CreateModpack", "Failed to create modrinth.index.json", e);
            }

        }
    }

}