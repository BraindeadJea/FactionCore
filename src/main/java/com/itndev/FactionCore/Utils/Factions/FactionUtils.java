package com.itndev.FactionCore.Utils.Factions;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Factions.Storage.FactionStorage;
import com.itndev.FactionCore.Factions.Lang;
import com.itndev.FactionCore.Lock.Lock;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.CommonUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FactionUtils {
    public static String newFactionUUID() {
        String uuid;
        Long time = System.currentTimeMillis();
        UUID uuid2 = UUID.randomUUID();
        uuid = String.valueOf(time) + "=" + String.valueOf(uuid2);
        time = null;
        uuid2 = null;
        return uuid;
    }

    public static void SetPlayerRank(String UUID, String Rank) {
        if(Rank == null) {
            Storage.AddCommandToQueue("update:=:FactionRank:=:remove:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(Rank.toLowerCase(Locale.ROOT)));
        } else {
            Storage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(Rank.toLowerCase(Locale.ROOT)));
        }
    }

    public static String SetPlayerRank_GETRAWCMD(String UUID, String Rank) {
        if(Rank == null) {
            return ("update:=:FactionRank:=:remove:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte("NULL"));
        } else {
            return ("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(Rank.toLowerCase(Locale.ROOT)));
        }
    }

    public static String getFormattedFaction(String UUID) {
        if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
            return "";
        } else {
            return "&f[&r&a" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) + "&r&f]";
        }
    }

    public static String getFormattedRank(String UUID) {
        if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
            return "";
        } else {
            return "&r&7" + FactionUtils.getPlayerLangRank(UUID);
        }
    }

    public static String getFactionLeader(String FactionUUID) {
        //ArrayList<String> members = FactionStorage.FactionMember.get(FactionUUID);
        String finalUUID = null;
        for(String UUID : FactionStorage.FactionMember.get(FactionUUID)) {
            if(FactionUtils.getPlayerRank(UUID).equals(Config.Leader)) {
                finalUUID = UUID;
            }
        }
        return finalUUID;
    }

    public static String getAnyOneInFaction(String FactionUUID) {
        for(String UUID : FactionStorage.FactionMember.get(FactionUUID)) {
            return UUID;
        }
        return null;
    }

    public static Boolean isExistingFaction(String FactionName) {
        return FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT));
    }


    public static void SetPlayerFaction(String UUID, String FactionUUID) {
        if(FactionUUID == null) {
            Storage.AddCommandToQueue("update:=:PlayerFaction:=:remove:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte("ddd"));
        } else {
            Storage.AddCommandToQueue("update:=:PlayerFaction:=:add:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID));
        }
    }

    public static String SetPlayerFaction_GETRAWCMD(String UUID, String FactionUUID) {
        if(FactionUUID == null) {
            return "update:=:PlayerFaction:=:remove:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte("ddd");
        } else {
            return "update:=:PlayerFaction:=:add:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID);
        }
    }

    public static void SetFactionMember(String UUID, String FactionUUID, Boolean Remove) {
        if(FactionUUID == null) {
            return;
        }
        if(!Remove) {
            Storage.AddCommandToQueue("update:=:FactionMember:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(UUID));
        } else {
            Storage.AddCommandToQueue("update:=:FactionMember:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(UUID));
        }
    }

    public static String SetFactionMember_GETRAWCMD(String UUID, String FactionUUID, Boolean Remove) {
        if(!Remove) {
            return "update:=:FactionMember:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(UUID);
        } else {
            return "update:=:FactionMember:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(UUID);
        }
    }

    public static void SetFactionName(String FactionUUID, String NewFactionName) {
        //Faction name update
        String oldFactionName = getFactionName(FactionUUID);
        if(oldFactionName.equalsIgnoreCase(NewFactionName)) {
            Storage.AddCommandToQueue("update:=:FactionNameToFactionName:=:add:=:" + CommonUtils.String2Byte(NewFactionName.toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(NewFactionName));
            //JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + oldFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + CommonUtils.String2Byte(NewFactionName.toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID));
            Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(NewFactionName.toLowerCase(Locale.ROOT)));
        } else {
            Storage.AddCommandToQueue("update:=:FactionNameToFactionName:=:add:=:" + CommonUtils.String2Byte(NewFactionName.toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(NewFactionName));
            Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + CommonUtils.String2Byte(oldFactionName.toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(NewFactionName));
            Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + CommonUtils.String2Byte(NewFactionName.toLowerCase(Locale.ROOT)) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID));
            Storage.AddCommandToQueue("update:=:FactionUUIDToFactionName:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(NewFactionName.toLowerCase(Locale.ROOT)));
        }
        oldFactionName = null;
    }

    public static void CreateFaction(String LeaderUUID, String FactionUUID, String FactionOrginName) {
        String FactionName = FactionOrginName.toLowerCase(Locale.ROOT);
        List<String> BulkCMD = new ArrayList<>();
        BulkCMD.add("update:=:FactionMember:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(LeaderUUID));
        BulkCMD.add("update:=:FactionNameToFactionUUID:=:add:=:" + CommonUtils.String2Byte(FactionName) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID));
        BulkCMD.add("update:=:FactionNameToFactionName:=:add:=:" + CommonUtils.String2Byte(FactionName) + ":=:add:=:" + CommonUtils.String2Byte(FactionOrginName));
        BulkCMD.add("update:=:FactionUUIDToFactionName:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(FactionName));
        BulkCMD.add("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(LeaderUUID) + ":=:add:=:" + CommonUtils.String2Byte(Config.Leader));
        BulkCMD.add("update:=:PlayerFaction:=:add:=:" + CommonUtils.String2Byte(LeaderUUID) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID));
        Storage.AddBulkCommandToQueue(BulkCMD);
        FactionName = null;
        BulkCMD = null;
    }

    public static String Build_PERMLVLINFO(int PERMLVL, int FINALPERMLVL, String SUCCESSID) {
        return ":=:" + Config.PERM_LEVEL_INDICATOR + PERMLVL + ":=:" + Config.PERM_FINAL_LEVEL_INDICATOR + FINALPERMLVL + ":=:" + Config.SUCCESS_ID_INDICATOR + SUCCESSID;
    }

    public static void FactionAdmin() {


    }


    public static void DeleteFaction(String FactionUUID) {
        String FactionName = getFactionName(FactionUUID).toLowerCase(Locale.ROOT);
        List<String> BulkCMD = new ArrayList<>();
        BulkCMD.add("update:=:FactionMember:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte("UUID"));
        BulkCMD.add("update:=:FactionNameToFactionUUID:=:remove:=:" + CommonUtils.String2Byte(FactionName) + ":=:add:=:" + CommonUtils.String2Byte("UUID"));
        BulkCMD.add("update:=:FactionNameToFactionName:=:remove:=:" + CommonUtils.String2Byte(FactionName) + ":=:add:=:" + CommonUtils.String2Byte("UUID"));
        BulkCMD.add("update:=:FactionUUIDToFactionName:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte("UUID"));
        List<String> Members = FactionStorage.FactionMember.get(FactionUUID);
        if(FactionStorage.FactionToLand.containsKey(FactionUUID)) {
            for(String Chunkkey : FactionStorage.FactionToLand.get(FactionUUID)) {
                BulkCMD.add("update:=:LandToFaction:=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:remove:=:" + CommonUtils.String2Byte(FactionUUID));
                BulkCMD.add("update:=:FactionToLand:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(Chunkkey));
            }
        }
        //ClearFactionInfo(FactionUUID);
        if(FactionStorage.FactionInfoList.containsKey(FactionUUID)) {
            BulkCMD.add("update:=:FactionInfoList:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte("NULL"));
            for(String key : FactionStorage.FactionInfoList.get(FactionUUID)) {
                BulkCMD.add("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=" +  key) + ":=:remove:=:" + CommonUtils.String2Byte("MULL"));
            }
        }
        if(FactionStorage.FactionToOutPost.containsKey(FactionUUID)) {
            BulkCMD.add("update:=:FactionToOutPost:=:add:=:" + CommonUtils.String2Byte(FactionUUID)  + ":=:remove:=:" + CommonUtils.String2Byte("Chunkkey"));
            for(String Chunkkey : FactionStorage.FactionToOutPost.get(FactionUUID)) {
                BulkCMD.add("update:=:OutPostToFaction:=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:remove:=:" + CommonUtils.String2Byte(FactionUUID) );
            }
        }
        //Storage.AddCommandToQueue("update:=:FactionToLand:=:remove:=:" + FactionUUID + ":=:remove:=:" + FactionUUID + ":=:bulk");
        BulkCMD.add("update:=:FactionToLand:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte("nothing"));
        BulkCMD.add("update:=:FactionOutPost:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte("nothing"));
        BulkCMD.add("update:=:FactionWarpLocations:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte("nothing"));
        Storage.AddBulkCommandToQueue(BulkCMD);
        new Thread(() -> {
            for(String PlayerUUID : Members) {
                try {
                    synchronized (Lock.tryOptainLock(PlayerUUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                        synchronized (Lock.tryOptainLock(FactionUUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                            forcePlayerLeaveFaction(PlayerUUID, FactionUUID);
                        }
                    }
                } catch (TimeoutException | ExecutionException | InterruptedException e) {
                    SystemUtils.UUID_BASED_MSG_SENDER(PlayerUUID, "&c&lERROR &7오류 발생 : 오류코드 TIMEOUT_LOCK_002 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static Boolean isUsedFactionUUID(String FactionUUID) {
        return (FactionStorage.FactionUUIDToFactionName.containsKey(FactionUUID));
    }
    public static void AsyncRemovePlayerFromFaction(String UUID, String FactionUUID) {
        new Thread(() -> {
            try {
                synchronized (Lock.tryOptainLock(UUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                    synchronized (Lock.tryOptainLock(FactionUUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                        makePlayerLeaveFaction(UUID, FactionUUID);
                    }
                }
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&c&lERROR &7오류 발생 : 오류코드 TIMEOUT_LOCK_002 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
                e.printStackTrace();
            }
            /*if(Lock.CachedhasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    removePlayerFactionLock(UUID, FactionUUID);
                }
            } else {
                if (Lock.hasLock(UUID)) {
                    synchronized (Lock.getLock(UUID).getLock()) {
                        removePlayerFactionLock(UUID, FactionUUID);
                        Lock.AckLock(UUID);
                    }
                } else {
                    synchronized (Lock.getPublicLock()) {
                        removePlayerFactionLock(UUID, FactionUUID);
                    }
                }
            }*/
        }).start();
    }

    /*private static void removePlayerFactionLock(String PlayerUUID, String FromFactionUUID) {
        if(Lock.CachedhasLock(FromFactionUUID)) {
            synchronized (Lock.getLock(FromFactionUUID).getLock()) {
                makePlayerLeaveFaction(PlayerUUID, FromFactionUUID);
            }
        } else {
            if (Lock.hasLock(FromFactionUUID)) {
                synchronized (Lock.getLock(FromFactionUUID).getLock()) {
                    makePlayerLeaveFaction(PlayerUUID, FromFactionUUID);
                    Lock.AckLock(FromFactionUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    makePlayerLeaveFaction(PlayerUUID, FromFactionUUID);
                }
            }
        }
    }*/

    private static void makePlayerLeaveFaction(String UUID, String FromFactionUUID) {
        List<String> BulkCMD = new ArrayList<>();
        BulkCMD.add("update:=:FactionMember:=:add:=:" + FromFactionUUID + ":=:remove:=:" + UUID);
        if(FactionStorage.PlayerFaction.containsKey(UUID) && FactionStorage.PlayerFaction.get(UUID).equals(FromFactionUUID)) {
            BulkCMD.add("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(Config.Nomad));
            BulkCMD.add("update:=:PlayerFaction:=:remove:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte("ddd"));
        }
        Storage.AddBulkCommandToQueue(BulkCMD);
    }

    private static void forcePlayerLeaveFaction(String UUID, String FromFactionUUID) {
        List<String> BulkCMD = new ArrayList<>();
        BulkCMD.add("update:=:FactionMember:=:add:=:" + CommonUtils.String2Byte(FromFactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(UUID));
        BulkCMD.add("update:=:FactionRank:=:add:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(Config.Nomad));
        BulkCMD.add("update:=:PlayerFaction:=:remove:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte("ddd"));
        Storage.AddBulkCommandToQueue(BulkCMD);
    }

    public static Boolean isExistingOutPost(String FactionUUID, String OutPostName) {
        if(FactionStorage.FactionOutPostList.containsKey(FactionUUID)) {
            return FactionStorage.FactionOutPostList.get(FactionUUID).contains(OutPostName.toLowerCase(Locale.ROOT));
        } else {
            return false;
        }
    }



    public static void SendFactionMessage(String playeruuid, String targetuuid, String type, String message) {
        if(type.equalsIgnoreCase("single")) {
            //type : SIBAL, TeamChat, all
            Storage.AddCommandToQueue("notify:=:" + CommonUtils.String2Byte(playeruuid) + ":=:" + CommonUtils.String2Byte(targetuuid) + ":=:" + CommonUtils.String2Byte(message) + ":=:" + CommonUtils.String2Byte("false"));
        } else {
            Storage.AddCommandToQueue("notify:=:" + CommonUtils.String2Byte(playeruuid) + ":=:" + CommonUtils.String2Byte(type) + ":=:" + CommonUtils.String2Byte(message) + ":=:" + CommonUtils.String2Byte("true"));
        }
    }

    public static void SendFactionMessage_GETRAWCMD(String playeruuid, String targetuuid, String type, String message) {
        if(type.equalsIgnoreCase("single")) {
            //type : SIBAL, TeamChat, all
            Storage.AddCommandToQueue("notify:=:" + CommonUtils.String2Byte(playeruuid) + ":=:" + CommonUtils.String2Byte(targetuuid) + ":=:" + CommonUtils.String2Byte(message) + ":=:" + "false");
        } else {
            Storage.AddCommandToQueue("notify:=:" + CommonUtils.String2Byte(playeruuid) + ":=:" + CommonUtils.String2Byte(type) + ":=:" + CommonUtils.String2Byte(message) + ":=:" + "true");
        }
    }

    public static void ClaimLand(String FactionUUID, String Chunkkey) {
        FactionStorage.LandToFaction.put(Chunkkey, FactionUUID);
        Storage.AddCommandToQueue("update:=:LandToFaction:=:add:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:" + Server.getServerName());
        ArrayList<String> updatelist = new ArrayList<>();
        if(FactionStorage.FactionToLand.containsKey(FactionUUID)) {
            updatelist = FactionStorage.FactionToLand.get(FactionUUID);
        }
        updatelist.add(Chunkkey);
        FactionStorage.FactionToLand.put(FactionUUID, updatelist);
        Storage.AddCommandToQueue("update:=:FactionToLand:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:" + Server.getServerName());
        updatelist = null;
    }

    public static void UnClaimLand(String FactionUUID, String Chunkkey) {
        FactionStorage.LandToFaction.remove(Chunkkey);
        Storage.AddCommandToQueue("update:=:LandToFaction:=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:" + Server.getServerName());
        ArrayList<String> updatelist = FactionStorage.FactionToLand.get(FactionUUID);
        updatelist.remove(Chunkkey);
        if(updatelist.isEmpty()) {
            FactionStorage.FactionToLand.remove(FactionUUID);
        } else {
            FactionStorage.FactionToLand.put(FactionUUID, updatelist);
        }
        Storage.AddCommandToQueue("update:=:FactionToLand:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:" + Server.getServerName());
        updatelist = null;
    }

    public static void ClaimOutPost(String FactionUUID, String Chunkkey) {
        FactionStorage.OutPostToFaction.put(Chunkkey, FactionUUID);
        Storage.AddCommandToQueue("update:=:OutPostToFaction:=:add:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:" + Server.getServerName());
        ArrayList<String> updatelist = new ArrayList<>();
        if(FactionStorage.FactionToOutPost.containsKey(FactionUUID)) {
            updatelist = FactionStorage.FactionToOutPost.get(FactionUUID);
        }
        updatelist.add(Chunkkey);
        FactionStorage.FactionToOutPost.put(FactionUUID, updatelist);
        Storage.AddCommandToQueue("update:=:FactionToOutPost:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:" + Server.getServerName());
        updatelist = null;
    }

    public static void UnClaimOutPost(String FactionUUID, String Chunkkey) {
        FactionStorage.OutPostToFaction.remove(Chunkkey);
        Storage.AddCommandToQueue("update:=:OutPostToFaction:=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:" + Server.getServerName());
        ArrayList<String> updatelist = FactionStorage.FactionToOutPost.get(FactionUUID);
        updatelist.remove(Chunkkey);
        if(updatelist.isEmpty()) {
            FactionStorage.FactionToOutPost.remove(FactionUUID);
        } else {
            FactionStorage.FactionToOutPost.put(FactionUUID, updatelist);
        }
        Storage.AddCommandToQueue("update:=:FactionToOutPost:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(Chunkkey) + ":=:" + Server.getServerName());
        updatelist = null;
    }


    public static Boolean isAExistingLangRank(String LangRank) {
        String lowcaserank = LangRank.toLowerCase(Locale.ROOT);
        if(lowcaserank.equalsIgnoreCase(Config.Leader_Lang)) {
            lowcaserank = null;
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.CoLeader_Lang)) {
            lowcaserank = null;
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.VipMember_Lang)) {
            lowcaserank = null;
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.Warrior_Lang)) {
            lowcaserank = null;
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.Member_Lang)) {
            lowcaserank = null;
            return true;
        } else {
            lowcaserank = null;
            return false;
        }
    }

    public static String getFactionByClaim(String ChunkKey) {
        if(FactionStorage.LandToFaction.containsKey(ChunkKey)) {
            return FactionStorage.LandToFaction.get(ChunkKey);
        }
        return null;
    }

    public static String getPlayerFactionUUID(String UUID) {
        String finalUUID = null;
        if(FactionStorage.PlayerFaction.containsKey(UUID)) {
            finalUUID = FactionStorage.PlayerFaction.get(UUID);
        }
        return finalUUID;
    }

    public static String getFactionName(String FactionUUID) {
        String finalname = null;
        if(FactionStorage.FactionUUIDToFactionName.containsKey(FactionUUID)) {
            finalname = FactionStorage.FactionUUIDToFactionName.get(FactionUUID);
        }
        return finalname;
    }

    public static String getFactionUUID(String FactionName) {
        String finalUUID = null;
        if(FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT))) {
            finalUUID = FactionStorage.FactionNameToFactionUUID.get(FactionName.toLowerCase(Locale.ROOT));
        }
        return finalUUID;
    }

    public static String getCappedFactionName(String FactionName) {
        if(FactionStorage.FactionNameToFactionName.containsKey(FactionName.toLowerCase(Locale.ROOT))) {
            return FactionStorage.FactionNameToFactionName.get(FactionName.toLowerCase(Locale.ROOT));
        }
        return null;
    }

    public static String getCapFactionNameFromUUID(String FactionUUID) {
        if(FactionStorage.FactionUUIDToFactionName.containsKey(FactionUUID)) {
            String FactionTempName = FactionStorage.FactionUUIDToFactionName.get(FactionUUID);
            if(FactionStorage.FactionNameToFactionName.containsKey(FactionTempName)) {
                FactionTempName = null;
                return FactionStorage.FactionNameToFactionName.get(FactionTempName);
            }
            FactionTempName = null;
        }
        return null;
    }

    public static ArrayList<String> getFactionMember(String FactionUUID) {
        //ArrayList<String> finallist = new ArrayList<>();
        if(FactionStorage.FactionMember.containsKey(FactionUUID)) {
            return (ArrayList<String>) FactionStorage.FactionMember.get(FactionUUID);
        }
        return new ArrayList<>();
    }

    public static String getPlayerRank(String UUID) {
        if(FactionStorage.FactionRank.containsKey(UUID)) {
            return FactionStorage.FactionRank.get(UUID);
        } else {
            return Config.Nomad;
        }
    }

    public static String LangRankConvert(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Nomad)) {
            return Config.Nomad_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Member)) {
            return Config.Member_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Warrior)) {
            return Config.Warrior_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.VipMember)) {
            return Config.VipMember_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.CoLeader)) {
            return Config.CoLeader_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Leader)) {
            return Config.Leader_Lang;
        }
        return null;
    }

    public static String RankConvert(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Nomad_Lang)) {
            return Config.Nomad;
        } else
        if(Rank.equalsIgnoreCase(Config.Member_Lang)) {
            return Config.Member;
        } else
        if(Rank.equalsIgnoreCase(Config.Warrior_Lang)) {
            return Config.Warrior;
        } else
        if(Rank.equalsIgnoreCase(Config.VipMember_Lang)) {
            return Config.VipMember;
        } else
        if(Rank.equalsIgnoreCase(Config.CoLeader_Lang)) {
            return Config.CoLeader;
        } else
        if(Rank.equalsIgnoreCase(Config.Leader_Lang)) {
            return Config.Leader;
        }
        return null;
    }

    public static String FactionStatusConvert(String Status) {
        if(Status.equalsIgnoreCase(Config.Ally_Lang)) {
            return Config.Ally;
        } else if(Status.equalsIgnoreCase(Config.Enemy_Lang)) {
            return Config.Enemy;
        } else if(Status.equalsIgnoreCase(Config.Neutral_Lang)) {
            return Config.Neutral;
        } else {
            return null;
        }
    }

    public static String FactionLangStatusConvert(String Status) {
        if(Status.equalsIgnoreCase(Config.Ally)) {
            return Config.Ally_Lang;
        } else if(Status.equalsIgnoreCase(Config.Enemy)) {
            return Config.Enemy_Lang;
        } else if(Status.equalsIgnoreCase(Config.Neutral)) {
            return Config.Neutral_Lang;
        } else {
            return null;
        }
    }

    public static String getPlayerLangRank(String UUID) {
        if(FactionStorage.FactionRank.containsKey(UUID)) {
            return LangRankConvert(FactionStorage.FactionRank.get(UUID));
        } else {
            return LangRankConvert(Config.Nomad);
        }
    }

    public static Boolean isInFaction(String UUID) {
        return FactionStorage.PlayerFaction.containsKey(UUID);
    }

    public static Boolean isSameFaction(String UUID, String UUID2) {
        if(isInFaction(UUID) && isInFaction(UUID2)) {
            return (getPlayerFactionUUID(UUID).equalsIgnoreCase(getPlayerFactionUUID(UUID2)));
        }
        return false;
    }

    public static Boolean HigherThenorSameRank(String UUID, String Rank) {
        String PlayerRank = FactionUtils.getPlayerRank(UUID);
        if(RankPrio(PlayerRank) >= RankPrio(Rank)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean HigherThenRank(String UUID, String Rank) {
        String PlayerRank = FactionUtils.getPlayerRank(UUID);
        if(RankPrio(PlayerRank) > RankPrio(Rank)) {
            PlayerRank = null;
            return true;
        } else {
            PlayerRank = null;
            return false;
        }
    }

    public static Integer RankPrio(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Leader)) {
            return 6;
        } else if(Rank.equalsIgnoreCase(Config.CoLeader)) {
            return 5;
        } else if(Rank.equalsIgnoreCase(Config.VipMember)) {
            return 4;
        } else if(Rank.equalsIgnoreCase(Config.Warrior)) {
            return 3;
        } else if(Rank.equalsIgnoreCase(Config.Member)) {
            return 2;
        } else {
            return 1;
        }
    }


    public static void SetFactionSpawn(String FactionUUID, String ConvertLoc) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=spawn") + ":=:add:=:" + CommonUtils.String2Byte(Server.getServerName() + "===" + ConvertLoc));
        RegisterFactionInfo(FactionUUID, "spawn");
    }

    public static Boolean FactionSpawnExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=spawn");
    }

    public static String getFactionSpawn(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=spawn");
    }

    public static void ClearFactionInfo(String FactionUUID) {
        if(FactionStorage.FactionInfoList.containsKey(FactionUUID)) {
            for (String key : FactionStorage.FactionInfoList.get(FactionUUID)) {
                Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=" + key) + ":=:remove:=:" + CommonUtils.String2Byte("YES"));
                key = null;
            }
            Storage.AddCommandToQueue("update:=:FactionInfoList:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte("ddd"));
        }
    }

    public static void RemoveFactionSpawn(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=spawn") + ":=:add:=:" + CommonUtils.String2Byte(Server.getServerName()));
        Storage.AddCommandToQueue("update:=:FactionInfoList:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte("spawn"));
    }

    public static void SetFactionNotice(String FactionUUID, String factionnotice) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=notice") + ":=:add:=:" + CommonUtils.String2Byte(factionnotice));
        RegisterFactionInfo(FactionUUID, "notice");
    }

    public static Boolean FactionNoticeExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=notice");
    }

    public static String GetFactionNotice(String FactionUUID) {
        if(!FactionStorage.FactionInfo.containsKey(FactionUUID + "=notice")) {
            return Lang.FACTION_DEFAULT_NOTICE;
        } else {
            return FactionStorage.FactionInfo.get(FactionUUID + "=notice");
        }
    }

    public static String GetFactionOutPostWarpLocation(String FactionUUID, String OutPostName) {
        //String key = FactionUUID + "=warplocation=" + OutPostName;
        return FactionStorage.FactionInfo.get(FactionUUID + "=warplocation=" + OutPostName);
    }

    public static String GetBeaconLocation(String FactionUUID, String OutPostName) {
        //String key = FactionUUID + "=beacon=" + OutPostName;
        return FactionStorage.FactionInfo.get(FactionUUID + "=beacon=" + OutPostName);
    }

    public static String GetFactionOutPostName(String Chunkkey) {
        if(FactionStorage.OutPostToFaction.containsKey(Chunkkey)) {
            //String FactionUUID = FactionStorage.OutPostToFaction.get(Chunkkey);
            //String key = FactionUUID + "=outpost=" + Chunkkey;
            return FactionStorage.FactionInfo.get(FactionStorage.OutPostToFaction.get(Chunkkey) + "=outpost=" + Chunkkey);
        } else {
            return null;
        }
    }

    public static String GetFactionOutPostChunkkey(String FactionUUID, String OutPostName) {
        //String key = FactionUUID + "=" + OutPostName;
        return FactionStorage.FactionOutPost.get(FactionUUID + "=" + OutPostName);
    }

    public static void RemoveFactionNotice(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=notice") + ":=:add:=:" + CommonUtils.String2Byte("D"));
    }

    public static void SetFactionDesc(String FactionUUID, String factionDesc) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=desc") + ":=:add:=:" + CommonUtils.String2Byte(factionDesc));
        RegisterFactionInfo(FactionUUID, "desc");
    }

    public static Boolean FactionDescExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=desc");
    }

    public static String GetFactionDesc(String FactionUUID) {
        if(!FactionStorage.FactionInfo.containsKey(FactionUUID + "=desc")) {
            return Lang.FACTION_DEFAULT_DESC;
        } else {
            return FactionStorage.FactionInfo.get(FactionUUID + "=desc");
        }
    }

    public static void RemoveFactionDesc(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=desc") + ":=:add:=:" + CommonUtils.String2Byte("D"));
    }

    public static void WarpLocation(String UUID, String ServerName, String Location, Boolean isExpire) {
        if(!isExpire) {
            Storage.AddCommandToQueue("warplocation:=:" + CommonUtils.String2Byte(UUID) + ":=:" + ServerName + ":=:" + CommonUtils.String2Byte(Location) + ":=:" + CommonUtils.String2Byte("notexpired"));
        } else {
            Storage.AddCommandToQueue("warplocation:=:" + CommonUtils.String2Byte(UUID) + ":=:" + ServerName + ":=:" + CommonUtils.String2Byte(Location) + ":=:" + CommonUtils.String2Byte("expired"));
        }
    }

    public static void RegisterFactionInfo(String FactionUUID, String type) {
        Storage.AddCommandToQueue("update:=:FactionInfoList:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(type));
    }
    public static void UnregisterFactionInfo(String FactionUUID, String type) {
        Storage.AddCommandToQueue("update:=:FactionInfoList:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:remove:=:" + CommonUtils.String2Byte(type));
    }

    public static Boolean isInWar(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=war");
    }

    public static String getOPPWar(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=war");
    }

    public static void setOPPWar(String FactionUUID, String OPP_FactionUUID) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(FactionUUID + "=war") + ":=:add:=:" + CommonUtils.String2Byte(OPP_FactionUUID));
        Storage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + CommonUtils.String2Byte(OPP_FactionUUID + "=war") + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID));
        RegisterFactionInfo(FactionUUID, "war");
        RegisterFactionInfo(OPP_FactionUUID, "war");
    }

    public static void removeFromWar(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + CommonUtils.String2Byte(FactionUUID + "=war") + ":=:add:=:" + CommonUtils.String2Byte("DEAD_FACTION"));
        UnregisterFactionInfo(FactionUUID, "war");
    }

    public static Boolean hasMainBeacon(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=mainbeacon");
    }

    public static String getMainBeaconStringLocation(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=mainbeacon");
    }
}
