package com.phoenixro026.ftcfreightfrenzyscoreronline;

import java.util.HashMap;

public class MatchModel {
    public String teamName;

    public String teamCode;

    public String createTime;

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

    public String matchCode = "00000";

    public MatchModel(){}

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("teamName", teamName);
        result.put("teamCode", teamCode);
        result.put("createTime", createTime);
        result.put("teamColor", teamColor);
        result.put("autoTotalPoints", autoTotalPoints);
        result.put("duckDelivery", duckDelivery);
        result.put("autoStorage", autoStorage);
        result.put("autoHub", autoHub);
        result.put("freightBonus", freightBonus);
        result.put("teamElementUsed", teamElementUsed);
        result.put("autoParkedInStorage", autoParkedInStorage);
        result.put("autoParkedInWarehouse", autoParkedInWarehouse);
        result.put("autoParkedFully", autoParkedFully);
        result.put("driverTotalPoints", driverTotalPoints);
        result.put("driverStorage", driverStorage);
        result.put("driverHubL1", driverHubL1);
        result.put("driverHubL2", driverHubL2);
        result.put("driverHubL3", driverHubL3);
        result.put("driverShared", driverShared);
        result.put("endgameTotalPoints", endgameTotalPoints);
        result.put("carouselDucks", carouselDucks);
        result.put("balancedShipping", balancedShipping);
        result.put("leaningShared", leaningShared);
        result.put("endgameParked", endgameParked);
        result.put("endgameFullyParked", endgameFullyParked);
        result.put("capping", capping);
        result.put("penaltiesTotal", penaltiesTotal);
        result.put("penaltiesMinor", penaltiesMinor);
        result.put("penaltiesMajor", penaltiesMajor);
        result.put("totalPoints", totalPoints);
        result.put("matchCode", matchCode);

        return result;
    }
}
