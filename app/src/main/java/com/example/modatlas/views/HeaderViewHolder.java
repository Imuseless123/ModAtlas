package com.example.modatlas.views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;
import com.example.modatlas.models.RecyclerViewInterface;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    TextView filterHeader;
    public HeaderViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        filterHeader = itemView.findViewById(R.id.filter_header);
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
