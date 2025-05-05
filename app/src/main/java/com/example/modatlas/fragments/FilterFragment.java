package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.modatlas.R;
import com.example.modatlas.models.Category;
import com.example.modatlas.models.CategoryCallback;
import com.example.modatlas.models.DataPackFilter;
import com.example.modatlas.models.FilterHeader;
import com.example.modatlas.models.FilterItem;
import com.example.modatlas.models.FilterManager;
import com.example.modatlas.models.GameVersionCallback;
import com.example.modatlas.models.Loader;
import com.example.modatlas.models.LoaderCallback;
import com.example.modatlas.models.ModFilter;
import com.example.modatlas.models.ModPackFilter;
import com.example.modatlas.models.PluginFilter;
import com.example.modatlas.models.ResourcePackFilter;
import com.example.modatlas.models.ShaderFilter;
import com.example.modatlas.models.URLString;
import com.example.modatlas.viewmodels.FilterTable;
import com.example.modatlas.models.FilterTag;
import com.example.modatlas.models.GameVersion;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.RetrofitClient;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILTER_ID = "param1";
    private String filterId;
    private ModrinthApi api;
    private List<FilterItem> filterItemList = new ArrayList<>();
    private FilterTable filterTable;
    private FilterManager filterManager;
    private TabLayout tabLayout;
    private ChipGroup chipGroup;
    private String versionDivider = "0";

    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance(String filterId) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILTER_ID, filterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterTable = new ViewModelProvider(requireActivity()).get(FilterTable.class);
        if (getArguments() != null) {
            filterId = getArguments().getString(ARG_FILTER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        chipGroup = view.findViewById(R.id.chip_group);


        Log.i("test",filterId);
//        GameVersionFilter gameVersionFilter = new GameVersionFilter(gameVersionAdapter, recyclerView,getContext(),FilterFragment.this);


        this.api = RetrofitClient.getApi();
        this.filterManager = new FilterManager();
        if (this.filterId.equals(URLString.modId)){
            Log.i("test","set up mod filter");
            this.filterManager = new ModFilter();
        } else if (this.filterId.equals(URLString.dataPackId)) {
            Log.i("test","set up data pack filter");
            this.filterManager = new DataPackFilter();
        } else if (this.filterId.equals(URLString.modPackId)) {
            Log.i("test","set up mod pack filter");
            this.filterManager = new ModPackFilter();
        } else if (this.filterId.equals(URLString.pluginId)) {
            Log.i("test","set up plugin filter");
            this.filterManager = new PluginFilter();
        } else if (this.filterId.equals(URLString.resourcePackId)) {
            Log.i("test","set up resource pack filter");
            this.filterManager = new ResourcePackFilter();
        } else if (this.filterId.equals(URLString.shaderId)) {
            Log.i("test","set up shader filter");
            this.filterManager = new ShaderFilter();
        }


        addFilterSection();
        // Select the first tab to trigger chip display
        if (tabLayout.getTabCount() > 0) {
            TabLayout.Tab firstTab = tabLayout.getTabAt(0);
            if (firstTab != null) {
                firstTab.select(); // triggers `onTabSelected`
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedHeader = tab.getText().toString();
                chipGroup.removeAllViews();
                for (FilterItem item : filterItemList) {
                    if (item instanceof FilterTag && item.getHeader().equals(selectedHeader)) {
                        addChip(item);
                    }
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void addChip(FilterItem filterItem){
        if (filterItem.getHeader().equals("versions")){
            String input = filterItem.getItemName();
            String[] parts = input.split("\\.");
            String result = "";
            if (parts.length >= 2) {
                result = parts[0] + "." + parts[1] + ".";
            }
            if (!result.equals(versionDivider)){
                Log.i("test","version divider: "+result);
                versionDivider = result;
                Chip divider = new Chip(getContext());
                divider.setText(result+"x");
                divider.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                divider.setTextColor(ContextCompat.getColor(getContext(), R.color.on_primary));

                // Stretch to match parent width
                ChipGroup.LayoutParams params = new ChipGroup.LayoutParams(
                        ChipGroup.LayoutParams.MATCH_PARENT,
                        ChipGroup.LayoutParams.WRAP_CONTENT
                );
                divider.setLayoutParams(params);

                divider.setChipBackgroundColorResource(R.color.primary); // Optional styling
                divider.setClickable(false);
                divider.setCheckable(false);

                chipGroup.addView(divider);
            }
        }
        Chip chip = new Chip(getContext());
        chip.setText(filterItem.getItemName());
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        chip.setChipBackgroundColorResource(R.color.button_on_surface);
        chip.setTextColor(ContextCompat.getColor(getContext(), R.color.on_surface));

        if (filterItem.isSelected()){
            chip.setChecked(true);
        }
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("test", filterItem.getItemName() + " selected");
                    filterItem.setSelected(true);
                    if (!filterTable.haveVersion(filterItem.getHeader()+":"+filterItem.getItemName())){
                        filterTable.addVersion(filterItem.getHeader()+":"+filterItem.getItemName());
                    }
                } else {
                    Log.d("test", filterItem.getItemName() + " unselected");
                    filterItem.setSelected(false);
                    filterTable.removeVersion(filterItem.getHeader()+":"+filterItem.getItemName());
                }
            }
        });

        chipGroup.addView(chip);
    }

    private void addTab(String tab){
        tabLayout.addTab(tabLayout.newTab().setText(tab));
    }

    private void addCategoryFilter(){
        filterManager.setCategoryList(new CategoryCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
//                Log.i("test", categories.toString());
                filterItemList.add(new FilterHeader("categories"));
                addTab("categories");
                if (filterId.equals(URLString.modId)){
                    for (Category c:((ModFilter)filterManager).getMainCategory()) {
                        filterItemList.add(new FilterTag(c.getName(),"categories"));
                    }
                } else if (filterId.equals(URLString.resourcePackId)) {
                    for (Category c:((ResourcePackFilter)filterManager).getMainCategory()) {
                        filterItemList.add(new FilterTag(c.getName(),"categories"));
                    }
                    filterItemList.add(new FilterHeader("features"));
                    addTab("features");
                    for (Category c:((ResourcePackFilter)filterManager).getFeature()) {
                        filterItemList.add(new FilterTag(c.getName(),"features"));
                    }
                    filterItemList.add(new FilterHeader("resolutions"));
                    addTab("resolutions");
                    for (Category c:((ResourcePackFilter)filterManager).getResolution()) {
                        filterItemList.add(new FilterTag(c.getName(),"resolutions"));
                    }
                } else if (filterId.equals(URLString.dataPackId)) {
                    for (Category c:((DataPackFilter)filterManager).getMainCategory()) {
                        filterItemList.add(new FilterTag(c.getName(),"categories"));
                    }
                } else if (filterId.equals(URLString.shaderId)) {
                    for (Category c:((ShaderFilter)filterManager).getMainCategory()) {
                        filterItemList.add(new FilterTag(c.getName(),"categories"));
                    }
                    filterItemList.add(new FilterHeader("features"));
                    addTab("features");
                    for (Category c:((ShaderFilter)filterManager).getFeature()) {
                        filterItemList.add(new FilterTag(c.getName(),"features"));
                    }
                    filterItemList.add(new FilterHeader("performance impact"));
                    addTab("performance impact");
                    for (Category c:((ShaderFilter)filterManager).getPerformanceImpact()) {
                        filterItemList.add(new FilterTag(c.getName(),"performance impact"));
                    }
                } else if (filterId.equals(URLString.modPackId)) {
                    for (Category c:((ModPackFilter)filterManager).getMainCategory()) {
                        filterItemList.add(new FilterTag(c.getName(),"categories"));
                    }
                } else if (filterId.equals(URLString.pluginId)) {
                    for (Category c:((PluginFilter)filterManager).getMainCategory()) {
                        filterItemList.add(new FilterTag(c.getName(),"categories"));
                    }
                }
                for (FilterItem i : filterItemList) {
                    if (filterTable.haveVersion(i.getHeader() + ":" + i.getItemName())) {
                        i.setSelected(true);
                    }
                }
//                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("test", "API error: " + t.getMessage());
            }
        });
    }

    private void addVersionFilter(){
        filterManager.setGameVersionList(new GameVersionCallback(){

            @Override
            public void onGameVersionsLoaded(List<GameVersion> gameVersions) {
                filterItemList.add(new FilterHeader("versions"));
                addTab("versions");
                for (GameVersion v: gameVersions) {
                    filterItemList.add(new FilterTag(v.getVersion(),"versions"));
                }
                for (FilterItem i : filterItemList) {
                    if (filterTable.haveVersion(i.getHeader() + ":" + i.getItemName())) {
                        i.setSelected(true);
                    }
                }
//                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("test", "API error: " + t.getMessage());
            }
        });
    }

    private void addLoaderFilter() {
        if (this.filterId.equals(URLString.modId) || this.filterId.equals(URLString.shaderId) || this.filterId.equals(URLString.modPackId) || this.filterId.equals(URLString.pluginId) ) {
            filterManager.setLoader(new LoaderCallback() {
                @Override
                public void onLoadersLoaded(List<Loader> loaders) {
                    filterItemList.add(new FilterHeader("loader"));
                    addTab("loader");
                    if (filterId.equals(URLString.modId)){
                        for (Loader l : ((ModFilter) filterManager).getMainLoader()) {
                            filterItemList.add(new FilterTag(l.getName(), "loader"));
                        }
                    } else if (filterId.equals(URLString.shaderId)) {
                        for (Loader l : ((ShaderFilter) filterManager).getMainLoader()) {
                            filterItemList.add(new FilterTag(l.getName(), "loader"));
                        }
                    } else if (filterId.equals(URLString.modPackId)) {
                        for (Loader l : ((ModPackFilter) filterManager).getMainLoader()) {
                            filterItemList.add(new FilterTag(l.getName(), "loader"));
                        }
                    } else if (filterId.equals(URLString.pluginId)) {
                        for (Loader l : ((PluginFilter) filterManager).getMainLoader()) {
                            filterItemList.add(new FilterTag(l.getName(), "loader"));
                        }
                    }
                    for (FilterItem i : filterItemList) {
                        if (filterTable.haveVersion(i.getHeader() + ":" + i.getItemName())) {
                            i.setSelected(true);
                        }
                    }
//                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable t) {
                    Log.e("test", "API error: " + t.getMessage());
                }
            });
        }
    }


    private void addFilterSection(){
        addCategoryFilter();
        addLoaderFilter();
        addVersionFilter();
    }
}