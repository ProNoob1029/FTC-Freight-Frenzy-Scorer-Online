package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenixro026.ftcfreightfrenzyscoreronline.database.Match;
import com.phoenixro026.ftcfreightfrenzyscoreronline.recycleview.MatchListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private final List<Match> matchList = new ArrayList<>();
    public MatchListAdapter adapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new MatchListAdapter(new MatchListAdapter.MatchDiff());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

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

        mDatabase.child("matches").addChildEventListener(childEventListener);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(ListActivity.this, com.phoenixro026.ftcfreightfrenzyscoreronline.MainActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ListActivity.this, com.phoenixro026.ftcfreightfrenzyscoreronline.MainActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
