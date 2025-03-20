package com.example.modatlas.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.modatlas.R;
import com.example.modatlas.models.Mod;

public class AddContentEntryView extends LinearLayout {
    private ImageView icon;
    private TextView title, author, downloads;
    private Button actionButton;

    public AddContentEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddContentEntryView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setPadding(16, 16, 16, 16);
        setGravity(Gravity.CENTER_VERTICAL);

        // Icon
        icon = new ImageView(context);
        int iconSize = dpToPx(50);
        LayoutParams iconParams = new LayoutParams(iconSize, iconSize);
        icon.setLayoutParams(iconParams);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

        textContainer.addView(title);
        textContainer.addView(author);
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

        rightContainer.addView(downloads);
        rightContainer.addView(actionButton);
        addView(rightContainer);
    }

    public void bind(Mod mod) {
        title.setText(mod.getTitle());
        author.setText("by " + mod.getAuthor());
        downloads.setText(mod.getDownloads() + " downloads");

        // Load image from URL (using Glide/Picasso)
        Glide.with(getContext()).load(mod.getIconUrl()).into(icon);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
