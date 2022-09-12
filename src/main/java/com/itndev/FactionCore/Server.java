package com.itndev.FactionCore;

import com.itndev.EloSystem.Storage.SQLite;
import com.itndev.FactionCore.Database.Gui.GuiPanel;
import com.itndev.FactionCore.Database.MySQL.SQL;
import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeStreamReader;
import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Database.Redis.RedisTRIM;
import com.itndev.FactionCore.Database.Redis.StreamIO;
import com.itndev.FactionCore.Discord.BotConnect;
import com.itndev.FactionCore.Dump.MySQLDump;
import com.itndev.FactionCore.Dump.RedisDump;
import com.itndev.FactionCore.Dump.YamlDump;
import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.SocketConnection.Client.Client;
import com.itndev.FactionCore.SocketConnection.Main;
import com.itndev.FactionCore.SocketConnection.kurumi;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.ValidChecker;
import com.itndev.HttpAPI.Jetty;
import com.itndev.PlayerPower.PlayerPower;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//@SpringBootApplication
public class Server {


    public static GuiPanel GUI = null;
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

    public static Boolean FromMYSQL = false;

    public static void main(String[] args) {
        SystemUtils.logger(kurumi.Kurumi(5).toString());
        //YamlDump.LoadConnectionInfo();
        GUI = new GuiPanel();
        while(!GuiPanel.isLoaded) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("[WAITING] SELECT WHICH METHOD TO LOAD THE DATABASE FROM");
        }
        System.out.println("[STARTUP] STARTING UP DATABASE CONNECTION");
        SQL.connect();
        PingSQL();
        //Connect.RedisConnect();
        Streamable = true;
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //streamIO = new StreamIO();
            //streamIO.start_ReadStream();
            //BungeeStreamReader.RedisStreamReader();
            if(FromMYSQL) {
                try {
                    MySQLDump.LoadFromMySQL();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                TryLoadYaml();
            }
        }).start();
        Main.launch();
        Jetty jetty = new Jetty();
        try {
            jetty.start();
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            e.printStackTrace();
        }
        try {
            com.itndev.EloSystem.API.Loader.run().get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            SystemUtils.error_logger(e.getMessage());
            throw new RuntimeException(e);
        }
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
        /*new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                RedisTRIM.TryAskKeepAlive();
            }
        }).start();
        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(RedisTRIM.getSmallest() > 1000) {
                    RedisTRIM.Trim(RedisTRIM.getSmallest() - 1000);
                }
            }
        }).start();*/
        new Thread(() -> {
            com.itndev.PlayerPower.Loader loader = new com.itndev.PlayerPower.Loader();
            loader.setupConnectionInfo();
            loader.connect();
            new PlayerPower(loader);
        }).start();
        new Thread(() -> {
            new Client("hostname", 9999);
        }).start();
        int c = 0;
        while (true) {
            c++;
            try {
                Thread.sleep(100);
                if(c >= 100) {
                    try {
                        new Thread(() -> {
                            //RedisDump.deleteandupload("DUMP");
                            //TryDumpYaml();
                            try {
                                MySQLDump.DumpToMySQL();

                            } catch (Exception ex) {
                                System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + ex.getMessage());
                                //Connect.RedisConnect();
                                SQL.connect();
                            }
                        }).start();
                    } catch (Exception ex) {
                        System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + ex.getMessage());
                    }
                    System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] BACKUP DONE TO MYSQL");
                    c = 0;
                }
                if(Close) {


                    //streamIO.StopIO();
                    //RedisDump.deleteandupload("DUMP");
                    try {
                        MySQLDump.DumpToMySQL();
                    } catch (SQLException throwables) {
                        System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + throwables.getMessage());
                    }
                    try {
                        //RedisDump.set_Verification();
                    } catch (Exception e) {
                        SystemUtils.error_logger(e.getMessage());
                        e.printStackTrace();
                    }

                    try {
                        Main.close();
                    } catch (Exception e) {
                        SystemUtils.error_logger(e.getMessage());
                        e.printStackTrace();
                    }
                    Streamable = false;
                    Thread.sleep(1000);
                    /*try {
                        Connect.getRedisConnection().close();
                    } catch (Exception e) {
                        SystemUtils.error_logger(e.getMessage());
                        e.printStackTrace();
                    }*/
                    try {
                        TryDumpYaml();
                    } catch (Exception e) {
                        SystemUtils.error_logger(e.getMessage());
                        e.printStackTrace();
                    }
                    try {
                        jetty.stop();
                    } catch (Exception e) {
                        SystemUtils.error_logger(e.getMessage());
                        e.printStackTrace();
                    }

                    try {
                        SQL.getConnection().getHikariConnection().close();
                        System.out.println("[SYSTEM/" + SystemUtils.getDate(System.currentTimeMillis()) + "] CLOSED MYSQL CONNECTION");
                    } catch (SQLException throwables) {
                        //throwables.printStackTrace();
                        System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] MYSQL CONNECTION FAILED TO CLOSE");
                        System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + throwables.getMessage());
                    }
                    SQLite.close();
                   break;
                }
                //System.out.println("[LOG] (EVERY 10 SECONDS) TIME ->" + SystemUtils.getDate(System.currentTimeMillis()));
            } catch (InterruptedException e) {
                System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + e.getMessage());
                //Connect.RedisConnect();
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
