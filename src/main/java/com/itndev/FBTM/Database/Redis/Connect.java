package com.itndev.FBTM.Database.Redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;

public class Connect {

    private static RedisClient client = null;
    private static StatefulRedisConnection<String, String> connection = null;
    private static RedisStreamCommands<String, String> commands = null;
    private static String LastID_INPUT = "0-0";

    private static String redis_address = "127.0.0.1";
    private static Integer redis_port = 6374;
    private static String redis_password = "password";
    private static Boolean sslEnabled = true;

    private static Boolean isClosed = false;

    public static void setRedis_address(String address) {
        redis_address = address;
    }

    public static void setRedis_port(Integer port) {
        redis_port = port;
    }

    public static void setRedis_password(String password) {
        redis_password = password;
    }

    public static void setSslEnabled(Boolean enabled) {
        sslEnabled = enabled;
    }

    public static void setLastID_INPUT(String new_LastID_INPUT) {
        LastID_INPUT = new_LastID_INPUT;
    }

    // GET METHOD
    public static String get_LastID_INPUT() {
        return LastID_INPUT;
    }

    public static Boolean get_isClosed() {
        return isClosed;
    }

    // CONNECT TO REDIS
    @Deprecated
    public static void RedisConnect() {
        RedisURI redisURI = RedisURI.Builder.redis(redis_address, redis_port).withPassword(redis_password).withSsl(sslEnabled).build();
        client = RedisClient.create(redisURI);
        connection = client.connect();
        commands = connection.sync();
    }

    public static void RedisDisConnect() {
        connection.close();
        isClosed = true;
    }

    public static StatefulRedisConnection<String, String> getRedisConnection() {
        if (connection == null || !connection.isOpen()) {
            connection = client.connect();
        }
        return connection;
    }

    public static RedisStreamCommands<String, String> getRedisCommands() {
        if(commands == null) {
            commands = getRedisConnection().sync();
        }
        return commands;
    }
}
