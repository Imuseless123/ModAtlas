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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

        // Adjust the loader category if needed
        String loaderCategory = loader;
        if ("fabric-loader".equals(loader)) {
            loaderCategory = "fabric";
        } else if ("quilt-loader".equals(loader)) {
            loaderCategory = "quilt";
        }

        String url = "https://api.modrinth.com/v2/search?query=" + query +
                "&limit=20&offset=0&index=downloads&facets=[[" +
                "\"categories:" + loaderCategory + "\"],[" +
                "\"versions:" + version + "\"],[" +
                "\"project_type:mod\"]]";

        Log.v("ModrinthSearch", "Request URL: " + url);

        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                String responseData = response.body().string();

                JSONObject jsonObject = new JSONObject(responseData);
                JSONArray results = jsonObject.getJSONArray("hits");

                StringBuilder resultString = new StringBuilder();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject mod = results.getJSONObject(i);
                    resultString.append(mod.getString("title")).append("\n");
                }

                String finalResult = resultString.toString();
                getActivity().runOnUiThread(() -> resultText.setText(finalResult.isEmpty() ? "No results found" : finalResult));

            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> resultText.setText("Error fetching data."));
            }
        }).start();
    }
}
