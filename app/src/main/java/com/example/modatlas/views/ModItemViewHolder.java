package com.example.modatlas.views;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;

import java.text.DecimalFormat;

public class ModItemViewHolder extends RecyclerView.ViewHolder{
    ImageView modImage;
    TextView modTitle;
    TextView modAuthor;
    TextView modDownload;
    TextView modDescription;
    ConstraintLayout gradientLayout;

    public ModItemViewHolder(@NonNull View itemView) {
        super(itemView);
        modImage = itemView.findViewById(R.id.modImage);
        modTitle = itemView.findViewById(R.id.modTitle);
        modAuthor = itemView.findViewById(R.id.modAuthor);
        modDownload = itemView.findViewById(R.id.modDownload);
        modDescription = itemView.findViewById(R.id.modDescription);
        gradientLayout = itemView.findViewById(R.id.gradientLayout);
    }

    public void setGradientBackground(Integer colorInt) {
        if (colorInt != null) {
            int startColor = ContextCompat.getColor(gradientLayout.getContext(), R.color.background);
            int endColor = Color.rgb(
                    (colorInt >> 16) & 0xFF, // Extract red
                    (colorInt >> 8) & 0xFF,  // Extract green
                    colorInt & 0xFF          // Extract blue
            );

            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{startColor, endColor}
            );
            gradientDrawable.setCornerRadius(16f); // Match the CardView's corner radius
            gradientLayout.setBackground(gradientDrawable);
        }
    }
    public static String formatLargeNumber(long count) {
        if (count < 1000) return String.valueOf(count);
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.#");
        String value = format.format(count / Math.pow(1000, exp));
        return String.format("%s%c", value, "KMGTPE".charAt(exp - 1));
    }
}
