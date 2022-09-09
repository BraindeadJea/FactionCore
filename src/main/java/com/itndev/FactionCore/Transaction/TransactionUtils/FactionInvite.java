package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.Factions.Storage.FactionStorage;
import com.itndev.FactionCore.Factions.Storage.FactionSync;
import com.itndev.FactionCore.Lock.Lock;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.Locale;

public class FactionInvite {

    public static void FactionInvite(String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 초대 &7(이름)");
            return;
        }
        if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                FactionInvite_Lock(UUID, args);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    FactionInvite_Lock(UUID, args);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionInvite_Lock(UUID, args);
                }
            }
        }
    }

    private static void FactionInvite_Lock(String UUID, String[] args) {
        if (FactionUtils.isInFaction(UUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        String FacitonUUID = FactionUtils.getPlayerFactionUUID(UUID);
        if(Lock.CachedhasLock(FacitonUUID)) {
            synchronized (Lock.getLock(FacitonUUID).getLock()) {
                FactionInvite_run(UUID, args);
            }
        } else {
            if (Lock.hasLock(FacitonUUID)) {
                synchronized (Lock.getLock(FacitonUUID).getLock()) {
                    FactionInvite_run(UUID, args);
                    Lock.AckLock(FacitonUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionInvite_run(UUID, args);
                }
            }
        }
    }

    private static void FactionInvite_run(String UUID, String[] args) {
        if (FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 초대를 할수 없습니다");
            return;
        }
        if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
            if (UserInfoUtils.hasJoined(args[1])) {
                String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                String CasedName = UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(InviteUUID));
                if (!FactionUtils.isInFaction(InviteUUID)) {
                    SystemUtils.UUID_BASED_MSG_SENDER(InviteUUID, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " 을 당신의 국가에 초대하였습니다");
                    String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                    new Thread(() -> {
                        FactionTimeOut.InvitePlayer(FactionUUID, InviteUUID);
                    }).start();
                } else {
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&c" + CasedName + "&r&f(은)는 이미 다른 국가에 소속되어 있습니다");
                }
            } else {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
            }
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
        }
    }

    public static void FactionInviteCancel(String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 초대취소 &7(이름)");
            return;
        }
        if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                FactionInviteCancel_Lock(UUID, args);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    FactionInviteCancel_Lock(UUID, args);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionInviteCancel_Lock(UUID, args);
                }
            }
        }
    }

    private static void FactionInviteCancel_Lock(String UUID, String[] args) {
        if (FactionUtils.isInFaction(UUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        String FacitonUUID = FactionUtils.getPlayerFactionUUID(UUID);
        if(Lock.CachedhasLock(FacitonUUID)) {
            synchronized (Lock.getLock(FacitonUUID).getLock()) {
                FactionInviteCancel_run(UUID, args);
            }
        } else {
            if (Lock.hasLock(FacitonUUID)) {
                synchronized (Lock.getLock(FacitonUUID).getLock()) {
                    FactionInviteCancel_run(UUID, args);
                    Lock.AckLock(FacitonUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionInviteCancel_run(UUID, args);
                }
            }
        }
    }

    private static void FactionInviteCancel_run(String UUID, String[] args) {
        if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 초대취소를 할수 없습니다");
            return;
        }
        if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
            if (UserInfoUtils.hasJoined(args[1])) {
                String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                //String CasedName = UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(InviteUUID));
                FactionTimeOut.cancelInvite(UUID, FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
            } else {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
            }
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
        }
    }

    public static void FactionInviteAccept(String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 수락 &7(국가이름)");
            return;
        }
        if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                FactionInviteAccept_Lock(UUID, args);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    FactionInviteAccept_Lock(UUID, args);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionInviteAccept_Lock(UUID, args);
                }
            }
        }
    }

    private static void FactionInviteAccept_Lock(String UUID, String[] args) {
        if (FactionUtils.isInFaction(UUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        String FacitonUUID = FactionUtils.getPlayerFactionUUID(UUID);
        if(Lock.CachedhasLock(FacitonUUID)) {
            synchronized (Lock.getLock(FacitonUUID).getLock()) {
                FactionInviteAccept_run(UUID, args);
            }
        } else {
            if (Lock.hasLock(FacitonUUID)) {
                synchronized (Lock.getLock(FacitonUUID).getLock()) {
                    FactionInviteAccept_run(UUID, args);
                    Lock.AckLock(FacitonUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    FactionInviteAccept_run(UUID, args);
                }
            }
        }
    }

    private static void FactionInviteAccept_run(String UUID, String[] args) {
        if (FactionUtils.isExistingFaction(args[1])) {
            String FactionUUID = FactionUtils.getFactionUUID(args[1].toLowerCase(Locale.ROOT));
            if (FactionUtils.isInWar(FactionUUID)) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 국가는 전쟁 중입니다. 수락이 불가능합니다");
                return;
            }
            new Thread(() -> {
                FactionTimeOut.AcceptInvite(UUID, FactionUUID);
            }).start();
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 국가 " + args[1] + " 은 존재하지 않습니다");
        }
    }
}
