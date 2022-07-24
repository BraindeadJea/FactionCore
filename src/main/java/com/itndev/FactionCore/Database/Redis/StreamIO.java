package com.itndev.FactionCore.Database.Redis;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Database.Redis.Obj.StreamConfig;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.Database.Redis.Read;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StreamIO {

    public void start_ReadStream() {
        while(!Server.Streamable) {
            try {
                Thread.sleep(1000);
                SystemUtils.logger("Waiting To execute Database Task Before Loading Storage...");
            } catch (InterruptedException e) {
                SystemUtils.logger(e.getMessage());
            }
        }
        StreamReader();
        StreamWriter();
    }

    private void StreamReader() {
        new Thread(() -> {
            while (true) {
                try {
                    StreamReader_INNER();
                    StreamReader_INPUT();
                } catch (Exception e) {
                    //e.printStackTrace();
                    SystemUtils.logger(e.getMessage());
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    SystemUtils.logger(e.getMessage());
                }
                if(Server.Streamable) {
                    break;
                }
            }
        }).start();
    }

    private void StreamWriter() {
        new Thread(() -> {
            while (true) {
                StreamWriter_OUTPUT();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    SystemUtils.logger(e.getMessage());
                }
                if(Server.Streamable) {
                    break;
                }
            }
        }).start();
    }

    @Deprecated
    private void StreamReader_INNER() {
        List<StreamMessage<String, String>> messages = Connect.getRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INTERCONNECT_NAME(), Connect.get_LastID_INNER()));
        for (StreamMessage<String, String> message : messages) {
            Connect.setLastID_INNER(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            Read.READ_COMPRESSEDMAP(compressedhashmap);
            message = null;
            compressedhashmap = null;
        }
        messages = null;
    }

    @Deprecated
    private void StreamReader_INPUT() {
        List<StreamMessage<String, String>> messages = Connect.getRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INPUT_NAME(), Connect.get_LastID_INPUT()));
        for (StreamMessage<String, String> message : messages) {
            Connect.setLastID_INPUT(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            Read.READ_COMPRESSEDMAP(compressedhashmap);
            message = null;
            compressedhashmap = null;
        }
        messages = null;
    }

    @Deprecated
    private void StreamWriter_OUTPUT() {
        String compressedhashmap;
        synchronized (Storage.TempCommandQueue) {
            compressedhashmap = Read.HashMap2String(Storage.TempCommandQueue);
            Storage.TempCommandQueue.clear();
        }
        if(compressedhashmap != null) {
            Map<String, String> body = Collections.singletonMap(StaticVal.getCommand(), compressedhashmap);
            Connect.getRedisCommands().xadd(StreamConfig.get_Stream_OUTPUT_NAME(), body);
        }
        compressedhashmap = null;
    }
}
