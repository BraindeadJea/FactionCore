package com.itndev.FactionCore.Database.Redis;

import com.itndev.FactionCore.Database.Redis.Obj.StreamConfig;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.util.ArrayList;

public class RedisTRIM {

    private static ArrayList<String> list = new ArrayList<>();
    private static String this_UUID = "";

    private static Long this_Time;

    public static void TryAskKeepAlive() {
        //
        list.clear();
        list.add("client1");
        list.add("client2");
        list.add("client3");
        this_Time = System.currentTimeMillis();
        String UUID = SystemUtils.KeepAlive();
        this_UUID = UUID;
    }

    public static void KeepAliveResponce(String ServerName, String UUID) {
        if(this_UUID == UUID) {
            list.remove(ServerName);
        }
        CheckResponce();
    }

    public static void CheckResponce() {
        if(list.isEmpty()) {
            this_UUID = "";
            Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_BUNGEE_LINE(), this_Time);
            Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_INPUT_NAME(), this_Time);
            Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_OUTPUT_NAME(), this_Time);
            Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_INTERCONNECT_NAME(), this_Time);
            Connect.getAsyncRedisCommands().xtrim(StreamConfig.get_Stream_INTERCONNECT_NAME() + "2", this_Time);
            SystemUtils.logger("TRIMMING Redis Streams Data for Memory Freeing");
        }
    }
}
