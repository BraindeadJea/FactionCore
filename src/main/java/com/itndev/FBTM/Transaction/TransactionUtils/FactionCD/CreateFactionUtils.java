package com.itndev.FBTM.Transaction.TransactionUtils.FactionCD;

import com.itndev.FBTM.Database.MySQL.SQL;
import com.itndev.FBTM.Database.Redis.Obj.Storage;
import com.itndev.FBTM.Utils.Factions.FactionUtils;
import com.itndev.FBTM.Utils.Factions.SystemUtils;

import java.util.Locale;

public class CreateFactionUtils {

    @Deprecated
    public static void CreateFaction(String UUID, String newFactionUUID, String FactionName) {
        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&a&o&l[ &r&f국가 &a&o&l] &r&f새 국가 &c" + FactionName + " 을(를) 새웠습니다");
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "all" + ":=:" + "&a&o&l[ &r&f국가 &a&o&l] &r&f" + "&r&f새 국가 &c" + FactionName + " (이)가 새워졌습니다" + ":=:" + "true");
        FactionUtils.CreateFaction(UUID, newFactionUUID, FactionName);
        //Main.database.AddNewFactionName(FactionName, newFactionUUID);
        SQL.getDatabase().CreateNewDTR(newFactionUUID, FactionName.toLowerCase(Locale.ROOT));
        SQL.getDatabase().CreateNewBank(newFactionUUID, FactionName.toLowerCase(Locale.ROOT));
    }
}
