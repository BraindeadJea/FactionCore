package com.itndev.FactionCore.Database.Redis;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Database.Redis.Obj.StreamConfig;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.Database.Redis.Read;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
                SystemUtils.error_logger(e.getMessage());
                e.printStackTrace();
            }
        }
        StreamReader();
        StreamWriter();
    }

    private void StreamReader() {
        new Thread(() -> {
            while (Reader_1) {
                try {
                    //StreamReader_INNER();
                    //StreamReader_INPUT();
                    READ_STREAM_ASYNC();
                    Thread.sleep(20);
                } catch (Exception e) {
                    SystemUtils.error_logger(e.getMessage());
                    e.printStackTrace();
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
                    SystemUtils.error_logger(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void READ_STREAM_ASYNC() throws ExecutionException, InterruptedException, TimeoutException {
        RedisFuture<List<StreamMessage<String, String>>> OUTPUT = Connect.getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INPUT_NAME(), Connect.get_LastID_INPUT()));
        RedisFuture<List<StreamMessage<String, String>>> INTER = Connect.getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INTERCONNECT_NAME(), Connect.get_LastID_INNER()));
        Thread.sleep(5);
        List<StreamMessage<String, String>> INPUT_RESPONCE = OUTPUT.get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);
        List<StreamMessage<String, String>> INTER_RESPONCE = INTER.get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);
        for (StreamMessage<String, String> message : INPUT_RESPONCE) {
            Connect.setLastID_INPUT(message.getId());
            Read.READ_COMPRESSEDMAP(message.getBody().get(StaticVal.getCommand()));
            message = null;
        }
        INPUT_RESPONCE = null;
        for (StreamMessage<String, String> message : INTER_RESPONCE) {
            Connect.setLastID_INNER(message.getId());
            //String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            Read.READ_COMPRESSEDMAP(message.getBody().get(StaticVal.getCommand()));
            message = null;
        }
        INTER_RESPONCE = null;
    }

    private void StreamReader_INNER() throws ExecutionException, InterruptedException, TimeoutException {
        List<StreamMessage<String, String>> messages = Connect.getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INTERCONNECT_NAME(), Connect.get_LastID_INNER())).get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);
        for (StreamMessage<String, String> message : messages) {
            Connect.setLastID_INNER(message.getId());
            //String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            Read.READ_COMPRESSEDMAP(message.getBody().get(StaticVal.getCommand()));
            message = null;
        }
        messages = null;
    }

    private void StreamReader_INPUT() throws ExecutionException, InterruptedException, TimeoutException {
        List<StreamMessage<String, String>> messages = Connect.getAsyncRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamConfig.get_Stream_INPUT_NAME(), Connect.get_LastID_INPUT())).get(StaticVal.getRedisCommandTimeoutInMillies(), TimeUnit.MILLISECONDS);
        for (StreamMessage<String, String> message : messages) {
            Connect.setLastID_INPUT(message.getId());
            Read.READ_COMPRESSEDMAP(message.getBody().get(StaticVal.getCommand()));
            message = null;
        }
        messages = null;
    }

    private void StreamWriter_OUTPUT() {
        String compressedhashmap;
        synchronized (Storage.TempCommandQueue) {
            compressedhashmap = Read.HashMap2String(Storage.TempCommandQueue);
            Storage.TempCommandQueue.clear();
        }
        if(compressedhashmap != null) {
            Connect.getAsyncRedisCommands().xadd(StreamConfig.get_Stream_OUTPUT_NAME(), Collections.singletonMap(StaticVal.getCommand(), compressedhashmap));
        }
        compressedhashmap = null;
    }
}
