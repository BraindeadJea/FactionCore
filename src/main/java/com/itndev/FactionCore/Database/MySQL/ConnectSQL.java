package com.itndev.FactionCore.Database.MySQL;

import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectSQL {
    HikariDataSource dataSource;

    private Connection connection;

    private String host, port, database, username, password;

    private Boolean useSSL;

    public void setupHikariInfo() {
        host = "db.itndev.com";
        port = "5567";
        database = "factioninfo";
        username = "Skadi";
        password = "l80oKGTFA#@fCRH75v5w6fefw";
        useSSL = true;
    }
    //com.mysql.jdbc.jdbc2.optional.MysqlDataSource

    public void ConnectHikari() {
        setupHikariInfo();
        dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(10);
        dataSource.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", username);
        dataSource.addDataSourceProperty("password", password);
        PingDatabase();
    }

    public void PingDatabase() {
    }


    public Connection getHikariConnection() throws SQLException {
        try {
            if(connection != null && !connection.isClosed()) {
                return connection;
            } else {
                try {
                    ConnectHikari();
                    connection = dataSource.getConnection();
                    return connection;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return connection;
                }
            }
        } catch (SQLException e) {
            ConnectHikari();
            try {
                connection = dataSource.getConnection();
                return connection;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    public void createHikariTable() {
        CreateFactionNameTable();
        CreateFactionDTRTable();
        CreateFactionBankTable();
        CreateFactionELOTable();
        CreateFactionBackupTable();
    }

    public void CreateFactionNameTable() {
        try {
            //Connection connection = SQL.getConnection().dataSource.getConnection();
            PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionName "
                    + "(FactionName VARCHAR(100),"
                    + "FactionUUID VARCHAR(100),"
                    + "FactionNameCap VARCHAR(100),"
                    + "PRIMARY KEY (FactionName))");
            ps.executeUpdate();
            SQL.closeConnections(null, ps, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateFactionDTRTable() {
        try {
            // connection = SQL.getConnection().dataSource.getConnection();
            PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionDTR "
                    + "(FactionUUID VARCHAR(100),"
                    + "FactionName VARCHAR(100),"
                    + "FactionDTR VARCHAR(100),"
                    + "PRIMARY KEY (FactionUUID))");
            ps.executeUpdate();
            SQL.closeConnections(null, ps, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateFactionBankTable() {
        try {
            //Connection connection = SQL.getConnection().dataSource.getConnection();
            PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionBank "
                    + "(FactionUUID VARCHAR(100),"
                    + "FactionName VARCHAR(100),"
                    + "FactionBank VARCHAR(100),"
                    + "PRIMARY KEY (FactionUUID))");
            ps.executeUpdate();
            SQL.closeConnections(null, ps, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateFactionELOTable() {
        try {
            //Connection connection = SQL.getConnection().dataSource.getConnection();
            PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionELO "
                    + "(FactionUUID VARCHAR(100),"
                    + "FactionName VARCHAR(100),"
                    + "FactionELO VARCHAR(100),"
                    + "PRIMARY KEY (FactionUUID))"); //%^G$%G$%D&*3#d^ %^U5jui34jhhy5i4$y7G54U^5ty
            ps.executeUpdate();
            SQL.closeConnections(null, ps, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateFactionBackupTable() {
        try {
            //Connection connection = SQL.getConnection().dataSource.getConnection();
            PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionBackup "
                    + "(MAP_NAME VARCHAR(100),"
                    + "MAP_KEY VARCHAR(100),"
                    + "MAP_VALUE VARCHAR(10000))"); //%^G$%G$%D&*3#d^ %^U5jui34jhhy5i4$y7G54U^5ty
            ps.executeUpdate();
            SQL.closeConnections(null, ps, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
