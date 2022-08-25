package com.itndev.FactionCore.Database.Redis;

import com.itndev.FactionCore.Database.Redis.Obj.StreamConfig;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import io.lettuce.core.XTrimArgs;

import java.util.ArrayList;
import java.util.Objects;

public class RedisTRIM {

    private static ArrayList<String> list = new ArrayList<>();

    private static Long client1 = 0L;
    private static Long client2 = 0L;
    private static Long client3 = 0L;

    public static void TryAskKeepAlive() {
        SystemUtils.KeepAlive();
    }

    public static void KeepAliveResponce(String ServerName, String UUID) {
        SystemUtils.logger(ServerName + " is currently online");
        if(ServerName.equalsIgnoreCase("client1")) {
            client1 = System.currentTimeMillis();
        } else if(ServerName.equalsIgnoreCase("client2")) {
            client2 = System.currentTimeMillis();
        } else if(ServerName.equalsIgnoreCase("client3")) {
            client3 = System.currentTimeMillis();
        }
    }

    public static Long getSmallest() {
        if(client1 <= client2 && client1 <= client3) {
            return client1;
        } else if(client2 <= client1 && client2 <= client3) {
            return client2;
        } else {
            return client3;
        }
    }

    public static void Trim(Long thistime) {
        String time = String.valueOf(thistime);
        Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_BUNGEE_LINE(),XTrimArgs.Builder.minId(time).exactTrimming());
        Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_INPUT_NAME(),XTrimArgs.Builder.minId(time));
        Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_OUTPUT_NAME(),XTrimArgs.Builder.minId(time));
        Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_INTERCONNECT_NAME(),XTrimArgs.Builder.minId(time));
        Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_INTERCONNECT_NAME() + "2",XTrimArgs.Builder.minId(time));
    }
}
