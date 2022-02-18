package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenixro026.ftcfreightfrenzyscoreronline.database.Match;
import com.phoenixro026.ftcfreightfrenzyscoreronline.databinding.ActivityMainBinding;
import com.phoenixro026.ftcfreightfrenzyscoreronline.recycleview.MatchListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final List<Match> matchList = new ArrayList<>();
    public MatchListAdapter adapter;
    private FirebaseAuth mAuth;

    ActivityMainBinding binding;

    DatabaseReference mDatabase;

    AlertDialog.Builder builder;

    DialogInterface.OnClickListener changePasswordDialogClickListener;

    private static final String TAG = "MainActivity";

    public void showProgressBar() {
        binding.progressBarMain.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        binding.progressBarMain.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View view = binding.getRoot();

        Vibrator myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new MatchListAdapter(new MatchListAdapter.MatchDiff());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        binding.fab.setOnClickListener(v -> {
            myVib.vibrate(20);
            String value="new";
            startActivity(new Intent(MainActivity.this, ScorerActivity.class).putExtra("key", value).putExtra("id", "1"));
        });

        changePasswordDialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    showProgressBar();
                    mDatabase.child("email").get().addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            String emailAddress = task.getResult().getValue(String.class);
                            assert emailAddress != null;
                            mAuth.sendPasswordResetEmail(emailAddress)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            hideProgressBar();
                                            Log.d(TAG, "Email sent.");
                                            mAuth.signOut();
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Email sent",
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                        }else{
                                            hideProgressBar();
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Please sign in and try again!",
                                                    Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                        }
                                    });
                        }
                    });
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };

        builder = new AlertDialog.Builder(view.getContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        matchList.clear();
        adapter.submitList(matchList);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // A new data item has been added, add it to the list
                MatchModel matchModel = dataSnapshot.getValue(MatchModel.class);
                Match match = new Match();
                match.id = dataSnapshot.getKey();
                assert matchModel != null;
                match.teamName = matchModel.teamName;
                match.createTime = matchModel.createTime;
                match.points = matchModel.totalPoints;
                matchList.add(match);
                adapter.submitList(matchList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // A data item has changed
                if(dataSnapshot.exists()){
                    MatchModel matchModel = dataSnapshot.getValue(MatchModel.class);
                    Match match = new Match();
                    match.id = dataSnapshot.getKey();
                    assert matchModel != null;
                    match.teamName = matchModel.teamName;
                    match.createTime = matchModel.createTime;
                    match.points = matchModel.totalPoints;
                    List<Match> tempList = new ArrayList<>(matchList);
                    matchList.clear();
                    for (int i = 0; i < tempList.size(); i++){
                        if(tempList.get(i).id.equals(match.id))
                            matchList.add(match);
                        else matchList.add(tempList.get(i));
                    }
                    adapter.submitList(matchList);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Match match = new Match();
                    match.id = dataSnapshot.getKey();
                    List<Match> tempList = new ArrayList<>(matchList);
                    matchList.clear();
                    for (int i = 0; i < tempList.size(); i++){
                        if(!tempList.get(i).id.equals(match.id))
                            matchList.add(tempList.get(i));
                    }
                    adapter.submitList(matchList);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        // Check auth on Activity start
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        }else{
            final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            mDatabase =
                    FirebaseDatabase.getInstance().getReference().child("users").child(userId);


            mDatabase.child("matches").addChildEventListener(childEventListener);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        switch (i){
            case R.id.action_logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                break;
            case R.id.action_change_password:
                builder.setMessage("Are you sure?").setPositiveButton("Yes", changePasswordDialogClickListener)
                        .setNegativeButton("No", changePasswordDialogClickListener).show();
                break;
            case R.id.action_change_email:
                changeEmail();
                break;

        }

        return super.onOptionsItemSelected(item);


    }

    public EditText txt;
    EditText editTextName1;

    public void changeEmail(){


        AlertDialog.Builder alertName = new AlertDialog.Builder(this);

        editTextName1 = new EditText(MainActivity.this);
        alertName.setTitle("New email:");
        alertName.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(this);
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        alertName.setView(layoutName);

        alertName.setPositiveButton("Continue", (dialog, whichButton) -> {
            txt = editTextName1; // variable to collect user input
            collectInput(); // analyze input (txt) in this method
        });

        alertName.setNegativeButton("Cancel", (dialog, whichButton) -> {
            dialog.cancel(); // closes dialog
             // display the dialog
        });

        alertName.show();
    }

    public void collectInput(){
        // convert edit text to string
        String getInput = txt.getText().toString();
        if (TextUtils.isEmpty(getInput)) {
            editTextName1.setError("Required");
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            showProgressBar();
            Objects.requireNonNull(user).updateEmail(getInput)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            hideProgressBar();
                        }else{
                            hideProgressBar();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Please sign in and try again!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    });
        }
    }

}
