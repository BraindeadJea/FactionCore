package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeAPI;
import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeStorage;
import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeStreamReader;
import com.itndev.FactionCore.Database.Redis.CmdExecute;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FaxLib.Utils.Data.DataStream;

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
    public static void run(DataStream stream) {
        switch (stream.getDataType()) {
            case "FrontEnd-Output":
                stream.getStream().forEach(CmdExecute.get()::CMD_READ);
            case "FrontEnd-Chat":
                ResponseList.get().response(stream);
            case "FrontEnd-Interconnect":
                stream.getStream().forEach(CmdExecute.get()::CMD_READ);
                ResponseList.get().response(stream);
            case "BungeeCord-Forward":
                stream.getStream().forEach(BungeeStorage::READ_Bungee_command);
                ResponseList.get().response(stream);
        }
        /*new Thread(() -> {
            stream.getStream().forEach(CmdExecute.get()::CMD_READ);
        }).start();
        String DataType = stream.getDataType();
        if (!stream.getDataType().equals("FrontEnd-Output")) {
            if (DataType.equals("BungeeCord-Forward")) {
                stream.getStream().forEach(BungeeStorage::READ_Bungee_command);
            }
            ResponseList.get().response(stream);
        }*/
    }
}
