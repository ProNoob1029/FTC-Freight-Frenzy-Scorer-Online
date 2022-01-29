package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private DatabaseReference mDatabase;
    private List<Match> matchList = new ArrayList<>();
    public MatchListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new MatchListAdapter(new MatchListAdapter.MatchDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // A new data item has been added, add it to the list
                MatchModel matchModel = dataSnapshot.getValue(MatchModel.class);
                Match match = new Match();
                match.id = dataSnapshot.getKey();
                match.teamName = matchModel.teamName;
                match.createTime = matchModel.createTime;
                List<Match> tempList = matchList;
                matchList.add(match);
                adapter.submitList(tempList);
                adapter.submitList(matchList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // A data item has changed
                if(dataSnapshot.exists()){
                    matchList.clear();
                    for (DataSnapshot dss:dataSnapshot.getChildren()){
                        MatchModel matchModel = dss.getValue(MatchModel.class);
                        Match match = new Match();
                        match.id = dss.getKey();
                        match.teamName = matchModel.teamName;
                        match.createTime = matchModel.createTime;
                        matchList.add(match);
                    }
                    adapter.submitList(matchList);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    matchList.clear();
                    for (DataSnapshot dss:dataSnapshot.getChildren()){
                        MatchModel matchModel = dss.getValue(MatchModel.class);
                        Match match = new Match();
                        match.id = dss.getKey();
                        match.teamName = matchModel.teamName;
                        match.createTime = matchModel.createTime;
                        matchList.add(match);
                    }
                    adapter.submitList(matchList);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                if(dataSnapshot.exists()){
                    matchList.clear();
                    for (DataSnapshot dss:dataSnapshot.getChildren()){
                        MatchModel matchModel = dss.getValue(MatchModel.class);
                        Match match = new Match();
                        match.id = dss.getKey();
                        match.teamName = matchModel.teamName;
                        match.createTime = matchModel.createTime;
                        matchList.add(match);
                    }
                    adapter.submitList(matchList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabase.child("matches").addChildEventListener(childEventListener);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
    }*/
}
