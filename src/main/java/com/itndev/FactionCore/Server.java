package com.itndev.FactionCore;

import com.itndev.FactionCore.Database.MySQL.SQL;
import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeStreamReader;
import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Database.Redis.StreamIO;
import com.itndev.FactionCore.Discord.BotConnect;
import com.itndev.FactionCore.Dump.MySQLDump;
import com.itndev.FactionCore.Dump.RedisDump;
import com.itndev.FactionCore.Dump.YamlDump;
import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.ValidChecker;

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
        YamlDump.LoadConnectionInfo();
        System.out.println("[STARTUP] STARTING UP DATABASE CONNECTION");
        SQL.connect();
        PingSQL();
        Connect.RedisConnect();
        Streamable = true;
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            streamIO = new StreamIO();
            streamIO.start_ReadStream();
            BungeeStreamReader.RedisStreamReader();
            try {
                if(!RedisDump.has_Verification()) {
                    //RedisDump.ReloadStorageFromRemoteServer("DUMP");
                    MySQLDump.LoadFromMySQL();
                } else {
                    TryLoadYaml();
                }
            } catch (Exception e) {
                //RedisDump.ReloadStorageFromRemoteServer("DUMP");
                //TryLoadYaml();
                SystemUtils.error_logger(e.getMessage());
            }
        }).start();

        ValidChecker.setvalid();
        FactionTimeOut.TimeoutManager();
        BotConnect.ConnectBot("OTY3NDcyNzQ2OTU3MjYyODg4.YmQzNQ.IMfgHmqwJDfbRAk64k6b97giWUE");
        System.out.println("[TASK] RUNNING TASK MANAGER");


        System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] Loading FlatFile...");
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
                        //RedisDump.deleteandupload("DUMP");
                        try {
                            MySQLDump.DumpToMySQL();
                        } catch (Exception ex) {
                            System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + ex.getMessage());
                            Connect.RedisConnect();
                            SQL.connect();
                        }
                    }).start();
                } catch (Exception ex) {
                    System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + ex.getMessage());
                }
                System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] BACKUP DONE TO MYSQL");
                if(Close) {


                    streamIO.StopIO();
                    //RedisDump.deleteandupload("DUMP");
                    try {
                        MySQLDump.DumpToMySQL();
                    } catch (SQLException throwables) {
                        System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + throwables.getMessage());
                    }
                    RedisDump.set_Verification();
                    Streamable = false;
                    Thread.sleep(1000);
                    Connect.getRedisConnection().close();
                    TryDumpYaml();
                    try {
                        SQL.getConnection().getHikariConnection().close();
                        System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] CLOSED MYSQL CONNECTION");
                    } catch (SQLException throwables) {
                        //throwables.printStackTrace();
                        System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] MYSQL CONNECTION FAILED TO CLOSE");
                        System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + throwables.getMessage());
                    }
                   break;
                }
                //System.out.println("[LOG] (EVERY 10 SECONDS) TIME ->" + SystemUtils.getDate(System.currentTimeMillis()));
            } catch (InterruptedException e) {
                System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + e.getMessage());
                Connect.RedisConnect();
            }
        }
        try {
            Thread.sleep(3000);
            YamlDump.SaveConnectionInfo();
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
