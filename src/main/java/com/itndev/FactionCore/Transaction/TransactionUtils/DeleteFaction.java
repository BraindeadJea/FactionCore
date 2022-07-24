package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.Transaction.TransactionUtils.FactionCD.DeleteFactionUtils;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

public class DeleteFaction {
    public static void DeleteFactionQueue(String UUID, String[] args) {
        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {
            if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 국가를 해체할수 없습니다");
                return;
            }
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            FactionTimeOut.DeleteFactionTEMP(FactionUUID ,UUID);
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&7/국가 해체수락 &r&f으로 국가 해체를 수락합니다\n" +
                    "해당 명령어는 &r&c20초&r&f후 자동 만료됩니다.");
            FactionUUID = null;
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
        }
    }

    @Deprecated
    public static void DeleteFaction(String UUID, String[] args) {
        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {
            if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 국가를 해체할수 없습니다");
                return;
            }
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            if(FactionTimeOut.Timeout1info.containsKey(FactionUUID)) {
                String temp394328UUID = FactionTimeOut.Timeout1info.get(FactionUUID);
                FactionTimeOut.Timeout1.remove(FactionUUID + "%" + temp394328UUID);
                DeleteFactionUtils.DeteleFaction(UUID, FactionUUID);
                temp394328UUID = null;
            } else {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가를 해체하시려면 먼저 &r&7/국가 해체 &r&f를 먼저 해주세요");
            }
            FactionUUID = null;
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
        }
    }
}
