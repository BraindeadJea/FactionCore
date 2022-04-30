package com.itndev.FBTM.Utils.Factions;

import com.itndev.FBTM.Database.MySQL.SQL;
import com.itndev.FBTM.Database.Redis.Obj.Storage;
import com.itndev.FBTM.Server;
import com.itndev.FBTM.Utils.Cache.CachedStorage;

import java.util.concurrent.CompletableFuture;

public class CacheUtils {

    @Deprecated
    public static void UpdateCachedDTR(String FactionUUID, Double DTR) {
        Storage.AddCommandToQueue("update:=:CachedDTR:=:add:=:" + FactionUUID + ":=:add:=:" + String.valueOf(DTR) + ":=:" + Server.getServerName());
        CachedStorage.CachedDTR.put(FactionUUID, DTR);
    }

    @Deprecated
    public static void UpdateCachedBank(String FactionUUID, Double Bank) {
        Storage.AddCommandToQueue("update:=:CachedBank:=:add:=:" + FactionUUID + ":=:add:=:" + String.valueOf(Bank) + ":=:" + Server.getServerName());
        CachedStorage.CachedBank.put(FactionUUID, Bank);
    }

    public static void UpdateLocalCachedBank(String FactionUUID, Double Bank) {
        //JedisTempStorage.AddCommandToQueue("update:=:CachedBank:=:add:=:" + FactionUUID + ":=:add:=:" + String.valueOf(Bank) + ":=:" + Main.ServerName);
        CachedStorage.CachedBank.put(FactionUUID, Bank);
    }

    @Deprecated
    public static void removeCachedDTR(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:CachedDTR:=:remove:=:" + FactionUUID + ":=:add:=:" + String.valueOf(0) + ":=:" + Server.getServerName());
        CachedStorage.CachedDTR.remove(FactionUUID);
    }

    @Deprecated
    public static void removeCachedBank(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:CachedBank:=:remove:=:" + FactionUUID + ":=:add:=:" + String.valueOf(0) + ":=:" + Server.getServerName());
        CachedStorage.CachedBank.remove(FactionUUID);
    }

    @Deprecated
    public static CompletableFuture<Double> getCachedDTR(String FactionUUID) {
        CompletableFuture<Double> FinalDTR = new CompletableFuture<>();
        if(CachedStorage.CachedDTR.containsKey(FactionUUID)) {
            FinalDTR.complete(CachedStorage.CachedDTR.get(FactionUUID));
        } else {
            FinalDTR = SQL.getDatabase().GetFactionDTR(FactionUUID);
        }
        return FinalDTR;
    }

    @Deprecated
    public static CompletableFuture<Double> getCachedBank(String FactionUUID) {
        CompletableFuture<Double> FinalBank = new CompletableFuture<>();
        if(CachedStorage.CachedBank.containsKey(FactionUUID)) {
            FinalBank.complete(CachedStorage.CachedBank.get(FactionUUID));
        } else {
            FinalBank = SQL.getDatabase().GetFactionBank(FactionUUID);
        }
        return FinalBank;
    }
}
