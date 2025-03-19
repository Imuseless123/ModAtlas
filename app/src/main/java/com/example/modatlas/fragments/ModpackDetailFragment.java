package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modatlas.ModpackActivity;
import com.example.modatlas.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ModpackDetailFragment extends Fragment {
    private static final String ARG_MODPACK_NAME = "modpack_name";
    private String modpackName;
    private String loader;
    private String version;

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
        TextView textView = view.findViewById(R.id.textModpackName);
        Button btnAddContent = view.findViewById(R.id.btnAddContent);
        TextView textJsonContent = view.findViewById(R.id.textJsonContent);
        Button btnDelete = view.findViewById(R.id.btnDeleteModpack);

        if (getArguments() != null) {
            modpackName = getArguments().getString(ARG_MODPACK_NAME);
            textView.setText(modpackName);


            // Load JSON file content
            File jsonFile = new File(requireContext().getFilesDir(), "modpacks/" + modpackName + "/modrinth.index.json");
            if (jsonFile.exists()) {
                try {
                    String jsonContent = new String(java.nio.file.Files.readAllBytes(jsonFile.toPath()));
                    textJsonContent.setText(jsonContent);

                    // Parse JSON
                    JSONObject jsonObject = new JSONObject(jsonContent);
                    JSONObject dependencies = jsonObject.getJSONObject("dependencies");

                    // Extract "minecraft" version
                    version = dependencies.optString("minecraft", "Unknown");

                    // Find the first key that is NOT "minecraft"
                    loader = "";
                    Iterator<String> keys = dependencies.keys(); // Fix: Use keys() instead of keySet()
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (!key.equals("minecraft")) {
                            loader = key;
                            break;
                        }
                    }

                } catch (IOException e) {
                    textJsonContent.setText("Failed to load JSON file.");
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                textJsonContent.setText("modrinth.index.json not found.");
            }
        }

        btnAddContent.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, AddContentFragment.newInstance(loader,version)) // Replace with the new fragment
                    .addToBackStack(null) // Add to back stack to allow back navigation
                    .commit();
        });
        // Delete modpack button click
        btnDelete.setOnClickListener(v -> deleteModpack());

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
