package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.phoenixro026.ftcfreightfrenzyscoreronline.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Vibrator myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        binding.buttonNew.setOnClickListener(v -> {
            myVib.vibrate(20);
            String value="new";
            startActivity(new Intent(MainActivity.this, ScorerActivity.class).putExtra("key", value).putExtra("id", "1"));
        });

        binding.buttonList.setOnClickListener(v -> {
            myVib.vibrate(20);
            startActivity(new Intent(MainActivity.this, com.phoenixro026.ftcfreightfrenzyscoreronline.ListActivity.class));
        });

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}