package com.itndev.FactionCore.Database.Redis.BungeeAPI;

import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Database.Redis.Obj.StreamConfig;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.Database.Redis.Read;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;

import java.util.HashMap;
import java.util.List;

public class BungeeStreamReader {

    public static void RedisStreamReader() {
        new Thread(() -> {
            while (true) {
                try {
                    READ_OUTPUT_STREAM();
                    Thread.sleep(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(Server.Streamable) {
                    break;
                }
            }
        }).start();
    }

    private static void READ_OUTPUT_STREAM() {
        List<StreamMessage<String, String>> messages = Connect.getRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_BUNGEE_LINE(), Connect.get_LastID_BUNGEE()));

        for (StreamMessage<String, String> message : messages) {
            Connect.set_LastID_BUNGEE(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            ReadCompressedHashMap_READ(StreamConfig.get_Stream_BUNGEE_LINE(), compressedhashmap);
            message = null;
            compressedhashmap = null;
        }
        messages = null;
    }

    private static void ReadCompressedHashMap_READ(String clientname, String compressedhashmap) {
        HashMap<String, String> map = Read.String2HashMap(compressedhashmap);
        if (!map.isEmpty()) {
            for(int c = 1; c <= Integer.parseInt(map.get(StaticVal.getMaxAmount())); c++) {
                BungeeStorage.READ_Bungee_command(map.get(String.valueOf(c)));
            }
        }
        map = null;
    }
}
