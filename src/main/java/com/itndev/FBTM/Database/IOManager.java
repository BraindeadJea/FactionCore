package com.itndev.FBTM.Database;

import com.itndev.FBTM.Database.Redis.Connect;
import com.itndev.FBTM.Database.Redis.StreamIO;

public class IOManager {

    private static StreamIO streamIO = new StreamIO();

    @Deprecated
    private static void REDIS_STARTUP() {
        Connect.RedisConnect();
        streamIO.start_ReadStream();
    }

    @Deprecated
    private static void REDIS_SHUTDOWN() {
        Connect.RedisDisConnect();
    }
}
