package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.modatlas.R;
import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.ModrinthResponse;
import com.example.modatlas.models.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContentFragment extends Fragment {
    private static final String ARG_LOADER = "param1";
    private static final String ARG_VERSION = "param2";
    private String loader;
    private String version;

    private EditText searchInput;
    private Button searchButton;
    private TextView resultText;

    public AddContentFragment() {
    }

    public static AddContentFragment newInstance(String loader, String version) {
        AddContentFragment fragment = new AddContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOADER, loader);
        args.putString(ARG_VERSION, version);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loader = getArguments().getString(ARG_LOADER);
            version = getArguments().getString(ARG_VERSION);
            Log.v("AddContent", "Loader: " + loader + ", Version: " + version);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_content, container, false);
        searchInput = view.findViewById(R.id.searchInput);
        searchButton = view.findViewById(R.id.searchButton);
        resultText = view.findViewById(R.id.resultText);

        searchButton.setOnClickListener(v -> searchModrinth(searchInput.getText().toString()));

        return view;
    }

    private void searchModrinth(String query) {
        if (query.isEmpty()) {
            resultText.setText("Enter a mod name to search.");
            return;
        }

        // Adjust loader category
        String loaderCategory = loader;
        if ("fabric-loader".equals(loader)) {
            loaderCategory = "fabric";
        } else if ("quilt-loader".equals(loader)) {
            loaderCategory = "quilt";
        }

        // Build facets JSON array
        String facets = "[[\"categories:" + loaderCategory + "\"], [\"versions:" + version + "\"], [\"project_type:mod\"]]";

        ModrinthApi api = RetrofitClient.getApi();
        Call<ModrinthResponse> call = api.searchMods(query, 20, 0, "relevance", facets);

        call.enqueue(new Callback<ModrinthResponse>() {
            @Override
            public void onResponse(Call<ModrinthResponse> call, Response<ModrinthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Mod> mods = response.body().getHits();
                    StringBuilder resultString = new StringBuilder();
                    for (Mod mod : mods) {
                        resultString.append(mod.getTitle()).append("\n");
                    }

                    resultText.setText(resultString.toString().isEmpty() ? "No results found" : resultString.toString());
                } else {
                    resultText.setText("Failed to retrieve mods.");
                }

            }

            @Override
            public void onFailure(Call<ModrinthResponse> call, Throwable t) {
                resultText.setText("Error: " + t.getMessage());
            }
        });
    }

}
