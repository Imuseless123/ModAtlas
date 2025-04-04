package com.example.modatlas.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;
import com.example.modatlas.models.Mod;

import java.util.List;

public class AddContentEntryAdapter extends RecyclerView.Adapter<AddContentEntryAdapter.AddContentEntryViewHolder> {
    private List<Mod> modList;
    private OnModButtonClickListener listener;
    private String loader;

    public interface OnModButtonClickListener {
        void onModButtonClick(Mod mod);
    }
    public AddContentEntryAdapter(List<Mod> modList,String loader, OnModButtonClickListener listener ) {
        this.modList = modList;
        this.listener = listener;
        this.loader = loader;
    }

    @NonNull
    @Override
    public AddContentEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddContentEntryViewHolder(new AddContentEntryView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull AddContentEntryViewHolder holder, int position) {
        holder.bind(modList.get(position),loader,listener);
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

        public void bind(Mod mod, String loader, OnModButtonClickListener listener) {
            addContentEntryView.bind(mod, loader);
            addContentEntryView.setOnActionButtonClickListener(slug -> {
                if (mod.getImportState() == LoadState.READY) {
                    mod.setImportState(LoadState.LOADING);
                    addContentEntryView.setImportState(LoadState.LOADING);
                }
                listener.onModButtonClick(mod);
            });
        }
    }
}
