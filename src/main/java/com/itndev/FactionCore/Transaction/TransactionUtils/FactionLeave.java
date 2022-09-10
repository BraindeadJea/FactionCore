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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FactionLeave {

    public static void FactionLeave(String UUID, String[] args) {
        try {
            synchronized (Lock.tryOptainLock(UUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                if (!FactionUtils.isInFaction(UUID)) {
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
                    return;
                }
                String FacitonUUID = FactionUtils.getPlayerFactionUUID(UUID);
                synchronized (Lock.tryOptainLock(FacitonUUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                    run(UUID);
                }
            }
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&c&lERROR &7오류 발생 : 오류코드 TIMEOUT_LOCK_002 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
            e.printStackTrace();
        }
        /*if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                lock(UUID);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    lock(UUID);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    lock(UUID);
                }
            }
        }*/
    }

    /*private static void lock(String UUID) {
        if (!FactionUtils.isInFaction(UUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
        if(Lock.CachedhasLock(FactionUUID)) {
            synchronized (Lock.getLock(FactionUUID).getLock()) {
                run(UUID);
            }
        } else {
            if (Lock.hasLock(FactionUUID)) {
                synchronized (Lock.getLock(FactionUUID).getLock()) {
                    run(UUID);
                    Lock.AckLock(FactionUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    run(UUID);
                }
            }
        }
    }*/

    private static void run(String UUID) {
        if (FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 중에는 국가에서 나갈수 없습니다");
            return;
        }
        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가의 " + Config.Leader_Lang + " 은 국가를 나갈 수 없습니다. 국가의 소유권을 양도하거나 해체해야 합니다.\n" +
                    "&f(&7/국가 양도 (이름)&8, &7/국가 해체&f)");
            return;
        }
        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
        //PERMLVL 20, FINALPERMLVL 20
        List<String> BulkCMD = new ArrayList<>();
        BulkCMD.add(FactionUtils.SetFactionMember_GETRAWCMD(UUID, FactionUUID, true));
        BulkCMD.add(FactionUtils.SetPlayerFaction_GETRAWCMD(UUID, null));
        BulkCMD.add(FactionUtils.SetPlayerRank_GETRAWCMD(UUID, Config.Nomad));
        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f성공적으로 국가 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에서 나왔습니다.");
        Storage.AddCommandToQueue("notify:=:" + FactionUtils.getFactionLeader(FactionUUID) + ":=:" + "SIBAL" + ":=:" + "&r&f" + UserInfoUtils.getPlayerUUIDOriginName(UUID) + " 이가 당신의 국가에서 나갔습니다" + ":=:" + "true");
        BulkCMD = null;
        Storage.AddBulkCommandToQueue(BulkCMD);
    }
}
