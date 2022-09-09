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

public class FactionKick {
    public static void FactionKick(String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 추방 &7(이름)");
            return;
        }
        if(Lock.CachedhasLock(UUID)) {
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
        }
    }

    private static void lock(String UUID, String[] args) {
        if (FactionUtils.isInFaction(UUID)) {
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
    }

    private static void run(String UUID, String[] args) {
        if (FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 추방을 할수 없습니다");
            return;
        }
        if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
            String name = args[1];
            if (UserInfoUtils.hasJoined(args[1].toLowerCase(Locale.ROOT))) {
                String KICKUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                String OriginName = UserInfoUtils.getPlayerOrginName(args[1].toLowerCase(Locale.ROOT));
                if (FactionUtils.isInFaction(KICKUUID) && FactionUtils.isSameFaction(UUID, KICKUUID)) {
                    if (FactionUtils.HigherThenRank(UUID, FactionUtils.getPlayerRank(KICKUUID))) {
                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + OriginName + " 이를 국가에서 추방했습니다.");
                        FactionUtils.SendFactionMessage(KICKUUID, KICKUUID, "single", "&r&f당신은 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에서 추방했습니다.");
                        FactionUtils.SendFactionMessage(UUID, UUID, UUID, "&r&f" + UserInfoUtils.getPlayerUUIDOriginName(UUID) + " 이가 " + OriginName + " 이를 국가에서 추방했습니다.");
                        FactionUtils.AsyncRemovePlayerFromFaction(KICKUUID, FactionUUID);
                    } else {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + OriginName + "(은)는 당신보다 등급이 높은 &r&c" + FactionUtils.getPlayerRank(KICKUUID) + " &r&f입니다. 자신보다 높은 등급인 멤버를 추방할수 없습니다");
                    }
                } else {
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + OriginName + "(은)는 당신의 팀이 아닙니다");
                }
            } else {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
            }
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
        }
    }
}
