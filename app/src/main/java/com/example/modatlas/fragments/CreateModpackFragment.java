package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.modatlas.viewmodels.ModpackViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateModpackFragment extends Fragment {
    private ModpackViewModel modpackViewModel;

    private EditText editTextModpack;
    private Spinner spinnerMinecraftVersion, spinnerLoader;
    private Button btnSave;
    private List<String> minecraftVersions = new ArrayList<>();
    private String[] loaders = {"forge", "neoforge", "fabric-loader", "quilt-loader"};


    public CreateModpackFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_modpack, container, false);
        modpackViewModel = new ViewModelProvider(requireActivity()).get(ModpackViewModel.class);
        editTextModpack = view.findViewById(R.id.editTextModpack);
        spinnerMinecraftVersion = view.findViewById(R.id.spinnerMinecraftVersion);
        spinnerLoader = view.findViewById(R.id.spinnerLoader);
        btnSave = view.findViewById(R.id.btnSave);

        ArrayAdapter<String> loaderAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, loaders);
        loaderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerLoader.setAdapter(loaderAdapter);

        fetchMinecraftVersions();
        // Set up Spinners
        ArrayAdapter<String> versionAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, minecraftVersions);
        versionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMinecraftVersion.setAdapter(versionAdapter);




        btnSave.setOnClickListener(v -> {
            String modpackName = editTextModpack.getText().toString().trim();
            String minecraftVersion = spinnerMinecraftVersion.getSelectedItem().toString();
            String loader = spinnerLoader.getSelectedItem().toString();
            modpackViewModel.createModpack(modpackName,minecraftVersion,loader);
            Toast.makeText(requireContext(), "Modpack Created!", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof ModpackActivity) {
                ((ModpackActivity) getActivity()).refreshModpacks();
            }
            getParentFragmentManager().popBackStack();
        });

        return view;
    }

//    private void createModpack() {
//        String modpackName = editTextModpack.getText().toString().trim();
//        String minecraftVersion = spinnerMinecraftVersion.getSelectedItem().toString();
//        String loader = spinnerLoader.getSelectedItem().toString();
//        if (modpackName.isEmpty() || minecraftVersion.isEmpty()||loader.isEmpty()) {
//            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        File modpackDir = new File(requireContext().getFilesDir(), "modpacks/" + modpackName);
//        if (!modpackDir.exists() && modpackDir.mkdirs()) {
//            File jsonFile = new File(modpackDir, "modrinth.index.json");
//
//            try (FileWriter writer = new FileWriter(jsonFile)) {
//                JSONObject modpackJson = new JSONObject();
//                modpackJson.put("formatVersion", 1);
//                modpackJson.put("game", "minecraft");
//                modpackJson.put("versionId", minecraftVersion);
//                modpackJson.put("name", modpackName);
//                modpackJson.put("files", new JSONArray()); // Empty files array for now
//
//                JSONObject dependencies = new JSONObject();
//                dependencies.put(loader, loaderVersion);
//                dependencies.put("minecraft", minecraftVersion);
//
//                modpackJson.put("dependencies", dependencies);
//
//                writer.write(modpackJson.toString(4)); // Pretty print JSON
//                writer.flush();
//
//                Toast.makeText(requireContext(), "Modpack Created!", Toast.LENGTH_SHORT).show();
//
//                if (getActivity() instanceof ModpackActivity) {
//                    ((ModpackActivity) getActivity()).refreshModpacks();
//                }
//
//                getParentFragmentManager().popBackStack(); // Return to previous screen
//
//            } catch (IOException | JSONException e) {
//                Log.e("CreateModpack", "Failed to create modrinth.index.json", e);
//            }
//
//        }
//    }
    private void fetchMinecraftVersions() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.modrinth.com/v2/tag/game_version")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Failed to fetch versions", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        List<String> versions = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if ("release".equals(obj.optString("version_type"))) {
                                versions.add(obj.getString("version"));
                            }
                        }

                        minecraftVersions.clear();
                        minecraftVersions.addAll(versions);

                        getActivity().runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    getActivity(), R.layout.spinner_item, minecraftVersions
                            );
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spinnerMinecraftVersion.setAdapter(adapter);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}