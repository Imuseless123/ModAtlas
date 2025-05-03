package com.example.modatlas.fragments;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.modatlas.R;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.Project;
import com.example.modatlas.models.ProjectVersion;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.views.ProjectVersionAdapter;

import java.util.ArrayList;
import java.util.List;

import io.noties.markwon.Markwon;

import io.noties.markwon.image.ImagesPlugin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "param1";
    private boolean isChange = false;
    private String mProjectId;
    private Project project;
    private TextView markdownTextView;
    private TextView change;
    private RecyclerView projectVersionList;
    private ModrinthApi api;
    private List<ProjectVersion> projectVersion = new ArrayList<>();
    private ProjectVersionAdapter projectVersionAdapter;

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
        change = view.findViewById(R.id.change);
        projectVersionList = view.findViewById(R.id.modVersionList);
        projectVersionList.setLayoutManager(new LinearLayoutManager(getContext()));

        projectVersionAdapter = new ProjectVersionAdapter(projectVersion);
        projectVersionList.setAdapter(projectVersionAdapter);
        initListener();
        getProject();
        getProjectVersion();

        return view;
    }

    private void initListener(){
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChange){
                    isChange = true;
                    markdownTextView.setVisibility(INVISIBLE);
                    projectVersionList.setVisibility(VISIBLE);
                    change.setText("Go to Description");
                } else {
                    isChange = false;
                    projectVersionList.setVisibility(INVISIBLE);
                    markdownTextView.setVisibility(VISIBLE);
                    change.setText("Go to Mod versions");
                }
            }
        });
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
                            Log.e("test", "Response body is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<Project> call, Throwable t) {
                        Log.e("test", "API call failed: " + t.getMessage());
                    }
                });
    }

    private void getProjectVersion(){
        api.getProjectVersionById(mProjectId)
                .enqueue(new Callback<List<ProjectVersion>>() {
                    @Override
                    public void onResponse(Call<List<ProjectVersion>> call, Response<List<ProjectVersion>> response) {
                        if (response.body() != null) {
                            projectVersion.addAll(response.body());
                            projectVersionList.getAdapter().notifyDataSetChanged();
                        } else {
                            Log.e("test", "Response body is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProjectVersion>> call, Throwable t) {
                        Log.e("test", "API call failed: " + t.getMessage());
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
