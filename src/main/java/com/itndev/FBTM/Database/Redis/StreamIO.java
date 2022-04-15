package com.itndev.FBTM.Database.Redis;

import com.itndev.FBTM.Database.Redis.Obj.Storage;
import com.itndev.FBTM.Database.Redis.Obj.StreamConfig;
import com.itndev.FBTM.Utils.Database.Redis.Read;
import com.itndev.FBTM.Utils.Database.Redis.StaticVal;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StreamIO {

    public void start_ReadStream() {
        while (true) {
            try {
                StreamReader(StreamConfig.get_Stream_INPUT_NAME());
                StreamReader(StreamConfig.get_Stream_INTERCONNECT_NAME());
                 //0.005초마다
                StreamWriter(StreamConfig.get_Stream_OUTPUT_NAME());
                Thread.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(Connect.get_isClosed()) {
                break;
            }
        }
    }

    @Deprecated
    private void StreamReader(String StreamName) {
        List<StreamMessage<String, String>> messages = Connect.getRedisCommands().xread(
                XReadArgs.StreamOffset.from(StreamName, Connect.get_LastID_INPUT()));
        for (StreamMessage<String, String> message : messages) {
            Connect.setLastID_INPUT(message.getId());
            String compressedhashmap = message.getBody().get(StaticVal.getCommand());
            Read.READ_COMPRESSEDMAP(compressedhashmap);
        }
    }

    private void StreamWriter(String StreamName) {
        String compressedhashmap;
        synchronized (Storage.TempCommandQueue) {
            compressedhashmap = Read.HashMap2String(Storage.TempCommandQueue);
            Storage.TempCommandQueue.clear();
        }
        if(compressedhashmap != null) {
            Map<String, String> body = Collections.singletonMap(StreamName, compressedhashmap);
            Connect.getRedisCommands().xadd(StreamConfig.get_Stream_OUTPUT_NAME(), body);
        }
    }
}
