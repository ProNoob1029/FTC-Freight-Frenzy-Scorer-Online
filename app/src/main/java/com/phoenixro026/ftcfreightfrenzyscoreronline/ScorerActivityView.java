package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phoenixro026.ftcfreightfrenzyscoreronline.databinding.ActivityScorerViewBinding;

import java.util.Locale;
import java.util.Objects;

public class ScorerActivityView extends AppCompatActivity{

    protected ActivityScorerViewBinding binding;

    public String teamColor;

    ///Autonomous
    public int autoTotalPoints = 0;
    public boolean duckDelivery = false;
    public int autoStorage = 0;
    public int autoHub = 0;
    public boolean freightBonus = false;
    public boolean teamElementUsed = false;
    public boolean autoParkedInStorage = false;
    public boolean autoParkedInWarehouse = false;
    public boolean autoParkedFully = false;

    ///Driver
    public int driverTotalPoints = 0;
    public int driverStorage = 0;
    public int driverHubL1 = 0;
    public int driverHubL2 = 0;
    public int driverHubL3 = 0;
    public int driverShared = 0;

    ///Endgame
    public int endgameTotalPoints = 0;
    public int carouselDucks = 0;
    public boolean balancedShipping = false;
    public boolean leaningShared = false;
    public boolean endgameParked = false;
    public boolean endgameFullyParked = false;
    public int capping = 0;

    //Penalties
    public int penaltiesTotal = 0;
    public int penaltiesMinor = 0;
    public int penaltiesMajor = 0;

    //Total
    public int totalPoints = 0;

    String key;
    String matchId;
    MatchModel match = new MatchModel();

    //DatabaseReference mRootRef;
    DatabaseReference mMatchesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScorerViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Vibrator myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("key");
            matchId = extras.getString("id");
            //The key argument here must match that used in the other activity
        }

        final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mMatchesRef =
                FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("matches");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    match = dataSnapshot.getValue(MatchModel.class);
                    InsertValues(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mMatchesRef.child(matchId).addValueEventListener(postListener);

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Delete();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        binding.buttonEdit.setOnClickListener(v -> {
            myVib.vibrate(20);
            String value="edit";
            startActivity(new Intent(ScorerActivityView.this, ScorerActivity.class).putExtra("key", value).putExtra("id", matchId));
        });

        binding.buttonDelete.setOnClickListener(v -> {
            myVib.vibrate(20);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });
    }

    void Delete(){
        mMatchesRef.child(matchId).removeValue();
        finish();
    }

    public void InsertValues(View view) {
        binding.textTeamName.setText(match.teamName);
        binding.textTeamCode.setText(match.teamCode);
        if(match.teamColor.contentEquals("red")) {
            binding.buttonTeamRed.setTextAppearance(view.getContext(), R.style.color_button_theme);
            binding.buttonTeamRed.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.button_shape_red));
            binding.buttonTeamBlue.setTextAppearance(view.getContext(), R.style.Theme_FTC_FREIGHTFRENZY_Scorer);
            binding.buttonTeamBlue.setBackgroundColor(getResources().getColor(R.color.zero));
            teamColor = "red";
        }else {
            binding.buttonTeamBlue.setTextAppearance(view.getContext(), R.style.color_button_theme);
            binding.buttonTeamBlue.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.button_shape_blue));
            binding.buttonTeamRed.setTextAppearance(view.getContext(), R.style.Theme_FTC_FREIGHTFRENZY_Scorer);
            binding.buttonTeamRed.setBackgroundColor(getResources().getColor(R.color.zero));
            teamColor = "blue";
        }

        ///Autonomous
        autoTotalPoints = match.autoTotalPoints;
        duckDelivery = match.duckDelivery;
        autoStorage = match.autoStorage;
        autoHub = match.autoHub;
        freightBonus = match.freightBonus;
        teamElementUsed = match.teamElementUsed;
        autoParkedInStorage = match.autoParkedInStorage;
        autoParkedInWarehouse = match.autoParkedInWarehouse;
        autoParkedFully = match.autoParkedFully;

        ///Driver
        driverTotalPoints = match.driverTotalPoints;
        driverStorage = match.driverStorage;
        driverHubL1 = match.driverHubL1;
        driverHubL2 = match.driverHubL2;
        driverHubL3 = match.driverHubL3;
        driverShared = match.driverShared;

        ///Endgame
        endgameTotalPoints = match.endgameTotalPoints;
        carouselDucks = match.carouselDucks;
        balancedShipping = match.balancedShipping;
        leaningShared = match.leaningShared;
        endgameParked = match.endgameParked;
        endgameFullyParked = match.endgameFullyParked;
        capping = match.capping;

        //Penalties
        penaltiesTotal = match.penaltiesTotal;
        penaltiesMinor = match.penaltiesMinor;
        penaltiesMajor = match.penaltiesMajor;

        //Total
        totalPoints = match.totalPoints;

        //Autonomous
        binding.textAutoTotalPointsNr.setText(String.format(Locale.US,"%d", autoTotalPoints));
        if(duckDelivery)
            binding.duckDeliveryText.setText(R.string.yes);
        else binding.duckDeliveryText.setText(R.string.no);
        binding.autoFreightStorageNr.setText(String.format(Locale.US,"%d", autoStorage));
        binding.autoFreightHubNr.setText(String.format(Locale.US,"%d", autoHub));
        if(freightBonus){
            binding.autoFreightBonusText.setText(R.string.yes);
            binding.teamElementUsed.setVisibility(View.VISIBLE);
            binding.teamElementUsedText.setVisibility(View.VISIBLE);
            if(teamElementUsed)
                binding.teamElementUsedText.setText(R.string.yes);
            else binding.teamElementUsedText.setText(R.string.no);
        }else {
            binding.autoFreightBonusText.setText(R.string.no);
            binding.teamElementUsed.setVisibility(View.GONE);
            binding.teamElementUsedText.setVisibility(View.GONE);
        }
        if(autoParkedInStorage || autoParkedInWarehouse){
            if(autoParkedInStorage)
                binding.autoParkedText.setText(R.string.storage);
            else binding.autoParkedText.setText(R.string.warehouse);
            binding.autoFullyParked.setVisibility(View.VISIBLE);
            binding.autoFullyParkedText.setVisibility(View.VISIBLE);
            if(autoParkedFully)
                binding.autoFullyParkedText.setText(R.string.yes);
            else binding.autoFullyParkedText.setText(R.string.no);
        } else {
            binding.autoParkedText.setText(R.string.no);
            binding.autoFullyParked.setVisibility(View.GONE);
            binding.autoFullyParkedText.setVisibility(View.GONE);
        }

        //Driver

        binding.textDriverTotalPointsNr.setText(String.format(Locale.US, "%d", driverTotalPoints));
        binding.textDriverStorageNr.setText(String.format(Locale.US,"%d", driverStorage));
        binding.textDriverHubL1Nr.setText(String.format(Locale.US,"%d", driverHubL1));
        binding.textDriverHubL2Nr.setText(String.format(Locale.US,"%d", driverHubL2));
        binding.textDriverHubL3Nr.setText(String.format(Locale.US,"%d", driverHubL3));
        binding.textDriverSharedNr.setText(String.format(Locale.US,"%d", driverShared));

        //Endgame
        binding.textEndgameTotalPointsNr.setText(String.format(Locale.US, "%d", endgameTotalPoints));
        binding.textEndgameCarouselNr.setText(String.format(Locale.US,"%d", carouselDucks));
        if(balancedShipping)
            binding.switchEndgameBalancedShippingText.setText(R.string.yes);
        else binding.switchEndgameBalancedShippingText.setText(R.string.no);
        if(leaningShared)
            binding.switchEndgameLeaningSharedText.setText(R.string.yes);
        else binding.switchEndgameLeaningSharedText.setText(R.string.no);
        if(endgameParked){
            if(endgameFullyParked)
                binding.switchEndgameParkedText.setText(R.string.fully);
            else binding.switchEndgameParkedText.setText(R.string.partly);
        } else binding.switchEndgameParkedText.setText(R.string.no);
        binding.textEndgameCappingNr.setText(String.format(Locale.US,"%d", capping));

        //Penalties
        binding.textPenaltiesNr.setText(String.format(Locale.US, "%d", penaltiesTotal));
        binding.textPenaltiesMinorNr.setText(String.format(Locale.US,"%d", penaltiesMinor));
        binding.textPenaltiesMajorNr.setText(String.format(Locale.US,"%d", penaltiesMajor));
        binding.textTotalNr.setText(String.format(Locale.US, "%d", totalPoints));

    }
}
