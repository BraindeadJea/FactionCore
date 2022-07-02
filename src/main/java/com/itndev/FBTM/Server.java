package com.itndev.FBTM;

import com.itndev.FBTM.Database.MySQL.SQL;
import com.itndev.FBTM.Database.Redis.BungeeAPI.BungeeStreamReader;
import com.itndev.FBTM.Database.Redis.Connect;
import com.itndev.FBTM.Database.Redis.StreamIO;
import com.itndev.FBTM.Discord.BotConnect;
import com.itndev.FBTM.Dump.RedisDump;
import com.itndev.FBTM.Dump.YamlDump;
import com.itndev.FBTM.Factions.FactionTimeOut;
import com.itndev.FBTM.Utils.Factions.SystemUtils;
import com.itndev.FBTM.Utils.Factions.ValidChecker;

import javax.print.DocFlavor;
import java.sql.SQLException;

public class Server {

    private final static String MAIN_SPLITTER = ":=:";
    public static String getMainSplitter() {
        return MAIN_SPLITTER;
    }

    public static Boolean Close = false;

    public static Boolean Streamable = false;

    private final static String ServerName = "BACKEND_TRANSACTION";
    public static String getServerName() {
        return ServerName;
    }
    private static StreamIO streamIO = null;

    @Deprecated
    public static void main(String[] args) {
        System.out.println("[STARTUP] STARTING UP DATABASE CONNECTION");
        new Thread(() -> {
            Connect.RedisConnect();
            streamIO = new StreamIO();
            streamIO.start_ReadStream();
            BungeeStreamReader.RedisStreamReader();
            try {
                if(!RedisDump.has_Verification()) {
                    RedisDump.ReloadStorageFromRemoteServer("DUMP");
                }
            } catch (Exception e) {
                RedisDump.ReloadStorageFromRemoteServer("DUMP");
            }

            //Connect.ReloadStorageFromRemoteServer("DUMP");
        }).start();
        ValidChecker.setvalid();
        FactionTimeOut.TimeoutManager();
        SQL.connect();
        PingSQL();
        BotConnect.ConnectBot("OTY3NDcyNzQ2OTU3MjYyODg4.YmQzNQ.IMfgHmqwJDfbRAk64k6b97giWUE");
        System.out.println("[TASK] RUNNING TASK MANAGER");
        YamlDump.LoadConnectionInfo();
        TryLoadYaml();
        System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] Loading FlatFile...");
        Streamable = true;
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] Starting up Process");
        while (true) {
            try {
                Thread.sleep(10000);
                try {
                    new Thread(() -> {
                        RedisDump.deleteandupload("DUMP");
                    }).start();
                } catch (Exception ex) {
                    System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + ex.getMessage());
                    Connect.RedisConnect();
                }
                System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] BACKUP DONE TO REDIS");
                if(Close) {
                    RedisDump.deleteandupload("DUMP");
                    RedisDump.set_Verification();
                    YamlDump.SaveConnectionInfo();
                    TryDumpYaml();
                   break;
                }
                //System.out.println("[LOG] (EVERY 10 SECONDS) TIME ->" + SystemUtils.getDate(System.currentTimeMillis()));
            } catch (InterruptedException e) {
                e.printStackTrace();
                Connect.RedisConnect();
            }
        }
        try {
            Thread.sleep(3000);
            System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] STOPPING SERVICE");
            System.exit(0);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }

    }

    private static void TryLoadYaml() {
        try {
            YamlDump.TryLoadHashMaps_DISCORD();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            YamlDump.TryLoadHashMaps_Factions_LISTTYPE();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            YamlDump.TryLoadHashMaps_UUIDINFO();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            YamlDump.TryLoadHashMaps_Factions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void TryDumpYaml() {
        try {
            YamlDump.TryDumpHashMaps_DISCORD();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            YamlDump.TryDumpHashMaps_Factions();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            YamlDump.TryDumpHashMaps_UUIDINFO();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            YamlDump.TryDumpHashMaps_Factions_LISTTYPE();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PingSQL() {
        new Thread(() -> {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(true) {
                try {
                    SQL.getConnection().getHikariConnection().prepareStatement("SELECT 1").executeQuery();
                    Thread.sleep(1000*1000);
                } catch (SQLException | InterruptedException throwables) {
                    throwables.printStackTrace();
                    SQL.connect();
                }
            }
        }).start();
    }
}
