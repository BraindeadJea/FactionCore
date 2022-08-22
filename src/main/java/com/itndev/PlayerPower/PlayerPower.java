package com.itndev.PlayerPower;

import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;

public class PlayerPower {

    public PlayerPower(Loader loader) {
        new Thread(() -> {
            try {
                while (true) {
                    try {
                        PreparedStatement ps = loader.connection().prepareStatement("SELECT * FROM PlayerStats WHERE StatsType=?");
                        ps.setString(1, "POWER");
                        ResultSet rs = ps.executeQuery();
                        while(rs.next()) {
                            double power = Double.parseDouble(rs.getString("StatsValue"));
                            String UUID = rs.getString("PlayerUUID");
                            if(power < 10) {
                                double add = Math.min(power + 2, 10) - power;
                                PreparedStatement update = loader.connection().prepareStatement("Call ADDSTATS('" +
                                        UUID + "','" + "POWER" + "','" + 0 + "','" + (add * -1) + "','" + 0 + "')");
                                update.executeUpdate();
                                SystemUtils.UUID_BASED_PURE_MSG_SENDER(UUID, "");
                            }
                        }
                        Thread.sleep(1000 * 60 * 60 * 4);
                    } catch (Exception e) {
                        SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
                    }

                }
            } catch (Exception e) {
                SystemUtils.error_logger(Arrays.toString(e.getStackTrace()));
            }
        }).start();
    }
}
