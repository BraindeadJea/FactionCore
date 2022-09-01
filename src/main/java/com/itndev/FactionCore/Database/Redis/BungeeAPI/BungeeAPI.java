package com.itndev.FactionCore.Database.Redis.BungeeAPI;


import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.ArrayList;

public class BungeeAPI {

    public static Boolean isOnline(String UUID) {
        return BungeeStorage.UUID_TO_CONNECTEDSERVER.containsKey(UUID);
    }

    public static String colorIFONLINE(String UUID) {
        if(isOnline(UUID)) {
            return "&a";
        } else {
            return "&7";
        }
    }

    public static Integer getOnlineAmount() {
        return BungeeStorage.UUID_TO_CONNECTEDSERVER.size();
    }


    private static Boolean isfirst = true;
    public static ArrayList<String> getOnlineNames() {
        ArrayList<String> Names = new ArrayList<>();
        BungeeStorage.UUID_TO_CONNECTEDSERVER.keySet().forEach(key -> Names.add(UserInfoUtils.getPlayerUUIDOriginName(key)));
        return Names;
    }
}
