package com.itndev.FBTM.Factions;

import com.itndev.FBTM.Database.Redis.Obj.Storage;
import com.itndev.FBTM.Utils.Factions.FactionUtils;
import com.itndev.FBTM.Utils.Factions.SystemUtils;
import com.itndev.FBTM.Utils.Factions.UserInfoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FactionTimeOut {


    public static HashMap<String, String> Timeout1info = new HashMap<>();
    public static HashMap<String, Integer> Timeout1 = new HashMap<>();

    public static HashMap<String, ArrayList<String>> Timeout2info = new HashMap<>();
    public static HashMap<String, Integer> Timeout2 = new HashMap<>();


    @Deprecated
    public static void TimeoutManager() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(() -> {
                    for(String k : Timeout1.keySet()) {
                        int temp = Timeout1.get(k) - 1;
                        Timeout1.put(k, temp);
                        if(temp <= 0) {
                            Timeout1.remove(k);

                            String[] parts = k.split("%");

                            String FactionUUID = parts[0];

                            Timeout1info.remove(FactionUUID);

                            String playeruuid = parts[1];

                            FactionUtils.SendFactionMessage(playeruuid, playeruuid, "single", "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에 대한 해체수락이 만료되었습니다");
                        }
                    }
                    for(String k : Timeout2.keySet()) {
                        int temp = Timeout2.get(k) - 1;
                        Timeout2.put(k, temp);
                        if(temp <= 0) {
                            Timeout2.remove(k);

                            String parts[] = k.split("%");

                            String PlayerUUID = parts[0];
                            String FactionUUID = parts[1];

                            ArrayList<String> templist = Timeout2info.get(PlayerUUID);
                            if(!templist.isEmpty()) {
                                if(templist.contains(FactionUUID)) {
                                    templist.remove(FactionUUID);
                                    //FactionUtils.SendFactionMessage(PlayerUUID, PlayerUUID, "single", "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에서 보낸 초대가 만료되었습니다");
                                    SystemUtils.UUID_BASED_MSG_SENDER(PlayerUUID, "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에서 보낸 초대가 만료되었습니다");
                                    if(templist.isEmpty()) {
                                        Timeout2info.remove(PlayerUUID);
                                    } else {
                                        Timeout2info.put(PlayerUUID, templist);
                                    }
                                }

                            }
                        }
                    }
                }).start();
            }
        }).start();
    }

    public static void DeleteFactionTEMP(String FactionUUID, String UUID) {

        //메세지
        //SystemUtils.sendmessage(p, "");

        Timeout1.put(FactionUUID + "%" + UUID, 20);
        Timeout1info.put(FactionUUID, UUID);
    }

    public static void InvitePlayer(String FactionUUID, String UUID) {
        //SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " 을 당신의 국가에 초대하였습니다");
        Storage.AddCommandToQueue("update:=:Timeout2:=:add:=:" + UUID + "%" + FactionUUID + ":=:add:=:" + 30);
        Storage.AddCommandToQueue("update:=:Timeout2info:=:add:=:" + UUID + ":=:add:=:" + FactionUUID);
        String FactionName = FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID));
        FactionUtils.SendFactionMessage(UUID, UUID, "single", "&r&f" + FactionName + " 에서 당신을 초대했습니다.\n" +
                "&7(/국가 수락 " + FactionName + ")");
    }

    public static void AcceptInvite(String UUID, String FactionUUID) {
        if(!FactionUtils.isInFaction(UUID)) {
            if (Timeout2info.containsKey(UUID) && Timeout2info.get(UUID).contains(FactionUUID)) {
                Storage.AddCommandToQueue("update:=:Timeout2:=:remove:=:" + UUID + "%" + FactionUUID + ":=:add:=:" + 30);
                Storage.AddCommandToQueue("update:=:Timeout2info:=:remove:=:" + UUID + ":=:add:=:" + 30);
                //SystemUtils.sendfactionmessage(sender, "&r&f국가 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에 성공적으로 가입했습니다");
                //cmdonsucceed.add("notify:=:" + FactionUtils.getFactionLeader(FactionUUID) + ":=:" + "SIBAL" + ":=:" + "&r&f" + UserInfoUtils.getPlayerUUIDOriginName(UUID) + " 이가 당신의 국가에 가입했습니다" + ":=:" + "true");
                //FactionUtils.FactionUUIDNotify();
                FactionUtils.SetPlayerFaction(UUID, FactionUUID);
                FactionUtils.SetFactionMember(UUID, FactionUUID, false);
                FactionUtils.SetPlayerRank(UUID, Config.Member);
                List<String> BulkCMD = new ArrayList<>();
                BulkCMD.add(FactionUtils.SetPlayerFaction_GETRAWCMD(UUID, FactionUUID));
                BulkCMD.add(FactionUtils.SetFactionMember_GETRAWCMD(UUID, FactionUUID, false));
                BulkCMD.add(FactionUtils.SetPlayerRank_GETRAWCMD(UUID, Config.Member));
                Storage.AddBulkCommandToQueue(BulkCMD);
            } else {
                //SystemUtils.sendfactionmessage(sender, "&r&f해당 국가"
                //        + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID))
                //        + " 에서 보낸 초대장이 만료되었거나 존재하지 않습니다");
            }
        } else {
            //SystemUtils.sendfactionmessage(sender, "&r&f당신은 이미 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) + " 에 소속되어 있습니다");
        }
    }

    public static void cancelInvite(String FactionUUID, String UUID) {
        if (Timeout2info.containsKey(UUID) && Timeout2info.get(UUID).contains(FactionUUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " 에게 보낸 초대장을 취소하였습니다");
            FactionUtils.SendFactionMessage(UUID, UUID, "single", "&r&f국가" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에서 당신에게 보낸 초대장을 취소하였습니다");
            Storage.AddCommandToQueue("update:=:Timeout2:=:add:=:" + UUID + "%" + FactionUUID + ":=:add:=:" + 30);
            Storage.AddCommandToQueue("update:=:Timeout2info:=:add:=:" + UUID + ":=:add:=:" + FactionUUID);
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " 에게 보낸 초대장이 만료되었거나 존재하지 않습니다");
        }
    }

}
