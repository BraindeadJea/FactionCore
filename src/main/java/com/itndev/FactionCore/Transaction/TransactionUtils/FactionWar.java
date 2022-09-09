package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.EloSystem.API.EloUpdater;
import com.itndev.EloSystem.Package.EloResult;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.Factions.Storage.FactionStorage;
import com.itndev.FactionCore.Factions.Storage.FactionSync;
import com.itndev.FactionCore.Lock.Lock;
import com.itndev.FactionCore.Transaction.TransactionUtils.FactionCD.DeleteFactionUtils;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.*;

public class FactionWar {

    private static final Object WarSync = new Object();

    public static void FactionWarProcess(String UUID, String[] args) {
        String LoserFaction = FactionUtils.getPlayerFactionUUID(UUID);
        String WinnerFaction = FactionUtils.getOPPWar(FactionUtils.getPlayerFactionUUID(UUID));
        //long lf = Long.parseLong(LoserFaction.split("=")[0]);
        UUID loser = java.util.UUID.fromString(LoserFaction.split("=")[1]);
        //long wf = Long.parseLong(LoserFaction.split("=")[0]);
        UUID winner = java.util.UUID.fromString(WinnerFaction.split("=")[1]);
        if(loser.compareTo(winner) > 0) {
            if(Lock.CachedhasLock(WinnerFaction)) {
                synchronized (Lock.getLock(WinnerFaction).getLock()) {
                    FactionWarProcess_lock(UUID, args, LoserFaction);
                }
            } else {
                if (Lock.hasLock(WinnerFaction)) {
                    synchronized (Lock.getLock(WinnerFaction).getLock()) {
                        FactionWarProcess_lock(UUID, args, LoserFaction);
                        Lock.AckLock(WinnerFaction);
                    }
                } else {
                    synchronized (Lock.getPublicLock()) {
                        FactionWarProcess_lock(UUID, args, LoserFaction);
                    }
                }
            }
        } else if (loser.compareTo(winner) < 0) {
            if(Lock.CachedhasLock(LoserFaction)) {
                synchronized (Lock.getLock(LoserFaction).getLock()) {
                    FactionWarProcess_lock(UUID, args, WinnerFaction);
                }
            } else {
                if (Lock.hasLock(LoserFaction)) {
                    synchronized (Lock.getLock(LoserFaction).getLock()) {
                        FactionWarProcess_lock(UUID, args, WinnerFaction);
                        Lock.AckLock(LoserFaction);
                    }
                } else {
                    synchronized (Lock.getPublicLock()) {
                        FactionWarProcess_lock(UUID, args, WinnerFaction);
                    }
                }
            }
        }
    }
    private static void FactionWarProcess_lock(String UUID, String[] args, String secondlock) {
        if(Lock.CachedhasLock(secondlock)) {
            synchronized (Lock.getLock(secondlock).getLock()) {
                FactionWarProcess_run(UUID, args);
            }
        } else {
            if (Lock.hasLock(secondlock)) {
                synchronized (Lock.getLock(secondlock).getLock()) {
                    FactionWarProcess_run(UUID, args);
                    Lock.AckLock(secondlock);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionWarProcess_run(UUID, args);
                }
            }
        }
    }

    private static void FactionWarProcess_run(String UUID, String[] args) {
        String LoserFaction = FactionUtils.getPlayerFactionUUID(UUID);
        String WinnerFaction = FactionUtils.getOPPWar(FactionUtils.getPlayerFactionUUID(UUID));
        String LoserFactionName = FactionUtils.getCapFactionNameFromUUID(LoserFaction);
        String WinnerFactionName = FactionUtils.getCapFactionNameFromUUID(WinnerFaction);
        ArrayList<String> LoserFactionMember = FactionUtils.getFactionMember(LoserFaction);
        ArrayList<String> WinnerFactionMember = FactionUtils.getFactionMember(WinnerFaction);
        List<EloResult> eloResultList = EloUpdater.ProcessWarElo(WinnerFactionMember, LoserFactionMember, true, WinnerFaction, LoserFaction);
        new Thread(() -> {
            FactionWar.WarEloUpdate(LoserFactionName, WinnerFactionName, LoserFaction, WinnerFaction, LoserFactionMember, WinnerFactionMember, eloResultList);
        }).start();
        DeleteFactionUtils.DESTORYFaction(UUID, LoserFaction);
        FactionWar.NomoreinWar(args[1]);
    }

    public static void FactionWarCMD(String UUID, String[] args, String Additional) {
        if(args.length < 3) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 전쟁 &f(&7신청&8, &7수락&f) &7(국가이름))");
            return;
        }
        if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                FactionWarCMD_lock(UUID, args);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    FactionWarCMD_lock(UUID, args);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionWarCMD_lock(UUID, args);
                }
            }
        }
    }

    private static void FactionWarCMD_lock(String UUID, String[] args) {
        if (!FactionUtils.isInFaction(UUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
        if(Lock.CachedhasLock(FactionUUID)) {
            synchronized (Lock.getLock(FactionUUID).getLock()) {
                FactionWarCMD_run(UUID, args);
            }
        } else {
            if (Lock.hasLock(FactionUUID)) {
                synchronized (Lock.getLock(FactionUUID).getLock()) {
                    FactionWarCMD_run(UUID, args);
                    Lock.AckLock(FactionUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionWarCMD_run(UUID, args);
                }
            }
        }
    }

    private static void FactionWarCMD_run(String UUID, String[] args) {
        if (!FactionUtils.HigherThenorSameRank(UUID, Config.Leader)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가의 리더만 해당 명령어를 사용할수 있습니다");
            return;
        }
        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
        if (!FactionUtils.isExistingFaction(args[2].toLowerCase(Locale.ROOT))) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 나라 &c" + args[2] + " &r(은)는 존재하지 않습니다");
            return;
        }
        if (args[1].equalsIgnoreCase("신청")) {
            if (FactionUtils.isInWar(FactionUUID)) {
                String OPPFACNAME = FactionUtils.getCapFactionNameFromUUID(FactionUtils.getOPPWar(FactionUUID));
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f이미 &c" + OPPFACNAME + " &r국가와 전쟁 중입니다");
                OPPFACNAME = null;
                return;
            }
            if (!FactionUtils.hasMainBeacon(FactionUUID)) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f신호기가 없는 중립국은 전쟁을 할수 없습니다");
                return;
            }
            if (!FactionUtils.hasMainBeacon(FactionUtils.getFactionUUID(args[2].toLowerCase(Locale.ROOT)))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&상대 국가는 신호기가 없는 중립국이므로 전쟁을 할수 없습니다");
                return;
            }
            FactionTimeOut.FactionWarRequest(UUID, FactionUUID, FactionUtils.getFactionUUID(args[2].toLowerCase(Locale.ROOT)));
        } else if (args[1].equalsIgnoreCase("수락")) {
            if (FactionUtils.isInWar(FactionUUID)) {
                String OPPFACNAME = FactionUtils.getCapFactionNameFromUUID(FactionUtils.getOPPWar(FactionUUID));
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f이미 &c" + OPPFACNAME + " &r국가와 전쟁 중입니다");
                return;
            }
            if (!FactionUtils.hasMainBeacon(FactionUUID)) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f신호기가 없는 중립국은 전쟁을 할수 없습니다");
                return;
            }
            if (!FactionUtils.hasMainBeacon(FactionUtils.getFactionUUID(args[2].toLowerCase(Locale.ROOT)))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f상대 국가는 신호기가 없는 중립국이므로 전쟁을 할수 없습니다");
                return;
            }
            FactionTimeOut.FactionWarAccept(UUID, FactionUUID, FactionUtils.getFactionUUID(args[2].toLowerCase(Locale.ROOT)));
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 전쟁 &f(&7신청&8, &7수락&f) &7(국가이름))");
        }
    }

    public static void NomoreinWar(String FactionUUID) {
        synchronized (WarSync) {
            if(Lock.CachedhasLock(FactionUUID)) {
                synchronized (Lock.getLock(FactionUUID).getLock()) {
                    NomoreinWar_run(FactionUUID);
                }
            } else {
                if (Lock.hasLock(FactionUUID)) {
                    synchronized (Lock.getLock(FactionUUID).getLock()) {
                        NomoreinWar_run(FactionUUID);
                        Lock.AckLock(FactionUUID);
                    }
                } else {
                    synchronized (Lock.getPublicLock()) {
                        NomoreinWar_run(FactionUUID);
                    }
                }
            }

        }
    }

    private static void NomoreinWar_run(String FacitonUUID) {
        if(FactionUtils.isUsedFactionUUID(FacitonUUID)) {
            Storage.AddCommandToQueue("notify:=:" + FactionUtils.getFactionLeader(FacitonUUID) + ":=:" + "SIBAL" + ":=:" + "&r&f전쟁이 끝났습니다" + ":=:" + "true");
            FactionUtils.removeFromWar(FacitonUUID);
        }
    }

    public static void WarEloUpdate(String loserFactionName, String winnerFactionName, String loserFaction, String winnerFaction,
                                    ArrayList<String> loserFactionMember, ArrayList<String> winnerFactionMember,
                                    List<EloResult> eloResultList) {
        //====================//
        synchronized (WarSync) {
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
