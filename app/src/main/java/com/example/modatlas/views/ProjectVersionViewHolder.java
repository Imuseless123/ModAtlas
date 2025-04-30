package com.example.modatlas.views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;

public class ProjectVersionViewHolder extends RecyclerView.ViewHolder {
    TextView modVersion;
    TextView gameVersion;
    TextView platform;
    public ProjectVersionViewHolder(@NonNull View itemView) {
        super(itemView);

        modVersion = itemView.findViewById(R.id.modVersion);
        gameVersion = itemView.findViewById(R.id.gameVersion);
        platform = itemView.findViewById(R.id.platform);
    }
}
