package com.example.modatlas;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.fragments.DetailsFragment;
import com.example.modatlas.fragments.FilterFragment;
import com.example.modatlas.models.URLString;
import com.example.modatlas.viewmodels.FilterTable;
import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.ModrinthResponse;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.views.ModItemAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private ModrinthApi api;
    private String query;
    private String loader;
    private int currentPage;
    private List<Mod> mod;
    private RecyclerView modItems;
    private FragmentManager fragmentManager;
    private TextView openFilter;
    private TextView close;
    private static String searchId;
    private String facet;
    private boolean isLoading;
    private boolean isLastPage;
    private final int pageSize = 20;
    private boolean isFilterOpen;
    private FilterTable filterTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.init();
        filterTable.getSelectedVersions().observe(this, state ->{
            Log.i("test","saved tag: "+ state);
        });
    }

    private void init(){
        this.query = "";
        this.currentPage = 0;
        this.isLoading = false;
        this.isLastPage = false;
        this.isFilterOpen = false;
        this.loader = "";
        this.mod = new ArrayList<>();
        this.filterTable = new ViewModelProvider(this).get(FilterTable.class);
        Intent intent = getIntent();
        this.fragmentManager = getSupportFragmentManager();
        this.openFilter = findViewById(R.id.openFilter);
        this.close = findViewById(R.id.closeDetail);
        URLString.setProjectType(intent.getStringExtra("id"));
        this.searchId = URLString.facet;
        Log.i("test",this.searchId);
        this.facet = URLString.facet;
        this.api = RetrofitClient.getApi();
        this.modItems = findViewById(R.id.mod_items);
        this.modItems.setLayoutManager(new LinearLayoutManager(this));
        this.getMods();

        this.initListener();
    }

    private void initListener(){
        modItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null && !isLoading && !isLastPage) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize) {
                        loadMoreMods();
                    }
                }
            }
        });

        openFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFilterOpen){
                    replaceFilterFragment();
                    openFilter.setText("close filter");
                    isFilterOpen = true;
                } else {
                    popFilterFragment();
                    openFilter.setText("open filter");
                    isFilterOpen = false;
//                    filterTable.getVersionAt(0);
                    List<String> tag = filterTable.getSelectedVersions().getValue();
                    URLString.setProjectType(searchId);
                    URLString.resetLoader();
                    for (String s:tag) {
                        URLString.addFacet(s);
                        facet = URLString.facet;
                        loader = URLString.loader;
                        getMods();
                        Log.i("test",s);
                    }
                    Log.i("test",facet);
                    Log.i("test",loader);
                    if (tag != null) {
                        Log.i("test", "Selected versions: " + tag.toString());
                    } else {
                        Log.i("test", "Selected versions: null");
                    }
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterFragment();
                close.setVisibility(INVISIBLE);
            }
        });
    }

    private void getMods(){
        this.api.searchFacetMod(query, pageSize, currentPage * pageSize,this.facet,this.loader).enqueue(new Callback<ModrinthResponse>() {
            @Override
            public void onResponse(Call<ModrinthResponse> call, Response<ModrinthResponse> response) {
                if (response.body() != null) {
                    mod = response.body().getHits();
                    // Now that we have the data, update the RecyclerView
                    modItems.setAdapter(new ModItemAdapter(getApplicationContext(), mod, i -> {
                        // This is where you handle the click
//                        Toast.makeText(SearchActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        replaceDetailFragment(i.getProjectId());
                        close.setVisibility(VISIBLE);
                        // You could also start a new activity or show a dialog here
                    }));
                } else {
                    Log.e("test", "Response body is null");
                }
            }

            @Override
            public void onFailure(Call<ModrinthResponse> call, Throwable t) {
                Log.e("test", "API call failed: " + t.getMessage());
            }
        });
    }

    private void loadMoreMods() {
        isLoading = true;
        currentPage++;

        api.searchFacetMod(query, pageSize, currentPage * pageSize, this.facet,this.loader)
                .enqueue(new Callback<ModrinthResponse>() {
                    @Override
                    public void onResponse(Call<ModrinthResponse> call, Response<ModrinthResponse> response) {
                        if (response.body() != null && response.body().getHits() != null) {
                            List<Mod> newMods = response.body().getHits();
                            if (!newMods.isEmpty()) {
                                mod.addAll(newMods);
                                if (modItems.getAdapter() == null) {
                                    modItems.setAdapter(new ModItemAdapter(getApplicationContext(), mod, i->{
                                        // This is where you handle the click
//                                        Toast.makeText(SearchActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                                        replaceDetailFragment(i.getProjectId());
                                        close.setVisibility(VISIBLE);
                                        // You could also start a new activity or show a dialog here
                                    }));
                                } else {
                                    modItems.getAdapter().notifyDataSetChanged();
                                }
                            } else {
                                isLastPage = true;
                            }
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;
                    }

                    @Override
                    public void onFailure(Call<ModrinthResponse> call, Throwable t) {
                        Log.e("test", "API call failed: " + t.getMessage());
                        isLoading = false;
                    }
                });
    }

    private void replaceFilterFragment(){
        Fragment f = FilterFragment.newInstance(searchId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fillter,f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void replaceDetailFragment(String projectId){
        Fragment f = DetailsFragment.newInstance(projectId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.details,f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void popFilterFragment(){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}