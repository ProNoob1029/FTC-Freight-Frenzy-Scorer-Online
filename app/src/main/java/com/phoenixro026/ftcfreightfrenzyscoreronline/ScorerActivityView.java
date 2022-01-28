package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.phoenixro026.ftcfreightfrenzyscoreronline.database.Match;
import com.phoenixro026.ftcfreightfrenzyscoreronline.databinding.ActivityScorerViewBinding;
import com.phoenixro026.ftcfreightfrenzyscoreronline.recycleview.MatchViewModel;

import java.util.List;
import java.util.Locale;

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
    int matchId;
    List<Match> matchList;
    MatchViewModel mMatchViewModel;

    Match currentMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScorerViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Vibrator myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        mMatchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("key");
            matchId = extras.getInt("id");
            //The key argument here must match that used in the other activity
        }

        mMatchViewModel.getAllMatches().observe(this, newMatchList -> {
            // Update the cached copy of the words in the adapter.
            matchList = newMatchList;
            convertToMatch();
            if(key.contentEquals("edit"))
                InsertValues(view);
        });

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

    void convertToMatch(){
        int matchSize = matchList.size();
        for(int i = 0; i < matchSize; i++){
            if(matchList.get(i).id == matchId){
                currentMatch = matchList.get(i);
                break;
            }
        }
    }

    void Delete(){
        mMatchViewModel.deleteByUserId(matchId);
        finish();
    }

    public void InsertValues(View view) {
        binding.textTeamName.setText(currentMatch.teamName);
        binding.textTeamCode.setText(currentMatch.teamCode);
        if(currentMatch.teamColor.contentEquals("red")) {
            binding.buttonTeamRed.setTextAppearance(view.getContext(), R.style.button_theme);
            binding.buttonTeamRed.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.button_shape_red));
            binding.buttonTeamBlue.setTextAppearance(view.getContext(), R.style.Theme_FTC_FREIGHTFRENZY_Scorer);
            binding.buttonTeamBlue.setBackgroundColor(getResources().getColor(R.color.zero));
            teamColor = "red";
        }else {
            binding.buttonTeamBlue.setTextAppearance(view.getContext(), R.style.button_theme);
            binding.buttonTeamBlue.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.button_shape_blue));
            binding.buttonTeamRed.setTextAppearance(view.getContext(), R.style.Theme_FTC_FREIGHTFRENZY_Scorer);
            binding.buttonTeamRed.setBackgroundColor(getResources().getColor(R.color.zero));
            teamColor = "blue";
        }

        ///Autonomous
        autoTotalPoints = currentMatch.autoTotalPoints;
        duckDelivery = currentMatch.duckDelivery;
        autoStorage = currentMatch.autoStorage;
        autoHub = currentMatch.autoHub;
        freightBonus = currentMatch.freightBonus;
        teamElementUsed = currentMatch.teamElementUsed;
        autoParkedInStorage = currentMatch.autoParkedInStorage;
        autoParkedInWarehouse = currentMatch.autoParkedInWarehouse;
        autoParkedFully = currentMatch.autoParkedFully;

        ///Driver
        driverTotalPoints = currentMatch.driverTotalPoints;
        driverStorage = currentMatch.driverStorage;
        driverHubL1 = currentMatch.driverHubL1;
        driverHubL2 = currentMatch.driverHubL2;
        driverHubL3 = currentMatch.driverHubL3;
        driverShared = currentMatch.driverShared;

        ///Endgame
        endgameTotalPoints = currentMatch.endgameTotalPoints;
        carouselDucks = currentMatch.carouselDucks;
        balancedShipping = currentMatch.balancedShipping;
        leaningShared = currentMatch.leaningShared;
        endgameParked = currentMatch.endgameParked;
        endgameFullyParked = currentMatch.endgameFullyParked;
        capping = currentMatch.capping;

        //Penalties
        penaltiesTotal = currentMatch.penaltiesTotal;
        penaltiesMinor = currentMatch.penaltiesMinor;
        penaltiesMajor = currentMatch.penaltiesMajor;

        //Total
        totalPoints = currentMatch.totalPoints;

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
