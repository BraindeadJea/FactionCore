package com.itndev.FactionCore.Utils.Database.Redis;

public class StaticVal {

    private static String MaxAmount = "MAXAMOUNT";

    private static String splitter = "/=&C&:&G&:&1&=/";
    private static String buffer = "-buffer-";
    private static String command = "commands";

    private static Integer ServerNameArgs = -1;

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

    public static Integer getServerNameArgs() {
        return ServerNameArgs;
    }


    public static Integer DataTypeArgs = -2;
    public static Integer getDataTypeArgs() {
        return DataTypeArgs;
    }
}
