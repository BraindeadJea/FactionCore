package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.Database.Redis.CmdExecute;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;

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

    public static void run(HashMap<String, String> update) {
        synchronized (sync) {
            //
            if (!update.isEmpty()) {
                if(!update.containsKey(StaticVal.getDataTypeArgs())) {
                    return;
                }
                String DataType = update.get(StaticVal.getDataTypeArgs());
                if(DataType.equalsIgnoreCase("FrontEnd-Output")) {
                    for(int c = 1; c <= Integer.parseInt(update.get(StaticVal.getMaxAmount())); c++) {
                        CmdExecute.get().CMD_READ(update.get(String.valueOf(c)));
                    }
                } else if(DataType.equalsIgnoreCase("FrontEnd-Interconnect")) {
                    for(int c = 1; c <= Integer.parseInt(update.get(StaticVal.getMaxAmount())); c++) {
                        CmdExecute.get().CMD_READ(update.get(String.valueOf(c)));
                    }
                    ResponseList.get().response(update);
                } else if(DataType.equalsIgnoreCase("FrontEnd-Chat")) {
                    ResponseList.get().response(update);
                }

            }
        }
    }
}
