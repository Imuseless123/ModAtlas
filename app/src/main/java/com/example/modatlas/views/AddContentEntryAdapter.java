package com.example.modatlas.views;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.models.Mod;

import java.util.List;

public class AddContentEntryAdapter extends RecyclerView.Adapter<AddContentEntryAdapter.AddContentEntryViewHolder> {
    private List<Mod> modList;

    public AddContentEntryAdapter(List<Mod> modList) {
        this.modList = modList;
    }

    @NonNull
    @Override
    public AddContentEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddContentEntryViewHolder(new AddContentEntryView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull AddContentEntryViewHolder holder, int position) {
        holder.bind(modList.get(position));
    }

    @Override
    public int getItemCount() {
        return modList.size();
    }

    static class AddContentEntryViewHolder extends RecyclerView.ViewHolder {
        private AddContentEntryView addContentEntryView;

        public AddContentEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            addContentEntryView = (AddContentEntryView) itemView;
        }

        public void bind(Mod mod) {
            addContentEntryView.bind(mod);
        }
    }
}
