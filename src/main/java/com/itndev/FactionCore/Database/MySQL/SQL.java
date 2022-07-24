package com.itndev.FactionCore.Database.MySQL;

import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public static void closeConnections(Connection connection, PreparedStatement ps, ResultSet rs) {
        try {
            if(connection != null && !connection.isClosed()) {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                connection.close();
            } else if(connection == null) {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            }
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            e.printStackTrace();
        }
    }
}
