package com.itndev.FactionCore.Utils.Factions;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.UserInfoStorage;

import java.util.Locale;

public class UserInfoUtils {

    public static String getPlayerUUIDOriginName(String UUID) {
        return getPlayerOrginName(getPlayerName(UUID));
    }

    public static void setPlayerName(String UUID, String newName) {
        Storage.AddCommandToQueue("update:=:uuidname:=:add:=:" + UUID + ":=:add:=:" + newName.toLowerCase(Locale.ROOT));
    }

    public static void setPlayerUUID(String newName, String UUID) {

        String oldname = UserInfoStorage.uuidname.get(UUID);
        if(!newName.equalsIgnoreCase(oldname)) {
            Storage.AddCommandToQueue("update:=:nameuuid:=:add:=:" + newName.toLowerCase(Locale.ROOT) + ":=:add:=:" + UUID);
        }
        oldname = null;
    }

    public static void setPlayerOrginName(String newName, String UUID) {
        if(UserInfoStorage.uuidname.containsKey(UUID)) {
            String oldname = UserInfoStorage.uuidname.get(UUID);
            if (!newName.equalsIgnoreCase(oldname)) {
                Storage.AddCommandToQueue("update:=:namename:=:remove:=:" + oldname.toLowerCase(Locale.ROOT) + ":=:add:=:" + newName);
                Storage.AddCommandToQueue("update:=:namename:=:add:=:" + newName.toLowerCase(Locale.ROOT) + ":=:add:=:" + newName);
            } else {
                Storage.AddCommandToQueue("update:=:namename:=:add:=:" + newName.toLowerCase(Locale.ROOT) + ":=:add:=:" + newName);
            }
            oldname = null;
        } else {
            Storage.AddCommandToQueue("update:=:namename:=:add:=:" + newName.toLowerCase(Locale.ROOT) + ":=:add:=:" + newName);
        }
    }

    public static Boolean hasJoined(String name) {
        return (UserInfoStorage.nameuuid.containsKey(name.toLowerCase(Locale.ROOT)));
    }

    public static Boolean hasJoinedUUID(String UUID) {
        return (UserInfoStorage.uuidname.containsKey(UUID));
    }

    public static String getPlayerName(String UUID) {
        String finalname = null;
        if(UserInfoStorage.uuidname.containsKey(UUID)) {
            finalname = UserInfoStorage.uuidname.get(UUID);
        }
        return finalname;
    }
    public static String getPlayerUUID(String name) {
        String finalUUID = null;
        if(UserInfoStorage.nameuuid.containsKey(name)) {
            finalUUID = UserInfoStorage.nameuuid.get(name);
        }
        return finalUUID;
    }
    public static String getPlayerOrginName(String name) {
        String finalname = null;
        if(UserInfoStorage.namename.containsKey(name)) {
            finalname = UserInfoStorage.namename.get(name);
        }
        return finalname;
    }






}
