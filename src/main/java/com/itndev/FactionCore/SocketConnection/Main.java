package com.itndev.FactionCore.SocketConnection;

import com.itndev.FactionCore.Database.Redis.Connect;
import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.SocketConnection.IO.ProcessList;
import com.itndev.FactionCore.SocketConnection.IO.ResponseList;
import com.itndev.FactionCore.SocketConnection.Server.Server;
import com.itndev.FactionCore.Utils.Database.Redis.Read;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;

import java.util.HashMap;

public class Main {

    private static Server server;

    public static void launch() {
        server = new Server();
        new Thread(() -> server.run()).start();
        output();
    }

    private static void output() {
        new Thread(() -> {
            try {
                while (true) {
                    HashMap<Integer, String> map;
                    synchronized (Storage.TempCommandQueue) {
                        map = new HashMap<>(Storage.TempCommandQueue);
                        Storage.TempCommandQueue.clear();
                    }
                    if(!map.isEmpty()) {
                        map.put(StaticVal.getServerNameArgs(), "BackEnd");
                        map.put(StaticVal.getDataTypeArgs(), "BackEnd-Responce");
                        ResponseList.get().response(map);
                    }
                    Thread.sleep(2);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public static void close() {
        ResponseList.get().closeAll();
    }
}
