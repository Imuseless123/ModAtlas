package com.example.modatlas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateModpackFragment extends Fragment {
    private EditText editTextModpack;

    public CreateModpackFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_modpack, container, false);
        editTextModpack = view.findViewById(R.id.editTextModpack);
        Button btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> createModpack());

        return view;
    }

    private void createModpack() {
        String modpackName = editTextModpack.getText().toString().trim();
        if (!modpackName.isEmpty()) {
            File modpackDir = new File(requireContext().getFilesDir(), "modpacks/" + modpackName);

            if (!modpackDir.exists()) {
                if (modpackDir.mkdirs()) { // Create folder
                    Log.d("CreateModpack", "Modpack created: " + modpackDir.getAbsolutePath());

                    // Create modrinth.index.json file inside the folder
                    File jsonFile = new File(modpackDir, "modrinth.index.json");
                    try {
                        if (jsonFile.createNewFile()) { // Create the JSON file
                            Log.d("CreateModpack", "modrinth.index.json created: " + jsonFile.getAbsolutePath());

                            // Write initial JSON structure
                            FileWriter writer = new FileWriter(jsonFile);
                            writer.write("{\n  \"formatVersion\": 1,\n  \"game\": \"minecraft\"\n}");
                            writer.flush();
                            writer.close();
                        }
                    } catch (IOException e) {
                        Log.e("CreateModpack", "Failed to create modrinth.index.json", e);
                    }

                    // Notify MainActivity to refresh the list
                    if (getActivity() instanceof ModpackActivity) {
                        ((ModpackActivity) getActivity()).refreshModpacks();
                    }

                    getParentFragmentManager().popBackStack(); // Return to main screen
                } else {
                    Log.e("CreateModpack", "Failed to create modpack");
                }
            }
        }
    }

}