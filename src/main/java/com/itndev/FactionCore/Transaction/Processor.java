package com.itndev.FactionCore.Transaction;

import com.itndev.FactionCore.Discord.AuthStorage;
import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Transaction.TransactionUtils.*;
import com.itndev.FactionCore.Transaction.TransactionUtils.FactionCD.DeleteFactionUtils;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.Locale;

public class Processor {

    @Deprecated
    public static void Processor(String UUID, String[] args, String additionalinfo, String ServerName) {
        if(args.length < 1) {
            FactionHelp.FactionHelp(UUID);
            return;
        } else {
            try {
                if(args[0].equalsIgnoreCase("도움말")) {
                    FactionHelp.FactionHelp(UUID);
                } else if(args[0].equalsIgnoreCase("인증")) {
                    if (args[1].equalsIgnoreCase("확인")) {
                        AuthStorage.AuthID(UUID, args[2]);
                    } else if (args[1].equalsIgnoreCase("정보")) {
                        AuthStorage.AskForHasAuth(UUID);
                    } else if (args[1].equalsIgnoreCase("해제")) {
                        AuthStorage.RemoveAuth(UUID);
                    } else if (args[1].equalsIgnoreCase("정보요청")) {
                        AuthStorage.SendAuthInfo(UUID);
                    }
                } else if(args[0].equalsIgnoreCase("국가멸망")){
                    DeleteFactionUtils.DESTORYFaction(UUID, FactionUtils.getPlayerFactionUUID(UUID));
                    FactionWar.NomoreinWar(args[1]);
                } else if(args[0].equalsIgnoreCase("생성")) {

                    //=================생성=================

                    CreateFaction.CreateFaction(UUID, args);

                    //=================생성=================

                } else if(args[0].equalsIgnoreCase("해체")) {

                    //=================해체=================

                    DeleteFaction.DeleteFactionQueue(UUID, args);

                    //=================해체=================

                } else if(args[0].equalsIgnoreCase("해체수락")) {

                    //=================해체수락=================

                    DeleteFaction.DeleteFaction(UUID, args);

                    //=================해체수락=================

                } else if(args[0].equalsIgnoreCase("초대")) {

                    //=================초대=================

                    FactionInvite.FactionInvite(UUID, args);

                    //=================초대=================

                } else if(args[0].equalsIgnoreCase("초대취소")) {

                    //=================초대취소=================

                    FactionInvite.FactionInviteCancel(UUID, args);

                    //=================초대취소=================

                } else if(args[0].equalsIgnoreCase("수락")) {

                    //=================수락=================

                    FactionInvite.FactionInviteAccept(UUID, args);

                    //=================수락=================

                } else if(args[0].equalsIgnoreCase("추방")) {

                    //=================추방=================

                    FactionKick.FactionKick(UUID, args);

                    //=================추방=================

                } else if(args[0].equalsIgnoreCase("설정")) {

                    //=================설정=================

                    if (args.length < 2) {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f명령어 사용법 : &f/국가 설정 &7(설정)\n" +
                                "&a&o&l[ &r&f국가 &a&o&l] &r&f설정으로는 &7등급&8, &7설명&8, &7공지&8, &7스폰&8, &f(&7동맹&8, &7적대&8, &7중립&f) 이 있습니다.");
                        return;
                    }

                    if (FactionUtils.isInFaction(UUID)) {
                        if (args[1].equalsIgnoreCase("등급")) {

                            if (args.length < 4) {
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f명령어 사용법 : &f/국가 설정 등급 &7(등급이름) &7(이름)\n" +
                                        "&a&o&l[ &r&f국가 &a&o&l] &r&등급으로는 &7" + Config.CoLeader_Lang + "&8, &7" + Config.VipMember_Lang + "&8, &7" + Config.Warrior_Lang + "&8, &7" + Config.Member_Lang + " &f이 있습니다.");
                                return;
                            }

                            String name = args[3];
                            Boolean RealRank = FactionUtils.isAExistingLangRank(args[2]);
                            Boolean RealPlayer = UserInfoUtils.hasJoined(args[3].toLowerCase(Locale.ROOT));

                            //=================등급=================

                            if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                                if (!RealRank) {
                                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f해당 등급 " + args[2] + "(은)는 존재하지 않습니다");
                                    return;
                                }
                                String TheRank = FactionUtils.RankConvert(args[2]);
                                if (TheRank.equalsIgnoreCase(Config.Leader)) {
                                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f해당 등급 " + Config.Leader_Lang + "(은)는 다른 사람에게 부여할수 없습니다. /국가 양도 &7(이름) &r&f국가에 대한 소유권을 양도할수 있습니다");
                                    return;
                                }
                                if (!RealPlayer) {
                                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
                                    return;
                                }
                                String TargetUUID = UserInfoUtils.getPlayerUUID(name.toLowerCase(Locale.ROOT));
                                String CasedTargetName = UserInfoUtils.getPlayerOrginName(name.toLowerCase(Locale.ROOT));
                                if (TargetUUID.equals(UUID)) {
                                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f자신의 등급를 변경할 수 없습니다");
                                    return;
                                }
                                if (!FactionUtils.HigherThenRank(UUID, FactionUtils.getPlayerRank(TargetUUID))) {
                                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f해당 유저 " + CasedTargetName + "(은)는 당신보다 등급이 높은 &r&c" + FactionUtils.getPlayerRank(TargetUUID) + " &r&f입니다. 자신보다 높은 등급인 멤버의 등급를 바꿀 수 없습니다");
                                    return;
                                }
                                if (!FactionUtils.HigherThenRank(UUID, TheRank)) {
                                    SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f해당 유저 " + CasedTargetName + " 에게 지급할 등급은 자신의 등급보다 높거나 같아서는 안됩니다");
                                    return;
                                }
                                //성공
                                FactionUtils.SetPlayerRank(TargetUUID, TheRank);
                                FactionUtils.SendFactionMessage(UUID, UUID, "single", "&r&f" + CasedTargetName + " 이의 등급을 " + FactionUtils.LangRankConvert(TheRank) + " 으로 변경하였습니다");
                                FactionUtils.SendFactionMessage(TargetUUID, TargetUUID, "single", "&r&f당신의 등급이 " + FactionUtils.LangRankConvert(TheRank) + " 으로 변경되었습니다");

                                TheRank = null;
                                TargetUUID = null;
                                CasedTargetName = null;
                            } else {
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                            }
                            name = null;
                            RealRank = null;
                            RealPlayer = null;

                            //=================등급=================

                        } else if (args[1].equalsIgnoreCase("설명")) {

                            //=================설명=================

                            if (!FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f권한이 없습니다. &r&c" + Config.CoLeader_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                                return;
                            }

                            if (args.length < 3) {
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f명령어 사용법 : &f/국가 설정 설명 &7(설명)");
                                return;
                            }

                            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                            String Message = SystemUtils.Args2String(args, 2).replace("&", "&.");
                            FactionUtils.SetFactionDesc(FactionUUID, Message);

                            SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f국가 설명을 &7( " + Message + " ) &r&f으로 변경했습니다");
                            FactionUUID = null;
                            Message = null;

                            //=================설명=================

                        } else if (args[1].equalsIgnoreCase("공지")) {

                            //=================공지=================

                            if (!FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f권한이 없습니다. &r&c" + Config.CoLeader_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                                return;
                            }

                            if (args.length < 3) {
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f명령어 사용법 : &f/국가 설정 공지 &7(공지)");
                                return;
                            }

                            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                            String Message = SystemUtils.Args2String(args, 2);
                            FactionUtils.SetFactionNotice(FactionUUID, Message.replace("&", "&."));

                            SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f국가 공지를 &7( " + Message + " ) &r&f으로 변경했습니다");

                            FactionUUID = null;
                            Message = null;

                            //=================공지=================

                        } else if (args[1].equalsIgnoreCase("동맹") || args[1].equalsIgnoreCase("적대") || args[1].equalsIgnoreCase("중립")) {

                            //=================동맹/적대/중립=================

                            if (args.length < 3) {
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f명령어 사용법 : &f/국가 설정 &7(동맹/적대/중립) &7(국가이름)\n");
                                return;
                            }

                            if (!FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {
                                SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f권한이 없습니다. &r&c" + Config.CoLeader_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                                return;
                            }

                            //String OptionText = FactionUtils.FactionStatusConvert(args[1]);

                            //ally , enemy , neutral

                            //=================동맹/적대/중립=================

                        }
                    } else {
                        SystemUtils.UUID_BASED_MSG_SENDER(UUID, SystemUtils.getPrefix() + "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================설정=================

                } else if (args[0].equalsIgnoreCase("전쟁")){

                    //=================전쟁=================

                    FactionWar.FactionWarCMD(UUID, args, additionalinfo);

                    //=================전쟁=================

                } else if(args[0].equalsIgnoreCase("금고")) {

                    //=================금고=================

                    FactionBank.FactionBank(UUID, args, additionalinfo);

                    //=================금고=================

                } else if(args[0].equalsIgnoreCase("나가기")) {

                    //=================나가기=================

                    FactionLeave.FactionLeave(UUID, args);

                    //=================나가기=================

                } else if(args[0].equalsIgnoreCase("양도")) {

                    //=================양도=================

                    FactionLeaderPromote.FactionLeaderPromote(UUID, args);

                    //=================양도=================

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
