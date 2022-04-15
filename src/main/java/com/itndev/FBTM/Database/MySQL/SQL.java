package com.itndev.FBTM.Database.MySQL;

import com.itndev.FBTM.Database.Redis.Connect;

public class SQL {

    private static ConnectSQL connection = null;
    private static wtfDatabase database = null;

    public static wtfDatabase getDatabase() {
        if(database == null) {
            database = new wtfDatabase();
        }
        return database;
    }

    public static ConnectSQL getConnection() {
        if(connection == null) {
            connection = new ConnectSQL();
        }
        return connection;
    }

    public static void connect() {
        getConnection().setupHikariInfo();
        getConnection().ConnectHikari();
        getConnection().createHikariTable();
    }
}
