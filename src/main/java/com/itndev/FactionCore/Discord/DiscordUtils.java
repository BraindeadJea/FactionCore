package com.itndev.FactionCore.Discord;

import com.itndev.FactionCore.Utils.Factions.SystemUtils;

public class DiscordUtils {

    public static void error_logger(String text) {
        try {
            BotConnect.loggerchannel.sendMessage("[ " + BotConnect.mainguild.getPublicRole().getAsMention() + " ]\n" +
                    "***ERROR FOUND IN BACKEND SYSTEM***\n" +
                    "**INFO :** " + text).queue();
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void info_logger(String text) {
        try {
            BotConnect.loggerchannel.sendMessage("[INFO/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + text).queue();
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            e.printStackTrace();
        }
    }
}
