package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.util.Locale;

public class FactionWar {

    public static void FactionWarCMD(String UUID, String[] args, String Additional) {
        if(!FactionUtils.isInFaction(UUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        if(args.length < 3) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 전쟁 &f(&7신청&8, &7수락&f) &7(국가이름))");
            return;
        }
        if(!FactionUtils.HigherThenorSameRank(UUID, Config.Leader)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가의 리더만 해당 명령어를 사용할수 있습니다");
            return;
        }
        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
        if(!FactionUtils.isExistingFaction(args[2].toLowerCase(Locale.ROOT))) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 나라 &c" + args[2] + " &r(은)는 존재하지 않습니다");
            return;
        }
        if(args[1].equalsIgnoreCase("신청")) {
            if(FactionUtils.isInWar(FactionUUID)) {
                String OPPFACNAME = FactionUtils.getCapFactionNameFromUUID(FactionUtils.getOPPWar(FactionUUID));
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f이미 &c" + OPPFACNAME + " &r국가와 전쟁 중입니다");
                return;
            }
            if(!FactionUtils.hasMainBeacon(FactionUUID)) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f신호기가 없는 중립국은 전쟁을 할수 없습니다");
                return;
            }
            if(!FactionUtils.hasMainBeacon(FactionUtils.getFactionUUID(args[2].toLowerCase(Locale.ROOT)))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&상대 국가는 신호기가 없는 중립국이므로 전쟁을 할수 없습니다");
                return;
            }
            FactionTimeOut.FactionWarRequest(UUID, FactionUUID, FactionUtils.getFactionUUID(args[2].toLowerCase(Locale.ROOT)));
        } else if(args[1].equalsIgnoreCase("수락")) {
            if(FactionUtils.isInWar(FactionUUID)) {
                String OPPFACNAME = FactionUtils.getCapFactionNameFromUUID(FactionUtils.getOPPWar(FactionUUID));
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f이미 &c" + OPPFACNAME + " &r국가와 전쟁 중입니다");
                return;
            }
            if(!FactionUtils.hasMainBeacon(FactionUUID)) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f신호기가 없는 중립국은 전쟁을 할수 없습니다");
                return;
            }
            if(!FactionUtils.hasMainBeacon(FactionUtils.getFactionUUID(args[2].toLowerCase(Locale.ROOT)))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f상대 국가는 신호기가 없는 중립국이므로 전쟁을 할수 없습니다");
                return;
            }
            FactionTimeOut.FactionWarAccept(UUID, FactionUUID, FactionUtils.getFactionUUID(args[2].toLowerCase(Locale.ROOT)));
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 전쟁 &f(&7신청&8, &7수락&f) &7(국가이름))");
        }
    }
    public static void NomoreinWar(String FactionUUID) {
        Storage.AddCommandToQueue("notify:=:" + FactionUtils.getFactionLeader(FactionUUID) + ":=:" + "SIBAL" + ":=:" + "&r&f전쟁이 끝났습니다" + ":=:" + "true");
        FactionUtils.removeFromWar(FactionUUID);
    }
}
