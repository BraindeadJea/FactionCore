package com.itndev.FactionCore.Utils.Database.Redis;

public class StaticVal {

    private static String MaxAmount = "MAXAMOUNT";

    private static String splitter = "/=&C&:&G&:&1&=/";
    private static String buffer = "-buffer-";
    private static String command = "commands";

    private static String ServerNameArgs = "SERVERNAME";

    private static Long RedisCommandTimeoutInMillies = 40L;

    public static Long getRedisCommandTimeoutInMillies() {
        return RedisCommandTimeoutInMillies;
    }

    public static String getCommand() {
        return command;
    }

    public static String getbuffer() {
        return buffer;
    }

    public static String getMaxAmount() {
        return MaxAmount;
    }

    public static String getsplitter() {
        return splitter;
    }

    public static String getServerNameArgs() {
        return ServerNameArgs;
    }


    public static String DataTypeArgs = "DATATYPE";
    public static String getDataTypeArgs() {
        return DataTypeArgs;
    }
}
