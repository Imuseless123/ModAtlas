package com.example.modatlas;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modatlas.fragments.CreateModpackFragment;
import com.example.modatlas.viewmodels.ModpackViewModel;
import com.example.modatlas.views.ModpackAdapter;

import java.io.File;
import java.util.ArrayList;

public class ModpackActivity extends AppCompatActivity {
    private ModpackViewModel modpackViewModel;
    private ArrayList<String> modpacks = new ArrayList<>();
    private ModpackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_pack);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        modpackViewModel = new ViewModelProvider(this).get(ModpackViewModel.class);

        Button btnCreateModpack = findViewById(R.id.btnCreateModpack);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewModpacks);

        loadModpacks(); // Load existing modpacks

        // Set up RecyclerView
        adapter = new ModpackAdapter(modpacks, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Button Click - Switch to Fragment
        btnCreateModpack.setOnClickListener(v -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(android.R.id.content, new CreateModpackFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
    private void loadModpacks() {
        modpacks.clear();
        File modpackDir = new File(getFilesDir(), getString(R.string.modpacks_folder)); // Internal storage path

        if (!modpackDir.exists()) {
            modpackDir.mkdir(); // Create directory if it doesn't exist
        }

        File[] files = modpackDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) { // Only add folders as modpacks
                    modpacks.add(file.getName());
                }
            }
        }
    }
    public void refreshModpacks() {
        loadModpacks();
        adapter.notifyDataSetChanged(); // Refresh UI
    }
}