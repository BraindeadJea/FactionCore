package com.itndev.EloSystem.Storage;


import org.sqlite.SQLiteConfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLite {

    private static Connection connection;
    private static String dbFileName;
    private static boolean isOpened = false;

    public static void setup(String databaseFileName) throws ClassNotFoundException {
        dbFileName = databaseFileName;
        Class.forName("org.sqlite.JDBC");
    }

    public static void open() throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        connection = DriverManager.getConnection("jdbc:sqlite:/" + s + "/" + dbFileName, config.toProperties());
        CreateELOTable();
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void CreateELOTable() {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ELO "
                    + "(UUID INT(11),"
                    + "ELO VARCHAR(100),"
                    + "PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean close() {
        if(!isOpened) { return true; }
        try {
            connection.close();
        } catch(SQLException e) { e.printStackTrace(); return false; }
        return true;
    }
}
