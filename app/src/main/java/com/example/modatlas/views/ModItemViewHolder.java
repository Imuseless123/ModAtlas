package com.example.modatlas.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;

public class ModItemViewHolder extends RecyclerView.ViewHolder{
    ImageView modImage;
    TextView modTitle;
    TextView modAuthor;
    TextView modDownload;
    public ModItemViewHolder(@NonNull View itemView) {
        super(itemView);
        modImage = itemView.findViewById(R.id.modImage);
        modTitle = itemView.findViewById(R.id.modTitle);
        modAuthor = itemView.findViewById(R.id.modAuthor);
        modDownload = itemView.findViewById(R.id.modDownload);
    }

}
