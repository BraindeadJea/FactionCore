package com.itndev.FactionCore.Database.Redis;

import com.itndev.FactionCore.Discord.AuthStorage;
import com.itndev.FactionCore.Factions.FactionStorage;
import com.itndev.FactionCore.Factions.UserInfoStorage;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.api.sync.RedisStreamCommands;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Connect {

    private static RedisClient client = null;
    private static StatefulRedisConnection<String, String> connection = null;
    private static RedisStreamCommands<String, String> commands = null;
    private static RedisCommands<String, String> setcommands = null;
    private static String LastID_INPUT = "0-0";
    private static String LastID_INNER = "0-0";
    private static String LastID_BUNGEE = "0-0";

    private static String redis_address = "221.167.222.87";
    private static Integer redis_port = 6614;
    private static String redis_password = "54rg46ujhy7ju57wujndt35ytgryeutwefer4rt34rd34rsfg6hdf43truhgfwgr348yfgcs";
    private static Boolean sslEnabled = true;

    private static Boolean isClosed = false;

    public static void setRedis_address(String address) {
        redis_address = address;
    }

    public static void setRedis_port(Integer port) {
        redis_port = port;
    }

    public static void setRedis_password(String password) {
        redis_password = password;
    }

    public static void setSslEnabled(Boolean enabled) {
        sslEnabled = enabled;
    }

    public static void setLastID_INPUT(String new_LastID_INPUT) {
        LastID_INPUT = new_LastID_INPUT;
    }
    public static void setLastID_INNER(String new_LastID_INNER) {
        LastID_INNER = new_LastID_INNER;
    }
    // GET METHOD
    public static String get_LastID_INPUT() {
        return LastID_INPUT;
    }

    public static String get_LastID_INNER() {
        return LastID_INNER;
    }

    public static String get_LastID_BUNGEE() {
        return LastID_BUNGEE;
    }

    public static void set_LastID_BUNGEE(String data) {
        LastID_BUNGEE = data;
    }

    public static RedisCommands<String, String> getSetcommands() {
        return setcommands;
    }

    public static Boolean get_isClosed() {
        return isClosed;
    }

    // CONNECT TO REDIS
    @Deprecated
    public static void RedisConnect() {
        RedisURI redisURI = RedisURI.Builder.redis(redis_address, redis_port).withPassword(redis_password).build();
        client = RedisClient.create(redisURI);
        connection = client.connect();
        commands = connection.sync();
        setcommands = connection.sync();
    }

    public static void ReloadStorageFromRemoteServer(String key) {
        if(key == null) {
            FactionStorage.FactionInfo = FixMap2(setcommands.hgetall("FactionInfo"));
            //FactionStorage.AsyncLandToFaction = (ConcurrentHashMap<String, String>) setcommands.hmget("FactionInfo");
            HashMap<String, String> tempmap = new HashMap<>();
            tempmap = FixMap1(setcommands.hgetall("FactionMember"));
            FactionStorage.FactionMember = ConcurrentMapListConvert(tempmap);
            //
            FactionStorage.FactionNameToFactionName = FixMap2(setcommands.hgetall("FactionNameToFactionName"));
            //FactionStorage.AsyncOutPostToFaction;
            tempmap = FixMap1(setcommands.hgetall("FactionInfoList"));
            FactionStorage.FactionInfoList = ConcurrentMapListConvert(tempmap);
            FactionStorage.FactionNameToFactionUUID = FixMap2(setcommands.hgetall("FactionNameToFactionUUID"));

            FactionStorage.FactionOutPost = FixMap2(setcommands.hgetall("FactionOutPost"));

            tempmap = FixMap1(setcommands.hgetall("FactionOutPostList"));
            FactionStorage.FactionOutPostList = ConcurrentMapListConvert(tempmap);

            FactionStorage.FactionRank = FixMap2(setcommands.hgetall("FactionRank"));

            tempmap = FixMap1(setcommands.hgetall("FactionToLand"));
            FactionStorage.FactionToLand = MapListConvert(tempmap);

            tempmap = FixMap1(setcommands.hgetall("FactionToOutPost"));
            FactionStorage.FactionToOutPost = MapListConvert(tempmap);

            FactionStorage.FactionUUIDToFactionName = FixMap2(setcommands.hgetall("FactionUUIDToFactionName"));
            FactionStorage.FactionWarpLocations = FixMap2(setcommands.hgetall("FactionWarpLocations"));
            FactionStorage.OutPostToFaction = FixMap1(setcommands.hgetall("OutPostToFaction"));
            FactionStorage.PlayerFaction = FixMap2(setcommands.hgetall("PlayerFaction"));

            UserInfoStorage.namename = FixMap2(setcommands.hgetall("namename"));
            UserInfoStorage.uuidname = FixMap2(setcommands.hgetall("uuidname"));
            UserInfoStorage.nameuuid = FixMap2(setcommands.hgetall("nameuuid"));

            AuthStorage.UUID_TO_DISCORDID = FixMap1(setcommands.hgetall("UUID_TO_DISCORDID"));
            AuthStorage.DISCORDID_TO_UUID = FixMap1(setcommands.hgetall("DISCORDID_TO_UUID"));
        } else {

            String finalkey = key + "-";

            FactionStorage.FactionInfo = FixMap2(setcommands.hgetall(finalkey + "FactionInfo"));
            //FactionStorage.AsyncLandToFaction = (ConcurrentHashMap<String, String>) setcommands.hmget("FactionInfo");
            HashMap<String, String> tempmap = new HashMap<>();
            tempmap = FixMap1(setcommands.hgetall(finalkey + "FactionMember"));
            FactionStorage.FactionMember = ConcurrentMapListConvert(tempmap);
            //
            FactionStorage.FactionNameToFactionName = FixMap2(setcommands.hgetall(finalkey + "FactionNameToFactionName"));
            //FactionStorage.AsyncOutPostToFaction;
            tempmap = FixMap1(setcommands.hgetall(finalkey + "FactionInfoList"));
            FactionStorage.FactionInfoList = ConcurrentMapListConvert(tempmap);
            FactionStorage.FactionNameToFactionUUID = FixMap2(setcommands.hgetall(finalkey + "FactionNameToFactionUUID"));

            FactionStorage.FactionOutPost = FixMap2(setcommands.hgetall(finalkey + "FactionOutPost"));

            tempmap = FixMap1(setcommands.hgetall(finalkey + "FactionOutPostList"));
            FactionStorage.FactionOutPostList = ConcurrentMapListConvert(tempmap);

            FactionStorage.FactionRank = FixMap2(setcommands.hgetall(finalkey + "FactionRank"));

            tempmap = FixMap1(setcommands.hgetall(finalkey + "FactionToLand"));
            FactionStorage.FactionToLand = MapListConvert(tempmap);

            tempmap = FixMap1(setcommands.hgetall(finalkey + "FactionToOutPost"));
            FactionStorage.FactionToOutPost = MapListConvert(tempmap);

            FactionStorage.FactionUUIDToFactionName = FixMap2(setcommands.hgetall(finalkey + "FactionUUIDToFactionName"));
            FactionStorage.FactionWarpLocations = FixMap2(setcommands.hgetall(finalkey + "FactionWarpLocations"));
            FactionStorage.OutPostToFaction = FixMap1(setcommands.hgetall(finalkey + "OutPostToFaction"));
            FactionStorage.PlayerFaction = FixMap2(setcommands.hgetall(finalkey + "PlayerFaction"));

            UserInfoStorage.namename = FixMap2(setcommands.hgetall(finalkey + "namename"));
            UserInfoStorage.uuidname = FixMap2(setcommands.hgetall(finalkey + "uuidname"));
            UserInfoStorage.nameuuid = FixMap2(setcommands.hgetall(finalkey + "nameuuid"));

            AuthStorage.UUID_TO_DISCORDID = FixMap1(setcommands.hgetall(finalkey + "UUID_TO_DISCORDID"));
            AuthStorage.DISCORDID_TO_UUID = FixMap1(setcommands.hgetall(finalkey + "DISCORDID_TO_UUID"));
        }

    }

    public static ConcurrentHashMap<String, String> FixMap2(Map<String, String> map) {
        ConcurrentHashMap<String, String> finalmap = new ConcurrentHashMap<>();
        for(String key : map.keySet()) {
            finalmap.put(key, map.get(key));

        }
        return finalmap;
    }

    public static HashMap<String, String> FixMap1(Map<String, String> map) {
        HashMap<String, String> finalmap = new HashMap<>();
        for(String key : map.keySet()) {
            finalmap.put(key, map.get(key));

        }
        return finalmap;
    }

    public static HashMap<String, ArrayList<String>> FixMap3(Map<String, ArrayList<String>> map) {
        HashMap<String, ArrayList<String>> finalmap = new HashMap<>();
        for(String key : map.keySet()) {
            finalmap.put(key, map.get(key));

        }
        return finalmap;
    }

    public static ConcurrentHashMap<String, ArrayList<String>> FixMap4(Map<String, ArrayList<String>> map) {
        ConcurrentHashMap<String, ArrayList<String>> finalmap = new ConcurrentHashMap<>();
        for(String key : map.keySet()) {
            finalmap.put(key, map.get(key));

        }
        return finalmap;
    }

    public static HashMap<String, ArrayList<String>> MapListConvert(HashMap<String, String> map) {
        HashMap finalMap = new HashMap<String, ArrayList<String>>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                finalMap.put(key, SystemUtils.string2list(map.get(key)));
            }
        }
        return finalMap;
    }

    public static ConcurrentHashMap<String, ArrayList<String>> ConcurrentMapListConvert(HashMap<String, String> map) {
        ConcurrentHashMap finalMap = new ConcurrentHashMap<String, ArrayList<String>>();
        if(!map.isEmpty()) {
            for(String key : map.keySet()) {
                if(key != null && map.containsKey(key) && map.get(key) != null) {
                    finalMap.put(key, SystemUtils.string2list(map.get(key)));
                }
            }
        }
        return finalMap;
    }

    public static void RedisDisConnect() {
        connection.close();
        isClosed = true;
    }

    public static StatefulRedisConnection<String, String> getRedisConnection() {
        if (connection == null || !connection.isOpen()) {
            connection = client.connect();
        }
        return connection;
    }

    public static RedisStreamCommands<String, String> getRedisCommands() {
        if(commands == null) {
            commands = getRedisConnection().sync();
        }
        return commands;
    }
}
