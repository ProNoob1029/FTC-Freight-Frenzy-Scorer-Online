package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenixro026.ftcfreightfrenzyscoreronline.databinding.ActivityScorerBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ScorerActivity extends AppCompatActivity{

    protected ActivityScorerBinding binding;

    public String teamName;
    public String teamCode;
    public String teamColor = "";

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

    DatabaseReference mRootRef;
    DatabaseReference mMatchesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScorerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Vibrator vib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("key");
            matchId = extras.getString("id");
            //The key argument here must match that used in the other activity
        }


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mMatchesRef = mRootRef.child("matches");

        if(key.equals("edit")){
            mMatchesRef.child(matchId).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    match = task.getResult().getValue(MatchModel.class);
                    InsertValues(view);
                }
            });
        }

        setupOnClick(view, vib);
    }

    public void setupOnClick(View view, Vibrator myVib){
        binding.buttonTeamRed.setOnClickListener(v -> {
            binding.buttonTeamRed.setTextAppearance(view.getContext(), R.style.button_theme);
            binding.buttonTeamRed.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.button_shape_red));
            binding.buttonTeamBlue.setTextAppearance(view.getContext(), R.style.Theme_FTC_FREIGHTFRENZY_Scorer);
            binding.buttonTeamBlue.setBackgroundColor(getResources().getColor(R.color.zero));
            teamColor = "red";
        });

        binding.buttonTeamBlue.setOnClickListener(v -> {
            binding.buttonTeamRed.setTextAppearance(view.getContext(), R.style.Theme_FTC_FREIGHTFRENZY_Scorer);
            binding.buttonTeamRed.setBackgroundColor(getResources().getColor(R.color.zero));
            binding.buttonTeamBlue.setTextAppearance(view.getContext(), R.style.button_theme);
            binding.buttonTeamBlue.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.button_shape_blue));
            teamColor = "blue";
        });

        ///Autonomous

        binding.switchDuckDelivery.setOnClickListener(v -> {
            duckDelivery = binding.switchDuckDelivery.isChecked();
            CalculateAutoPoints();
        });

        binding.buttonAutoStoragePlus.setOnClickListener(v -> {
            autoStorage++;
            myVib.vibrate(20);
            binding.textAutoStorageNr.setText(String.format(Locale.US,"%d", autoStorage));
            CalculateAutoPoints();
        });

        binding.buttonAutoStorageMinus.setOnClickListener(v -> {
            if(autoStorage > 0){
                autoStorage--;
                myVib.vibrate(20);
                binding.textAutoStorageNr.setText(String.format(Locale.US,"%d", autoStorage));
                CalculateAutoPoints();
            }
        });

        binding.buttonAutoHubPlus.setOnClickListener(v -> {
            autoHub++;
            myVib.vibrate(20);
            binding.textAutoHubNr.setText(String.format(Locale.US,"%d", autoHub));
            CalculateAutoPoints();
        });

        binding.buttonAutoHubMinus.setOnClickListener(v -> {
            if(autoHub > 0){
                autoHub--;
                myVib.vibrate(20);
                binding.textAutoHubNr.setText(String.format(Locale.US,"%d", autoHub));
                CalculateAutoPoints();
            }
        });

        binding.switchFreightBonus.setOnClickListener(v -> {
            if(!binding.switchFreightBonus.isChecked()){
                binding.switchTeamElementUsed.setVisibility(View.GONE);
                binding.switchTeamElementUsed.setChecked(false);
                freightBonus = false;
                teamElementUsed = false;
            }
            else {
                binding.switchTeamElementUsed.setVisibility((View.VISIBLE));
                freightBonus = true;
            }
            CalculateAutoPoints();
        });

        binding.switchTeamElementUsed.setOnClickListener(v -> {
            teamElementUsed = binding.switchTeamElementUsed.isChecked();
            CalculateAutoPoints();
        });

        binding.switchAutoParkedInStorage.setOnClickListener(v -> {
            if(!binding.switchAutoParkedInStorage.isChecked()){
                binding.switchAutoParkedFully.setVisibility(View.GONE);
                binding.switchAutoParkedFully.setChecked(false);
                autoParkedFully = false;
                autoParkedInStorage = false;

            }
            else {
                binding.switchAutoParkedFully.setVisibility((View.VISIBLE));
                autoParkedInStorage = true;
                autoParkedInWarehouse = false;
                binding.switchAutoParkedInWarehouse.setChecked(false);
            }
            CalculateAutoPoints();
        });

        binding.switchAutoParkedInWarehouse.setOnClickListener(v -> {
            if(!binding.switchAutoParkedInWarehouse.isChecked()){
                binding.switchAutoParkedFully.setVisibility(View.GONE);
                binding.switchAutoParkedFully.setChecked(false);
                autoParkedFully = false;
                autoParkedInWarehouse = false;
            }
            else {
                binding.switchAutoParkedFully.setVisibility((View.VISIBLE));
                autoParkedInWarehouse = true;
                autoParkedInStorage = false;
                binding.switchAutoParkedInStorage.setChecked(false);
            }
            CalculateAutoPoints();
        });

        binding.switchAutoParkedFully.setOnClickListener(v -> {
            autoParkedFully = binding.switchAutoParkedFully.isChecked();
            CalculateAutoPoints();
        });

        ///Driver

        binding.buttonDriverStoragePlus.setOnClickListener(v -> {
            driverStorage++;
            myVib.vibrate(20);
            binding.textDriverStorageNr.setText(String.format(Locale.US,"%d", driverStorage));
            CalculateDriverPoints();
        });

        binding.buttonDriverStorageMinus.setOnClickListener(v -> {
            if(driverStorage > 0){
                driverStorage--;
                myVib.vibrate(20);
                binding.textDriverStorageNr.setText(String.format(Locale.US,"%d", driverStorage));
                CalculateDriverPoints();
            }
        });

        binding.buttonDriverHubL1Plus.setOnClickListener(v -> {
            driverHubL1++;
            myVib.vibrate(20);
            binding.textDriverHubL1Nr.setText(String.format(Locale.US,"%d", driverHubL1));
            CalculateDriverPoints();
        });

        binding.buttonDriverHubL1Minus.setOnClickListener(v -> {
            if(driverHubL1 > 0){
                driverHubL1--;
                myVib.vibrate(20);
                binding.textDriverHubL1Nr.setText(String.format(Locale.US,"%d", driverHubL1));
                CalculateDriverPoints();
            }
        });

        binding.buttonDriverHubL2Plus.setOnClickListener(v -> {
            driverHubL2++;
            myVib.vibrate(20);
            binding.textDriverHubL2Nr.setText(String.format(Locale.US,"%d", driverHubL2));
            CalculateDriverPoints();
        });

        binding.buttonDriverHubL2Minus.setOnClickListener(v -> {
            if(driverHubL2 > 0){
                driverHubL2--;
                myVib.vibrate(20);
                binding.textDriverHubL2Nr.setText(String.format(Locale.US,"%d", driverHubL2));
                CalculateDriverPoints();
            }
        });

        binding.buttonDriverHubL3Plus.setOnClickListener(v -> {
            driverHubL3++;
            myVib.vibrate(20);
            binding.textDriverHubL3Nr.setText(String.format(Locale.US,"%d", driverHubL3));
            CalculateDriverPoints();
        });

        binding.buttonDriverHubL3Minus.setOnClickListener(v -> {
            if(driverHubL3 > 0){
                driverHubL3--;
                myVib.vibrate(20);
                binding.textDriverHubL3Nr.setText(String.format(Locale.US,"%d", driverHubL3));
                CalculateDriverPoints();
            }
        });

        binding.buttonDriverSharedPlus.setOnClickListener(v -> {
            driverShared++;
            myVib.vibrate(20);
            binding.textDriverSharedNr.setText(String.format(Locale.US,"%d", driverShared));
            CalculateDriverPoints();
        });

        binding.buttonDriverSharedMinus.setOnClickListener(v -> {
            if(driverShared > 0){
                driverShared--;
                myVib.vibrate(20);
                binding.textDriverSharedNr.setText(String.format(Locale.US,"%d", driverShared));
                CalculateDriverPoints();
            }
        });

        ///Endgame

        binding.buttonEndgameCarouselPlus.setOnClickListener(v -> {
            if(carouselDucks < 12) {
                carouselDucks++;
                myVib.vibrate(20);
                binding.textEndgameCarouselNr.setText(String.format(Locale.US,"%d", carouselDucks));
                CalculateEndgamePoints();
            }
        });

        binding.buttonEndgameCarouselMinus.setOnClickListener(v -> {
            if(carouselDucks > 0){
                carouselDucks--;
                myVib.vibrate(20);
                binding.textEndgameCarouselNr.setText(String.format(Locale.US,"%d", carouselDucks));
                CalculateEndgamePoints();
            }
        });

        binding.switchEndgameBalancedShipping.setOnClickListener(v -> {
            balancedShipping = binding.switchEndgameBalancedShipping.isChecked();
            CalculateEndgamePoints();
        });

        binding.switchEndgameLeaningShared.setOnClickListener(v -> {
            leaningShared = binding.switchEndgameLeaningShared.isChecked();
            CalculateEndgamePoints();
        });

        binding.switchEndgameParked.setOnClickListener(v -> {
            endgameParked = binding.switchEndgameParked.isChecked();
            if(endgameParked)
                binding.switchEndgameParkedFully.setVisibility(View.VISIBLE);
            else {
                binding.switchEndgameParkedFully.setVisibility(View.GONE);
                binding.switchEndgameParkedFully.setChecked(false);
                endgameFullyParked = false;
            }
            CalculateEndgamePoints();
        });

        binding.switchEndgameParkedFully.setOnClickListener(v -> {
            endgameFullyParked = binding.switchEndgameParkedFully.isChecked();
            CalculateEndgamePoints();
        });

        binding.buttonEndgameCappingPlus.setOnClickListener(v -> {
            if(capping < 2){
                capping++;
                myVib.vibrate(20);
                binding.textEndgameCappingNr.setText(String.format(Locale.US,"%d", capping));
                CalculateEndgamePoints();
            }
        });

        binding.buttonEndgameCappingMinus.setOnClickListener(v -> {
            if(capping > 0){
                capping--;
                myVib.vibrate(20);
                binding.textEndgameCappingNr.setText(String.format(Locale.US,"%d", capping));
                CalculateEndgamePoints();
            }
        });

        //Penalties

        binding.buttonPenaltiesMinorPlus.setOnClickListener(v -> {
            penaltiesMinor++;
            myVib.vibrate(20);
            binding.textPenaltiesMinorNr.setText(String.format(Locale.US,"%d", penaltiesMinor));
            CalculatePenalties();
        });

        binding.buttonPenaltiesMinorMinus.setOnClickListener(v -> {
            if(penaltiesMinor > 0){
                penaltiesMinor--;
                myVib.vibrate(20);
                binding.textPenaltiesMinorNr.setText(String.format(Locale.US,"%d", penaltiesMinor));
                CalculatePenalties();
            }
        });

        binding.buttonPenaltiesMajorPlus.setOnClickListener(v -> {
            penaltiesMajor++;
            myVib.vibrate(20);
            binding.textPenaltiesMajorNr.setText(String.format(Locale.US,"%d", penaltiesMajor));
            CalculatePenalties();
        });

        binding.buttonPenaltiesMajorMinus.setOnClickListener(v -> {
            if(penaltiesMajor > 0){
                penaltiesMajor--;
                myVib.vibrate(20);
                binding.textPenaltiesMajorNr.setText(String.format(Locale.US,"%d", penaltiesMajor));
                CalculatePenalties();
            }
        });

        binding.buttonSave.setOnClickListener(v -> {
            Save();
            if(!teamName.contentEquals("") && !teamCode.contentEquals("") && !teamColor.contentEquals("")){
                String newKey;
                if(key.equals("edit")){
                    newKey = matchId;
                }else newKey = mMatchesRef.push().getKey();
                Map<String , Object> updates = new HashMap<>();
                Map<String, Object> map = match.toMap();
                updates.put(newKey, map);
                mMatchesRef.updateChildren(updates);
                finish();
            }else Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show();
        } );
    }

    public void CalculateAutoPoints() {
        autoTotalPoints = autoStorage * 2 + autoHub * 6;
        if(duckDelivery)
            autoTotalPoints += 10;
        if(freightBonus) {
            if(teamElementUsed)
                autoTotalPoints += 20;
            else autoTotalPoints += 10;
        }
        if(autoParkedInStorage) {
            if(autoParkedFully)
                autoTotalPoints += 6;
            else autoTotalPoints += 3;
        }else if(autoParkedInWarehouse) {
            if(autoParkedFully)
                autoTotalPoints += 10;
            else autoTotalPoints += 5;
        }
        binding.textAutoTotalPointsNr.setText(String.format(Locale.US,"%d", autoTotalPoints));
        CalculateTotalPoints();
    }

    public void CalculateDriverPoints() {
        driverTotalPoints = driverStorage + 2 * driverHubL1 + 4 * driverHubL2 + 6 * driverHubL3 + 4 * driverShared;
        binding.textDriverTotalPointsNr.setText(String.format(Locale.US, "%d", driverTotalPoints));
        CalculateTotalPoints();
    }

    public void CalculateEndgamePoints() {
        endgameTotalPoints = 6 * carouselDucks + 15 * capping;
        if(balancedShipping)
            endgameTotalPoints += 10;
        if(leaningShared)
            endgameTotalPoints += 20;
        if(endgameParked){
            if(endgameFullyParked)
                endgameTotalPoints += 6;
            else endgameTotalPoints += 3;
        }
        binding.textEndgameTotalPointsNr.setText(String.format(Locale.US, "%d", endgameTotalPoints));
        CalculateTotalPoints();
    }

    public void CalculatePenalties() {
        penaltiesTotal = -10 * penaltiesMinor + -30 * penaltiesMajor;
        binding.textPenaltiesNr.setText(String.format(Locale.US, "%d", penaltiesTotal));
        CalculateTotalPoints();
    }

    public void CalculateTotalPoints() {
        totalPoints = autoTotalPoints + driverTotalPoints + endgameTotalPoints + penaltiesTotal;
        binding.textTotalNr.setText(String.format(Locale.US, "%d", totalPoints));
    }

    public void Save() {
        teamName = binding.textTeamName.getText().toString();
        teamCode = binding.textTeamCode.getText().toString();
        Calendar currentTime = Calendar.getInstance();
        match.teamName = teamName;
        match.teamCode = teamCode;
        match.teamColor = teamColor;

        ///Autonomous
        match.autoTotalPoints = autoTotalPoints;
        match.duckDelivery = duckDelivery;
        match.autoStorage = autoStorage;
        match.autoHub = autoHub;
        match.freightBonus = freightBonus;
        match.teamElementUsed = teamElementUsed;
        match.autoParkedInStorage = autoParkedInStorage;
        match.autoParkedInWarehouse = autoParkedInWarehouse;
        match.autoParkedFully = autoParkedFully;

        ///Driver
        match.driverTotalPoints = driverTotalPoints;
        match.driverStorage = driverStorage;
        match.driverHubL1 = driverHubL1;
        match.driverHubL2 = driverHubL2;
        match.driverHubL3 = driverHubL3;
        match.driverShared = driverShared;

        ///Endgame
        match.endgameTotalPoints = endgameTotalPoints;
        match.carouselDucks = carouselDucks;
        match.balancedShipping = balancedShipping;
        match.leaningShared = leaningShared;
        match.endgameParked = endgameParked;
        match.endgameFullyParked = endgameFullyParked;
        match.capping = capping;

        //Penalties
        match.penaltiesTotal = penaltiesTotal;
        match.penaltiesMinor = penaltiesMinor;
        match.penaltiesMajor = penaltiesMajor;

        //Total
        match.totalPoints = totalPoints;

        if(!key.contentEquals("edit")){
            String min;
            int calMin = currentTime.get(Calendar.MINUTE);
            if(calMin < 10)
                min = String.format(Locale.US, "0%d", calMin);
            else min = String.format(Locale.US, "%d", calMin);
            match.createTime = String.format(Locale.US, "%s %d %d:%s", new SimpleDateFormat("MMM", Locale.US).format(currentTime.getTime()), currentTime.get(Calendar.DAY_OF_MONTH), currentTime.get(Calendar.HOUR_OF_DAY), min);
        }
    }

    public void InsertValues(View view) {
        binding.textTeamName.setText(match.teamName);
        binding.textTeamCode.setText(match.teamCode);
        if(match.teamColor.contentEquals("red")) {
            binding.buttonTeamRed.setTextAppearance(view.getContext(), R.style.button_theme);
            binding.buttonTeamRed.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.button_shape_red));
            teamColor = "red";
        }else {
            binding.buttonTeamBlue.setTextAppearance(view.getContext(), R.style.button_theme);
            binding.buttonTeamBlue.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.button_shape_blue));
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
        binding.switchDuckDelivery.setChecked(duckDelivery);
        binding.textAutoStorageNr.setText(String.format(Locale.US,"%d", autoStorage));
        binding.textAutoHubNr.setText(String.format(Locale.US,"%d", autoHub));
        binding.switchFreightBonus.setChecked(freightBonus);
        binding.switchTeamElementUsed.setChecked(teamElementUsed);
        binding.switchAutoParkedInStorage.setChecked(autoParkedInStorage);
        binding.switchAutoParkedInWarehouse.setChecked(autoParkedInWarehouse);
        binding.switchAutoParkedFully.setChecked(autoParkedFully);

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
        binding.switchEndgameBalancedShipping.setChecked(balancedShipping);
        binding.switchEndgameLeaningShared.setChecked(leaningShared);
        binding.switchEndgameParked.setChecked(endgameParked);
        binding.switchEndgameParkedFully.setChecked(endgameFullyParked);
        binding.textEndgameCappingNr.setText(String.format(Locale.US,"%d", capping));

        //Penalties
        binding.textPenaltiesNr.setText(String.format(Locale.US, "%d", penaltiesTotal));
        binding.textPenaltiesMinorNr.setText(String.format(Locale.US,"%d", penaltiesMinor));
        binding.textPenaltiesMajorNr.setText(String.format(Locale.US,"%d", penaltiesMajor));
        binding.textTotalNr.setText(String.format(Locale.US, "%d", totalPoints));

        if(freightBonus)
            binding.switchTeamElementUsed.setVisibility(View.VISIBLE);

        if(autoParkedInStorage || autoParkedInWarehouse)
            binding.switchAutoParkedFully.setVisibility(View.VISIBLE);

        if(endgameParked)
            binding.switchEndgameParkedFully.setVisibility(View.VISIBLE);

    }
}
