package com.itndev.FactionCore.Dump;

import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Discord.AuthStorage;
import com.itndev.FactionCore.Factions.FactionStorage;
import com.itndev.FactionCore.Factions.UserInfoStorage;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RedisDump {

    private static String key = "%$75675";
    private static String value = "45364";

    public static void deleteandupload(String key) {
        RemoveUploadedStorage(key);
        UploadStorageToRedis(key);
    }

    public static void remove_Verification() {
        Connect.getSetcommands().del(key);
    }

    public static void set_Verification() {
        Connect.getSetcommands().set(key, value);
    }

    public static Boolean has_Verification() {
        if(Connect.getSetcommands().get(key).equalsIgnoreCase(value)) {
            remove_Verification();
            return true;
        }
        return false;
    }

    public static void UploadStorageToRedis(String key) {
        if(key == null) {
            if (!FactionStorage.FactionInfo.isEmpty()) {
                Connect.getSetcommands().hmset("FactionInfo", FactionStorage.FactionInfo);
            }

            //FactionStorage.AsyncLandToFaction = (ConcurrentHashMap<String, String>) setcommands.hmget("FactionInfo");
            if(!FactionStorage.FactionMember.isEmpty()) {
                Connect.getSetcommands().hmset("FactionMember", ConcurrentListMapConvert(FactionStorage.FactionMember));
            }

            if(!FactionStorage.FactionNameToFactionName.isEmpty()) {
                Connect.getSetcommands().hmset("FactionNameToFactionName", FactionStorage.FactionNameToFactionName);
            }

            if(!FactionStorage.FactionInfoList.isEmpty()) {
                Connect.getSetcommands().hmset("FactionInfoList", ConcurrentListMapConvert(FactionStorage.FactionInfoList));
                //Connect..expireat("FactionInfoList", expiretime);
            }
            //FactionStorage.AsyncOutPostToFaction;

            if(!FactionStorage.FactionNameToFactionUUID.isEmpty()) {
                Connect.getSetcommands().hmset("FactionNameToFactionUUID", FactionStorage.FactionNameToFactionUUID);

            }

            if(!FactionStorage.FactionOutPost.isEmpty()) {
                Connect.getSetcommands().hmset("FactionOutPost", FactionStorage.FactionOutPost);
            }

            if(!FactionStorage.FactionOutPostList.isEmpty()) {
                Connect.getSetcommands().hmset("FactionOutPostList", ConcurrentListMapConvert(FactionStorage.FactionOutPostList));
            }

            if(!FactionStorage.FactionRank.isEmpty()) {
                Connect.getSetcommands().hmset("FactionRank", FactionStorage.FactionRank);
            }

            if(!FactionStorage.FactionToLand.isEmpty()) {
                Connect.getSetcommands().hmset("FactionToLand", ListMapConvert(FactionStorage.FactionToLand));
            }

            if(!FactionStorage.FactionToOutPost.isEmpty()) {
                Connect.getSetcommands().hmset("FactionToOutPost", ListMapConvert(FactionStorage.FactionToOutPost));
            }

            if(!FactionStorage.FactionUUIDToFactionName.isEmpty()) {
                Connect.getSetcommands().hmset("FactionUUIDToFactionName", FactionStorage.FactionUUIDToFactionName);
            }

            if(!FactionStorage.FactionWarpLocations.isEmpty()) {
                Connect.getSetcommands().hmset("FactionWarpLocations", FactionStorage.FactionWarpLocations);
            }

            if(!FactionStorage.OutPostToFaction.isEmpty()) {
                Connect.getSetcommands().hmset("OutPostToFaction", FactionStorage.OutPostToFaction);
            }

            if(!FactionStorage.PlayerFaction.isEmpty()) {
                Connect.getSetcommands().hmset("PlayerFaction", FactionStorage.PlayerFaction);
            }

            if(!UserInfoStorage.namename.isEmpty()) {
                Connect.getSetcommands().hmset("namename", UserInfoStorage.namename);
            }

            if(!UserInfoStorage.nameuuid.isEmpty()) {
                Connect.getSetcommands().hmset("nameuuid", UserInfoStorage.nameuuid);
            }

            if(!UserInfoStorage.uuidname.isEmpty()) {
                Connect.getSetcommands().hmset("uuidname", UserInfoStorage.uuidname);
            }

            if(!AuthStorage.DISCORDID_TO_UUID.isEmpty()) {
                Connect.getSetcommands().hmset("DISCORDID_TO_UUID", AuthStorage.DISCORDID_TO_UUID);
            }

            if(!AuthStorage.UUID_TO_DISCORDID.isEmpty()) {
                Connect.getSetcommands().hmset("UUID_TO_DISCORDID", AuthStorage.UUID_TO_DISCORDID);
            }
        } else {
            if (!FactionStorage.FactionInfo.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionInfo", FactionStorage.FactionInfo);
            }

            //FactionStorage.AsyncLandToFaction = (ConcurrentHashMap<String, String>) setcommands.hmget("FactionInfo");
            if(!FactionStorage.FactionMember.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionMember", ConcurrentListMapConvert(FactionStorage.FactionMember));
            }

            if(!FactionStorage.FactionNameToFactionName.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionNameToFactionName", FactionStorage.FactionNameToFactionName);
            }

            if(!FactionStorage.FactionInfoList.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionInfoList", ConcurrentListMapConvert(FactionStorage.FactionInfoList));
            }
            //FactionStorage.AsyncOutPostToFaction;

            if(!FactionStorage.FactionNameToFactionUUID.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionNameToFactionUUID", FactionStorage.FactionNameToFactionUUID);
            }

            if(!FactionStorage.FactionOutPost.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionOutPost", FactionStorage.FactionOutPost);
            }

            if(!FactionStorage.FactionOutPostList.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionOutPostList", ConcurrentListMapConvert(FactionStorage.FactionOutPostList));
            }

            if(!FactionStorage.FactionRank.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionRank", FactionStorage.FactionRank);
            }

            if(!FactionStorage.FactionToLand.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionToLand", ListMapConvert(FactionStorage.FactionToLand));
            }

            if(!FactionStorage.FactionToOutPost.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionToOutPost", ListMapConvert(FactionStorage.FactionToOutPost));
            }

            if(!FactionStorage.FactionUUIDToFactionName.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionUUIDToFactionName", FactionStorage.FactionUUIDToFactionName);
            }

            if(!FactionStorage.FactionWarpLocations.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "FactionWarpLocations", FactionStorage.FactionWarpLocations);
            }

            if(!FactionStorage.OutPostToFaction.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "OutPostToFaction", FactionStorage.OutPostToFaction);
            }

            if(!FactionStorage.PlayerFaction.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "PlayerFaction", FactionStorage.PlayerFaction);
            }

            if(!UserInfoStorage.namename.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "namename", UserInfoStorage.namename);
            }

            if(!UserInfoStorage.nameuuid.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "nameuuid", UserInfoStorage.nameuuid);
            }

            if(!UserInfoStorage.uuidname.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "uuidname", UserInfoStorage.uuidname);
            }

            if(!AuthStorage.DISCORDID_TO_UUID.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "DISCORDID_TO_UUID", AuthStorage.DISCORDID_TO_UUID);
            }

            if(!AuthStorage.UUID_TO_DISCORDID.isEmpty()) {
                Connect.getSetcommands().hmset(key + "-" + "UUID_TO_DISCORDID", AuthStorage.UUID_TO_DISCORDID);
            }
        }

    }

    public static void RemoveUploadedStorage(String key) {
        if(key == null) {
            Connect.getSetcommands().del("FactionInfo");
            Connect.getSetcommands().del("FactionMember");
            Connect.getSetcommands().del("FactionNameToFactionName");
            Connect.getSetcommands().del("FactionInfoList");
            Connect.getSetcommands().del("FactionNameToFactionUUID");
            Connect.getSetcommands().del("FactionOutPost");
            Connect.getSetcommands().del("FactionOutPostList");
            Connect.getSetcommands().del("FactionRank");
            Connect.getSetcommands().del("FactionToLand");
            Connect.getSetcommands().del("FactionToOutPost");
            Connect.getSetcommands().del("FactionUUIDToFactionName");
            Connect.getSetcommands().del("FactionWarpLocations");
            Connect.getSetcommands().del("OutPostToFaction");
            Connect.getSetcommands().del("PlayerFaction");
            Connect.getSetcommands().del("namename");
            Connect.getSetcommands().del("nameuuid");
            Connect.getSetcommands().del("uuidname");
        } else {
            Connect.getSetcommands().del(key + "-" + "FactionInfo");
            Connect.getSetcommands().del(key + "-" + "FactionMember");
            Connect.getSetcommands().del(key + "-" + "FactionNameToFactionName");
            Connect.getSetcommands().del(key + "-" + "FactionInfoList");
            Connect.getSetcommands().del(key + "-" + "FactionNameToFactionUUID");
            Connect.getSetcommands().del(key + "-" + "FactionOutPost");
            Connect.getSetcommands().del(key + "-" + "FactionOutPostList");
            Connect.getSetcommands().del(key + "-" + "FactionRank");
            Connect.getSetcommands().del(key + "-" + "FactionToLand");
            Connect.getSetcommands().del(key + "-" + "FactionToOutPost");
            Connect.getSetcommands().del(key + "-" + "FactionUUIDToFactionName");
            Connect.getSetcommands().del(key + "-" + "FactionWarpLocations");
            Connect.getSetcommands().del(key + "-" + "OutPostToFaction");
            Connect.getSetcommands().del(key + "-" + "PlayerFaction");
            Connect.getSetcommands().del(key + "-" + "namename");
            Connect.getSetcommands().del(key + "-" + "nameuuid");
            Connect.getSetcommands().del(key + "-" + "uuidname");
        }
    }

    public static void ReloadStorageFromRemoteServer(String key) {
        try {

        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }
        try {
            FactionStorage.FactionInfo = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionInfo");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }
        //FactionStorage.AsyncLandToFaction = (ConcurrentHashMap<String, String>) setcommands.hmget("FactionInfo");
        HashMap<String, String> tempmap = new HashMap<>();
        try {
            tempmap = (HashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionMember");
            FactionStorage.FactionMember = ConcurrentMapListConvert(tempmap);
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }
        //
        try {
            FactionStorage.FactionNameToFactionName = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionNameToFactionName");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }
        //FactionStorage.AsyncOutPostToFaction;
        try {
            tempmap = (HashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionInfoList");
            FactionStorage.FactionInfoList = ConcurrentMapListConvert(tempmap);
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            FactionStorage.FactionNameToFactionUUID = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionNameToFactionUUID");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            FactionStorage.FactionOutPost = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionOutPost");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            tempmap = (HashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionOutPostList");
            FactionStorage.FactionOutPostList = ConcurrentMapListConvert(tempmap);
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            FactionStorage.FactionRank = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionRank");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            tempmap = (HashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionToLand");
            FactionStorage.FactionToLand = MapListConvert(tempmap);
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            tempmap = (HashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionToOutPost");
            FactionStorage.FactionToOutPost = MapListConvert(tempmap);
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            FactionStorage.FactionUUIDToFactionName = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionUUIDToFactionName");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            FactionStorage.FactionWarpLocations = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionWarpLocations");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            FactionStorage.OutPostToFaction = (HashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "OutPostToFaction");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            FactionStorage.PlayerFaction = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "PlayerFaction");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            UserInfoStorage.namename = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "namename");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            UserInfoStorage.uuidname = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "uuidname");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }


        try {
            UserInfoStorage.nameuuid = (ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "nameuuid");
        } catch (Exception e) {
            SystemUtils.logger(e.getMessage());
        }

    }


    public static HashMap<String, String> ListMapConvert(HashMap<String, ArrayList<String>> map) {
        HashMap finalMap = new HashMap<String, String>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalMap.put(key, SystemUtils.list2string(map.get(key)));
            }
        }
        return finalMap;
    }

    public static HashMap<String, String> ConcurrentListMapConvert(ConcurrentHashMap<String, ArrayList<String>> map) {
        synchronized (map) {
            HashMap finalMap = new HashMap<String, String>();
            if(!map.isEmpty()) {
                for(String key : map.keySet()) {
                    finalMap.put(key, SystemUtils.list2string(map.get(key)));
                }
            }
            return finalMap;
        }
    }

    public static HashMap<String, ArrayList<String>> MapListConvert(HashMap<String, String> map) {
        HashMap<String, ArrayList<String>> finalmap = new HashMap<>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalmap.put(key, SystemUtils.string2list(map.get(key)));
            }
        }
        return finalmap;
    }

    public static ConcurrentHashMap<String, ArrayList<String>> ConcurrentMapListConvert(HashMap<String, String> map) {
        ConcurrentHashMap<String, ArrayList<String>> finalmap = new ConcurrentHashMap<>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalmap.put(key, SystemUtils.string2list(map.get(key)));
            }
        }
        return finalmap;
    }

    public static ConcurrentHashMap<String, String> FixMap2(HashMap<String, String> map) {
        ConcurrentHashMap<String, String> finalmap = new ConcurrentHashMap<>();
        for(String key : map.keySet()) {
            finalmap.put(key, map.get(key));

        }
        return finalmap;
    }

    public static HashMap<String, String> FixMap1(ConcurrentHashMap<String, String> map) {
        HashMap<String, String> finalmap = new HashMap<>();
        for(String key : map.keySet()) {
            finalmap.put(key, map.get(key));

        }
        return finalmap;
    }
}
