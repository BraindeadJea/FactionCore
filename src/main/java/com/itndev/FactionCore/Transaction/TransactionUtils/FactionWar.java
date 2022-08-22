package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.EloSystem.Package.EloResult;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
                OPPFACNAME = null;
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
                OPPFACNAME = null;
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

    public static void WarEloUpdate(String loserFactionName, String winnerFactionName, String loserFaction, String winnerFaction,
                                    ArrayList<String> loserFactionMember, ArrayList<String> winnerFactionMember,
                                    List<EloResult> eloResultList) {
        //====================//
        ArrayList<String> IncludedList = new ArrayList<>();
        IncludedList.addAll(loserFactionMember);
        IncludedList.addAll(winnerFactionMember);
        String message;
        message = "&r&f&m-----------------&r&a&o&l[ &r&f전쟁 결과 &r&a&o&l]&r&f&m-----------------\n" +
                "&r&7&l>&r&a  승전국 &7:&r " + winnerFactionName + "&r\n" +
                "&r&7&l>&r&f  " + ListEloUpdate(winnerFaction, eloResultList) + "&r\n" +
                "&r\n" +
                "&r&7&l>&r&c  패전국 &7:&r " + loserFactionName + "&r\n" +
                "&r&7&l>&r&f  " + ListEloUpdate(loserFaction, eloResultList) + "&r\n" +
                "&r&f&m-----------------&r&a&o&l[ &r&f전쟁 결과 &r&a&o&l]&r&f&m-----------------\n";
        //====================//
        IncludedList.forEach(UUID -> SystemUtils.UUID_BASED_PURE_MSG_SENDER(UUID, message));
    }

    public static String ListEloUpdate(String FactionUUID, List<EloResult> eloResultList) {
        String message = "";
        List<EloResult> list = new ArrayList<>();
        for(EloResult result : eloResultList) {
            if(Objects.equals(result.getFactionUUID(), FactionUUID)) {
                list.add(result);
            }
        }
        int size = 0;
        for(EloResult result : list) {
            size++;
            if(result.getHasWon()) {
                if (list.size() > size) {
                    message = message + "&r&e" + UserInfoUtils.getPlayerUUIDOriginName(result.getUUID()) + " &r&f: &r&a" + result.getBeforeElo() + " -> " + result.getCurrentElo() + " (+" + (result.getCurrentElo() - result.getBeforeElo()) + ")&r&8,&r ";
                } else {
                    message = message + "&r&e" + UserInfoUtils.getPlayerUUIDOriginName(result.getUUID()) + " &r&f: &r&a" + result.getBeforeElo() + " -> " + result.getCurrentElo() + " (+" + (result.getCurrentElo() - result.getBeforeElo()) + ")";
                }
            } else {
                if (list.size() > size) {
                    message = message + "&r&7" + UserInfoUtils.getPlayerUUIDOriginName(result.getUUID()) + " &r&f: &r&c" + result.getBeforeElo() + " -> " + result.getCurrentElo() + " (-" + (result.getBeforeElo() - result.getCurrentElo()) + ")&r&8,&r ";
                } else {
                    message = message + "&r&7" + UserInfoUtils.getPlayerUUIDOriginName(result.getUUID()) + " &r&f: &r&c" + result.getBeforeElo() + " -> " + result.getCurrentElo() + " (-" + (result.getBeforeElo() - result.getCurrentElo()) + ")";
                }
            }
        }
        return message;
    }

}
