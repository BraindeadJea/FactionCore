package com.itndev.FBTM.Transaction.TransactionUtils;

import com.itndev.FBTM.Factions.Config;
import com.itndev.FBTM.Factions.FactionTimeOut;
import com.itndev.FBTM.Utils.Factions.FactionUtils;
import com.itndev.FBTM.Utils.Factions.SystemUtils;
import com.itndev.FBTM.Utils.Factions.UserInfoUtils;

import java.util.Locale;

public class FactionInvite {

    @Deprecated
    public static void FactionInvite(String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 초대 &7(이름)");
            return;
        }

        if (FactionUtils.isInFaction(UUID)) {
            if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 초대를 할수 없습니다");
                return;
            }
            if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                if(UserInfoUtils.hasJoined(args[1])) {
                    String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                    String CasedName = UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(InviteUUID));
                    if(!FactionUtils.isInFaction(InviteUUID)) {
                        FactionTimeOut.InvitePlayer(FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
                    } else {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&c" + CasedName + "&r&f(은)는 이미 다른 국가에 소속되어 있습니다");
                    }
                } else {
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID,   "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
                }
            } else {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
            }
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
        }
    }

    @Deprecated
    public static void FactionInviteCancel(String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 초대취소 &7(이름)");
            return;
        }
        if (FactionUtils.isInFaction(UUID)) {
            if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 초대취소를 할수 없습니다");
                return;
            }
            if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                if (UserInfoUtils.hasJoined(args[1])) {
                    String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                    String CasedName = UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(InviteUUID));
                    FactionTimeOut.cancelInvite(FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
                } else {
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
                }
            } else {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
            }
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
        }
    }

    @Deprecated
    public static void FactionInviteAccept(String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 수락 &7(국가이름)");
            return;
        }

        if(!FactionUtils.isInFaction(UUID)) {
            if(FactionUtils.isExistingFaction(args[1])) {
                String FactionUUID = FactionUtils.getFactionUUID(args[1].toLowerCase(Locale.ROOT));
                if(FactionUtils.isInWar(FactionUUID)) {
                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 국가는 전쟁 중입니다. 수락이 불가능합니다");
                    return;
                }
                FactionTimeOut.AcceptInvite(UUID, FactionUUID);
            } else {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 국가 " + args[1] + " 은 존재하지 않습니다");
            }
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 이미 다른 국가에 소속되어 있습니다");
        }
    }
}
