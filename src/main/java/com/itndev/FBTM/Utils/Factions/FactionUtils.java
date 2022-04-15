package com.itndev.FBTM.Utils.Factions;

import com.itndev.FBTM.Database.Redis.Obj.Storage;
import com.itndev.FBTM.Factions.Config;
import com.itndev.FBTM.Factions.FactionStorage;
import com.itndev.FBTM.Factions.Lang;
import com.itndev.FBTM.Server;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FactionUtils {
    public static String newFactionUUID() {
        String uuid;
        Long time = System.currentTimeMillis();
        UUID uuid2 = UUID.randomUUID();
        uuid = String.valueOf(time) + "=" + String.valueOf(uuid2);
        return uuid;
    }

    public static void SetPlayerRank(String UUID, String Rank) {
        if(Rank == null) {
            Storage.AddCommandToQueue("update:=:FactionRank:=:remove:=:" + UUID + ":=:add:=:" + Rank.toLowerCase(Locale.ROOT));
        } else {
            Storage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + UUID + ":=:add:=:" + Rank.toLowerCase(Locale.ROOT));
        }
    }

    public static String SetPlayerRank_GETRAWCMD(String UUID, String Rank) {
        if(Rank == null) {
            return ("update:=:FactionRank:=:remove:=:" + UUID + ":=:add:=:" + Rank.toLowerCase(Locale.ROOT));
        } else {
            return ("update:=:FactionRank:=:add:=:" + UUID + ":=:add:=:" + Rank.toLowerCase(Locale.ROOT));
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
        for(String UUID : FactionUtils.getFactionMember(FactionUUID)) {
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                return UUID;
            }
        }
        return null;
    }

    public static Boolean isExistingFaction(String FactionName) {
        return FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT));
    }

    public static void SetPlayerFaction(String UUID, String FactionUUID) {
        if(FactionUUID == null) {
            Storage.AddCommandToQueue("update:=:PlayerFaction:=:remove:=:" + UUID + ":=:add:=:ddd");
        } else {
            Storage.AddCommandToQueue("update:=:PlayerFaction:=:add:=:" + UUID + ":=:add:=:" + FactionUUID);
        }
    }

    public static String SetPlayerFaction_GETRAWCMD(String UUID, String FactionUUID) {
        if(FactionUUID == null) {
            return "update:=:PlayerFaction:=:remove:=:" + UUID + ":=:add:=:ddd";
        } else {
            return "update:=:PlayerFaction:=:add:=:" + UUID + ":=:add:=:" + FactionUUID;
        }
    }

    public static void SetFactionMember(String UUID, String FactionUUID, Boolean Remove) {
        if(FactionUUID == null) {
            return;
        }
        if(!Remove) {
            Storage.AddCommandToQueue("update:=:FactionMember:=:add:=:" + FactionUUID + ":=:add:=:" + UUID);
        } else {
            Storage.AddCommandToQueue("update:=:FactionMember:=:add:=:" + FactionUUID + ":=:remove:=:" + UUID);
        }
    }

    public static String SetFactionMember_GETRAWCMD(String UUID, String FactionUUID, Boolean Remove) {
        if(!Remove) {
            return "update:=:FactionMember:=:add:=:" + FactionUUID + ":=:add:=:" + UUID;
        } else {
            return "update:=:FactionMember:=:add:=:" + FactionUUID + ":=:remove:=:" + UUID;
        }
    }

    public static void SetFactionName(String FactionUUID, String NewFactionName) {
        //Faction name update
        String oldFactionName = getFactionName(FactionUUID);
        if(oldFactionName.equalsIgnoreCase(NewFactionName)) {
            Storage.AddCommandToQueue("update:=:FactionNameToFactionName:=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            //JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + oldFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + FactionUUID);
            Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + FactionUUID + ":=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT));
        } else {
            Storage.AddCommandToQueue("update:=:FactionNameToFactionName:=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + oldFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + FactionUUID);
            Storage.AddCommandToQueue("update:=:FactionUUIDToFactionName:=:add:=:" + FactionUUID + ":=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT));
        }
    }

    public static void CreateFaction(String LeaderUUID, String FactionUUID, String FactionOrginName) {
        String FactionName = FactionOrginName.toLowerCase(Locale.ROOT);
        Storage.AddCommandToQueue("update:=:FactionMember:=:add:=:" + FactionUUID + ":=:add:=:" + LeaderUUID);
        Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + FactionName + ":=:add:=:" + FactionUUID);
        Storage.AddCommandToQueue("update:=:FactionNameToFactionName:=:add:=:" + FactionName + ":=:add:=:" + FactionOrginName);
        Storage.AddCommandToQueue("update:=:FactionUUIDToFactionName:=:add:=:" + FactionUUID + ":=:add:=:" + FactionName);
        Storage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + LeaderUUID + ":=:add:=:" + Config.Leader);
        Storage.AddCommandToQueue("update:=:PlayerFaction:=:add:=:" + LeaderUUID + ":=:add:=:" + FactionUUID);
    }

    public static String Build_PERMLVLINFO(int PERMLVL, int FINALPERMLVL, String SUCCESSID) {
        return ":=:" + Config.PERM_LEVEL_INDICATOR + PERMLVL + ":=:" + Config.PERM_FINAL_LEVEL_INDICATOR + FINALPERMLVL + ":=:" + Config.SUCCESS_ID_INDICATOR + SUCCESSID;
    }

    public static void DeleteFaction(String FactionUUID) {
        String FactionName = getFactionName(FactionUUID).toLowerCase(Locale.ROOT);
        Storage.AddCommandToQueue("update:=:FactionMember:=:remove:=:" + FactionUUID + ":=:remove:=:" + "UUID");
        Storage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + FactionName + ":=:add:=:" + "nothing");
        Storage.AddCommandToQueue("update:=:FactionNameToFactionName:=:remove:=:" + FactionName + ":=:add:=:" + "nothing");
        Storage.AddCommandToQueue("update:=:FactionUUIDToFactionName:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        if(FactionStorage.FactionMember.containsKey(FactionUUID)) {
            for(String PlayerUUID : FactionStorage.FactionMember.get(FactionUUID)) {
                Storage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + PlayerUUID + ":=:add:=:" + Config.Nomad);
                Storage.AddCommandToQueue("update:=:PlayerFaction:=:remove:=:" + PlayerUUID + ":=:add:=:ddd");
            }
        }
        for(String Chunkkey : FactionStorage.FactionToLand.keySet()) {
            UnClaimLand(FactionUUID, Chunkkey);
        }
        ClearFactionInfo(FactionUUID);
        for(String Chunkkey : FactionStorage.FactionToOutPost.keySet()) {
            UnClaimOutPost(FactionUUID, Chunkkey);
        }
        Storage.AddCommandToQueue("update:=:FactionToLand:=:remove:=:" + FactionUUID + ":=:remove:=:" + FactionUUID + ":=:bulk");
        Storage.AddCommandToQueue("update:=:FactionToLand:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        Storage.AddCommandToQueue("update:=:FactionOutPost:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        Storage.AddCommandToQueue("update:=:FactionWarpLocations:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
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
            Storage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + targetuuid + ":=:" + message + ":=:" + "false");
        } else {
            Storage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + type + ":=:" + message + ":=:" + "true");
        }
    }

    public static void SendFactionMessage_GETRAWCMD(String playeruuid, String targetuuid, String type, String message) {
        if(type.equalsIgnoreCase("single")) {
            //type : SIBAL, TeamChat, all
            Storage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + targetuuid + ":=:" + message + ":=:" + "false");
        } else {
            Storage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + type + ":=:" + message + ":=:" + "true");
        }
    }

    public static void ClaimLand(String FactionUUID, String Chunkkey) {
        FactionStorage.LandToFaction.put(Chunkkey, FactionUUID);
        Storage.AddCommandToQueue("update:=:LandToFaction:=:add:=:" + Chunkkey + ":=:add:=:" + FactionUUID + ":=:" + Server.getServerName());
        ArrayList<String> updatelist = new ArrayList<>();
        if(FactionStorage.FactionToLand.containsKey(FactionUUID)) {
            updatelist = FactionStorage.FactionToLand.get(FactionUUID);
        }
        updatelist.add(Chunkkey);
        FactionStorage.FactionToLand.put(FactionUUID, updatelist);
        Storage.AddCommandToQueue("update:=:FactionToLand:=:add:=:" + FactionUUID + ":=:add:=:" + Chunkkey + ":=:" + Server.getServerName());
    }

    public static void UnClaimLand(String FactionUUID, String Chunkkey) {
        FactionStorage.LandToFaction.remove(Chunkkey);
        Storage.AddCommandToQueue("update:=:LandToFaction:=:remove:=:" + Chunkkey + ":=:remove:=:" + FactionUUID + ":=:" + Server.getServerName());
        ArrayList<String> updatelist = FactionStorage.FactionToLand.get(FactionUUID);
        updatelist.remove(Chunkkey);
        if(updatelist.isEmpty()) {
            FactionStorage.FactionToLand.remove(FactionUUID);
        } else {
            FactionStorage.FactionToLand.put(FactionUUID, updatelist);
        }
        Storage.AddCommandToQueue("update:=:FactionToLand:=:add:=:" + FactionUUID + ":=:remove:=:" + Chunkkey + ":=:" + Server.getServerName());
    }

    public static void ClaimOutPost(String FactionUUID, String Chunkkey) {
        FactionStorage.OutPostToFaction.put(Chunkkey, FactionUUID);
        Storage.AddCommandToQueue("update:=:OutPostToFaction:=:add:=:" + Chunkkey + ":=:add:=:" + FactionUUID + ":=:" + Server.getServerName());
        ArrayList<String> updatelist = new ArrayList<>();
        if(FactionStorage.FactionToOutPost.containsKey(FactionUUID)) {
            updatelist = FactionStorage.FactionToOutPost.get(FactionUUID);
        }
        updatelist.add(Chunkkey);
        FactionStorage.FactionToOutPost.put(FactionUUID, updatelist);
        Storage.AddCommandToQueue("update:=:FactionToOutPost:=:add:=:" + FactionUUID + ":=:add:=:" + Chunkkey + ":=:" + Server.getServerName());
    }

    public static void UnClaimOutPost(String FactionUUID, String Chunkkey) {
        FactionStorage.OutPostToFaction.remove(Chunkkey);
        Storage.AddCommandToQueue("update:=:OutPostToFaction:=:remove:=:" + Chunkkey + ":=:remove:=:" + FactionUUID + ":=:" + Server.getServerName());
        ArrayList<String> updatelist = FactionStorage.FactionToOutPost.get(FactionUUID);
        updatelist.remove(Chunkkey);
        if(updatelist.isEmpty()) {
            FactionStorage.FactionToOutPost.remove(FactionUUID);
        } else {
            FactionStorage.FactionToOutPost.put(FactionUUID, updatelist);
        }
        Storage.AddCommandToQueue("update:=:FactionToOutPost:=:add:=:" + FactionUUID + ":=:remove:=:" + Chunkkey + ":=:" + Server.getServerName());
    }


    public static Boolean isAExistingLangRank(String LangRank) {
        String lowcaserank = LangRank.toLowerCase(Locale.ROOT);
        if(lowcaserank.equalsIgnoreCase(Config.Leader_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.CoLeader_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.VipMember_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.Warrior_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.Member_Lang)) {
            return true;
        } else {
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

    public static ArrayList<String> getFactionMember(String FactionUUID) {
        ArrayList<String> finallist = new ArrayList<>();
        if(FactionStorage.FactionMember.containsKey(FactionUUID)) {
            finallist = FactionStorage.FactionMember.get(FactionUUID);
        }
        return finallist;
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
        if(RankPrio(PlayerRank) > RankPrio(Rank)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean HigherThenRank(String UUID, String Rank) {
        String PlayerRank = FactionUtils.getPlayerRank(UUID);
        if(RankPrio(PlayerRank) >= RankPrio(Rank)) {
            return true;
        } else {
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
        Storage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=spawn" + ":=:add:=:" + Server.getServerName() + "===" + ConvertLoc);
        RegisterFactionInfo(FactionUUID, "spawn");
    }

    public static Boolean FactionSpawnExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=spawn");
    }

    public static String getFactionSpawn(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=spawn");
    }

    public static void ClearFactionInfo(String FactionUUID) {
        if(FactionStorage.FactionInfo.containsKey(FactionUUID)) {
            for (String key : FactionStorage.FactionInfoList.get(FactionUUID)) {
                Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=" + key + ":=:remove:=:");
            }
            Storage.AddCommandToQueue("update:=:FactionInfoList:=:remove:=:" + FactionUUID + ":=:remove:=:");
        }
    }

    public static void RemoveFactionSpawn(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=spawn" + ":=:add:=:" + Server.getServerName());
        Storage.AddCommandToQueue("update:=:FactionInfoList:=:add:=:" + FactionUUID + "" + ":=:remove:=:" + "spawn");
    }

    public static void SetFactionNotice(String FactionUUID, String factionnotice) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=notice" + ":=:add:=:" + factionnotice);
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
        String key = FactionUUID + "=warplocation=" + OutPostName;
        return FactionStorage.FactionInfo.get(key);
    }

    public static String GetBeaconLocation(String FactionUUID, String OutPostName) {
        String key = FactionUUID + "=beacon=" + OutPostName;
        return FactionStorage.FactionInfo.get(key);
    }

    public static String GetFactionOutPostName(String Chunkkey) {
        if(FactionStorage.OutPostToFaction.containsKey(Chunkkey)) {
            String FactionUUID = FactionStorage.OutPostToFaction.get(Chunkkey);
            String key = FactionUUID + "=outpost=" + Chunkkey;
            return FactionStorage.FactionInfo.get(key);
        } else {
            return null;
        }
    }

    public static String GetFactionOutPostChunkkey(String FactionUUID, String OutPostName) {
        String key = FactionUUID + "=" + OutPostName;
        return FactionStorage.FactionOutPost.get(key);
    }

    public static void RemoveFactionNotice(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=notice" + ":=:add:=:" + "D");
    }

    public static void SetFactionDesc(String FactionUUID, String factionDesc) {
        Storage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=desc" + ":=:add:=:" + factionDesc);
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
        Storage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=desc" + ":=:add:=:" + "D");
    }

    public static void WarpLocation(String UUID, String ServerName, String Location, Boolean isExpire) {
        if(!isExpire) {
            Storage.AddCommandToQueue("warplocation:=:" + UUID + ":=:" + ServerName + ":=:" + Location + ":=:notexpired");
        } else {
            Storage.AddCommandToQueue("warplocation:=:" + UUID + ":=:" + ServerName + ":=:" + Location + ":=:expired");
        }
    }

    public static void RegisterFactionInfo(String FactionUUID, String type) {
        Storage.AddCommandToQueue("update:=:FactionInfoList:=:add:=:" + FactionUUID + ":=:add:=:" + type);
    }
}
