package com.itndev.FactionCore.Transaction.TransactionUtils.FactionCD;

import com.itndev.FactionCore.Database.MySQL.SQL;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

public class DeleteFactionUtils {
    @Deprecated
    public static void DeteleFaction(String UUID, String FactionUUID) {
        String FactionName = FactionUtils.getFactionName(FactionUUID);
        String OriginFactionName = FactionUtils.getCappedFactionName(FactionName);
        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 &c" + OriginFactionName + " 을(를) 해체했습니다");
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "SIBAL" + ":=:" + "&r&f당신의 국가가 &c몰락&f했습니다" + ":=:" + "true");
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "all" + ":=:" + "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 &c" + OriginFactionName + " &r(이)가 &c몰락&r&f했습니다" + ":=:" + "true");
        FactionUtils.DeleteFaction(FactionUUID);
        SQL.getDatabase().DeleteFactionName(FactionUUID);
        SQL.getDatabase().DeleteFactionBank(FactionUUID);
        SQL.getDatabase().DeleteFactionDTR(FactionUUID);
    }

    @Deprecated
    public static void DESTORYFaction(String UUID, String FactionUUID) {
        String FactionName = FactionUtils.getFactionName(FactionUUID);
        String OriginFactionName = FactionUtils.getCappedFactionName(FactionName);
        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 &c" + OriginFactionName + " (이)가 멸망했습니다");
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "SIBAL" + ":=:" + "&r&f당신의 국가가 &c멸망&f했습니다" + ":=:" + "true");
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "all" + ":=:" + "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 &c" + OriginFactionName + " &r(이)가 &c멸망&r&f했습니다" + ":=:" + "true");
        FactionUtils.DeleteFaction(FactionUUID);
        SQL.getDatabase().DeleteFactionName(FactionUUID);
        SQL.getDatabase().DeleteFactionBank(FactionUUID);
        SQL.getDatabase().DeleteFactionDTR(FactionUUID);
    }


}
