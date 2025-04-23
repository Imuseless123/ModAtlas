package com.example.modatlas.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.modatlas.R;
import com.example.modatlas.models.Mod;
import com.google.android.material.button.MaterialButton;

public class AddContentEntryView extends RelativeLayout {
    private ImageView icon;
    private TextView title, author, downloads, categoryText;
    private MaterialButton actionButton;
    private OnActionButtonClickListener buttonClickListener;
    private String slug;
    private LoadState importState = LoadState.READY;


    public interface OnActionButtonClickListener {
        void onActionButtonClick(String slug);
    }

    public void setOnActionButtonClickListener(OnActionButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public AddContentEntryView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_add_content_entry, this, true);

        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        downloads = findViewById(R.id.downloads);
        categoryText = findViewById(R.id.categoryText);
        actionButton = findViewById(R.id.actionButton);

        actionButton.setOnClickListener(v -> {
            if (buttonClickListener != null) {
                setImportState(LoadState.LOADING);
                buttonClickListener.onActionButtonClick(slug);
            }
        });
    }

    public void bind(Mod mod, String loader) {
        title.setText(mod.getTitle());

        if (mod.getAuthor() == null || mod.getAuthor().isEmpty()) {
            author.setVisibility(GONE);
        } else {
            author.setText("by " + mod.getAuthor());
            author.setVisibility(VISIBLE);
        }

        if (mod.getCategories().contains(loader)) {
            categoryText.setText("Native");
            categoryText.setTextColor(Color.GREEN);
        } else {
            categoryText.setText("Compatible");
            categoryText.setTextColor(Color.BLUE);
        }

        downloads.setText(formatDownloads(mod.getDownloads()));
        slug = mod.getSlug();

        Glide.with(getContext()).load(mod.getIconUrl()).into(icon);
        setImportState(mod.getImportState());
    }

    private String formatDownloads(int downloads) {
        if (downloads >= 1_000_000) {
            return (downloads / 1_000_000) + "M downloads";
        } else if (downloads >= 1_000) {
            return (downloads / 1_000) + "K downloads";
        } else {
            return downloads + " downloads";
        }
    }
    public void setImportState(LoadState state) {
        this.importState = state;

        switch (state) {
            case READY:
                actionButton.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.import_icon));
                actionButton.setEnabled(true);
                break;

            case LOADING:
                actionButton.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.dots_horizontal));
                actionButton.setEnabled(false); // Disable while importing
                break;

            case DONE:
                actionButton.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.check));
                actionButton.setEnabled(false); // Disable after import completes
                break;
        }
    }

}
