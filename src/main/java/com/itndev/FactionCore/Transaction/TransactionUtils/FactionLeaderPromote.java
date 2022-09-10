package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Factions.Storage.FactionSync;
import com.itndev.FactionCore.Lock.Lock;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FactionLeaderPromote {

    public static void FactionLeaderPromote(String UUID, String[] args) {
        new Thread(() -> {
            if(args.length < 2) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 소속 &7(이름)\n");
                return;
            }
            if (!UserInfoUtils.hasJoined(args[1].toLowerCase(Locale.ROOT))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + args[1] + "(은)는 서버에 접속한 적이 없습니다");
                return;
            }
            try {
                synchronized (Lock.tryOptainLock(UUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                    if (!FactionUtils.isInFaction(UUID)) {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
                        return;
                    }
                    String FacitonUUID = FactionUtils.getPlayerFactionUUID(UUID);
                    synchronized (Lock.tryOptainLock(FacitonUUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                        run(UUID, args);
                    }
                }
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&c&lERROR &7오류 발생 : 오류코드 TIMEOUT_LOCK_002 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
                e.printStackTrace();
            }
            /*if(Lock.CachedhasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    lock(UUID, args);
                }
            } else {
                if (Lock.hasLock(UUID)) {
                    synchronized (Lock.getLock(UUID).getLock()) {
                        lock(UUID, args);
                        Lock.AckLock(UUID);
                    }
                } else {
                    synchronized (Lock.getPublicLock()) {
                        lock(UUID, args);
                    }
                }
            }*/
        }).start();
    }

    /*private static void lock(String UUID, String[] args) {
        if (!FactionUtils.isInFaction(UUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
        if(Lock.CachedhasLock(FactionUUID)) {
            synchronized (Lock.getLock(FactionUUID).getLock()) {
                run(UUID, args);
            }
        } else {
            if (Lock.hasLock(FactionUUID)) {
                synchronized (Lock.getLock(FactionUUID).getLock()) {
                    run(UUID, args);
                    Lock.AckLock(FactionUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    run(UUID, args);
                }
            }
        }
    }*/

    private static void run(String UUID, String[] args) {
        if (!FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가의 " + Config.Leader_Lang + " 만 이 명령어를 사용할수 있습니다\n");
            return;
        }
        String TargetUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
        String givername = UserInfoUtils.getPlayerUUIDOriginName(UUID);
        String originname = UserInfoUtils.getPlayerUUIDOriginName(TargetUUID);
        if (!FactionUtils.isSameFaction(UUID, TargetUUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + originname + "(은)는 당신의 국가 소속이 아닙니다");
            return;
        }
        List<String> BulkCMD = new ArrayList<>();
        BulkCMD.add(FactionUtils.SetPlayerRank_GETRAWCMD(TargetUUID, Config.Leader));
        BulkCMD.add(FactionUtils.SetPlayerRank_GETRAWCMD(UUID, Config.CoLeader));
        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + originname + " 에게 국가의 소유권을 양도하였습니다\n" +
                "변경된 당신의 등급 &7&l: &r&f" + Config.CoLeader_Lang);
        FactionUtils.SendFactionMessage(TargetUUID, TargetUUID, "single", "&r&f국가의 " + Config.Leader_Lang + "인 " + givername + " 이가 당신에게 국가의 소유권을 양도하였습니다\n" +
                "변경된 당신의 등급 &7&l: &r&f" + Config.Leader_Lang);
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "SIBAL" + ":=:" + "&r&f" + givername + " 이가 국가의 소유권을 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(TargetUUID)) + " 에게 넘겨주었습니다" + ":=:" + "true");
        Storage.AddBulkCommandToQueue(BulkCMD);
    }

}
