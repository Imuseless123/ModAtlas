package com.example.modatlas.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.modatlas.R;
import com.example.modatlas.models.Mod;

import java.text.DecimalFormat;
import java.util.List;

public class ModItemAdapter extends RecyclerView.Adapter<ModItemViewHolder> {

    Context context;
    List<Mod> mods;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Mod mod);
    }

    public ModItemAdapter(Context context, List<Mod> mods, OnItemClickListener listener) {
        this.context = context;
        this.mods = mods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ModItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModItemViewHolder(LayoutInflater.from(context).inflate(R.layout.mod_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ModItemViewHolder holder, int position) {
        Mod currentMod = mods.get(position);

        holder.modTitle.setText(mods.get(position).getTitle());
        holder.modAuthor.setText(mods.get(position).getAuthor());
        holder.modDownload.setText(ModItemViewHolder.formatLargeNumber(mods.get(position).getDownloads())+" downloads");
        holder.modDescription.setText(mods.get(position).getDescription());
        Glide.with(holder.itemView.getContext())
                .load(mods.get(position).getIconUrl()) // Load image from URL
                .into(holder.modImage); // Set image into ImageView
        // Set the gradient background
        holder.setGradientBackground(mods.get(position).getColor());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentMod);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mods.size();
    }
    public static String formatLargeNumber(long count) {
        if (count < 1000) return String.valueOf(count);
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.#");
        String value = format.format(count / Math.pow(1000, exp));
        return String.format("%s%c", value, "KMGTPE".charAt(exp - 1));
    }
}
