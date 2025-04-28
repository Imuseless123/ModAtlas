package com.example.modatlas.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.modatlas.R;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.Project;
import com.example.modatlas.models.RetrofitClient;

import io.noties.markwon.Markwon;

import io.noties.markwon.image.ImagesPlugin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "param1";

    private String mProjectId;
    private Project project;
    private TextView markdownTextView;
    private ModrinthApi api;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String projectId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProjectId = getArguments().getString(ARG_PROJECT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        markdownTextView = view.findViewById(R.id.markdownText);
        api = RetrofitClient.getApi();

        getProject();

        return view;
    }

    private void getProject() {
        api.getProjectById(mProjectId)
                .enqueue(new Callback<Project>() {
                    @Override
                    public void onResponse(Call<Project> call, Response<Project> response) {
                        if (response.body() != null) {
                            project = response.body();
                            setUpMarkDown(project.getBody());
                        } else {
                            Log.e("DetailsFragment", "Response body is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<Project> call, Throwable t) {
                        Log.e("DetailsFragment", "API call failed: " + t.getMessage());
                    }
                });
    }

    private void setUpMarkDown(String markdown) {
        // Correct way: use builder
        Markwon markwon = Markwon.builder(requireContext())
                .usePlugin(ImagesPlugin.create())
                .build();

        // Now render markdown with images
        markwon.setMarkdown(markdownTextView, markdown);
    }

}
