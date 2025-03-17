package com.example.modatlas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String modpackName = modpacks.get(position);
        holder.textView.setText(modpackName);

        // Open ModpackDetailFragment on click
        holder.itemView.setOnClickListener(v -> {
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
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
