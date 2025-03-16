package com.example.modatlas;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String state = "State";
    private Button signInButton;
    private Spinner option;
    private ImageView mods;
    private ImageView resourcePacks;
    private ImageView dataPacks;
    private ImageView shaders;
    private ImageView modPacks;
    private ImageView plugins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(state,"onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.init();
    }

    private void init(){
        this.signInButton = findViewById(R.id.signInButton);
        this.option = findViewById(R.id.option);
        this.mods = findViewById(R.id.mods);
        this.resourcePacks = findViewById(R.id.resourcePacks);
        this.dataPacks = findViewById(R.id.dataPacks);
        this.shaders = findViewById(R.id.shaders);
        this.modPacks = findViewById(R.id.modPacks);
        this.plugins = findViewById(R.id.plugins);
        this.initListener();
    }

    private void initListener(){
        this.option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.mods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "mods");
                startActivity(searchModsIntent);
            }
        });

        this.resourcePacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "Resource Packs");
                startActivity(searchModsIntent);
            }
        });

        this.dataPacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "Data Packs");
                startActivity(searchModsIntent);
            }
        });

        this.shaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "Shaders");
                startActivity(searchModsIntent);
            }
        });

        this.modPacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "Mod Packs");
                startActivity(searchModsIntent);
            }
        });

        this.plugins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Pass a string value
                searchModsIntent.putExtra("id", "Plugins");
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
}