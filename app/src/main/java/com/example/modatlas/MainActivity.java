package com.example.modatlas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String state = "State";
    private Button signInButton;
    private Button mrpackButton;
    private ImageView mods;
    private ImageView resourcePacks;
    private ImageView dataPacks;
    private ImageView shaders;
    private ImageView modPacks;
    private ImageView plugins;

    private boolean isSpinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(state,"onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        String modpacksFolder = getString(R.string.modpacks_folder);
        createModpackFolder(modpacksFolder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.init();
    }

    private void init(){
        this.signInButton = findViewById(R.id.signInButton);
        this.mrpackButton = findViewById(R.id.btnmrpack);
        this.mods = findViewById(R.id.mods);
        this.resourcePacks = findViewById(R.id.resourcePacks);
        this.dataPacks = findViewById(R.id.dataPacks);
        this.shaders = findViewById(R.id.shaders);
        this.modPacks = findViewById(R.id.modPacks);
        this.plugins = findViewById(R.id.plugins);
        this.initListener();
    }

    private void initListener(){
        this.mrpackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ModPackIntent = new Intent(MainActivity.this, ModpackActivity.class);
                startActivity(ModPackIntent);
            }
        });

        this.mods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "mod");
                startActivity(searchModsIntent);
            }
        });

        this.resourcePacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "resourcepack");
                startActivity(searchModsIntent);
            }
        });

        this.dataPacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "datapack");
                startActivity(searchModsIntent);
            }
        });

        this.shaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "shader");
                startActivity(searchModsIntent);
            }
        });

        this.modPacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "modpack");
                startActivity(searchModsIntent);
            }
        });

        this.plugins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "plugin");
                startActivity(searchModsIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(state,"onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(state,"onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(state,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(state,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(state,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(state,"onStop");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(state,"onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(state,"onSaveInstanceState");
    }


    private void createModpackFolder(String folderName) {
        File modpackDir = new File(getFilesDir(), folderName);
        if (!modpackDir.exists()) {
            boolean success = modpackDir.mkdir();
            if (success) {
                Log.d("ModpackManager", "Modpack folder created at: " + modpackDir.getAbsolutePath());
            } else {
                Log.e("ModpackManager", "Failed to create modpack folder");
            }
        }
    }
}