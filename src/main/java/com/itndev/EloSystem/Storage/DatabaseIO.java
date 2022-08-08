package com.itndev.EloSystem.Storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseIO {

    public static Integer getElo(String UUID) {
        try {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("SELECT ELO FROM ELO WHERE UUID=?;");
            ps.setString(1, UUID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("ELO");
            } else {
                PreparedStatement ps2 = SQLite.getConnection().prepareStatement("INSERT IGNORE INTO ELO (UUID, ELO) VALUES (?, ?);");
                ps2.setString(1, UUID);
                ps2.setInt(2, 0);
                ResultSet rs2 = ps.executeQuery();
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setElo(String UUID, Integer Elo) {
        try {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("UPDATE ELO SET ELO=? WHERE UUID=?;");
            ps.setInt(1, Elo);
            ps.setString(2, UUID);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                PreparedStatement ps2 = SQLite.getConnection().prepareStatement("INSERT IGNORE INTO ELO (UUID, ELO) VALUES (?, ?);");
                ps2.setString(1, UUID);
                ps2.setInt(2, 0);
                ResultSet rs2 = ps.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
