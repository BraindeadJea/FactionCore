package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeAPI;
import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeStorage;
import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeStreamReader;
import com.itndev.FactionCore.Database.Redis.CmdExecute;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.util.HashMap;
import java.util.Queue;

public class ProcessList {


    private static final Object sync = new Object();
    /*public static void run() {
        new Thread(() -> {
            while(true) {
                if(Server.Streamable) {

                }
            }
        }).start();

    }

     */
    public static void run(HashMap<Integer, String> update) {
        new Thread(() -> {
            for (int c = 1; c <= update.size() - 2; c++) {
                CmdExecute.get().CMD_READ(update.get(c));
            }
        }).start();
        if (!update.isEmpty() && update.containsKey(StaticVal.getDataTypeArgs())) {
            String DataType = update.get(StaticVal.getDataTypeArgs());
            if (!DataType.equals("FrontEnd-Output")) {
                if (DataType.equals("BungeeCord-Forward")) {
                    for (int c = 1; c <= update.size() - 2; c++) {
                        BungeeStorage.READ_Bungee_command(update.get(c));
                    }
                }
                ResponseList.get().response(update);
            }
        }
    }
}
