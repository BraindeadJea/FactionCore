package com.itndev.FactionCore.Utils.Factions;

import com.itndev.FactionCore.Database.MySQL.SQL;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.Cache.CachedStorage;
import com.itndev.FactionCore.Utils.CommonUtils;

import java.util.concurrent.CompletableFuture;

public class CacheUtils {

    public static void UpdateCachedDTR(String FactionUUID, Double DTR) {
        Storage.AddCommandToQueue("update:=:CachedDTR:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(String.valueOf(DTR)) + ":=:" + Server.getServerName());
        CachedStorage.CachedDTR.put(FactionUUID, DTR);
    }

    public static void UpdateCachedBank(String FactionUUID, Double Bank) {
        Storage.AddCommandToQueue("update:=:CachedBank:=:add:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(String.valueOf(Bank)) + ":=:" + Server.getServerName());
        CachedStorage.CachedBank.put(FactionUUID, Bank);
    }

    public static void UpdateLocalCachedBank(String FactionUUID, Double Bank) {
        //JedisTempStorage.AddCommandToQueue("update:=:CachedBank:=:add:=:" + FactionUUID + ":=:add:=:" + String.valueOf(Bank) + ":=:" + Main.ServerName);
        CachedStorage.CachedBank.put(FactionUUID, Bank);
    }

    public static void removeCachedDTR(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:CachedDTR:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(String.valueOf(0)) + ":=:" + Server.getServerName());
        CachedStorage.CachedDTR.remove(FactionUUID);
    }

    public static void removeCachedBank(String FactionUUID) {
        Storage.AddCommandToQueue("update:=:CachedBank:=:remove:=:" + CommonUtils.String2Byte(FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(String.valueOf(0)) + ":=:" + Server.getServerName());
        CachedStorage.CachedBank.remove(FactionUUID);
    }

    public static CompletableFuture<Double> getCachedDTR(String FactionUUID) {
        CompletableFuture<Double> FinalDTR = new CompletableFuture<>();
        if(CachedStorage.CachedDTR.containsKey(FactionUUID)) {
            FinalDTR.complete(CachedStorage.CachedDTR.get(FactionUUID));
        } else {
            FinalDTR = SQL.getDatabase().GetFactionDTR(FactionUUID);
        }
        return FinalDTR;
    }

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
