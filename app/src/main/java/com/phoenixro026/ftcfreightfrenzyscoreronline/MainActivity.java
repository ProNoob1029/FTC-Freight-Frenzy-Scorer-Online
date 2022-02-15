package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenixro026.ftcfreightfrenzyscoreronline.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
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
        });*/



        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        fab = binding.fab;
        fab.setVisibility(View.GONE);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.nav_host_fragment, SignInFragment.class, null)
                    .commit();
        }

        /*navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(R.navigation.nav_graph_java);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.MainFragment) {
                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            navController.navigate(R.id.action_MainFragment_to_NewPostFragment);
                        }
                    });
                } else {
                    fab.setVisibility(View.GONE);
                }
            }
        });*/
    }


}