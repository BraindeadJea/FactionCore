package com.itndev.FactionCore.SocketConnection.IO;

import com.itndev.FactionCore.Database.Redis.BungeeAPI.BungeeStorage;
import com.itndev.FactionCore.Database.Redis.CmdExecute;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;

import java.util.HashMap;
import java.util.List;

public class PacketProcessor {


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
    public static void run(HashMap<Integer, Object> stream) {
        //System.out.println(ProcessList.class.getCanonicalName() + " - " + stream);
        String ServerName = (String) stream.get(StaticVal.getServerNameArgs());
        String DataType = (String) stream.get(StaticVal.getDataTypeArgs());
        if(DataType.equalsIgnoreCase("FrontEnd-Output")) {
            ((List<String>)stream.get(1)).forEach(key -> new Thread(() ->CmdExecute.CMD_READ(key, ServerName)).start());
        } else if(DataType.equalsIgnoreCase("FrontEnd-Interconnect")) {
            ((List<String>)stream.get(1)).forEach(key -> new Thread(() ->CmdExecute.CMD_READ(key, ServerName)).start());
            ResponseList.response(stream);
        } else if(DataType.equalsIgnoreCase("FrontEnd-Chat")) {
            ResponseList.response(stream);
        } else if(DataType.equalsIgnoreCase("BungeeCord-Forward")) {
            ((List<String>)stream.get(1)).forEach(key -> new Thread(() ->BungeeStorage.READ_Bungee_command(key)).start());
            ResponseList.response(stream);
        }


        /*String ServerName = (String) stream.get(StaticVal.getServerNameArgs());
        switch ((String)stream.get(StaticVal.getDataTypeArgs())) {
            case "FrontEnd-Output":
                ((List<String>)stream.get(1)).forEach(key -> new Thread(() -> CmdExecute.CMD_READ(key, ServerName)).start());
            case "FrontEnd-Chat":
                ResponseList.response(stream);
            case "FrontEnd-Interconnect":
                ((List<String>)stream.get(1)).forEach(key -> new Thread(() -> CmdExecute.CMD_READ(key, ServerName)).start());
                ResponseList.response(stream);
            case "BungeeCord-Forward":
                ((List<String>)stream.get(1)).forEach(BungeeStorage::READ_Bungee_command);
                ResponseList.response(stream);
        }*/
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
