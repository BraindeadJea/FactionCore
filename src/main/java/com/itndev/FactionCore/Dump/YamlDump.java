package com.itndev.FactionCore.Dump;

import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Discord.AuthStorage;
import com.itndev.FactionCore.Factions.FactionStorage;
import com.itndev.FactionCore.Factions.UserInfoStorage;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class YamlDump {
    private static Boolean debug = true;

    public static String StoragePath = "Storage";

    public static void CreateFile(String FileName) {
        try {
            File file = new File(FileName);
            if(!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SaveConnectionInfo() {
        //CreateDirectory();
        DumpYaml(Connect.get_LastID_INNER(), "LastID_INNER");
        DumpYaml(Connect.get_LastID_INPUT(), "LastID_INPUT");
        DumpYaml(Connect.get_LastID_BUNGEE(), "LastID_BUNGEE");
    }

    public static void LoadConnectionInfo() {
        //CreateDirectory();
        if(LoadYaml("LastID_INNER") != null) {
            Connect.setLastID_INNER((String) LoadYaml("LastID_INNER"));
        }
        if(LoadYaml("LastID_INPUT") != null) {
            Connect.setLastID_INPUT((String) LoadYaml("LastID_INPUT"));
        }
        if(LoadYaml("LastID_BUNGEE") != null) {
            Connect.set_LastID_BUNGEE((String) LoadYaml("LastID_BUNGEE"));
        }
    }

    public static void TryDumpHashMaps_DISCORD() {
        HashMap<String, String> TEMP_DISCORDID_TO_UUID = AuthStorage.DISCORDID_TO_UUID;
        HashMap<String, String> TEMP_UUID_TO_DISCORDID = AuthStorage.UUID_TO_DISCORDID;
        DumpYaml(TEMP_DISCORDID_TO_UUID, "DISCORDID_TO_UUID");
        DumpYaml(TEMP_UUID_TO_DISCORDID, "UUID_TO_DISCORDID");
    }

    public static void TryLoadHashMaps_DISCORD() {
        if (new File("DISCORDID_TO_UUID.yml").exists()) {
            AuthStorage.DISCORDID_TO_UUID = (HashMap<String, String>) LoadYaml("DISCORDID_TO_UUID");
        }
        if (new File("UUID_TO_DISCORDID.yml").exists()) {
            AuthStorage.UUID_TO_DISCORDID = (HashMap<String, String>) LoadYaml("UUID_TO_DISCORDID");
        }
    }

    public static void TryDumpHashMaps_Factions_LISTTYPE() {
        ConcurrentHashMap<String, ArrayList<String>> TEMP_FactionInfoList = FactionStorage.FactionInfoList;
        DumpYaml(RedisDump.ConcurrentListMapConvert(TEMP_FactionInfoList), "FactionInfoList");
        ConcurrentHashMap<String, ArrayList<String>> TEMP_FactionOutPostList = FactionStorage.FactionOutPostList;
        DumpYaml(RedisDump.ConcurrentListMapConvert(TEMP_FactionOutPostList), "FactionOutPostList");
        ConcurrentHashMap<String, ArrayList<String>> TEMP_FactionMember = FactionStorage.FactionMember;
        DumpYaml(RedisDump.ConcurrentListMapConvert(TEMP_FactionMember), "FactionMember");
        HashMap<String, ArrayList<String>> TEMP_FactionToLand = FactionStorage.FactionToLand;
        DumpYaml(RedisDump.ListMapConvert(TEMP_FactionToLand), "FactionToLand");
        HashMap<String, ArrayList<String>> TEMP_FactionToOutPost = FactionStorage.FactionToOutPost;
        DumpYaml(RedisDump.ListMapConvert(TEMP_FactionToOutPost), "FactionToOutPost");
    }

    public static void TryLoadHashMaps_Factions_LISTTYPE() {
        if (new File("FactionInfoList.yml").exists()) {
            HashMap<String, String> TEMP_FactionInfoList = (HashMap<String, String>) LoadYaml("FactionInfoList");
            FactionStorage.FactionInfoList = RedisDump.ConcurrentMapListConvert(TEMP_FactionInfoList);
        }

        if (new File("FactionOutPostList.yml").exists()) {
            HashMap<String, String> TEMP_FactionOutPostList = (HashMap<String, String>) LoadYaml("FactionOutPostList");
            FactionStorage.FactionOutPostList = RedisDump.ConcurrentMapListConvert(TEMP_FactionOutPostList);
        }

        if (new File("FactionMember.yml").exists()) {
            HashMap<String, String> TEMP_FactionMember = (HashMap<String, String>) LoadYaml("FactionMember");
            FactionStorage.FactionMember = RedisDump.ConcurrentMapListConvert(TEMP_FactionMember);
        }

        if (new File("FactionToLand.yml").exists()) {
            HashMap<String, String> TEMP_FactionToLand = (HashMap<String, String>) LoadYaml("FactionToLand");
            FactionStorage.FactionToLand = RedisDump.MapListConvert(TEMP_FactionToLand);
        }

        if (new File("FactionToOutPost.yml").exists()) {
            HashMap<String, String> TEMP_FactionToOutPost = (HashMap<String, String>) LoadYaml("FactionToOutPost");
            FactionStorage.FactionToOutPost = RedisDump.MapListConvert(TEMP_FactionToOutPost);
        }
    }

    public static void TryDumpHashMaps_Factions() {
        ConcurrentHashMap<String, String> TEMP_FactionInfo = FactionStorage.FactionInfo;
        DumpYaml(RedisDump.FixMap1(TEMP_FactionInfo), "FactionInfo");

        ConcurrentHashMap<String, String> TEMP_FactionNameToFactionName = FactionStorage.FactionNameToFactionName;
        DumpYaml(RedisDump.FixMap1(TEMP_FactionNameToFactionName), "FactionNameToFactionName");

        ConcurrentHashMap<String, String> TEMP_FactionRank = FactionStorage.FactionRank;
        DumpYaml(RedisDump.FixMap1(TEMP_FactionRank), "FactionRank");

        ConcurrentHashMap<String, String> TEMP_FactionNameToFactionUUID = FactionStorage.FactionNameToFactionUUID;
        DumpYaml(RedisDump.FixMap1(TEMP_FactionNameToFactionUUID), "FactionNameToFactionUUID");

        ConcurrentHashMap<String, String> TEMP_FactionUUIDToFactionName = FactionStorage.FactionUUIDToFactionName;
        DumpYaml(RedisDump.FixMap1(TEMP_FactionUUIDToFactionName), "FactionUUIDToFactionName");

        ConcurrentHashMap<String, String> TEMP_FactionOutPost = FactionStorage.FactionOutPost;
        DumpYaml(RedisDump.FixMap1(TEMP_FactionOutPost), "FactionOutPost");

        ConcurrentHashMap<String, String> TEMP_FactionWarpLocations = FactionStorage.FactionWarpLocations;
        DumpYaml(RedisDump.FixMap1(TEMP_FactionWarpLocations), "FactionWarpLocations");

        ConcurrentHashMap<String, String> TEMP_PlayerFaction = FactionStorage.PlayerFaction;
        DumpYaml(RedisDump.FixMap1(TEMP_PlayerFaction), "PlayerFaction");



        //normal hashmap
        HashMap<String, String> TEMP_LandToFaction = FactionStorage.LandToFaction;
        DumpYaml(TEMP_LandToFaction, "LandToFaction");

        HashMap<String, String> TEMP_OutPostToFaction = FactionStorage.OutPostToFaction;
        DumpYaml(TEMP_OutPostToFaction, "OutPostToFaction");
    }

    public static void TryLoadHashMaps_Factions() {
        if (new File("FactionInfo.yml").exists()) {
            HashMap<String, String> TEMP_FactionInfo = (HashMap<String, String>) LoadYaml("FactionInfo");
            FactionStorage.FactionInfo = RedisDump.FixMap2(TEMP_FactionInfo);
        }

        if (new File("FactionNameToFactionName.yml").exists()) {
            HashMap<String, String> TEMP_FactionNameToFactionName = (HashMap<String, String>) LoadYaml("FactionNameToFactionName");
            FactionStorage.FactionNameToFactionName = RedisDump.FixMap2(TEMP_FactionNameToFactionName);
        }

        if (new File("FactionRank.yml").exists()) {
            HashMap<String, String> TEMP_FactionRank = (HashMap<String, String>) LoadYaml("FactionRank");
            FactionStorage.FactionRank = RedisDump.FixMap2(TEMP_FactionRank);
        }

        if (new File("FactionNameToFactionUUID.yml").exists()) {
            HashMap<String, String> TEMP_FactionNameToFactionUUID = (HashMap<String, String>) LoadYaml("FactionNameToFactionUUID");
            FactionStorage.FactionNameToFactionUUID = RedisDump.FixMap2(TEMP_FactionNameToFactionUUID);
        }

        if (new File("FactionUUIDToFactionName.yml").exists()) {
            HashMap<String, String> TEMP_FactionUUIDToFactionName = (HashMap<String, String>) LoadYaml("FactionUUIDToFactionName");
            FactionStorage.FactionUUIDToFactionName = RedisDump.FixMap2(TEMP_FactionUUIDToFactionName);
        }

        if (new File("FactionOutPost.yml").exists()) {
            HashMap<String, String> TEMP_FactionOutPost = (HashMap<String, String>) LoadYaml("FactionOutPost");
            FactionStorage.FactionOutPost = RedisDump.FixMap2(TEMP_FactionOutPost);
        }

        if (new File("FactionWarpLocations.yml").exists()) {
            HashMap<String, String> TEMP_FactionWarpLocations = (HashMap<String, String>) LoadYaml("FactionWarpLocations");
            FactionStorage.FactionWarpLocations = RedisDump.FixMap2(TEMP_FactionWarpLocations);
        }

        if (new File("PlayerFaction.yml").exists()) {
            HashMap<String, String> TEMP_PlayerFaction = (HashMap<String, String>) LoadYaml("PlayerFaction");
            FactionStorage.PlayerFaction = RedisDump.FixMap2(TEMP_PlayerFaction);
        }

        if (new File("LandToFaction.yml").exists()) {
            FactionStorage.LandToFaction = (HashMap<String, String>) LoadYaml("LandToFaction");
        }

        if (new File("OutPostToFaction.yml").exists()) {
            FactionStorage.OutPostToFaction = (HashMap<String, String>) LoadYaml("OutPostToFaction");
        }
    }

    public static void TryDumpHashMaps_UUIDINFO() {
        ConcurrentHashMap<String, String> TEMP_namename = UserInfoStorage.namename;
        DumpYaml(RedisDump.FixMap1(TEMP_namename), "namename");

        ConcurrentHashMap<String, String> TEMP_nameuuid = UserInfoStorage.nameuuid;
        DumpYaml(RedisDump.FixMap1(TEMP_nameuuid), "nameuuid");

        ConcurrentHashMap<String, String> TEMP_uuidname = UserInfoStorage.uuidname;
        DumpYaml(RedisDump.FixMap1(TEMP_uuidname), "uuidname");
    }

    public static void TryLoadHashMaps_UUIDINFO() {
        if (new File("namename.yml").exists()) {
            HashMap<String, String> TEMP_namename = (HashMap<String, String>) LoadYaml("namename");
            UserInfoStorage.namename = RedisDump.FixMap2(TEMP_namename);
        }

        if (new File("nameuuid.yml").exists()) {
            HashMap<String, String> TEMP_nameuuid = (HashMap<String, String>) LoadYaml("nameuuid");
            UserInfoStorage.nameuuid = RedisDump.FixMap2(TEMP_nameuuid);
        }

        if (new File("uuidname.yml").exists()) {
            HashMap<String, String> TEMP_uuidname = (HashMap<String, String>) LoadYaml("uuidname");
            UserInfoStorage.uuidname = RedisDump.FixMap2(TEMP_uuidname);
        }
    }

    public static Object LoadYaml(String filename) {
        try {
            Yaml yaml = new Yaml();
            FileReader reader = new FileReader(filename + ".yml");
            return yaml.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ERROR] Cant Load Data");
        return null;
    }

    public static void DumpYaml(Object data, String filename) {
        try {
            Yaml yaml = new Yaml(new Constructor(data.getClass()));
            FileWriter writer;
            writer = new FileWriter(filename + ".yml");
            yaml.dump(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
