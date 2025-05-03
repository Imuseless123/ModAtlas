package com.example.modatlas.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;
import com.example.modatlas.models.RecyclerViewInterface;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView filterTag;
    ImageView checkIcon;
    public ItemViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        filterTag = itemView.findViewById(R.id.filter_tag);
        checkIcon = itemView.findViewById(R.id.check_icon);
        itemView.setOnClickListener(v -> {
            if (recyclerViewInterface != null){
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION){
                    recyclerViewInterface.onItemClick(position);
                }
            }
        });
    }
}
