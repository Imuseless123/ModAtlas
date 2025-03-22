package com.example.modatlas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.models.Mod;
import com.example.modatlas.models.ModrinthApi;
import com.example.modatlas.models.ModrinthResponse;
import com.example.modatlas.models.RetrofitClient;
import com.example.modatlas.models.urlString;
import com.example.modatlas.views.ModItemAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private ModrinthApi api;
    private String query="";
    private int currentPage=0;
    private List<Mod> mod;
    private RecyclerView modItems;

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
        Log.e("test","in SearchActivity");
        // Get the intent
        Intent intent = getIntent();
        String modName = urlString.getProjectType( intent.getStringExtra("id"));
        Log.e("test","mod name: "+modName);

        this.api = RetrofitClient.getApi();
        Log.e("test","load api");
        this.modItems = findViewById(R.id.mod_items);
        this.modItems.setLayoutManager(new LinearLayoutManager(this));
        this.api.searchFacetMod(query, 20, currentPage * 20,modName).enqueue(new Callback<ModrinthResponse>() {
            @Override
            public void onResponse(Call<ModrinthResponse> call, Response<ModrinthResponse> response) {
                if (response.body() != null) {
                    mod = response.body().getHits();
                    Log.e("test", mod.get(0).getTitle() + "\n" + mod.get(0).getAuthor() + "\n" + mod.get(0).getDownloads());

                    // Now that we have the data, update the RecyclerView
                    modItems.setAdapter(new ModItemAdapter(getApplicationContext(), mod));
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
}