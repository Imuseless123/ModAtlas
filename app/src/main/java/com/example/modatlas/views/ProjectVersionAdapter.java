package com.example.modatlas.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;
import com.example.modatlas.models.ProjectVersion;

import java.util.ArrayList;
import java.util.List;

public class ProjectVersionAdapter extends RecyclerView.Adapter<ProjectVersionViewHolder> {
    private List<ProjectVersion> projectVersion = new ArrayList<>();

    public ProjectVersionAdapter(List<ProjectVersion> projectVersion){
        this.projectVersion = projectVersion;
    }

    @NonNull
    @Override
    public ProjectVersionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mod_verison_item, parent, false);
        return new ProjectVersionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectVersionViewHolder holder, int position) {
        ProjectVersion item = projectVersion.get(position);
        holder.modVersion.setText(item.getVersionNumber());
        holder.gameVersion.setText(item.getGameVersions().get(0));
        holder.platform.setText(item.getLoaders().get(0));
    }

    @Override
    public int getItemCount() {
        return projectVersion.size();
    }
}
