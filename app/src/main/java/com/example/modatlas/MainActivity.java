package com.example.modatlas;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

public class MainActivity extends AppCompatActivity {

    private static final String state = "State";

    private Button signInButton;

    private ImageView option;

    private ImageView mods;

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

        this.signInButton = findViewById(R.id.signInButton);
        this.option = findViewById(R.id.option);
        this.mods = findViewById(R.id.mods);

        this.mods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchModsIntent = new Intent(MainActivity.this, SearchMods.class);
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