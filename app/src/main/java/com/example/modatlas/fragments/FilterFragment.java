package com.example.modatlas.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.modatlas.models.RecyclerViewInterface;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.views.FilterAdapter;


import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment implements RecyclerViewInterface {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILTER_ID = "param1";
    private String filterId;
    private RecyclerView recyclerView;
    private FilterAdapter filterAdapter;
    private ModrinthApi api;
    private List<FilterItem> tes = new ArrayList<>();
    private FilterTable filterTable;
    private FilterManager filterManager;

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
        recyclerView = view.findViewById(R.id.modFilterTable);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        filterAdapter = new FilterAdapter(getContext(),tes, FilterFragment.this);
        recyclerView.setAdapter(filterAdapter);
        addFilterSection();

        return view;
    }

    private void addCategoryFilter(){
        filterManager.setCategoryList(new CategoryCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
//                Log.i("test", categories.toString());
                tes.add(new FilterHeader("categories"));
                if (filterId.equals(URLString.modId)){
                    for (Category c:((ModFilter)filterManager).getMainCategory()) {
                        tes.add(new FilterTag(c.getName(),"categories"));
                    }
                } else if (filterId.equals(URLString.resourcePackId)) {
                    for (Category c:((ResourcePackFilter)filterManager).getMainCategory()) {
                        tes.add(new FilterTag(c.getName(),"categories"));
                    }
                    tes.add(new FilterHeader("features"));
                    for (Category c:((ResourcePackFilter)filterManager).getFeature()) {
                        tes.add(new FilterTag(c.getName(),"features"));
                    }
                    tes.add(new FilterHeader("resolutions"));
                    for (Category c:((ResourcePackFilter)filterManager).getResolution()) {
                        tes.add(new FilterTag(c.getName(),"resolutions"));
                    }
                } else if (filterId.equals(URLString.dataPackId)) {
                    for (Category c:((DataPackFilter)filterManager).getMainCategory()) {
                        tes.add(new FilterTag(c.getName(),"categories"));
                    }
                } else if (filterId.equals(URLString.shaderId)) {
                    for (Category c:((ShaderFilter)filterManager).getMainCategory()) {
                        tes.add(new FilterTag(c.getName(),"categories"));
                    }
                    tes.add(new FilterHeader("features"));
                    for (Category c:((ShaderFilter)filterManager).getFeature()) {
                        tes.add(new FilterTag(c.getName(),"features"));
                    }
                    tes.add(new FilterHeader("performance impact"));
                    for (Category c:((ShaderFilter)filterManager).getPerformanceImpact()) {
                        tes.add(new FilterTag(c.getName(),"performance impact"));
                    }
                } else if (filterId.equals(URLString.modPackId)) {
                    for (Category c:((ModPackFilter)filterManager).getMainCategory()) {
                        tes.add(new FilterTag(c.getName(),"categories"));
                    }
                } else if (filterId.equals(URLString.pluginId)) {
                    for (Category c:((PluginFilter)filterManager).getMainCategory()) {
                        tes.add(new FilterTag(c.getName(),"categories"));
                    }
                }
                for (FilterItem i : tes) {
                    if (filterTable.haveVersion(i.getHeader() + ":" + i.getItemName())) {
                        i.setSelected(true);
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
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
                tes.add(new FilterHeader("versions"));
                for (GameVersion v: gameVersions) {
                    tes.add(new FilterTag(v.getVersion(),"versions"));
                }
                for (FilterItem i : tes) {
                    if (filterTable.haveVersion(i.getHeader() + ":" + i.getItemName())) {
                        i.setSelected(true);
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
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
                    tes.add(new FilterHeader("loader"));
                    if (filterId.equals(URLString.modId)){
                        for (Loader l : ((ModFilter) filterManager).getMainLoader()) {
                            tes.add(new FilterTag(l.getName(), "loader"));
                        }
                    } else if (filterId.equals(URLString.shaderId)) {
                        for (Loader l : ((ShaderFilter) filterManager).getMainLoader()) {
                            tes.add(new FilterTag(l.getName(), "loader"));
                        }
                    } else if (filterId.equals(URLString.modPackId)) {
                        for (Loader l : ((ModPackFilter) filterManager).getMainLoader()) {
                            tes.add(new FilterTag(l.getName(), "loader"));
                        }
                    } else if (filterId.equals(URLString.pluginId)) {
                        for (Loader l : ((PluginFilter) filterManager).getMainLoader()) {
                            tes.add(new FilterTag(l.getName(), "loader"));
                        }
                    }
                    for (FilterItem i : tes) {
                        if (filterTable.haveVersion(i.getHeader() + ":" + i.getItemName())) {
                            i.setSelected(true);
                        }
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
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

    private void removeVersionFilter(){
        for (GameVersion v:filterManager.getGameVersionList()) {
            tes.removeIf(item -> item.getItemName().equals(v.getVersion()));
        }
    }

    private void removeCategoryFilter(){
        for (Category c: filterManager.getCategoryList()) {
            tes.removeIf(item -> item.getItemName().equals(c.getName()) && item.getHeader().equals("categories"));
        }
    }

    private void removeLoaderFilter(){
        for (Loader l: filterManager.getLoader()) {
            tes.removeIf(item -> item.getItemName().equals(l.getName()));
        }
    }

    private void removeFeatureFilter(){
        for (Category c: filterManager.getCategoryList()) {
            tes.removeIf(item -> item.getItemName().equals(c.getName()) && item.getHeader().equals("features"));
        }
    }

    private void removeResolutionFilter(){
        for (Category c: filterManager.getCategoryList()) {
            tes.removeIf(item -> item.getItemName().equals(c.getName()) && item.getHeader().equals("resolutions"));
        }
    }

    private void removePerformanceImpactFilter(){
        for (Category c: filterManager.getCategoryList()) {
            tes.removeIf(item -> item.getItemName().equals(c.getName()) && item.getHeader().equals("performance impact"));
        }
    }

    private void removeFilterSection(String header){
        if (header.equals("versions")){
            removeVersionFilter();
        } else if (header.equals("categories")) {
            removeCategoryFilter();
        } else if (header.equals("loader")) {
            removeLoaderFilter();
        } else if (header.equals("features")) {
            removeFeatureFilter();
        } else if (header.equals("resolutions")) {
            removeResolutionFilter();
        } else if (header.equals("performance impact")) {
            removePerformanceImpactFilter();
        }
    }

    private void recoverVersionFilter(String header, Integer headerPosition){
        for (GameVersion v:filterManager.getGameVersionList()) {
            tes.add(headerPosition + 1, new FilterTag(v.getVersion(),header));
            headerPosition++;
        }
    }

    private void recoverCategoryFilter(String header, Integer headerPosition){
        if (this.filterId.equals(URLString.modId)){
            for (Category c: ((ModFilter)filterManager).getMainCategory()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.dataPackId)) {
            for (Category c: ((DataPackFilter)filterManager).getMainCategory()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.resourcePackId)) {
            for (Category c: ((ResourcePackFilter)filterManager).getMainCategory()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.shaderId)) {
            for (Category c: ((ShaderFilter)filterManager).getMainCategory()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.modPackId)) {
            for (Category c: ((ModPackFilter)filterManager).getMainCategory()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.pluginId)) {
            for (Category c: ((PluginFilter)filterManager).getMainCategory()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        }
    }

    private void recoverLoaderFilter(String header, Integer headerPosition){
        if (this.filterId.equals(URLString.modId)){
            for (Loader l:((ModFilter)filterManager).getMainLoader()) {
                tes.add(headerPosition + 1, new FilterTag(l.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.shaderId)) {
            for (Loader l:((ShaderFilter)filterManager).getMainLoader()) {
                tes.add(headerPosition + 1, new FilterTag(l.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.modPackId)) {
            for (Loader l:((ModPackFilter)filterManager).getMainLoader()) {
                tes.add(headerPosition + 1, new FilterTag(l.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.pluginId)) {
            for (Loader l:((PluginFilter)filterManager).getMainLoader()) {
                tes.add(headerPosition + 1, new FilterTag(l.getName(),header));
                headerPosition++;
            }
        }
    }

    private void recoverFeatureFilter(String header, Integer headerPosition){
        if (this.filterId.equals(URLString.resourcePackId)){
            for (Category c:((ResourcePackFilter)filterManager).getFeature()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        } else if (this.filterId.equals(URLString.shaderId)) {
            for (Category c:((ShaderFilter)filterManager).getFeature()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        }
    }

    private void recoverResolutionFilter(String header, Integer headerPosition){
        if (this.filterId.equals(URLString.resourcePackId)){
            for (Category c:((ResourcePackFilter)filterManager).getResolution()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        }
    }

    private void recoverPerformanceImpactFilter(String header, Integer headerPosition){
        if (this.filterId.equals(URLString.shaderId)){
            for (Category c:((ShaderFilter)filterManager).getPerformanceImpact()) {
                tes.add(headerPosition + 1, new FilterTag(c.getName(),header));
                headerPosition++;
            }
        }
    }

    private void recoverFilterSection(String header, Integer headerPosition){
        if (header.equals("versions")){
            recoverVersionFilter(header,headerPosition);
        } else if (header.equals("categories")) {
            recoverCategoryFilter(header,headerPosition);
        } else if (header.equals("loader")) {
            recoverLoaderFilter(header,headerPosition);
        } else if (header.equals("features")) {
            recoverFeatureFilter(header,headerPosition);
        } else if (header.equals("resolutions")) {
            recoverResolutionFilter(header,headerPosition);
        }else if (header.equals("performance impact")) {
            recoverPerformanceImpactFilter(header,headerPosition);
        }
    }

    @Override
    public void onItemClick(int position) {
        String selectedTag = tes.get(position).getItemName();
        String header = "";
        int headerPosition = position;
        if (tes.get(position).isTag()){
            header = tes.get(position).getHeader();
            Log.i( "test",selectedTag);

            if (!tes.get(position).isSelected()){
                tes.get(position).setSelected(true);
//                tes.get(position);
            } else {
                tes.get(position).setSelected(false);
            }

            if (!filterTable.haveVersion(header+":"+selectedTag)){
                filterTable.addVersion(header+":"+selectedTag);
            } else {
                filterTable.removeVersion(header+":"+selectedTag);
            }
            recyclerView.getAdapter().notifyItemChanged(position);
        } else {
            Log.i("test",selectedTag);
            header = tes.get(position).getHeader();
            boolean matched = false;
            for (FilterItem i:tes) {
//                Log.i("test",i.getItemName());
                if (i.isTag() && (i.getHeader().equals(header))){
                    removeFilterSection(header);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                recoverFilterSection(header,headerPosition);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }


        List<String> versions = filterTable.getSelectedVersions().getValue();

        if (versions != null) {
            Log.i("test", "Selected versions: " + versions.toString());
        } else {
            Log.i("test", "Selected versions: null");
        }
    }
}