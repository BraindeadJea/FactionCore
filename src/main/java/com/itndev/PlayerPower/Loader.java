package com.itndev.PlayerPower;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Loader {

    private HikariDataSource dataSource;

    private Connection connection;

    private String host, port, database, username, password;

    private Boolean useSSL;

    public void setupConnectionInfo() {
        host = "db.itndev.com";
        port = "3306";
        database = "PlayerStats";
        username = "Gherbo426";
        password = "%#72Y20$3HT^N2fq#%0@$tG53h(D35$CE";
        useSSL = true;
    }
    //com.mysql.jdbc.jdbc2.optional.MysqlDataSource

    public void connect() {
        dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(10);
        dataSource.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", username);
        dataSource.addDataSourceProperty("password", password);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        CreateStatsTable();
        //PingDatabase();
    }

    private void CreateStatsTable() {
        try {
            PreparedStatement ps = connection().prepareStatement("CREATE TABLE IF NOT EXISTS PlayerStats "
                    + "(PlayerUUID VARCHAR(100),"
                    + "StatsType VARCHAR(100),"
                    + "StatsValue VARCHAR(100))");
            ps.executeUpdate();
            //PreparedStatement ps2 = connection().prepareStatement("CREATE UNIQUE INDEX StatsID ON PLAYERSTATS (PlayerUUID, StatsType)");
            //ps2.executeQuery();
            close(null, ps, null);
            //close(null, ps2, null);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connection() throws SQLException {
        try {
            if(connection != null && !connection.isClosed()) {
                return connection;
            } else {
                try {
                    connect();
                    connection = dataSource.getConnection();
                    return connection;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return connection;
                }
            }
        } catch (SQLException e) {
            connect();
            try {
                connection = dataSource.getConnection();
                return connection;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    public static void close(Connection connection, PreparedStatement ps, ResultSet rs) {
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
            e.printStackTrace();
        }
    }
}
