package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FactionLeaderPromote {

    @Deprecated
    public static void FactionLeaderPromote(String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 소속 &7(이름)\n");
            return;
        }
        String name = args[1];
        if(!FactionUtils.isInFaction(UUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }


        if(!FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가의 " + Config.Leader_Lang + " 만 이 명령어를 사용할수 있습니다\n");
            return;
        }

        if(!UserInfoUtils.hasJoined(args[1].toLowerCase(Locale.ROOT))) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
            return;
        }

        String TargetUUID = UserInfoUtils.getPlayerUUID(name.toLowerCase(Locale.ROOT));
        String givername = UserInfoUtils.getPlayerUUIDOriginName(UUID);
        String originname = UserInfoUtils.getPlayerUUIDOriginName(TargetUUID);
        if(!FactionUtils.isSameFaction(UUID, TargetUUID)) {
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
