package com.example.modatlas.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;
import com.example.modatlas.fragments.ModpackDetailFragment;

import java.util.ArrayList;

public class ModpackAdapter extends RecyclerView.Adapter<ModpackAdapter.ViewHolder> {
    private ArrayList<String> modpacks;
    private final AppCompatActivity activity;

    public ModpackAdapter(ArrayList<String> modpacks, AppCompatActivity activity) {
        this.modpacks = modpacks;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modpack, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String modpackName = modpacks.get(position);
        holder.tvModpackName.setText(modpackName);

        // Open ModpackDetailFragment when button is clicked
        holder.btnOpenModpack.setOnClickListener(v -> {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(android.R.id.content, ModpackDetailFragment.newInstance(modpackName));
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return modpacks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvModpackName;
        Button btnOpenModpack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModpackName = itemView.findViewById(R.id.tvModpackName);
            btnOpenModpack = itemView.findViewById(R.id.btnOpenModpack);
        }
    }
}
