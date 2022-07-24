package com.itndev.FactionCore.Discord;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthStorage {

    public static HashMap<String, String> UUID_TO_DISCORDID = new HashMap<>();
    public static HashMap<String, String> DISCORDID_TO_UUID = new HashMap<>();


    public static HashMap<String, ArrayList<String>> TEMP_AUTH_CACHE = new HashMap<>();
    public static HashMap<String, String> TEMP_UUID_ID_CACHE_TO_DISCORDID = new HashMap<>();


    public static Boolean hasAuth(String UUID) {
        if(UUID_TO_DISCORDID.containsKey(UUID)) {
            if(DISCORDID_TO_UUID.get(UUID_TO_DISCORDID.get(UUID)).equals(UUID)) {
                return true;
            }
        }
        return false;
    }

    public static void RemoveAuth(String UUID) {
        if(UUID_TO_DISCORDID.containsKey(UUID)) {
            String DiscordID = UUID_TO_DISCORDID.get(UUID);
            UUID_TO_DISCORDID.remove(UUID);
            DISCORDID_TO_UUID.remove(DiscordID);
            FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "[디스코드]" + " 계정 인증이 풀렸습니다");
            Storage.AddCommandToQueue("discord:=:auth:=:" + UUID + ":=:" + "NULL");
            BotConnect.mainguild.removeRoleFromMember(DiscordID, BotConnect.mainguild.getRolesByName("USER", false).get(0)).queue();
            Member m = BotConnect.mainguild.retrieveMemberById(DiscordID).complete();
            m.modifyNickname(m.getUser().getName()).queue();
            DiscordID = null;
            m = null;
        } else {
            FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "[디스코드]" + " 연동되어있는 계정이 없습니다");
        }
    }

    public static void SendAuthInfo(String UUID) {
        String finalmsg = "";
        finalmsg = finalmsg + "&3&m-------------------------------------\n";
        finalmsg = finalmsg + "&9&l연동정보\n";
        if(UUID_TO_DISCORDID.containsKey(UUID)) {
            String DiscordTag = BotConnect.bot.retrieveUserById(UUID_TO_DISCORDID.get(UUID)).complete().getAsTag();
            finalmsg = finalmsg + "&7현재 당신의 계정은 " + DiscordTag + "&r&7 와 연동되어 있습니다\n";
        } else {
            finalmsg = finalmsg + "&7현재 당신의 계정은 연동되어 있지 않습니다\n";
        }
        finalmsg = finalmsg + "&3&m-------------------------------------";
        SystemUtils.UUID_BASED_PURE_MSG_SENDER(UUID, finalmsg);
        finalmsg = null;
    }

    public static String getID_FROM_UUID(String UUID) {
        return UUID_TO_DISCORDID.getOrDefault(UUID, null);
    }

    public static void AskForHasAuth(String UUID) {
        new Thread(() -> {
            if(hasAuth(UUID)) {
                Storage.AddCommandToQueue("discord:=:auth:=:" + UUID + ":=:" + getID(getID_FROM_UUID(UUID)).getAsTag());
            } else {
                Storage.AddCommandToQueue("discord:=:auth:=:" + UUID + ":=:" + "NULL");
            }
        }).start();
    }

    public static User getID(String DiscordID) {
        return BotConnect.bot.retrieveUserById(DiscordID).complete();
    }

    public static void AddAuth(String UUID, String ID, String DiscordID) {
        ArrayList<String> list;
        if(TEMP_AUTH_CACHE.containsKey(UUID)) {
            list = TEMP_AUTH_CACHE.get(UUID);
        } else {
            list = new ArrayList<>();
        }
        list.add(ID);
        TEMP_AUTH_CACHE.put(UUID, list);
        TEMP_UUID_ID_CACHE_TO_DISCORDID.put(UUID + ID, DiscordID);
        list = null;
    }

    public static void AuthID(String UUID, String ID) {
        new Thread(() -> {
            if(TEMP_AUTH_CACHE.containsKey(UUID)) {
                if(TEMP_AUTH_CACHE.get(UUID).contains(ID)) {
                    //인증
                    String DiscordID = TEMP_UUID_ID_CACHE_TO_DISCORDID.get(UUID + ID);
                    UUID_TO_DISCORDID.put(UUID, DiscordID);
                    DISCORDID_TO_UUID.put(DiscordID, UUID);
                    for(String k : TEMP_AUTH_CACHE.get(UUID)) {
                        TEMP_UUID_ID_CACHE_TO_DISCORDID.remove(UUID + k);
                        k = null;
                    }
                    TEMP_AUTH_CACHE.remove(UUID);
                    String tag = BotConnect.bot.retrieveUserById(DiscordID).complete().getAsTag();
                    FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "[디스코드]" + " 해당 디스코드 계정 " + tag + " 와 성공적으로 연동되었습니다");
                    Storage.AddCommandToQueue("discord:=:auth:=:" + UUID + ":=:" + tag);
                    BotConnect.mainguild.addRoleToMember(DiscordID, BotConnect.mainguild.getRolesByName("USER", false).get(0)).queue();
                    BotConnect.mainguild.retrieveMemberById(DiscordID).complete().modifyNickname("[USER] " + UserInfoUtils.getPlayerUUIDOriginName(UUID)).queue();
                    tag = null;
                    DiscordID = null;
                } else {
                    FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "[디스코드]" + " 잘못된 아이디입니다");
                }
            } else {
                FactionUtils.SendFactionMessage(UUID, "puremessagesendoptiontrue", "single", "[디스코드]" + " 디스코드 서버 인증채널에서 !인증 <닉네임> 을 한후 사용해주시기 바랍니다");
            }
        }).start();
    }
}
