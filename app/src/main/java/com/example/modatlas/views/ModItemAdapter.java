package com.example.modatlas.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modatlas.R;
import com.example.modatlas.models.Mod;

import java.util.List;

public class ModItemAdapter extends RecyclerView.Adapter<ModItemViewHolder> {

    Context context;
    List<Mod> mods;

    public ModItemAdapter(Context context, List<Mod> mods) {
        this.context = context;
        this.mods = mods;
    }

    @NonNull
    @Override
    public ModItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModItemViewHolder(LayoutInflater.from(context).inflate(R.layout.mod_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModItemViewHolder holder, int position) {
        holder.modTitle.setText(mods.get(position).getTitle());
        holder.modAuthor.setText(mods.get(position).getAuthor());
        holder.modDownload.setText(String.valueOf(mods.get(position).getDownloads()));
        Glide.with(holder.itemView.getContext())
                .load(mods.get(position).getIconUrl()) // Load image from URL
                .into(holder.modImage); // Set image into ImageView
    }

    @Override
    public int getItemCount() {
        return mods.size();
    }
}
