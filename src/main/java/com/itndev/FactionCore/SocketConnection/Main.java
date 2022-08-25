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
        new Thread(Main::output).start();
    }

    private static void output() {
        while(true) {
            if(!com.itndev.FactionCore.Server.Streamable) {
                break;
            }
            synchronized (Storage.TempCommandQueue) {
                if(!Storage.TempCommandQueue.isEmpty()) {
                    HashMap<String, String> map = (HashMap<String, String>) Storage.TempCommandQueue.clone();
                    map.put(StaticVal.getServerNameArgs(), "BackEnd");
                    ResponseList.get().response(map);
                    Storage.TempCommandQueue.clear();
                }
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close() {
        ResponseList.get().closeAll();
    }
}
