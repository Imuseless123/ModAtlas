package com.example.modatlas.viewmodels;

import static java.security.AccessController.getContext;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.modatlas.models.Hashes;
import com.example.modatlas.models.ModFile;
import com.example.modatlas.models.Modpack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ModpackViewModel extends AndroidViewModel {
    private final MutableLiveData<Modpack> modpackLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> rawJsonLiveData = new MutableLiveData<>();

    public ModpackViewModel(@NonNull Application application) {
        super(application);
    }

    // LiveData for observing the selected modpack
    public LiveData<Modpack> getModpack() {
        return modpackLiveData;
    }
    public LiveData<String> getRawJson() {
        return rawJsonLiveData;
    }

    // Switch to a new modpack
    public void loadModpack(Modpack modpack) {
        modpackLiveData.setValue(modpack);
    }

    public void loadModpack(String modpackName) {
        File jsonFile = new File(getApplication().getFilesDir(), "modpacks/" + modpackName + "/modrinth.index.json");
        if (!jsonFile.exists()) {
            rawJsonLiveData.postValue("modrinth.index.json not found.");
            return;
        };

        try {
            String jsonContent = new String(Files.readAllBytes(jsonFile.toPath()));
            rawJsonLiveData.postValue(jsonContent);
            JSONObject jsonObject = new JSONObject(jsonContent);

            String name = jsonObject.getString("name");
            JSONObject dependencies = jsonObject.getJSONObject("dependencies");

            // Extract Minecraft version
            String minecraftVersion = dependencies.optString("minecraft", "Unknown");

            // Extract loader name and version
            String loader = "";
            String loaderVersion = "";
            Iterator<String> keys = dependencies.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.equals("minecraft")) {
                    loader = key.replace("-loader", ""); // Remove "-loader" suffix
                    loaderVersion = dependencies.optString(key, "Unknown");
                    break;
                }
            }

            // Extract mod files
            List<ModFile> files = new ArrayList<>();
            if (jsonObject.has("files")) {
                JSONArray filesArray = jsonObject.getJSONArray("files");
                for (int i = 0; i < filesArray.length(); i++) {
                    JSONObject fileObj = filesArray.getJSONObject(i);

                    // Extract fields
                    String path = fileObj.getString("path").replace("mods/", ""); // Remove "mods/"
                    String url = fileObj.getJSONArray("downloads").getString(0);
                    long size = fileObj.getLong("fileSize");

                    JSONObject hashesObj = fileObj.getJSONObject("hashes");
                    String sha1 = hashesObj.optString("sha1", "");
                    String sha512 = hashesObj.optString("sha512", "");

                    Hashes hashes = new Hashes(sha1, sha512);
                    files.add(new ModFile(path, url, size, hashes));
                }
            }

            // Set data to LiveData
            Modpack modpack = new Modpack(name, minecraftVersion, loader, loaderVersion, files);
            modpackLiveData.postValue(modpack);
            Log.v("Modpack", "Modpack loaded into ViewModel: " + modpack.getLoader());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    public void addModFile(ModFile modFile) {
        if(modpackLiveData.getValue().getFiles().contains(modFile)){
            Toast.makeText(getApplication(), modFile.getFilename()+" is already exists in the pack", Toast.LENGTH_SHORT).show();

            return;
        }

        File jsonFile = new File(getApplication().getFilesDir(), "modpacks/"+modpackLiveData.getValue().getName()+"/modrinth.index.json");

        if (!jsonFile.exists()) {
            Log.e("ModpackViewModel", "modrinth.index.json not found.");
            return;
        }

        try {
            // Read existing JSON file
            String jsonContent = new String(Files.readAllBytes(jsonFile.toPath()));
            JSONObject jsonObject = new JSONObject(jsonContent);

            // Ensure "files" array exists
            JSONArray filesArray = jsonObject.optJSONArray("files");
            if (filesArray == null) {
                filesArray = new JSONArray();
                jsonObject.put("files", filesArray);
            }

            // Create JSON object for the new mod file
            JSONObject modFileJson = new JSONObject();
            modFileJson.put("path", "mods/" + modFile.getFilename());
            modFileJson.put("fileSize", modFile.getSize());

            // Add hashes
            JSONObject hashesJson = new JSONObject();
            hashesJson.put("sha1", modFile.getHashes().getSha1());
            hashesJson.put("sha512", modFile.getHashes().getSha512());
            modFileJson.put("hashes", hashesJson);

            // Add downloads array
            JSONArray downloadsArray = new JSONArray();
            downloadsArray.put(modFile.getUrl());
            modFileJson.put("downloads", downloadsArray);

            // Append to "files" array
            filesArray.put(modFileJson);
            Log.v("AddMod",modFileJson.toString());

            // Write back to file
            Files.write(jsonFile.toPath(), jsonObject.toString(4).getBytes());

            // Update LiveData
            Modpack currentModpack = modpackLiveData.getValue();
            if (currentModpack != null) {
                List<ModFile> updatedFiles = new ArrayList<>(currentModpack.getFiles());
                updatedFiles.add(modFile);
                Modpack updatedModpack = new Modpack(
                        currentModpack.getName(),
                        currentModpack.getMinecraftVersion(),
                        currentModpack.getLoader(),
                        currentModpack.getLoaderVersion(),
                        updatedFiles
                );
                modpackLiveData.postValue(updatedModpack);
            }

            Log.d("ModpackViewModel", "Mod file added: " + modFile.getFilename());

        } catch (IOException | JSONException e) {
            Log.e("ModpackViewModel", "Error adding mod file: " + e.getMessage());
        }
    }
    public void removeModFile(ModFile modFile) {
        File jsonFile = new File(getApplication().getFilesDir(), "modpacks/"+modpackLiveData.getValue().getName()+"/modrinth.index.json");

        if (!jsonFile.exists()) {
            Log.e("ModpackViewModel", "modrinth.index.json not found.");
            return;
        }

        try {
            // Read existing JSON file
            String jsonContent = new String(Files.readAllBytes(jsonFile.toPath()));
            JSONObject jsonObject = new JSONObject(jsonContent);

            // Check if "files" array exists
            JSONArray filesArray = jsonObject.optJSONArray("files");
            if (filesArray == null) {
                Log.e("ModpackViewModel", "No files found in modrinth.index.json.");
                return;
            }

            // Iterate through the files array and remove matching mod file
            JSONArray updatedFilesArray = new JSONArray();
            boolean removed = false;

            for (int i = 0; i < filesArray.length(); i++) {
                JSONObject fileObj = filesArray.getJSONObject(i);
                String filePath = fileObj.getString("path").replace("mods/", "");

                if (filePath.equals(modFile.getFilename())) {
                    removed = true; // Mark as removed
                    continue; // Skip adding this file to the updated array
                }

                updatedFilesArray.put(fileObj);
            }

            if (!removed) {
                Log.e("ModpackViewModel", "Mod file not found in JSON.");
                return;
            }

            // Update JSON object
            jsonObject.put("files", updatedFilesArray);

            // Write back to file
            Files.write(jsonFile.toPath(), jsonObject.toString(4).getBytes());

            // Update LiveData
            Modpack currentModpack = modpackLiveData.getValue();
            if (currentModpack != null) {
                List<ModFile> updatedFiles = new ArrayList<>(currentModpack.getFiles());
                updatedFiles.removeIf(file -> file.getFilename().equals(modFile.getFilename()));

                Modpack updatedModpack = new Modpack(
                        currentModpack.getName(),
                        currentModpack.getMinecraftVersion(),
                        currentModpack.getLoader(),
                        currentModpack.getLoaderVersion(),
                        updatedFiles
                );
                modpackLiveData.postValue(updatedModpack);
            }

            Log.d("ModpackViewModel", "Mod file removed and LiveData updated: " + modFile.getFilename());

        } catch (IOException | JSONException e) {
            Log.e("ModpackViewModel", "Error removing mod file: " + e.getMessage());
        }
    }
    public void createModpack(String modpackName, String minecraftVersion, String loader) {
        if (modpackName.isEmpty() || minecraftVersion.isEmpty() || loader.isEmpty()) {
            Toast.makeText(getApplication(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        File modpackDir = new File(getApplication().getFilesDir(), "modpacks/" + modpackName);
        if (!modpackDir.exists() && modpackDir.mkdirs()) {
            File jsonFile = new File(modpackDir, "modrinth.index.json");

            try (FileWriter writer = new FileWriter(jsonFile)) {
                JSONObject modpackJson = new JSONObject();
                modpackJson.put("formatVersion", 1);
                modpackJson.put("game", "minecraft");
                modpackJson.put("name", modpackName);

                JSONObject dependencies = new JSONObject();
                dependencies.put("minecraft", minecraftVersion);
                dependencies.put(loader , "latest");

                modpackJson.put("files", new JSONArray()); // Empty mods list
                modpackJson.put("dependencies", dependencies);


                writer.write(modpackJson.toString(4));

                // Load the created modpack into LiveData
                Modpack modpack = new Modpack(modpackName, minecraftVersion, loader, "latest", new ArrayList<>());
                modpackLiveData.postValue(modpack);

                Log.d("ModpackViewModel", "New modpack created: " + modpackName);

            } catch (IOException | JSONException e) {
                Log.e("ModpackViewModel", "Error creating modpack: " + e.getMessage());
            }
        } else {
            Toast.makeText(getApplication(), "Modpack folder creation failed", Toast.LENGTH_SHORT).show();
        }
    }



}
