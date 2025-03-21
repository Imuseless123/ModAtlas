package com.example.modatlas.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;
import com.example.modatlas.models.ModFile;

import java.util.List;

public class ModFileAdapter extends RecyclerView.Adapter<ModFileAdapter.ModFileViewHolder> {
    private List<ModFile> modFileList;
    private final OnRemoveClickListener removeClickListener;

    public interface OnRemoveClickListener {
        void onRemoveClick(ModFile modFile);
    }

    public ModFileAdapter(List<ModFile> modFileList, OnRemoveClickListener listener) {
        this.modFileList = modFileList;
        this.removeClickListener = listener;
    }

    @NonNull
    @Override
    public ModFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mod_file, parent, false);
        return new ModFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModFileViewHolder holder, int position) {
        ModFile modFile = modFileList.get(position);
        holder.textModFileName.setText(modFile.getFilename());

        holder.btnRemove.setOnClickListener(v -> removeClickListener.onRemoveClick(modFile));
    }

    @Override
    public int getItemCount() {
        return modFileList.size();
    }

    public void updateList(List<ModFile> newList) {
        this.modFileList = newList;
        notifyDataSetChanged();
    }

    public static class ModFileViewHolder extends RecyclerView.ViewHolder {
        TextView textModFileName;
        Button btnRemove;

        public ModFileViewHolder(@NonNull View itemView) {
            super(itemView);
            textModFileName = itemView.findViewById(R.id.textModFileName);
            btnRemove = itemView.findViewById(R.id.btnRemoveMod);
        }
    }
}
