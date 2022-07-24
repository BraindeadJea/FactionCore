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

    private Boolean Reader_1 = true;
    private Boolean Reader_2 = true;

    public void StopIO() {
        Reader_1 = false;
        Reader_2 = false;
    }

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
            while (Reader_1) {
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
            }
        }).start();
    }

    private void StreamWriter() {
        new Thread(() -> {
            while (Reader_2) {
                StreamWriter_OUTPUT();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    SystemUtils.logger(e.getMessage());
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
            //String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            Read.READ_COMPRESSEDMAP(message.getBody().get(StaticVal.getCommand()));
        }
        messages = null;
    }

    @Deprecated
    private void StreamReader_INPUT() {
        List<StreamMessage<String, String>> messages = Connect.getRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INPUT_NAME(), Connect.get_LastID_INPUT()));
        for (StreamMessage<String, String> message : messages) {
            Connect.setLastID_INPUT(message.getId());
            Read.READ_COMPRESSEDMAP(message.getBody().get(StaticVal.getCommand()));
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
            Connect.getRedisCommands().xadd(StreamConfig.get_Stream_OUTPUT_NAME(), Collections.singletonMap(StaticVal.getCommand(), compressedhashmap));
        }
        compressedhashmap = null;
    }
}
