package com.example.modatlas.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.modatlas.R;
import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModFile;
import com.example.modatlas.models.ModVersion;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AddContentEntryView extends RelativeLayout {
    private ImageView icon;
    private TextView title, author, downloads, categoryText;
    private MaterialButton actionButton;
    private OnActionButtonClickListener buttonClickListener;
    private String slug;

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
}
