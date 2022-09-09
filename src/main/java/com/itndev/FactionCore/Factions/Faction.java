package com.itndev.FactionCore.Factions;

import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeAPI;
import com.itndev.FactionCore.Factions.Storage.FactionStorage;
import com.itndev.FactionCore.Utils.Factions.CacheUtils;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Faction {

    private Boolean isBuilt = false;

    private String FactionName = null;
    private String FactionCapName = null;
    private String FactionUUID = null;

    private ArrayList<String> FactionMembers = null;
    private Double Bank = null;
    private Double DTR = null;
    private Integer ClaimLand = null;

    public Faction(String factionUUID2) {
        FactionUUID = factionUUID2;
        try {
            BuildFactionInfo(null);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void BuildFactionInfo(String FactionUUID2) throws ExecutionException, InterruptedException, TimeoutException {
        if(FactionUUID2 == null) {
            FactionUUID2 = FactionUUID;
        }
        CompletableFuture<Double> FutureBank = CacheUtils.getCachedBank(FactionUUID2);
        CompletableFuture<Double> FutureDTR = CacheUtils.getCachedDTR(FactionUUID2);

        FactionName = FactionUtils.getFactionName(FactionUUID2);
        FactionUUID = FactionUUID2;
        FactionCapName = FactionUtils.getCappedFactionName(FactionName);

        FactionMembers = FactionUtils.getFactionMember(FactionUUID);
        Bank = FutureBank.get(40, TimeUnit.MILLISECONDS);
        DTR = FutureDTR.get(40, TimeUnit.MILLISECONDS);
        if(FactionStorage.FactionToLand.containsKey(FactionUUID2)) {
            ClaimLand = FactionStorage.FactionToLand.get(FactionUUID2).size();
        } else {
            ClaimLand = 0;
        }
        isBuilt = true;
    }

    public void DangerousBuildFactionInfo(String FactionUUID2) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Double> FutureBank = CacheUtils.getCachedBank(FactionUUID2);
        CompletableFuture<Double> FutureDTR = CacheUtils.getCachedDTR(FactionUUID2);

        FactionName = FactionUtils.getFactionName(FactionUUID2);
        FactionUUID = FactionUUID2;
        FactionCapName = FactionUtils.getCappedFactionName(FactionName);

        FactionMembers = FactionUtils.getFactionMember(FactionUUID);
        Bank = FutureBank.get();
        DTR = FutureDTR.get();
        if(FactionStorage.FactionToLand.containsKey(FactionUUID2)) {
            ClaimLand = FactionStorage.FactionToLand.get(FactionUUID2).size();
        } else {
            ClaimLand = 0;
        }
        isBuilt = true;
    }

    public String getFactionName() {
        if(!isBuilt) {
            return null;
        }
        return FactionName;
    }

    public String getFactionUUID() {
        if(!isBuilt) {
            return null;
        }
        return FactionUUID;
    }

    public String getFactionCapName() {
        if(!isBuilt) {
            return null;
        }
        return FactionCapName;
    }

    public Double getBank() {
        if(!isBuilt) {
            return null;
        }
        return Bank;
    }

    public Double getDTR() {
        if(!isBuilt) {
            return null;
        }
        return DTR;
    }

    public Integer getClaimLand() {
        if(!isBuilt) {
            return null;
        }
        return ClaimLand;
    }

    public String getFormattedMembers(String Rank) {
        if(!isBuilt) {
            return null;
        }
        String Formatted = "";
        int num = 0;
        //) {
        ArrayList<String> RankedMemebers = new ArrayList<>();
        for(String uuids : FactionMembers) {
            if(FactionUtils.getPlayerRank(uuids).equalsIgnoreCase(Rank)) {
                RankedMemebers.add(uuids);
            }

        }
        for(String uuids : RankedMemebers) {
            num = num + 1;
            if (RankedMemebers.size() != num) {
                Formatted = Formatted + BungeeAPI.colorIFONLINE(uuids) + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(uuids)) + "&8&l, &r";
            } else {
                Formatted = Formatted + BungeeAPI.colorIFONLINE(uuids) + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(uuids)) + "&r";
            }
        }
        return Formatted;
    }


    public String getFactionDesc() {
        if(!isBuilt) {
            return null;
        }
        if(FactionUtils.FactionDescExists(FactionUUID)) {
            return FactionUtils.GetFactionDesc(FactionUUID);
        } else {
            return Lang.FACTION_DEFAULT_DESC;
        }
    }

    public ArrayList<String> getFactionMembers() {
        if(!isBuilt) {
            return null;
        }
        return FactionMembers;
    }
    public ArrayList<String> getFactionMembers(Boolean toName) {
        if(!isBuilt) {
            return null;
        }
        if(toName) {
            ArrayList<String> finalmembers = new ArrayList<>();
            for(String UUIDS : FactionMembers) {
                finalmembers.add(UserInfoUtils.getPlayerOrginNameFromUUID(UUIDS));
            }
            return finalmembers;
        }
        return FactionMembers;
    }

    public ArrayList<String> getFactionMembers(String Rank) {
        ArrayList<String> finalmembers = new ArrayList<>();
        for(String uuids : FactionMembers) {
            if(FactionUtils.getPlayerRank(uuids).equalsIgnoreCase(Rank)) {
                finalmembers.add(uuids);
            }
        }
        return finalmembers;
    }

    public ArrayList<String> getFactionMembers(String Rank, Boolean toName) {
        ArrayList<String> finalmembers = new ArrayList<>();
        if(toName) {
            for (String uuids : FactionMembers) {
                if (FactionUtils.getPlayerRank(uuids).equalsIgnoreCase(Rank)) {
                    finalmembers.add(UserInfoUtils.getPlayerOrginNameFromUUID(uuids));
                }
            }
        } else {
            for (String uuids : FactionMembers) {
                if (FactionUtils.getPlayerRank(uuids).equalsIgnoreCase(Rank)) {
                    finalmembers.add(uuids);
                }
            }
        }
        return finalmembers;
    }
}
