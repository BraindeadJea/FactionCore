package com.itndev.FactionCore.Database.Redis.BungeeAPI;


import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.UserInfoStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class BungeeStorage {

    public static ConcurrentHashMap<String, ArrayList<String>> SERVER_TO_UUIDLIST = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> UUID_TO_CONNECTEDSERVER = new ConcurrentHashMap<>();

    private static void removePlayer(String UUID) {
        if(UUID_TO_CONNECTEDSERVER.containsKey(UUID)) {
            String ConnectedServer = UUID_TO_CONNECTEDSERVER.get(UUID);
            ArrayList<String> UUID_LIST;
            if(SERVER_TO_UUIDLIST.containsKey(ConnectedServer)) {
                 UUID_LIST = SERVER_TO_UUIDLIST.get(ConnectedServer);
            } else {
                 UUID_LIST = new ArrayList<>();
            }
            UUID_LIST.remove(UUID);
            SERVER_TO_UUIDLIST.put(ConnectedServer, UUID_LIST);
            UUID_TO_CONNECTEDSERVER.remove(UUID);
            ConnectedServer = null;
            UUID_LIST = null;
        }
    }

    private static void addPlayer(String UUID, String ConnectedServer) {
        UUID_TO_CONNECTEDSERVER.put(UUID, ConnectedServer);
        ArrayList<String> UUID_LIST;
        if(SERVER_TO_UUIDLIST.containsKey(ConnectedServer)) {
            UUID_LIST = SERVER_TO_UUIDLIST.get(ConnectedServer);
        } else {
            UUID_LIST = new ArrayList<>();
        }
        UUID_LIST.add(UUID);
        SERVER_TO_UUIDLIST.put(ConnectedServer, UUID_LIST);
        UUID_LIST = null;
    }

    public static void READ_Bungee_command(String command) {
        if(command.contains(":=:")) {
            String[] cmd_args = command.split(":=:");
            if(cmd_args[0].equalsIgnoreCase("PROXY-JOIN")) {
                String[] TEMP = cmd_args[1].replace("{", "").replace("}", "").split(",");
                String UUID = TEMP[0];
                String NAME = TEMP[1];
                UPDATE_USERINFO(UUID, NAME);
                UUID = null;
                NAME = null;
                TEMP = null;
                //FactionUtils.FactionNotify(UUID, "TeamChat", "&r&7" + FactionUtils.getPlayerLangRank(UUID) + " &c" + NAME + "&f 님이 서버에 접속했습니다", "true");
            } else if(cmd_args[0].equalsIgnoreCase("PROXY-LEAVE")) {
                String[] TEMP = cmd_args[1].replace("{", "").replace("}", "").split(",");
                String UUID = TEMP[0];
                String NAME = TEMP[1];
                //FactionUtils.FactionNotify(UUID, "TeamChat", "&r&7" + FactionUtils.getPlayerLangRank(UUID) + " &c" + NAME + "&f 님이 서버에서 나갔습니다", "true");
                removePlayer(UUID);
                UUID = null;
                NAME = null;
                TEMP = null;
            } else if(cmd_args[0].equalsIgnoreCase("PROXY-PLAYERINFO")) {
                String[] TEMP = cmd_args[1].replace("{", "").replace("}", "").split(",");
                String UUID = TEMP[0];
                String NAME = TEMP[1];
                String ServerName = TEMP[2];
                addPlayer(UUID, ServerName);
                UPDATE_USERINFO(UUID, NAME);
                UUID = null;
                NAME = null;
                TEMP = null;
                ServerName = null;
            } else if(cmd_args[0].equalsIgnoreCase("PROXY-CONNECTSERVER")) {
                String[] TEMP = cmd_args[1].replace("{", "").replace("}", "").split(",");
                String UUID = TEMP[0];
                String NAME = TEMP[1];
                String ServerName = TEMP[2];
                addPlayer(UUID, ServerName);
                UUID = null;
                NAME = null;
                TEMP = null;
                ServerName = null;
            } else if(cmd_args[0].equalsIgnoreCase("PROXY-REFRESH")) {
                UUID_TO_CONNECTEDSERVER.clear();
                SERVER_TO_UUIDLIST.clear();
            }
        }
    }

    public static void UPDATE_USERINFO(String UUID, String Name) {
        List<String> bulkcmd = new ArrayList<>();
        if(UserInfoStorage.uuidname.containsKey(UUID)) {
            if(!UserInfoStorage.uuidname.get(UUID).equals(Name)) {
                String OriginalName = UserInfoStorage.uuidname.get(UUID);
                bulkcmd.add("update:=:nameuuid:=:remove:=:" + OriginalName.toLowerCase(Locale.ROOT) + ":=:add:=:" + UUID);
                bulkcmd.add("update:=:namename:=:remove:=:" + OriginalName.toLowerCase(Locale.ROOT) + ":=:add:=:" + Name);
                //
                bulkcmd.add("update:=:uuidname:=:add:=:" + UUID + ":=:add:=:" + Name.toLowerCase(Locale.ROOT));
                bulkcmd.add("update:=:nameuuid:=:add:=:" + Name.toLowerCase(Locale.ROOT) + ":=:add:=:" + UUID);
                bulkcmd.add("update:=:namename:=:add:=:" + Name.toLowerCase(Locale.ROOT) + ":=:add:=:" + Name);
                OriginalName = null;
            }
        } else {
            bulkcmd.add("update:=:uuidname:=:add:=:" + UUID + ":=:add:=:" + Name.toLowerCase(Locale.ROOT));
            bulkcmd.add("update:=:nameuuid:=:add:=:" + Name.toLowerCase(Locale.ROOT) + ":=:add:=:" + UUID);
            bulkcmd.add("update:=:namename:=:add:=:" + Name.toLowerCase(Locale.ROOT) + ":=:add:=:" + Name);
        }
        Storage.AddBulkCommandToQueue(bulkcmd);
        bulkcmd = null;
    }
}
