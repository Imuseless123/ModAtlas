package com.example.modatlas.viewmodels;

import android.app.Application;
import android.util.Log;

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
}
