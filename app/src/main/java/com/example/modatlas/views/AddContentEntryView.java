package com.example.modatlas.views;

import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.modatlas.R;
import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModFile;
import com.example.modatlas.models.ModVersion;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContentEntryView extends LinearLayout {
    private ImageView icon;
    private TextView title, author, downloads, categoryText;
    private Button actionButton;
    private OnActionButtonClickListener buttonClickListener;
    private String slug; // Store slug for API request
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
        setOrientation(HORIZONTAL);
        setPadding(16, 16, 16, 16);
        setGravity(Gravity.CENTER_VERTICAL);

        // Set background with rounded corners
        GradientDrawable background = new GradientDrawable();
        background.setColor(ContextCompat.getColor(context, R.color.surface));
        background.setCornerRadius(dpToPx(16));
        setBackground(background);

        // Icon
        icon = new ImageView(context);
        int iconSize = dpToPx(50);
        LayoutParams iconParams = new LayoutParams(iconSize, iconSize);
        icon.setLayoutParams(iconParams);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        icon.setClipToOutline(true); // Enable rounded corners
        icon.setBackgroundResource(R.drawable.rounded_icon); // Define shape drawable
        addView(icon);

        // Middle section: Title & Author
        LinearLayout textContainer = new LinearLayout(context);
        textContainer.setOrientation(VERTICAL);
        textContainer.setPadding(16, 0, 16, 0);
        LayoutParams textParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        textContainer.setLayoutParams(textParams);

        title = new TextView(context);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setTextSize(16);

        author = new TextView(context);
        author.setTextSize(14);
        author.setTextColor(Color.GRAY);

        categoryText = new TextView(context);
        categoryText.setTextSize(14);
        categoryText.setTypeface(Typeface.DEFAULT_BOLD);
        categoryText.setPadding(0, dpToPx(4), 0, 0);

        textContainer.addView(title);
        textContainer.addView(author);
        textContainer.addView(categoryText);
        addView(textContainer);

        // Right section: Downloads & Button
        LinearLayout rightContainer = new LinearLayout(context);
        rightContainer.setOrientation(VERTICAL);
        rightContainer.setGravity(Gravity.END);

        downloads = new TextView(context);
        downloads.setTextSize(14);
        downloads.setTextColor(Color.DKGRAY);

        actionButton = new Button(context);
        actionButton.setText("Import");
        actionButton.setOnClickListener(v -> {
            if (buttonClickListener != null) {
                buttonClickListener.onActionButtonClick(slug);
            }
        });


        rightContainer.addView(downloads);
        rightContainer.addView(actionButton);
        addView(rightContainer);
    }

    public void bind(Mod mod, String loader) {
        title.setText(mod.getTitle());
        // Hide author if empty
        if (mod.getAuthor() == null || mod.getAuthor().isEmpty()) {
            author.setVisibility(GONE);
        } else {
            author.setText("by " + mod.getAuthor());
            author.setVisibility(VISIBLE);
        }
        // Set category text
        if (mod.getCategories().contains(loader)) {
            categoryText.setText("Native");
            categoryText.setTextColor(Color.GREEN);
        } else {
            categoryText.setText("Compatible");
            categoryText.setTextColor(Color.BLUE);
        }
        downloads.setText(mod.getDownloads() + " downloads");
        slug = mod.getSlug(); // Store slug for API request

        // Load image from URL (using Glide)
        Glide.with(getContext()).load(mod.getIconUrl()).into(icon);
    }



    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
