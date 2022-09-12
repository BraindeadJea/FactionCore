package com.itndev.FactionCore.SocketConnection.BackLog;

import com.itndev.FactionCore.Database.MySQL.SQL;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Dump.MySQLDump;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.util.ArrayList;
import java.util.List;

public class StorageSyncTask {

    private String TargetServer;
    private Boolean finish = false;

    public StorageSyncTask(String TargetServer) {
        this.TargetServer = TargetServer;
    }

    public void finish() {
        finish = true;
    }

    public void run() {
        new Thread(() -> {
            synchronized (Storage.TempCommandQueue) {
                try {
                    MySQLDump.DumpToMySQL();
                } catch (Exception ex) {
                    System.out.println("[ERROR/" + SystemUtils.getDate(System.currentTimeMillis()) + "] " + ex.getMessage());
                    //Connect.RedisConnect();
                    SQL.connect();
                }
                List<String> stream = new ArrayList<>();
                stream.add("sync:=:" + this.TargetServer);
                while(!finish) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

    }
}
