package com.example.modatlas.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.R;
import com.example.modatlas.models.FilterHeader;
import com.example.modatlas.models.FilterItem;
import com.example.modatlas.viewmodels.FilterTable;
import com.example.modatlas.models.FilterTag;
import com.example.modatlas.models.RecyclerViewInterface;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final RecyclerViewInterface recyclerViewInterface;
    private List<FilterItem> filterList;
    static Context context;
    public FilterAdapter(Context context, List<FilterItem> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.filterList = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public int getItemViewType(int position) {
        FilterItem item = filterList.get(position);
        if (item instanceof FilterHeader) {
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
//            Log.i("test","TYPE_HEADER: "+viewType);
            View view = LayoutInflater.from(context).inflate(R.layout.filter_header, parent, false);
            return new HeaderViewHolder(view, recyclerViewInterface);
        } else {
//            Log.i("test","TYPE_ITEM: "+viewType);
            View view = LayoutInflater.from(context).inflate(R.layout.filter_tag, parent, false);
            return new ItemViewHolder(view, recyclerViewInterface);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FilterItem item = filterList.get(position);
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            FilterHeader header = (FilterHeader) item;
            headerHolder.filterHeader.setText(header.getItemName());

        }
        else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            FilterTag tag = (FilterTag) item;
            itemHolder.filterTag.setText(tag.getItemName());

            // Show checkmark if selected
            if (tag.isSelected()) {
                itemHolder.checkIcon.setVisibility(View.VISIBLE);
//                itemHolder.itemView.setBackgroundResource(R.drawable.selected_background);
            } else {
                itemHolder.checkIcon.setVisibility(View.GONE);
                itemHolder.itemView.setBackground(null);
            }

        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView filterHeader;
        public HeaderViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            filterHeader = itemView.findViewById(R.id.filter_header);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView filterTag;
        ImageView checkIcon;
        FilterTable vm;
        public ItemViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            filterTag = itemView.findViewById(R.id.filter_tag);
            checkIcon = itemView.findViewById(R.id.check_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
