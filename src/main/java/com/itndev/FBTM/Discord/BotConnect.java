package com.itndev.FBTM.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class BotConnect {

    public static JDA bot = null;

    public static MessageChannel mainchannel;

    public static Guild mainguild;

    public static void ConnectBot(String Token) {
        new Thread( () -> {
            try {
                bot = JDABuilder.createDefault(Token)
                        .enableIntents(GatewayIntent.GUILD_MEMBERS)
                        .addEventListeners(new DiscordListener())
                        .build();

                // optionally block until JDA is ready
                bot.awaitReady();
                System.out.println("Logged in as " + bot.getSelfUser().getName() + "#" + bot.getSelfUser().getDiscriminator() + "!");
                mainchannel = bot.getTextChannelById(967482907579011142L);
                mainguild = bot.getGuildById(934429946565234758L);
                mainchannel.sendMessage("인증봇 작동중").queue();
            } catch (LoginException | InterruptedException e) {
                // Print the error.
                e.printStackTrace();
            }
        }).start();
    }
}
